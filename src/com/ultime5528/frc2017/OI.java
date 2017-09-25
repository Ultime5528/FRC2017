package com.ultime5528.frc2017;

import com.ultime5528.frc2017.commands.FermerTreuil;
import com.ultime5528.frc2017.commands.Grimper;
import com.ultime5528.frc2017.commands.OuvrirRampe;
import com.ultime5528.frc2017.commands.OuvrirTreuil;
import com.ultime5528.frc2017.commands.RemonterBalles;
import com.ultime5528.frc2017.commands.SafeSpin;
import com.ultime5528.frc2017.commands.StopShoot;
import com.ultime5528.frc2017.commands.Viser;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * Operator Interface. Crée le lien entre la console de contrôle et les commandes/groupes de commandes 
 * qui contrôlent le robot.
 */
public class OI {
	
	//Joystick principal
	private Joystick stick = new Joystick(0);
	private JoystickButton button2 = new JoystickButton(stick, 2);
	private JoystickButton button3 = new JoystickButton(stick, 3);
	private JoystickButton button9 = new JoystickButton(stick, 9);
	private JoystickButton button10 = new JoystickButton(stick, 10);
	private JoystickButton button11 = new JoystickButton(stick, 11);
	
	
	//XBox
	private XboxController gamepad = new XboxController(1);
	private JoystickButton buttonA = new JoystickButton(gamepad, 1);
	private JoystickButton buttonB = new JoystickButton(gamepad, 2);
	private JoystickButton buttonX = new JoystickButton(gamepad, 3);
	
	
	public OI() {
		
		stick = new Joystick(0);
		
		button2.toggleWhenPressed(new RemonterBalles());
		button3.toggleWhenPressed(new Viser());
		button9.whenPressed(new OuvrirTreuil());
		button10.whenPressed(new FermerTreuil());
		button11.whileHeld(new Grimper());
		
		buttonA.whenPressed(new SafeSpin());
		buttonB.whenPressed(new OuvrirRampe());
		buttonX.whenPressed(new StopShoot());
		
	}
	
	/**
	 * Retourne le joystick principal.
	 * @return Le joystick principal (port 0).
	 */
	public Joystick getJoystick() {
		return stick;
	}
	
	
}
