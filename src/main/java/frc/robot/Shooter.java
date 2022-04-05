package frc.robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {
    OI oi = new OI();
    Limelight limelight = new Limelight();
    Intake intake; 
    private TalonFX shooterMotor;
    private TalonSRX hoodMotor;
    private double percentOutput;
    private double LastOutput; 
    private double Kp;
    double dx = limelight.distanceFromLimelightToGoalInches;
    private double shooterSpeed =  -37.06963 * dx - 7361.12338;
    private double hoodValue = 9.51786 * dx - 1253.00509;

    public Shooter(Intake in){
        shooterMotor = new TalonFX(Constants.shooterMotorPort);
        hoodMotor = new TalonSRX(Constants.hoodMotorPort);
        percentOutput = 0;
        LastOutput = 0;
        Kp = 0.00000105;
        intake = in;
    }

    public void shooterInit(){
        hoodMotor.configFactoryDefault();
        hoodMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        hoodMotor.setSelectedSensorPosition(0);
        shooterMotor.configFactoryDefault();
        shooterMotor.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 10);
    }

    public void shooterTeleop(){
        shooterMotor(Math.abs(oi.targetSpeedManual()));
        
        //hoodMotor(oi.hoodSpeed());


        // shooterMotor(oi.shootConfigs());
        //hoodMotor(oi.hoodConfigs());
        
    }

    boolean shouldShoot;
    
    public void shooterMotor(double targetSpeed){
        shooterMotor.setNeutralMode(NeutralMode.Coast);
        double fxspd = shooterMotor.getSelectedSensorVelocity();
        double difference  = Math.abs(targetSpeed) - Math.abs(fxspd);
        double error = difference*Kp;
        LastOutput = LastOutput + error;
        percentOutput = LastOutput;
        if(percentOutput > 1){
            percentOutput = 1;
        }
        if (targetSpeed == 0){
            percentOutput = 0;
            difference = 0;
            error = 0;
            LastOutput = 0;
        }  
        if(Math.abs(difference)<50){
            if(targetSpeed != 0){
                shouldShoot=true;
                
                intake.translateMotor(oi.translateRunSpeed);}
        }
        else{shouldShoot=false;}   
        SmartDashboard.putBoolean("ShouldYouShoot", shouldShoot);




    
       // if(oi.Xbox2.getRightBumper()){
        shooterMotor.set(ControlMode.PercentOutput, shooterSpeed);
       // }
        // else{
        //     shooterMotor.set(ControlMode.PercentOutput, 0);
        // }
        // SmartDashboard.putNumber("difference", difference);
        // SmartDashboard.putNumber("FX speed", fxspd);
        // SmartDashboard.putNumber("oitargetSpeed", oi.targetSpeedManual());
        // SmartDashboard.putNumber("percentOutput", percentOutput);
        // SmartDashboard.putNumber("error", error);
    }

    public void hoodMotor(double hoodSpeed){
        if(hoodSpeed!=0){
        hoodMotor.set(ControlMode.PercentOutput, hoodSpeed);
        }
        else{
            hoodMotor.set(ControlMode.PercentOutput, 0);
        }
        }

    public void hoodMotorRunToPos(double targetPos){
        hoodMotor.set(ControlMode.Position, targetPos);
    }

    public double hoodEncoder(){
        double hoodPos = hoodMotor.getSelectedSensorPosition();
        SmartDashboard.putNumber("HoodPosition", hoodPos);
        return hoodPos;
    }
    
    public double talonFXSpeed(){
        return shooterMotor.getSelectedSensorVelocity();
    }
    
    
    
}


