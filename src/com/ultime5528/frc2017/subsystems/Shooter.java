package com.ultime5528.frc2017.subsystems;

import com.ultime5528.frc2017.K;
import com.ultime5528.frc2017.Robot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;


/**
 * {@link PIDSubsystem} qui gère le shooter du robot.
 * 
 * Il comprend :
 * <ul>
 *	<li>la roue lançant les balles</li>
 * 	<li>l'encodeur qui y est rattaché</li>
 * </ul>
 *
 * @author Étienne
 * @see PIDSubsystem
 */
public class Shooter extends PIDSubsystem {
	
	private VictorSP moteur;
	private Encoder encoder;
	
	
	//Constructeur par défaut
	public Shooter() {
		
		super("Shooter", K.Shooter.P, K.Shooter.I, K.Shooter.D, K.Shooter.F);
		
		moteur = new VictorSP(K.Ports.SHOOTER_MOTEUR);
		LiveWindow.addActuator("Shooter", "Moteur", moteur);
		
		encoder = new Encoder(K.Ports.SHOOTER_ENCODER_A, K.Ports.SHOOTER_ENCODER_B, false, EncodingType.k4X);
		LiveWindow.addSensor("Shooter", "Encoder", encoder);
		
		//TODO : est-ce nécessaire?
		//getPIDController().setContinuous(false);
		
		LiveWindow.addActuator("Shooter", "PIDController", getPIDController());
		
		setOutputRange(-1.0, 1.0);
		setAbsoluteTolerance(K.Shooter.TOLERANCE);
		
		//TODO: est-ce nécessaire?
		disable();
		
	}
	
	
	@Override
	protected void initDefaultCommand() {
		//Aucune commande par défaut
	}
	
	
	@Override
	protected double returnPIDInput() {
		
		double rate = encoder.getRate();
		
		if(Robot.DEBUG)
			SmartDashboard.putNumber("Shooter PID Input", rate);
		
		return rate;
		
	}

	
	@Override
	protected void usePIDOutput(double output) {
		
		if(Robot.DEBUG)
			SmartDashboard.putNumber("Shooter PID Output", output);
		
		moteur.set(output);
		
	}
	
}
