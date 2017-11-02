package com.ultime5528.frc2017.commands;

import com.ultime5528.frc2017.Robot;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Tourner extends Command {

	private double angle;
	
    public Tourner(double angle) {
        requires(Robot.basePilotable);
        this.angle = angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//TODO Remove debug
    	angle = Preferences.getInstance().getDouble("angle", 0.0);
    	
    	Robot.basePilotable.setForward(0.0);
    	Robot.basePilotable.resetGyro();
    	Robot.basePilotable.getAnglePID().setSetpoint(angle);
    	Robot.basePilotable.getAnglePID().enable();
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.basePilotable.arcadeDrivePID();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.basePilotable.getAnglePID().onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.basePilotable.getAnglePID().disable();
    	Robot.basePilotable.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
