// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.AutobahnConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.SwerveConstants;
import frc.robot.hardware.RobotWheelMover;
import frc.robot.subsystems.Swerve;
import frc.robot.util.Communicator;
import frc.robot.util.CustomMath;
import frc.robot.util.controller.FlightStick;
import org.github.godbrigero.Autobahn;
import org.pwrup.SwerveDrive;
import org.pwrup.util.Config;
import org.pwrup.util.Vec2;
import org.pwrup.util.Wheel;

public class RobotContainer {

  // private final SwerveSubsystem m_swerveSubsystem = new SwerveSubsystem();

  final FlightStick m_leftFlightStick = new FlightStick(
    OperatorConstants.kFlightPortLeft
  );

  final FlightStick m_rightFlightStick = new FlightStick(
    OperatorConstants.kFlightPortRight
  );

  final Swerve m_swerveSubsystem = new Swerve();

  final Autobahn m_autobahnSubsystem;

  public RobotContainer() {
    this.m_autobahnSubsystem =
      new Autobahn(
        AutobahnConstants.kAutobahnHost,
        AutobahnConstants.kAutobahnPort
      );
  }

  public void teleopInit() {
    m_swerveSubsystem.setDefaultCommand(
      new RunCommand(
        () -> {
          m_swerveSubsystem.drive(
            new Vec2(
              CustomMath.deadband(
                m_rightFlightStick.getRawAxis(
                  FlightStick.AxisEnum.JOYSTICKY.value
                ) *
                -1,
                SwerveConstants.kXSpeedDeadband,
                SwerveConstants.kXSpeedMinValue
              ),
              CustomMath.deadband(
                m_rightFlightStick.getRawAxis(
                  FlightStick.AxisEnum.JOYSTICKX.value
                ),
                SwerveConstants.kYSpeedDeadband,
                SwerveConstants.kYSpeedMinValue
              )
            ),
            CustomMath.deadband(
              m_rightFlightStick.getRawAxis(
                FlightStick.AxisEnum.JOYSTICKROTATION.value
              ),
              SwerveConstants.kRotDeadband,
              SwerveConstants.kRotMinValue
            ),
            0.2
          );
        },
        m_swerveSubsystem
      )
    );

    new JoystickButton(m_leftFlightStick, FlightStick.ButtonEnum.A.value)
      .onTrue(
        m_swerveSubsystem.runOnce(() -> {
          m_swerveSubsystem.resetGyro();
        })
      );
  }
}
