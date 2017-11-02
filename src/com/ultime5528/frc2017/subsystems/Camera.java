package com.ultime5528.frc2017.subsystems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.ultime5528.frc2017.GripPipeline;
import com.ultime5528.frc2017.K;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

//Toutes les contantes de Camera sont importées directement.
import static com.ultime5528.frc2017.K.Camera.*;

/**
 *
 */
public class Camera extends Subsystem {
	
	private Spark light;
	
	private UsbCamera camAvant;
	private UsbCamera camCorde;
	
	private Thread visionThread;
	
	private AtomicBoolean runVision;
	
	private BiConsumer<Double, Double> callback;
	
	//Thread safe
	private double center;
	private double largeur;
	private Object lock;
    
	public Camera() {
		
		light = new Spark(K.Ports.CAMERA_LIGHT);
		LiveWindow.addActuator("Camera", "Light", light);
		
		camAvant = new UsbCamera("CamAvant", 0);
		camAvant.setExposureManual(HIGH_EXPOSURE);
		
		camCorde = new UsbCamera("CamCorde", 1);
		camCorde.setExposureManual(HIGH_EXPOSURE);
		
		runVision = new AtomicBoolean(false);
		
		lock = new Object();
		
		//Thread qui exécute la méthode threadVision
		visionThread = new Thread(this::visionLoop);
		visionThread.start();
		
		
	}
	
	
	
	/**
	 * Boucle du code de la vision.
	 * La méthode UNIQUEMENT être appelé dans un Thread, car elle est bloquante. 
	 */
	private void visionLoop() {
		
		//TODO : quelles méthodes sont vraiments nécessaires? À essayer
		
		//Référence vers le serveur
		CameraServer cs = CameraServer.getInstance();
		
		//
		// Caméra avant
		//
		CvSink sinkAvant = new CvSink("SinkCamAvant");
		sinkAvant.setSource(camAvant);
		//
		CvSource sourceAvant = new CvSource("SourceAvant", PixelFormat.kMJPEG, LARGEUR, HAUTEUR, 30);
		cs.addCamera(sourceAvant);
		MjpegServer serverAvant = cs.addServer("ServerAvant");
		serverAvant.setSource(sourceAvant);
		
		//
		// Caméra corde (treuil)
		//
		CvSink sinkCorde = new CvSink("SinkCamCorde");
		sinkCorde.setSource(camCorde);
		//
		CvSource sourceCorde = new CvSource("SourceCorde", PixelFormat.kMJPEG, LARGEUR, HAUTEUR, 20);
		cs.addCamera(sourceCorde);
		MjpegServer serverCorde = cs.addServer("ServerCorde");
		serverCorde.setSource(sourceCorde);
		
		//
		// Envoi images de vision
		//
		CvSource sourceVision = new CvSource("SourceVision", PixelFormat.kMJPEG, LARGEUR, HAUTEUR, 30);
		cs.addCamera(sourceVision);
		MjpegServer serverVision = cs.addServer("ServerVision");
		serverVision.setSource(sourceVision);
		
		//
		// Images
		//
		Mat imgAvant = new Mat(HAUTEUR, LARGEUR, CvType.CV_8UC3, new Scalar(255, 0, 0));
		Imgproc.putText(imgAvant, "Cam avant", new Point(20, 120),
				Core.FONT_HERSHEY_TRIPLEX,1.0, new Scalar(255, 255, 255));
		
		Mat imgVision = new Mat(HAUTEUR, LARGEUR, CvType.CV_8UC3, new Scalar(0, 0, 255));
		Imgproc.putText(imgVision, "GRIP", new Point(20, 120),
				Core.FONT_HERSHEY_TRIPLEX,1.0, new Scalar(255, 255, 255));
		
		
		Mat imgCorde = new Mat(HAUTEUR, LARGEUR, CvType.CV_8UC3, new Scalar(0, 255, 0));
		Imgproc.putText(imgCorde, "Cam corde", new Point(20, 120),
				Core.FONT_HERSHEY_TRIPLEX,1.0, new Scalar(255, 255, 255));
		
		
		while(!Thread.interrupted()) {
			
			try {
				sinkCorde.grabFrame(imgCorde);
				sourceCorde.putFrame(imgCorde);
				
				sinkAvant.grabFrame(imgAvant);
				sourceAvant.putFrame(imgAvant);
				
				//TODO Bug d'affichage
				if(runVision.get()) {
					analyseImage(imgAvant, imgVision);
				}
				
				//TODO Clone avant de la donner à putFrame?
				
				//imgCorde.copyTo(imgVision);
				sourceVision.putFrame(imgVision);
			}
			catch(Exception e) {
				//DriverStation.reportError(e.getMessage(), true);
				e.printStackTrace();
			}
		}
		
		DriverStation.reportError("Thread Vision s'arrete", false);
	}
	
	
	
