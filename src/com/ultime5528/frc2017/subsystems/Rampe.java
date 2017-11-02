package com.ultime5528.frc2017.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import com.ultime5528.frc2017.K;
import com.ultime5528.frc2017.commands.FermerRampe;

/**
 * Laisse passer ou non les balles pour aller au {@link Shooter}. Comprend :
 * <ul>
 * 	<li> le servo</li>
 * </ul>
 * 
 * @author Étienne
 */
public class Rampe extends Subsystem {	
	
	private Servo servo;
	
	
	//Constructeur par défaut
	public Rampe() {
		super("Rampe");
		
		servo = new Servo(K.Ports.RAMPE_SERVO);
		LiveWindow.addActuator("Rampe", "Servo", servo);
		
	}
	
	@Override
    public void initDefaultCommand() {
        setDefaultCommand(new FermerRampe());
    }
    
	
    /**
     * Bloque le chemin aux balles.
     */
    public void fermer() {
    	servo.setAngle(K.Rampe.POSITION_FERMEE);
    }
    
    
    /**
     * Laisse les balles passer pour aller dans le shooter.
     */
    public void ouvrir() {
    	servo.setAngle(K.Rampe.POSITION_OUVERTE);
    }
    
}

