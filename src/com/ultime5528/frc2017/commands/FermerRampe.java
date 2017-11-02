package com.ultime5528.frc2017.commands;

import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.subsystems.*;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Ferme la {@link Rampe}, soit l'accès des balles au {@link Shooter}.
 * Cette commande hérite de InstantCommand, elle n'a donc pas de 
 * méthode isFinished, car elle est automatiquement terminée instantanément.
 * 
 * @author Etienne
 */
public class FermerRampe extends InstantCommand {

    public FermerRampe() {
    	super("FermerRampe");
        requires(Robot.rampe);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    		
    	Robot.rampe.fermer();
    	
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