	/**
	 * Détermine le centre de la cible.
	 * @param input Image source (caméra)
	 * @param output Image résultante (noir et blanc avec rectangles)
	 */
	private void analyseImage(Mat input, Mat output) {
		
		int nbContours;
		double center, largeur;
		GripPipeline grip = GripPipeline.getInstance();
		ArrayList<MatOfPoint> allContours;
		ArrayList<Point> mainContoursList = new ArrayList<>();
		MatOfPoint mainContoursMat = new MatOfPoint();
		Rect rect;
		
		//Utilisation de GRIP pour déterminer les contours
		grip.process(input);
		
		//Copie du résultat
		//TODO
		//grip.cvErodeOutput().copyTo(output);
		//grip.hsvThresholdOutput().copyTo(output);
		Imgproc.cvtColor(grip.cvErodeOutput(), output, Imgproc.COLOR_GRAY2BGR, 3);
		
		// On prend les contours et le nombre de contours trouvés.
		allContours = grip.findContoursOutput();
		nbContours = allContours.size();
		
		// On trace chaque contour(rectangle) trouvé sur l'image.
		for(MatOfPoint contour : allContours) {
			Rect eachRect = Imgproc.boundingRect(contour);
			/*Imgproc.rectangle(output, new Point(eachRect.x, eachRect.y),
					new Point(eachRect.x + eachRect.width, eachRect.y + eachRect.height),
					new Scalar(255, 0, 0));*/
		}
		
		
		//
		// Analyse des contours
		//
		
		if(nbContours == 0) { // Aucun contour
			DriverStation.reportWarning("Aucun contour detecte", false);
		}
		else {
			
			if(nbContours == 1) { // Un seul contour
				
				DriverStation.reportWarning("1 seul contour detecte", false);
				
				// On utilise le centre de l'unique contour.
				mainContoursList.addAll(allContours.get(0).toList());
				
			}
			else if(allContours.size() == 2) { // Deux contours, situation idéale
				
				 mainContoursList.addAll(allContours.get(0).toList());
				 mainContoursList.addAll(allContours.get(1).toList());
				 
			}
			else { // Plus de 2 contours : il faut déterminer les meilleurs
			
				// Test de tous les couples de contours possibles. Pour n contours, il y a 
				// n(n - 1) / 2 possibilités. Par exemple, avec n = 3, on a (1, 2), (1, 3)
				// et (2, 3), donc 3 possibilités.
				
				int currentCouple = 0;
				ContourCoupleScore[] scores = new ContourCoupleScore[nbContours * (nbContours - 1) / 2];
				
				// Pour chaque couple possible, on détermine leur score.
				for(int i = 0; i < nbContours; i++)
					for(int j = i + 1; j < nbContours; j++) {
						
						scores[currentCouple] = new ContourCoupleScore(i, j);
						determineScore(allContours.get(i), allContours.get(j), scores[currentCouple]);
						currentCouple += 1;
						
					}
				
				int best = 0; // Indice du meilleur couple
				
				// On détermine l'indice du score maximal
				for(currentCouple = 1; currentCouple < nbContours; currentCouple++) {
					
					if(scores[currentCouple].score() > scores[best].score())
						best = currentCouple;
					
				}
				
				// Utilisation du couple optimal
				mainContoursList.addAll(allContours.get(scores[best].first).toList());
				mainContoursList.addAll(allContours.get(scores[best].second).toList());
				
			}
			
			// Conversion de la liste des points choisis en rectangle.
			
			mainContoursMat.fromList(mainContoursList);
			rect = Imgproc.boundingRect(mainContoursMat);
			
			// Calcul 
			center = rect.x + rect.width / 2.0;
			largeur = (double)rect.width / input.cols();
			
			synchronized (lock) {
				this.center = center;
				this.largeur = largeur;
				
				if(callback != null)
					callback.accept(center, largeur);
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
	
	
	public void setCallback(BiConsumer<Double, Double> callback) {
		synchronized (lock) {
			this.callback = callback;
		}
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
        // Aucune commande par défaut.
    }
	
	
	public static class VisionReport {
		
		public boolean isValid = false;
		public double centre = 0.0;
		public double largeur = 0.0;
		
	}
	
	
	public static class ContourCoupleScore {
		
		public int first = 0;
		public int second = 0;
		
		
		public double espaceCentral = 0.0;
		public double largeurSemblable = 0.0;
		public double hauteurSemblable = 0.0;
		public double largeurTotale = 0.0;
		public double hauteurTotale = 0.0;
		
		private final int NB_CHAMPS = 5;
		private double score = Double.NaN;
		
		public ContourCoupleScore(int first, int second) {
			
			this.first = first;
			this.second = second;
			
		}
		
		public void computeScore() {
			score = normalizeResult(espaceCentral)
					+ normalizeResult(largeurSemblable)
					+ normalizeResult(hauteurSemblable)
					+ normalizeResult(largeurTotale)
					+ normalizeResult(hauteurTotale);
			
			score /= NB_CHAMPS;	
		}
		
		public double score() {
			
			if(Double.isNaN(score))
				computeScore();
			
			return score;
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

