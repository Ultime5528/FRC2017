package com.ultime5528.frc2017.commands;

import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.subsystems.*;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Ferme la {@link Rampe} et éteint le {@link RemonteBalles} et le {@link Shooter}.
 * 
 * @author Etienne
 */
public class StopShoot extends InstantCommand {

    public StopShoot() {
    	super("StopShoot");
    	requires(Robot.shooter);
    	requires(Robot.remonteBalles);
    	requires(Robot.rampe);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
