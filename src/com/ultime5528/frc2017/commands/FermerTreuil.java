package com.ultime5528.frc2017.commands;

import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.subsystems.Treuil;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Ferme le {@link Treuil} pour retenir la corde
 * 
 * @author Etienne
 */
public class FermerTreuil extends InstantCommand {

    public FermerTreuil() {
    	super("FermerTreuil");
        requires(Robot.treuil);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.treuil.ouvrir();
    }

    
    
    // Rien d'autre à faire
    
    protected void execute() {
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
