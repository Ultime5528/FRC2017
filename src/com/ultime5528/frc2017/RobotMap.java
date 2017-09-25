package com.ultime5528.frc2017;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 * 
 */
public class RobotMap {
	
	
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
	public static final int SHOOTER_ENCODER_A 				= 4;
	public static final int SHOOTER_ENCODER_B 				= 5;
	
}
