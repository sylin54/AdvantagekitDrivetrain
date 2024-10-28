package frc.robot.subsystems.Arm;

import edu.wpi.first.math.geometry.Pose2d;
import org.littletonrobotics.junction.AutoLog;

public interface TestIO {

  @AutoLog
  public static class TestIOInputs {
    int num;
    Pose2d pose2d;
  }

  public default void updateInputs(TestIOInputs inputs) {}
}
