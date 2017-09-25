package com.ultime5528.frc2017.subsystems;

import com.ultime5528.frc2017.RobotMap;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Gère le mécanisme pour grimper. Comprend : 
 * <ul>
 *   <li>un moteur</li>
 *   <li>un servo</li>
 * </ul>
 * 
 * @author Etienne
 */
public class Treuil extends Subsystem {

	public static double POSITION_OUVERTE = 170.0;
	public static double POSITION_FERMEE = 165.0;
	
	
    private VictorSP moteur;
    private Servo servo;

    
    //Constructeur par défaut
    public Treuil() {
    	
    	super("Treuil");
    	
    	moteur = new VictorSP(RobotMap.TREUIL_MOTEUR);
    	servo = new Servo(RobotMap.TREUIL_SERVO);
    	
    }
    
    
    @Override
    public void initDefaultCommand() {
        // Aucune commande par défaut
    }
    
    
    /**
     * Ouvre le servo pour laisser passer la corde.
     */
    public void ouvrir() {
    	servo.setAngle(POSITION_OUVERTE);
    }
    
    
    /**
     * Ferme le servo pour empêcher la corde de sortir.
     */
    public void fermer() {
    	servo.setAngle(POSITION_FERMEE);
    }
    
    
    /**
     * Active le treuil pour s'enrouler à la corde.
     */
    public void grimper() {
    	moteur.set(1.0);
    }
    
    
    /**
     * Arrête tout mouvement du treuil.
     */
    public void stop() {
    	moteur.set(0.0);
    }
}

