package frc.robot.subsystems.Arm;

import java.util.function.Supplier;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj.DigitalInput;


//class to help me learn how to use this library
public class Arm {
    private TalonFX ShooterMotor1 = new TalonFX(0);
    private TalonFX ShooterMotor2 = new TalonFX(0);
    private CANSparkMax intake = new CANSparkMax(0, MotorType.kBrushless);

    private RelativeEncoder intakeEncoder;

    //returns wether the limit switch is pressed
    private DigitalInput limitSwitch = new DigitalInput(0);

    //a debounce filter removes faast on and off periods(this is for the limti switch)
    private Debouncer limitSwitchDebouncer = new Debouncer(0.1, DebounceType.kBoth);

    //do some testing for whats consistent
    private int limitSwitchRate = 100; // Hz

    private boolean brakeMode = false;

    private InterpolatingDoubleTreeMap shootMap =  new InterpolatingDoubleTreeMap();

    public Arm() {
      buildMap();

      ShooterMotor1.setNeutralMode(NeutralModeValue.Brake);
      ShooterMotor2.setNeutralMode(NeutralModeValue.Brake);


      double limit = 40;
      //current limits on the shooters
      ShooterMotor1.getConfigurator().apply(new CurrentLimitsConfigs().withStatorCurrentLimitEnable(true).withStatorCurrentLimit(limit).withSupplyCurrentLimitEnable(true).withSupplyCurrentLimit(limit).withSupplyCurrentThreshold(limit+5));
      ShooterMotor2.getConfigurator().apply(new CurrentLimitsConfigs().withStatorCurrentLimitEnable(true).withStatorCurrentLimit(limit).withSupplyCurrentLimitEnable(true).withSupplyCurrentLimit(limit).withSupplyCurrentThreshold(limit+5));
      //current limits on the aarm
      //SPARK
      //brakes on the motors so it doesnt stop abruptly
      intake.setIdleMode(IdleMode.kBrake);


      //this might need to be put in the intake function
      intakeEncoder = intake.getEncoder();

    }

    private void mySuperFastLoop() {
      while (true) {
        // do some fast stuff

        if(!brakeMode) return;


        boolean limitSwitch = getSwitch();
        
        if(!limitSwitch) return;

        setShootSpeed(0);
        setIntakeSpeed(0);

        try {
          Thread.sleep(1000 / limitSwitchRate);
        } catch (InterruptedException ex) {}
      }
    }


    //inits the fast thread responsible for handling hte limit switch
    public void initAsyncThread() {
      Thread superFastLoopThread = new Thread(this::mySuperFastLoop);
      superFastLoopThread.setDaemon(true);
      superFastLoopThread.setName("LimitSwitch faster thread");
      superFastLoopThread.start();
    }



    //might need to replace it with the other motor if we're having issues
    public double getShooterSpeed() {
      return Math.abs(ShooterMotor1.get());
    }


    public void setShootSpeed(double speed){

      if(brakeMode) speed = 0;
        //ArmMotor.set(armPower);
        ShooterMotor1.set(speed); // both run the shooter
        ShooterMotor2.set(speed);
      }
      /**
       */
    public void setIntakeSpeed(double speed){
  
      if(brakeMode) speed = 0;

        double Velocity = intakeEncoder.getVelocity();
        //calculates in rpm
        //if intake power goes up to -1 it slows down gradually and if it goes to 0 goes up for speed boost
        System.out.println(speed);
        if(speed < 0) {
          speed = -0.4 * (450-(Velocity*-1))/450;
          if(speed > 0) {
            speed = -0.3;
          }
        } else {
          //intakePower = intakePower;
        }
        intake.set(speed);
    }

    //it is reccomendec to use thsi in class instead of the limitswitch.get function to remove debounce times(debouncer class)
    public boolean getSwitch() {
      return limitSwitchDebouncer.calculate(limitSwitch.get());
    }

    //sets wether the robot stops intake and shootre motors on limit switch detection
    public void setBrakeMode(boolean brakeMode) {
      this.brakeMode = brakeMode;
    }

    public double getShooterAngle(double distance) {
      return shootMap.get(distance);
    }

    public double getShooterAngle(Supplier<Double> distance) {
      return shootMap.get(distance.get());
    }

    private void buildMap() {
      shootMap.put(0d, 0d);
    }
}
