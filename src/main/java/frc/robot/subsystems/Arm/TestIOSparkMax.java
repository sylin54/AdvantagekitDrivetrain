package frc.robot.subsystems.Arm;

import edu.wpi.first.math.geometry.Pose2d;

public class TestIOSparkMax implements TestIO {

  Pose2d pose2d = new Pose2d();
  int num = 0;

  @Override
  public void updateInputs(TestIOInputs inputs) {
    pose2d = inputs.pose2d;
    num = inputs.num;
  }
}
