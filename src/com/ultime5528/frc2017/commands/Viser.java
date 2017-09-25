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

    public Viser() {
        requires(Robot.camera);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	if(Robot.DEBUG) {
    		
    		Preferences prefs = Preferences.getInstance();

    		GripPipeline.H_MIN = prefs.getDouble("cameraHMin", GripPipeline.H_MIN);
    		GripPipeline.H_MAX = prefs.getDouble("cameraHMax", GripPipeline.H_MAX);
    		GripPipeline.S_MIN = prefs.getDouble("cameraSMin", GripPipeline.S_MIN);
    		GripPipeline.S_MAX = prefs.getDouble("cameraSMax", GripPipeline.S_MAX);
    		GripPipeline.V_MIN = prefs.getDouble("cameraVMin", GripPipeline.V_MIN);
    		GripPipeline.V_MAX = prefs.getDouble("cameraVMax", GripPipeline.V_MAX);
    		Camera.LOW_EXPOSURE = prefs.getInt("cameraLowExposure", Camera.LOW_EXPOSURE);
    		Camera.HIGH_EXPOSURE = prefs.getInt("cameraHighExposure", Camera.HIGH_EXPOSURE);
    		
    	}
    	
    	Robot.camera.startVision();

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
    	Robot.camera.stopVision();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
