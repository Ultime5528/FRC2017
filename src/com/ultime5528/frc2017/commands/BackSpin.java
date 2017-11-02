package com.ultime5528.frc2017.commands;

import edu.wpi.first.wpilibj.command.Command;

import com.ultime5528.frc2017.K;
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
    	
    	// Donne la vitesse à atteindre au PID du shooter
    	Robot.shooter.setSetpoint(K.Shooter.BACK_VITESSE);
    	
    	// Démarre le PID
    	Robot.shooter.enable(); 
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// Rien à faire, le PID s'occupe de faire les corrections.
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	
    	//On s'assure que le PID est bien arrêté.
    	Robot.shooter.setSetpoint(0.0);
    	Robot.shooter.disable();
    	
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
