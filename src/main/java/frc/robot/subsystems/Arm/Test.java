package frc.robot.subsystems.Arm;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Test extends SubsystemBase {

  private final TestIO testIO;
  private final TestIOInputsAutoLogged testIOInputsAutoLogged = new TestIOInputsAutoLogged();

  @AutoLogOutput
  private Pose2d testOutput = new Pose2d();

  public Test(TestIO testIO) {
    this.testIO = testIO;
  }

  @Override
  public void periodic() {
    testIO.updateInputs(testIOInputsAutoLogged);
    Logger.processInputs("Test/Test", testIOInputsAutoLogged);

    testOutput.rotateBy(new Rotation2d(testIOInputsAutoLogged.num));
  }

  public void setPose(Pose2d pose2d) {
    testIOInputsAutoLogged.pose2d = pose2d;
  }

  public void setNumber(int num) {
    testIOInputsAutoLogged.num = num;
  }
}
