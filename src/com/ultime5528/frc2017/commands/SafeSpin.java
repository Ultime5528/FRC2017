package com.ultime5528.frc2017.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * 
 * 
 * @author Etienne
 */
public class SafeSpin extends CommandGroup {

    public SafeSpin() {
        
    	//BackSpin pendant un petit délai
    	addParallel(new BackSpin());
    	addSequential(new WaitCommand("Attente SafeSpin", 0.25));
    	
    	//Spin normal
    	addSequential(new Spin());
    	
    }
    
}
