package com.ultime5528.frc2017.commands;

import edu.wpi.first.wpilibj.command.Command;
import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.subsystems.BasePilotable;;

/**
 * Commande par défaut de la {@link BasePilotable}, pour permettre son déplacement.
 * 
 * @author Etienne
 */
public class Pilotage extends Command {

    public Pilotage() {
    	super("Pilotage");				//Constructeur par défaut. On donne le nom de la commande en paramètre.
        requires(Robot.basePilotable);	//Spécifie les sous-systèmes requis par cette commande.
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.basePilotable.drive();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.basePilotable.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end(); //On la rappelle la méthode end(), déclarée juste au-dessus.
    }
}
