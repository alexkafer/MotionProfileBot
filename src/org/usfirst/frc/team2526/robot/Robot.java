package org.usfirst.frc.team2526.robot;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically it 
 * contains the code necessary to operate a robot with tank drive.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
    CANTalon leftOne, leftTwo;
    CANTalon rightOne, rightTwo;
    
    RobotDrive drive;
    
    Joystick leftStick;  // set to ID 1 in DriverStation
    
    File motionProfile;
    public Robot() {
        leftOne = new CANTalon(2);
        leftTwo = new CANTalon(1);
        rightOne = new CANTalon(4);
        rightTwo = new CANTalon(3);
        
        drive = new RobotDrive(leftOne, leftTwo, rightOne, rightTwo);
        
        leftStick = new Joystick(0);
        
        SmartDashboard.putBoolean("Reset", true);
        
        File motionProfile = new File("motionprofile.txt");
        
        try {
			if (motionProfile.createNewFile()){
			    System.out.println("Creating Motion Profile File");
			  }else{
			    System.out.println("Found Motion Profile File");
			  }
		} catch (IOException e) {
			System.out.println("Sad robot is sad.");
			e.printStackTrace();
		}
    }

    
    /**
     * Runs the motors with tank steering.
     */
    public void operatorControl() {
    	long startTime = System.currentTimeMillis();
    	
    	
    	FileWriter writer = null;
		try {
			writer = new FileWriter(motionProfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		BufferedWriter bufferedwriter = null;
		if (writer != null) 
			bufferedwriter = new BufferedWriter(writer);
    	
        while (isOperatorControl() && isEnabled()) {
        	
        	drive.arcadeDrive(leftStick);
        	
        	if (SmartDashboard.getBoolean("Reset")) {
        		startTime = System.currentTimeMillis();
        	}
        	
        	// Recording. 
        	long elapsedTime = System.currentTimeMillis()-startTime;
            Timer.delay(0.005);		// wait for a motor update time
            
            String logLine = elapsedTime 
            + ":" + leftOne.get() 
            + ":" + leftTwo.get() 
            + ":" + rightOne.get() 
            + ":" + rightTwo.get();
            
            try {
				if (bufferedwriter != null) 
					bufferedwriter.write(logLine);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    public void autonomous() {
        // Play!
    	long startTime = System.currentTimeMillis();
    	
    	while (isAutonomous() && isEnabled()) {
        	// Recording. 
        	long elapsedTime = System.currentTimeMillis()-startTime;
        	String getLogLineAtElaspsedTime = "1:0:0:0:0";
        	
        	
        	
        	String[] data = getLogLineAtElaspsedTime.split(":");
        	
        	double leftOneSpeed = Double.parseDouble(data[1]);
        	double leftTwoSpeed = Double.parseDouble(data[2]);
        	double rightOneSpeed = Double.parseDouble(data[3]);
        	double rightTwoSpeed = Double.parseDouble(data[4]);
        	
        	leftOne.set(leftOneSpeed);
        	leftTwo.set(leftTwoSpeed);
        	rightOne.set(rightOneSpeed);
        	rightTwo.set(rightTwoSpeed);
        	
            Timer.delay(0.005);		
        }
    }
}
