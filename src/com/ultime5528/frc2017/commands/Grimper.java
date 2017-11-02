package com.ultime5528.frc2017.commands;

import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.subsystems.Treuil;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * {@link InstantCommand} activant le {@link Treuil} pour s'enrouler à la corde.
 * Cette commande est Instant, car on l'utilise avec la méthode whileHeld de
 * JoystickButton (voir OI).
 * 
 * @author Etienne
 */
public class Grimper extends InstantCommand {

    public Grimper() {
    	super("Grimper");
        requires(Robot.treuil);
    }

    
    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.treuil.grimper();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.treuil.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
