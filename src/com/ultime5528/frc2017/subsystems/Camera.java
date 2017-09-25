package com.ultime5528.frc2017.subsystems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.ultime5528.frc2017.GripPipeline;
import com.ultime5528.frc2017.RobotMap;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class Camera extends Subsystem {

	public static final int HAUTEUR = 240;
	public static final int LARGEUR = 320;
	
	public static int HIGH_EXPOSURE = 40;
	public static int LOW_EXPOSURE = 5;
	
	private Spark light;
	
	private UsbCamera camAvant;
	private UsbCamera camCorde;
	
	private Thread visionThread;
	
	private AtomicBoolean runVision;
	
	//Thread safe
	private double center;
	private double largeur;
	private Object lock;
    
	public Camera() {
		
		light = new Spark(RobotMap.CAMERA_LIGHT);
		LiveWindow.addActuator("Camera", "Light", light);
		
		camAvant = new UsbCamera("CamAvant", 0);
		camAvant.setExposureManual(HIGH_EXPOSURE);
		
		camCorde = new UsbCamera("CamCorde", 1);
		camCorde.setExposureManual(HIGH_EXPOSURE);
		
		runVision = new AtomicBoolean(false);
		
		lock = new Object();
		
		visionThread = new Thread( () -> {
			
			CameraServer cs = CameraServer.getInstance();
			
			CvSink sinkCamAvant = new CvSink("SinkCamAvant");
			sinkCamAvant.setSource(camAvant);
			
			CvSink sinkCamCorde = new CvSink("SinkCamCorde");
			sinkCamCorde.setSource(camCorde);
			
			CvSource sourceAvant = new CvSource("SourceAvant", PixelFormat.kMJPEG, LARGEUR, HAUTEUR, 30);
			cs.addCamera(sourceAvant);
			MjpegServer serverAvant = cs.addServer("ServerAvant");
			serverAvant.setSource(sourceAvant);
			
			CvSource sourceVision = new CvSource("SourceVision", PixelFormat.kMJPEG, LARGEUR, HAUTEUR, 30);
			cs.addCamera(sourceVision);
			MjpegServer serverVision = cs.addServer("ServerVision");
			serverVision.setSource(sourceVision);
			
			CvSource sourceCorde = new CvSource("SourceCorde", PixelFormat.kMJPEG, LARGEUR, HAUTEUR, 20);
			cs.addCamera(sourceCorde);
			MjpegServer serverCorde = cs.addServer("ServerCorde");
			serverCorde.setSource(sourceCorde);
			
			
			Mat imgAvant = new Mat(HAUTEUR, LARGEUR, CvType.CV_8UC3, new Scalar(255, 0, 0));
			Imgproc.putText(imgAvant, "Cam avant", new Point(20, 120),
					Core.FONT_HERSHEY_TRIPLEX,1.0, new Scalar(255, 255, 255));
			
			Mat imgGrip = new Mat(HAUTEUR, LARGEUR, CvType.CV_8UC3, new Scalar(0, 0, 255));
			Imgproc.putText(imgGrip, "GRIP", new Point(20, 120),
					Core.FONT_HERSHEY_TRIPLEX,1.0, new Scalar(255, 255, 255));
			
			Mat imgCorde = new Mat(HAUTEUR, LARGEUR, CvType.CV_8UC3, new Scalar(0, 255, 0));
			Imgproc.putText(imgCorde, "Cam corde", new Point(20, 120),
					Core.FONT_HERSHEY_TRIPLEX,1.0, new Scalar(255, 255, 255));
			
			while(!Thread.interrupted()) {
				
				try {
					
					sinkCamCorde.grabFrame(imgCorde);
					sourceCorde.putFrame(imgCorde);
					
					sinkCamAvant.grabFrame(imgAvant);
					sourceAvant.putFrame(imgAvant);
					
					if(runVision.get()) {
						
						Analyse(imgAvant, imgGrip);
						
					}
					
					sourceVision.putFrame(imgGrip);
					
				}
				catch(Exception e) {
					//DriverStation.reportError(e.getMessage(), true);
					e.printStackTrace();
				}
				
				
			}
			
			DriverStation.reportError("Vision Thread will stop", false);
			
		});
		visionThread.start();
		
		
	}

	private void Analyse(Mat input, Mat output) {
		
		int nbContours;
		double center, largeur;
		GripPipeline grip = GripPipeline.getInstance();
		ArrayList<MatOfPoint> allContours;
		ArrayList<Point> mainContoursList = new ArrayList<>();
		MatOfPoint mainContoursMat = new MatOfPoint();
		Rect rect;
		
		grip.process(input);
		grip.cvErodeOutput().copyTo(output);
		
		allContours = grip.findContoursOutput();
		nbContours = allContours.size();
		
		for(MatOfPoint contour : allContours) {
			Rect eachRect = Imgproc.boundingRect(contour);
			Imgproc.rectangle(output, new Point(eachRect.x, eachRect.y),
					new Point(eachRect.x + eachRect.width, eachRect.y + eachRect.height),
					new Scalar(255, 0, 0));
		}
		
		
		if(nbContours == 0) {
			DriverStation.reportWarning("Aucun contour detecte", false);
		}
		else {
			
			if(nbContours == 1) {
				
				DriverStation.reportWarning("1 seul contour detecte", false);
				mainContoursList.addAll(allContours.get(0).toList());
				
			}
			else if(allContours.size() == 2) {
				
				 mainContoursList.addAll(allContours.get(0).toList());
				 mainContoursList.addAll(allContours.get(1).toList());
				 
			}
			else {
			
				int k = -1;
				ContourCoupleScore[] scores = new ContourCoupleScore[nbContours * (nbContours - 1) / 2];
				
				for(int i = 0; i < nbContours; i++)
					for(int j = i + 1; j < nbContours; j++) {
						
						scores[++k] = new ContourCoupleScore(i, j);
						determineScore(allContours.get(i), allContours.get(j), scores[k]);
						
					}
				
				int best = 0;
				
				for(k = 1; k < nbContours; k++) {
					
					//TODO : pas optimal d'appeler score() à chaque fois
					if(scores[k].score() > scores[best].score())
						best = k;
					
				}
				
				mainContoursList.addAll(allContours.get(scores[best].first).toList());
				mainContoursList.addAll(allContours.get(scores[best].second).toList());
				
			}
			
			mainContoursMat.fromList(mainContoursList);
			rect = Imgproc.boundingRect(mainContoursMat);
			
			center = rect.x + rect.width / 2.0;
			largeur = (double)rect.width / input.cols();
			
			synchronized (lock) {
				this.center = center;
				this.largeur = largeur;
			}
			
			Imgproc.rectangle(output, new Point(rect.x, rect.y),
					new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}
			
	}
	
	private void determineScore(MatOfPoint first, MatOfPoint second, ContourCoupleScore score) {
		
		List<Point> list = first.toList();
		list.addAll(second.toList());
		MatOfPoint all = new MatOfPoint();
		all.fromList(list);
		
		Rect rect1 = Imgproc.boundingRect(first);
		Rect rect2 = Imgproc.boundingRect(second);
		Rect rectTotal = Imgproc.boundingRect(all);
		
		//Espace central
		int d1 = Math.abs(rect1.x - (rect2.x + rect2.width));
		int d2 = Math.abs(rect2.x - (rect1.x + rect1.width));
		int largeurInterieure = Math.min(d1, d2);
		score.espaceCentral = rectTotal.width / (1.64 * largeurInterieure);
		
		//Largeur semblable
		score.largeurSemblable = (double)rect1.width / rect2.width;
		
		//Hauteur semblable
		score.hauteurSemblable = (double)rect1.height / rect2.height;
		
		//Largeur totale
		double largeurMoyenne = (rect1.width + rect2.width) / 2.0;
		score.largeurTotale = 5.125 * (largeurMoyenne / rectTotal.width);
		
		//Hauteur totale
		double hauteurMoyenne = (rect1.height + rect2.height) / 2.0;
		score.hauteurTotale = hauteurMoyenne / rectTotal.height;
		
	}

	
	public void startVision() {
		
		light.set(1.0);
		camAvant.setExposureManual(LOW_EXPOSURE);
		runVision.set(true);
		
	}
	
	
	public void stopVision() {
		
		light.set(0.0);
		camAvant.setExposureManual(HIGH_EXPOSURE);
		runVision.set(false);
		
	}
	
	
	public double getCenter() {
		
		synchronized (lock) {
			return center;
		}
		
	}
	
	public double getLargeur() {
		
		synchronized (lock) {
			return largeur;
		}
		
	}
	
	public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
	
	
	public static class VisionReport {
		
		public boolean isValid = false;
		public double centre = 0.0;
		public double largeur = 0.0;
		
	}
	
	
	public static class ContourCoupleScore {
		
		public int first = 0;
		public int second = 0;
		
		private final int NB_CHAMPS = 5;
		
		public double espaceCentral = 0.0;
		public double largeurSemblable = 0.0;
		public double hauteurSemblable = 0.0;
		public double largeurTotale = 0.0;
		public double hauteurTotale = 0.0;
		
		
		public ContourCoupleScore(int first, int second) {
			
			this.first = first;
			this.second = second;
			
		}
		
		
		public double score() {
			
			double score = normalizeResult(espaceCentral)
					+ normalizeResult(largeurSemblable)
					+ normalizeResult(hauteurSemblable)
					+ normalizeResult(largeurTotale)
					+ normalizeResult(hauteurTotale);
			
			return score / NB_CHAMPS;
		}
		
		
		public String toString() {
			
			return "Contours " + first + " et " + second + " : "
					+ "\n\t- Espace central : " + espaceCentral
					+ "\n\t Largeur semblable : " + largeurSemblable
					+ "\n\t Hauteur semblable : " + hauteurSemblable
					+ "\n\t Largeur totale : " + largeurTotale
					+ "\n\t Hauteur totale : " + hauteurTotale;
			
		}
		
		
		private static double normalizeResult(double result) {
			return 1.0 - Math.abs(1.0 - result);
		}
	}
	
}

