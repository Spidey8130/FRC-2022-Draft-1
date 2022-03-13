package frc.robot;
// import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsControlModule;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
// import edu.wpi.first.wpilibj.DoubleSolenoid.Value.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DigitalInput;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drive {

    OI oi = new OI();
    Limelight limeLight = new Limelight();

    private CANSparkMax rFMotor = new CANSparkMax(Constants.rFMotorPort, MotorType.kBrushless);
    private CANSparkMax rRMotor = new CANSparkMax(Constants.rRMotorPort, MotorType.kBrushless);
    private CANSparkMax lFMotor = new CANSparkMax(Constants.lFMotorPort, MotorType.kBrushless);
    private CANSparkMax lRMotor = new CANSparkMax(Constants.lRMotorPort, MotorType.kBrushless);


    DigitalInput rFLimit = new DigitalInput(Constants.rFLimitPort);
    DigitalInput rRLimit = new DigitalInput(Constants.rRLimitPort);
    DigitalInput lFLimit = new DigitalInput(Constants.lFLimitPort);
    DigitalInput lRLimit = new DigitalInput(Constants.lRLimitPort);
    //false means limit switch active

    

    

    PneumaticsControlModule PCML = new PneumaticsControlModule(Constants.PCMLPort); //left
    PneumaticsControlModule PCMR = new PneumaticsControlModule(Constants.PCMRPort); //right
 
    Compressor compressor = new Compressor(Constants.PCMLPort, PneumaticsModuleType.CTREPCM);
    private DoubleSolenoid frontdrivePistons = new DoubleSolenoid(Constants.PCMRPort, PneumaticsModuleType.CTREPCM, Constants.fDPForwardChannel, Constants.fDPReverseChannel);
    private DoubleSolenoid reardrivePistons = new DoubleSolenoid(Constants.PCMRPort, PneumaticsModuleType.CTREPCM, Constants.rDPForwardChannel, Constants.rDPReverseChannel);


    //drive objects
    MecanumDrive driveMecanum = new MecanumDrive(lFMotor, lRMotor, rFMotor, rRMotor);
    MotorControllerGroup leftSide = new MotorControllerGroup(lRMotor, lFMotor);
    MotorControllerGroup rightSide = new MotorControllerGroup(rRMotor, rFMotor); 
    DifferentialDrive differentialDrive = new DifferentialDrive(leftSide, rightSide);





    public void drivebaseInit(){
        
        rFMotor.setInverted(true);
        rRMotor.setInverted(true);
        SmartDashboard.putString("Right motors reversed", "Yes");
    }


   




    //limit switches for drivebase tested and work. logic works.
    
    public void dualDrivebase(){
        boolean dropped = oi.dropped(); 
        SmartDashboard.putBoolean("dropped drive", dropped);
        
        double mForward = -oi.mForward(); 
        double mStrafe = oi.mStrafe(); 
        double mRotate = 0.75*oi.mRotate();

        double aForward = -oi.aForward();
        double aRotate = -0.75 *oi.aRotate();
  
        //Values taken from the OI to be fed into this program. 

        if (frontdrivePistons.get() == Value.kForward){ //Puts piston data to the smart dashboard
            SmartDashboard.putBoolean("Front Drive Pistons Down", true);
        }
        else if (frontdrivePistons.get() == Value.kReverse){
            SmartDashboard.putBoolean("Front Drive Pistons Down", false);
        }

        
        if (dropped){ //Takes in boolean and switches solenoid output based on it. 
            frontdrivePistons.set(Value.kForward);
            reardrivePistons.set(Value.kForward);
            }
        else {
            frontdrivePistons.set(Value.kReverse);
            reardrivePistons.set(Value.kReverse);
            }
                
    
        if (dropped){ //Takes in boolean and switches drive output based on it. 
            differentialDrive.arcadeDrive(aForward,  aRotate);
            SmartDashboard.putString("Drivebase", "Arcade");
        }
        else if (dropped == false && lFLimit.get() == false && lRLimit.get() == false && rFLimit.get() == false && rRLimit.get() == false){
            // If solenoids don't drop the motors and all the limits are switched. False means switch is clicked
            driveMecanum.driveCartesian(mForward, mStrafe, mRotate);
            SmartDashboard.putString("Drivebase", "Mecanum");
        }
        else{
           // differentialDrive.tankDrive(0, 0);
            SmartDashboard.putString("Drivebase", "Issues with drivebase");
        }
        SmartDashboard.putNumber("mForward", mForward);
        SmartDashboard.putNumber("mStrafe", mStrafe);
        SmartDashboard.putNumber("mRotate", mRotate);

        
    
    }





    public void limelightDrive(){
        //changes the way the robot turns based on if the robot is in mecanum or arcade mode
        if(oi.limeLightTurn)         {
            if(oi.dropped){
                differentialDrive.arcadeDrive(0, limeLight.PIDC());
            }
            else if(oi.dropped == false){
                //driveMecanum.driveCartesian(0, 0, limeLight.PIDC(), 0);
            }
        }
        
    }







    public void triDrivebase(){
        //This if statement decides if the limelight turn button is clicked and changes how its driven accordingly.
        if(oi.limeLightTurn()){
            limelightDrive();
        }
        else{
            dualDrivebase();
        }
    }







    public void backleftmotortest(){
        lRMotor.set(0.1);
        rRMotor.set(0.1);
    }

    public void limitSwitchTest(){
        SmartDashboard.putBoolean("LF Switch", lFLimit.get());
        SmartDashboard.putBoolean("LR Switch", lRLimit.get());
        SmartDashboard.putBoolean("RF Switch", rFLimit.get());
        SmartDashboard.putBoolean("RR Switch", rRLimit.get());

    }

    public void motorSpeedTest(){
        SmartDashboard.putNumber("rFMotor Speed", rFMotor.get());
        SmartDashboard.putNumber("rRMotor Speed", rRMotor.get());
        SmartDashboard.putNumber("lFMotor Speed", lFMotor.get());
        SmartDashboard.putNumber("lRMotor Speed", lRMotor.get());
    
    }

}



















