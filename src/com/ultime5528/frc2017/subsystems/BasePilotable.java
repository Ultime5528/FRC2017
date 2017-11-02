package com.ultime5528.frc2017.subsystems;

import com.ultime5528.frc2017.ADIS16448_IMU;
import com.ultime5528.frc2017.K;
import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.commands.Pilotage;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import static com.ultime5528.frc2017.K.BasePilotable.*;

/**
 * Gère les mouvements de la base pilotable. Comprend deux moteurs.
 * 
 * @author Etienne
 */
public class BasePilotable extends Subsystem {

    private VictorSP moteurGauche;
    private VictorSP moteurDroit;
    private RobotDrive robotDrive;
    private ADIS16448_IMU gyro;
    private Encoder encoderGauche;
    private Encoder encoderDroit;
    
    // PID angle et vitesse
    private double currentRotation;
    private double currentForward;
    private PIDController anglePID;
    private PIDController distancePID;
    private Object lock;

    //Constructeur par défaut
    public BasePilotable() {
    	super("Base pilotable");
    	
    	moteurGauche = new VictorSP(K.Ports.BASE_PILOTABLE_MOTEUR_GAUCHE);
    	LiveWindow.addActuator("Base pilotable", "Moteur gauche", moteurGauche);
    	
    	moteurDroit = new VictorSP(K.Ports.BASE_PILOTABLE_MOTEUR_DROIT);
    	LiveWindow.addActuator("Base pilotable", "Moteur droit", moteurDroit);
    	
    	robotDrive = new RobotDrive(moteurGauche, moteurDroit);
    	
    	gyro = new ADIS16448_IMU();
    	gyro.calibrate();
    	gyro.reset();
    	gyro.setPIDSourceType(PIDSourceType.kDisplacement);
    	LiveWindow.addSensor("Base pilotable", "Gyro", gyro);
    	
    	encoderGauche = new Encoder(K.Ports.BASE_PILOTABLE_ENCODER_GAUCHE_A, K.Ports.BASE_PILOTABLE_ENCODER_GAUCHE_B);
    	encoderGauche.setDistancePerPulse(0.001544);
    	encoderGauche.setPIDSourceType(PIDSourceType.kDisplacement);
    	LiveWindow.addSensor("Base pilotable", "Encoder gauche", encoderGauche);
    	
    	encoderDroit = new Encoder(K.Ports.BASE_PILOTABLE_ENCODER_DROITE_A, K.Ports.BASE_PILOTABLE_ENCODER_DROITE_B);
    	encoderDroit.setDistancePerPulse(-0.001544);
    	LiveWindow.addSensor("Base pilotable", "Encoder droit", encoderDroit);
    	
    	lock = new Object();
    	
    	anglePID = new PIDController(ANGLE_P, ANGLE_I, ANGLE_D, gyro, this::setRotation);
    	anglePID.setInputRange(0.0, 360.0);
    	anglePID.setOutputRange(-1.0, 1.0);
    	anglePID.setContinuous();
    	LiveWindow.addActuator("Base pilotable", "Angle PID", anglePID);
    	
    	distancePID = new PIDController(DISTANCE_P, DISTANCE_I, DISTANCE_D, encoderGauche, this::setForward);
    	distancePID.setOutputRange(-1.0, 1.0);
    	LiveWindow.addActuator("Base pilotable", "Distance PID", distancePID);
    	
    }
    
    
    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new Pilotage());
    }
    
    
    public void setConstants() {
    	
    	anglePID.setPID(ANGLE_P, ANGLE_I, ANGLE_D);
    	anglePID.setAbsoluteTolerance(ANGLE_TOLERANCE);
    	
    	distancePID.setPID(DISTANCE_P, DISTANCE_I, DISTANCE_D);
    	distancePID.setAbsoluteTolerance(DISTANCE_TOLERANCE);
    	
    }
    
    
    public void setForward(double forward) {
    	synchronized (lock) {
			currentForward = forward;
		}
    }
    
    
    public void setRotation(double rotation) {
    	synchronized (lock) {
			currentRotation = rotation;
		}
    }
    
    public PIDController getAnglePID() {
    	return anglePID;
    }
    
    
    public PIDController getDistancePID() {
    	return distancePID;
    }
    
    
    public void resetGyro() {
    	gyro.reset();
    }
    
    
    public void arcadeDrivePID() {
    	
    	double rotation, forward;
    	
    	synchronized (lock) {
    		rotation = currentRotation;
    		forward = currentForward;
		}
    	
    	// TODO Essayer méthode .drive() ?
    	robotDrive.arcadeDrive(forward, rotation);
    	
    }
    
    /**
     * Transmet à la base pilotable les vitesses
     * correspondant au joystick principal.
     */
    public void drive() {
    	// TODO Essayer méthode .drive() ?
    	robotDrive.arcadeDrive(-Robot.oi.getJoystickY(), Robot.oi.getJoystickX()); //TODO Inverse?
    }
    
    
    /**
     * Arrête tout mouvement de la base pilotable.
     */
    public void stop() {
    	moteurGauche.set(0.0);
    	moteurDroit.set(0.0);
    }
    
    
    private class AnglePID implements PIDOutput {

		@Override
		public void pidWrite(double output) {
			// TODO Auto-generated method stub
			
		}

    	
    }
    
}

