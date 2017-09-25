package com.ultime5528.frc2017.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.subsystems.Shooter;

/**
 * Fais tourner à l'envers le {@link Shooter}.
 * Permet d'enlever les balles restées à l'intérieur.
 * 
 * @author Etienne
 */
public class BackSpin extends Command {

    public BackSpin() {
        super("BackSpin");
        requires(Robot.shooter);
    }
    

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	if(Robot.DEBUG) {
    		Preferences prefs = Preferences.getInstance();
    		Shooter.BACK_VITESSE = prefs.getDouble("shooterBackVitesse", Shooter.BACK_VITESSE);
    	}
    		
    	Robot.shooter.setSetpoint(Shooter.BACK_VITESSE);
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
