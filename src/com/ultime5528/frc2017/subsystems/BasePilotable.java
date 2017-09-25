package com.ultime5528.frc2017.subsystems;

import com.ultime5528.frc2017.Robot;
import com.ultime5528.frc2017.RobotMap;
import com.ultime5528.frc2017.commands.Pilotage;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Gère les mouvements de la base pilotable. Comprend deux moteurs.
 * 
 * @author Etienne
 */
public class BasePilotable extends Subsystem {

    private VictorSP moteurGauche;
    private VictorSP moteurDroit;
    private RobotDrive robotDrive;

    //Constructeur par défaut
    public BasePilotable() {
    	super("BasePilotable");
    	
    	moteurGauche = new VictorSP(RobotMap.BASE_PILOTABLE_MOTEUR_GAUCHE);
    	moteurDroit = new VictorSP(RobotMap.BASE_PILOTABLE_MOTEUR_DROIT);
    	
    	robotDrive = new RobotDrive(moteurGauche, moteurDroit);
    }
    
    
    @Override
    public void initDefaultCommand() {
        setDefaultCommand(new Pilotage());
    }
    
    
    /**
     * Transmet à la base pilotable les vitesses
     * correspondant au joystick principal.
     */
    public void drive() {
    	robotDrive.arcadeDrive(Robot.oi.getJoystick());
    }
    
    
    /**
     * Arrête tout mouvement de la base pilotable.
     */
    public void stop() {
    	moteurGauche.set(0.0);
    	moteurDroit.set(0.0);
    }
}

