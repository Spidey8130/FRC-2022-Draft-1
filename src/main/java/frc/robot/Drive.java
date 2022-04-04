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
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drive {

    OI oi = new OI();
    Limelight limeLight = new Limelight();
    CargoVision cargoVision = new CargoVision();

    private CANSparkMax rFMotor = new CANSparkMax(Constants.rFMotorPort, MotorType.kBrushless);
    private CANSparkMax rRMotor = new CANSparkMax(Constants.rRMotorPort, MotorType.kBrushless);
    private CANSparkMax lFMotor = new CANSparkMax(Constants.lFMotorPort, MotorType.kBrushless);
    private CANSparkMax lRMotor = new CANSparkMax(Constants.lRMotorPort, MotorType.kBrushless);
   // RelativeEncoder rFEncoder = new RelativeEncoder(SparkMaxRelativeEncoder.Type,Constants.rFMotorPort);
    RelativeEncoder lFEncoder = lFMotor.getEncoder();
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



    public void driveTeleop(){
        dualDrivebase(oi.dropped());
        // limelightDrive();
        if (oi.Xbox1.getAButton()) {
            mecanumDrive(0, 0, limeLight.limeLightTurn());
        }
    }

    public void drivebaseInit(){
        rFMotor.restoreFactoryDefaults();
        rRMotor.restoreFactoryDefaults();
        lFMotor.restoreFactoryDefaults();
        lRMotor.restoreFactoryDefaults();
        rFMotor.setInverted(true);
        rRMotor.setInverted(true);
        // SmartDashboard.putString("Right motors reversed", "Yes");
    }

    public void mecanumDrive(double ySpeed, double xSpeed, double zRotate){
        driveMecanum.driveCartesian(ySpeed, xSpeed, zRotate);
    }

   





    //limit switches for drivebase tested and work. logic works.
    
    public void dualDrivebase(boolean droppedP){
        boolean dropped = droppedP; 
        SmartDashboard.putBoolean("dropped drive", dropped);
        
        double mForward = -oi.mForward(); 
        double mStrafe = oi.mStrafe(); 
        double mRotate = 0.75*oi.mRotate();

        double aForward = -oi.aForward();
        double aRotate = 0.75 *oi.aRotate();
        //Values taken from the OI to be fed into this program. 

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
    }





    // public void limelightDrive(){
    //     //changes the way the robot turns based on if the robot is in mecanum or arcade mode
    //     if(oi.limeLightTurn)         {
    //         if(oi.dropped){
    //             differentialDrive.arcadeDrive(0, limeLight.PIDC());
    //             if (limeLight.PIDC() == 0) {
    //                 SmartDashboard.putBoolean("Shooter Aimed", true);
    //             }
    //             else {
    //                 SmartDashboard.putBoolean("Shooter Aimed", false);
    //             }
    //         }
    //         else if(oi.dropped == false){
    //             driveMecanum.driveCartesian(0, 0, limeLight.PIDC(), 0);
    //         }
    //     }
        
    // }







    // public void triDrivebase(){
    //     //This if statement decides if the limelight turn button is clicked and changes how its driven accordingly.
    //     if(oi.limeLightTurn()){
    //         limelightDrive();
    //     }
    //     else{
    //         dualDrivebase(oi.dropped());
    //     }
    // }



    // boolean toggle = false;
    // public void reverseRightMotors(boolean input){
    
    //     if (input){
    //         toggle = !toggle;
    //     }
    //     boolean reverse = toggle;

    //         rFMotor.setInverted(reverse);
    //         rRMotor.setInverted(reverse);
    //         SmartDashboard.putBoolean("rF is Inverted", rFMotor.getInverted());
    // }
    // public boolean rightMotorsReversed(){
    //     return rFMotor.getInverted();
    // }
    // public double lFEncoderValue(){
    //     return lFEncoder.getPosition(); //negative position is forward
    // }



    // public void driveToCargo() {
    //     if(oi.cargoTurn) {
    //         if(oi.dropped){
    //             differentialDrive.arcadeDrive(0, cargoVision.getRotationValue());
    //         }
    //         else if(oi.dropped == false){
    //             driveMecanum.driveCartesian(0, 0, cargoVision.getRotationValue());
    //         }
    //     }
    // }
}



















