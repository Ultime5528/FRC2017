package com.ultime5528.frc2017;

import edu.wpi.first.wpilibj.Preferences;

/**
 * La classe K (constantes) contient les numéros des ports de tout
 * ce qui est branché dans le RoboRIO et les constantes utilisées par
 * les sous-systèmes. On s'assure ainsi de ne pas assigner deux fois
 * le même port. Cela permet aussi de voir clairement en un endroit
 * tous les branchements effectués et les valeurs assignées. 
 * 
 * @author Etienne
 * 
 */
public final class K {

	public static void update() {
		
		Preferences prefs = Preferences.getInstance();
		
		//Shooter
		Shooter.P = prefs.getDouble("shooter_p", Shooter.P);
		Shooter.I = prefs.getDouble("shooter_i", Shooter.I);
		Shooter.D = prefs.getDouble("shooter_d", Shooter.D);
		Shooter.F = prefs.getDouble("shooter_f", Shooter.F);
		Shooter.TOLERANCE = prefs.getDouble("shooter_tolerance", Shooter.TOLERANCE);
		Shooter.VITESSE = prefs.getDouble("shooter_vitesse", Shooter.VITESSE);
		Shooter.BACK_VITESSE = prefs.getDouble("shooter_back_vitesse", Shooter.BACK_VITESSE);
		//
		Robot.shooter.getPIDController().setPID(Shooter.P, Shooter.I, Shooter.D, Shooter.F);
		Robot.shooter.getPIDController().setAbsoluteTolerance(Shooter.TOLERANCE);
		
		
		//Camera
		Camera.HIGH_EXPOSURE = prefs.getInt("high_exposure", Camera.HIGH_EXPOSURE);
		Camera.LOW_EXPOSURE = prefs.getInt("low_exposure", Camera.LOW_EXPOSURE);
		
		
		//Rampe
		Rampe.POSITION_OUVERTE = prefs.getDouble("rampe_ouverte", Rampe.POSITION_OUVERTE);
		Rampe.POSITION_FERMEE = prefs.getDouble("rampe_fermee", Rampe.POSITION_FERMEE);
		
		
		//Base pilotable
		BasePilotable.ANGLE_P = prefs.getDouble("angle_p", BasePilotable.ANGLE_P);
		BasePilotable.ANGLE_I = prefs.getDouble("angle_i", BasePilotable.ANGLE_I);
		BasePilotable.ANGLE_D = prefs.getDouble("angle_d", BasePilotable.ANGLE_D);
		BasePilotable.ANGLE_TOLERANCE = prefs.getDouble("angle_tolerance", BasePilotable.ANGLE_TOLERANCE);
		//
		Robot.basePilotable.getAnglePID().setPID(BasePilotable.ANGLE_P, BasePilotable.ANGLE_I, BasePilotable.ANGLE_D);
		Robot.basePilotable.getAnglePID().setAbsoluteTolerance(BasePilotable.ANGLE_TOLERANCE);
		//
		BasePilotable.DISTANCE_P = prefs.getDouble("distance_p", BasePilotable.DISTANCE_P);
		BasePilotable.DISTANCE_I = prefs.getDouble("distance_i", BasePilotable.DISTANCE_I);
		BasePilotable.DISTANCE_D = prefs.getDouble("distance_d", BasePilotable.DISTANCE_D);
		BasePilotable.DISTANCE_TOLERANCE = prefs.getDouble("distance_tolerance", BasePilotable.DISTANCE_TOLERANCE);
		//
		Robot.basePilotable.getDistancePID().setPID(BasePilotable.DISTANCE_P, BasePilotable.DISTANCE_I, BasePilotable.DISTANCE_D);
		Robot.basePilotable.getDistancePID().setAbsoluteTolerance(BasePilotable.DISTANCE_TOLERANCE);
		
		
		//Remonte-balles
		RemonteBalles.VITESSE = prefs.getDouble("remonte_vitesse", RemonteBalles.VITESSE);
		
		//Treuil
		Treuil.POSITION_FERMEE = prefs.getDouble("treuil_ouverte", Treuil.POSITION_FERMEE);
		Treuil.POSITION_OUVERTE = prefs.getDouble("treuil_ouverte", Treuil.POSITION_OUVERTE);
		   
		//Grip
		Grip.H_MIN = prefs.getDouble("h_min", Grip.H_MIN);
		Grip.H_MAX = prefs.getDouble("h_max", Grip.H_MAX);
		Grip.S_MIN = prefs.getDouble("s_min", Grip.S_MIN);
		Grip.S_MAX = prefs.getDouble("s_max", Grip.S_MAX);
		Grip.V_MIN = prefs.getDouble("v_min", Grip.V_MIN);
		Grip.V_MAX = prefs.getDouble("v_max", Grip.V_MAX);
		
	}
	
	
	/**
	 * Tous les ports utilisés sur le robot.
	 */
	public static final class Ports {
		
