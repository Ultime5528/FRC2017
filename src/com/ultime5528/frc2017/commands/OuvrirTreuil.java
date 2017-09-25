package com.ultime5528.frc2017.commands;


import com.ultime5528.frc2017.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Ouvre l'accès au {@link Treuil}.
 * 
 * @author Etienne
 */
public class OuvrirTreuil extends InstantCommand {

    public OuvrirTreuil() {
    	super("OuvrirTreuil");
        requires(Robot.treuil);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.treuil.ouvrir();
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
