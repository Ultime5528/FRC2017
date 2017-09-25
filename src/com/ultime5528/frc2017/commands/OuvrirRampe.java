package com.ultime5528.frc2017.commands;

import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.subsystems.*;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;;

/**
 * Ouvre la {@link Rampe} les balles pour laisser les balles aller au {@link Shooter}.
 * Empêche l'ouverture si le {@link Shooter} n'est pas démarré.
 * Ferme la {@link Rampe} à la fin de la commande.
 * 
 * @author Etienne
 */
public class OuvrirRampe extends Command {

    public OuvrirRampe() {
        super("OuvrirRampe");
        requires(Robot.rampe);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	//On empêche l'ouverture si le shooter n'est pas démarré.
    	if(Robot.shooter.getPIDController().isEnabled()) {
    		this.cancel();
    		return;
    	}
    	
    	if(Robot.DEBUG) {
    		Preferences prefs = Preferences.getInstance();
    		Rampe.POSITION_FERMEE = prefs.getDouble("rampePositionFermee", Rampe.POSITION_FERMEE);
    		Rampe.POSITION_OUVERTE = prefs.getDouble("rampePositionOuverte", Rampe.POSITION_OUVERTE);
    	}
    	
    	Robot.rampe.ouvrir();
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
    	Robot.rampe.fermer();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