		//Ports PWM
		public static final int RAMPE_SERVO 					= 0;
		public static final int CAMERA_LIGHT 					= 1;
		public static final int TREUIL_SERVO 					= 2;
		//														= 3;
		//														= 4;
		public static final int BASE_PILOTABLE_MOTEUR_GAUCHE 	= 5;
		public static final int BASE_PILOTABLE_MOTEUR_DROIT 	= 6;
		public static final int SHOOTER_MOTEUR 					= 7;
		public static final int TREUIL_MOTEUR 					= 8;
		public static final int REMONTE_BALLES_MOTEUR 			= 9;
		
		//Ports DIO
		public static final int BASE_PILOTABLE_ENCODER_DROITE_A = 0;
		public static final int BASE_PILOTABLE_ENCODER_DROITE_B = 1;
		public static final int BASE_PILOTABLE_ENCODER_GAUCHE_A = 2;
		public static final int BASE_PILOTABLE_ENCODER_GAUCHE_B = 3;
		public static final int SHOOTER_ENCODER_A 				= 4;
		public static final int SHOOTER_ENCODER_B 				= 5;
		
	}
	
	
	/**
	 * Les constantes utilisées par le Shooter.
	 */
	public static final class Shooter {
		
		public static double P = 0.0;
		public static double I = 0.0;
		public static double D = 0.0;
		public static double F = 0.0;
		public static double TOLERANCE = 0.0; //Tolérance absolue du PID
		public static double VITESSE = 0.0; //Setpoint de vitesse
		public static double BACK_VITESSE = 0.0; //Vitesse inverse au début
		
	}
	
	
	/**
	 * Les constantes utilisées par la Camera.
	 */
	public static final class Camera {
		
		//Image
		public static final int HAUTEUR = 240;
		public static final int LARGEUR = 320;
		
		//Lighting camera
		public static int HIGH_EXPOSURE = 40;
		public static int LOW_EXPOSURE = 5;
		
	}
	
	
	/**
	 * Les contantes utilisées par la Rampe.
	 */
	public static final class Rampe {
		
		public static double POSITION_OUVERTE = 150.0;
		public static double POSITION_FERMEE = 100.0;
		
	}
	
	
	
	public static final class BasePilotable {
		
		public static double ANGLE_P = 0.0;
		public static double ANGLE_I = 0.0;
		public static double ANGLE_D = 0.0;
		public static double ANGLE_TOLERANCE = 2.0;
		
		public static double DISTANCE_P = 0.0;
		public static double DISTANCE_I = 0.0;
		public static double DISTANCE_D = 0.0;
		public static double DISTANCE_TOLERANCE = 0.2;
		
	}
	
	
	public static final class Grip {
		
		public static volatile double H_MIN = 55.0;
		public static volatile double H_MAX = 100.0;
		public static volatile double S_MIN = 100.0;
		public static volatile double S_MAX = 255.0;
		public static volatile double V_MIN = 100.0;
		public static volatile double V_MAX = 255.0;
		
	}
	
	
	public static final class RemonteBalles {
		
		public static double VITESSE = -0.4;

	}
	
	
	
	public static final class Treuil {
		
		public static double POSITION_OUVERTE = 170.0;
		public static double POSITION_FERMEE = 165.0;
		
	}
	
}
