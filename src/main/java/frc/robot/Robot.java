// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private final SpeedControllerGroup m_leftMotors = 
      new SpeedControllerGroup(
        new WPI_TalonFX(1), 
        new WPI_TalonFX(2));
    
  private final SpeedControllerGroup m_rightMotors = 
      new SpeedControllerGroup(
        new WPI_TalonFX(3), 
        new WPI_TalonFX(4));
    
  private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotors, m_rightMotors);

  private final XboxController controller = new XboxController(1);

  private final Timer m_timer = new Timer();

  private JSONArray xValues = new JSONArray();
  private JSONArray yValues = new JSONArray();

  private JSONObject jsonObject = new JSONObject();

  private boolean written = false;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {

  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {

  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    m_timer.reset();
    m_timer.start();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double x = controller.getRawAxis(1);
    double y = controller.getRawAxis(4);

    m_drive.arcadeDrive(x, y);

    if (m_timer.get() < 60) {
      xValues.add(x);
      yValues.add(y);
    } else {
      if(!written) {
        jsonObject.put("x", xValues);
        jsonObject.put("y", yValues);

        try {
          FileWriter file = new FileWriter("/home/lvuser/test.json");
          file.write(jsonObject.toJSONString());
          file.close();
        } catch (IOException e) {
          e.printStackTrace();
        }

        written = true;
      }
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
