package com.ultime5528.frc2017.commands;

import edu.wpi.first.wpilibj.command.Command;

import com.ultime5528.frc2017.Robot;

/**
 *
 */
public class ExampleCommand extends Command {
	
	/**
	 * Le nombre de fois que cette commande a été utilisée.
	 */
	int numberOfUses = 0;
	
	public ExampleCommand() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.exampleSubsystem);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		numberOfUses++;
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
