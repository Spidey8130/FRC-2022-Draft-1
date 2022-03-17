// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
  Drive drive = new Drive();
  Intake intake = new Intake();
  Shooter shooter = new Shooter();
  Climber climber = new Climber();
  Limelight limelight = new Limelight();
  CargoVision cargoVision = new CargoVision();
  OI oi = new OI();
  Auto auto = new Auto(drive, intake, shooter, limelight, cargoVision);
  private static final String kRedAuto = "Red Auto";
  private static final String kBlueAuto = "Blue Auto";
  private String m_autoSelected;
  public final SendableChooser<String> m_chooser = new SendableChooser<>();
  

  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Red Auto", kRedAuto);
    m_chooser.addOption("Blue Auto", kBlueAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    if (m_chooser.getSelected() == kRedAuto) {
      auto.autoInit(Constants.redAlliance);
    }
    else if (m_chooser.getSelected() == kBlueAuto) {
      auto.autoInit(Constants.blueAlliance);
    }
    drive.drivebaseInit(); 
    shooter.shooterInit();
    intake.intakeInit();
  }

 
  @Override
  public void robotPeriodic() {
    SmartDashboard.putBoolean("rF is Inverted", drive.rightMotorsReversed());
    // drive.lFEncoder.setInverted(true);
    // SmartDashboard.putBoolean("lFEncoder Inverted", drive.lFEncoder.getInverted());
  }

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    m_autoSelected = SmartDashboard.getString("Auto Selector", kRedAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  @Override
  public void autonomousPeriodic() {
  auto.auto();
  }

  @Override
  public void teleopInit() {
    //intake.intakeSolenoid(true); //up
    drive.lFEncoder.setPosition(0);
    shooter.shooterInit();
    //climber.clampPiston(true);
  }

  @Override
  public void teleopPeriodic() {
    drive.driveTeleop();
    intake.intakeTeleop();
    shooter.shooterTeleop();
    cargoVision.getDriverView();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}
}
