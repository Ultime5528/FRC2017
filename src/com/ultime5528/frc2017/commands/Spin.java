package com.ultime5528.frc2017.commands;

import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.subsystems.Shooter;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Démarre le {@link Shooter} pour qu'il atteigne sa vitesse de tir.
 * 
 * @author Étienne
 */
public class Spin extends Command {

    public Spin() {
    	super("Spin");
        requires(Robot.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	if(Robot.DEBUG) {
    		
    		Preferences prefs = Preferences.getInstance();
    		
    		Shooter.P = prefs.getDouble("p", Shooter.P);
    		Shooter.I = prefs.getDouble("i", Shooter.I);
    		Shooter.D = prefs.getDouble("d", Shooter.D);
    		Shooter.F = prefs.getDouble("f", Shooter.F);
    		Robot.shooter.getPIDController().setPID(Shooter.P, Shooter.I, Shooter.D, Shooter.F);
    		
    		Shooter.TOLERANCE = prefs.getDouble("tolerance", Shooter.TOLERANCE);
    		Robot.shooter.setAbsoluteTolerance(Shooter.TOLERANCE);
    		
    		Shooter.VITESSE = prefs.getDouble("vitesse", Shooter.VITESSE);
    	}
    	
    	
    	Robot.shooter.setSetpoint(Shooter.VITESSE);
    	Robot.shooter.enable();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }
    

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    
    // Called once after isFinished returns true
    protected void end() {
    	
    	Robot.shooter.setSetpoint(0.0);
    	Robot.shooter.disable();
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
