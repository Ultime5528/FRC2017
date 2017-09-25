package com.ultime5528.frc2017.subsystems;

import com.ultime5528.frc2017.RobotMap;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Gère le remonte-balles à l'arrière du robot. Comprend un moteur.
 * 
 * @author Etienne
 */
public class RemonteBalles extends Subsystem {

	public static double VITESSE = -0.4;
	
	
    private VictorSP moteur;

    
    //Constructeur par défaut
    public RemonteBalles() {
    	super("RemonteBalles");
    	
    	moteur = new VictorSP(RobotMap.REMONTE_BALLES_MOTEUR);
    	
    }
    
    
    @Override
    public void initDefaultCommand() {
        // Aucune commande par défaut
    }
    
    
    /**
     * Fais remonter les balles.
     */
    public void remonter() {
    	moteur.set(VITESSE);
    }
    
    
    /**
     * Arrête tous les mouvements du remonte-balles.
     */
    public void stop() {
    	moteur.set(0.0);
    }
    
}

