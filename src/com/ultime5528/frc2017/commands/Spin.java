package com.ultime5528.frc2017.commands;

import com.ultime5528.frc2017.K;
import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.subsystems.Shooter;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Démarre le {@link Shooter} pour qu'il atteigne sa vitesse de tir.
 * Il est mieux d'utiliser la commande {@link SafeSpin} que celle-ci. 
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

    	Robot.shooter.setSetpoint(K.Shooter.VITESSE);
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
