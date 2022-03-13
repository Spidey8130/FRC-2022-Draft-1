package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Intake {
    OI oi = new OI();
    
    private CANSparkMax intake = new CANSparkMax(Constants.intakeMotorPort, MotorType.kBrushless);
    private CANSparkMax translate = new CANSparkMax(Constants.translateMotorPort, MotorType.kBrushless);
    DigitalInput translateSwitch = new DigitalInput(4);
    DoubleSolenoid intakeSolenoid = new DoubleSolenoid(Constants.PCMRPort, PneumaticsModuleType.CTREPCM, Constants.intakeForwardChannel, Constants.intakeReverseChannel);


    public void intakeMotor(){
        intake.set(oi.intakeSpeed()); 
    }
    public void translateMotor(){
        if(oi.Xbox1.getLeftBumper() && translateSwitch.get() == true){
            translate.set(oi.translateRunSpeed);
        }
        else{
            translate.set(oi.translateSpeed());
        }
    }



    public void intakeSolenoid(){
         if (oi.intakeSolenoid()){ //Takes in boolean and switches solenoid output based on it. 
            intakeSolenoid.set(Value.kForward);
            }
        else {
            intakeSolenoid.set(Value.kReverse);
            }
    }
}
