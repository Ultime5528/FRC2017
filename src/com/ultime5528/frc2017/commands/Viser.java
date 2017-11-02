package com.ultime5528.frc2017.commands;

import com.ultime5528.frc2017.GripPipeline;
import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.subsystems.Camera;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Viser extends Command {

	private Object lock;
	private double _centre;
	private double _largeur;
	
    public Viser() {
    	requires(Robot.camera);
    	lock = new Object();
    }
    
    
    public void setCoordinates(double centre, double largeur) {
    	synchronized (lock) {
			_centre = centre;
			_largeur = largeur;
		}
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	Robot.camera.setCallback(this::setCoordinates);
    	Robot.camera.startVision();

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	double centre, largeur;
    	
    	synchronized (lock) {
			centre = _centre;
			largeur = _largeur;
		}
    	
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.camera.stopVision();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
