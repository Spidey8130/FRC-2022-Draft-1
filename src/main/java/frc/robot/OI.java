package frc.robot;
//import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.Default;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class OI {
    Limelight limelight = new Limelight();
    
    //Xbox object
    XboxController Xbox1 = new XboxController(0);
    XboxController Xbox2 = new XboxController(1);

    double deadzone = 0.2;
    double shootAutomatedSpeed = 0;
    double hoodAutomatedPos = 0;
    boolean shootNow = false;

    

   
   
   
   
   
   
   
   
   
    //drive piston drops
    boolean dropped = false;
    boolean dropped(){
       
        if(Xbox1.getBButtonReleased()){ //&& Xbox1.getLeftTriggerAxis() >= 0.5){
            dropped = !dropped;
        }
        return dropped;
       
    }
    //mecanum drive values
    double mForwardSpeed = 0;
    double mForward(){
        if (Math.abs(Xbox1.getLeftY()) <= deadzone){
            mForwardSpeed = 0;
        }
        else{
            mForwardSpeed = Xbox1.getLeftY();
        }
        return mForwardSpeed;
    }
    double mStrafeSpeed = 0;
    double mStrafe(){
        if (Math.abs(Xbox1.getLeftX()) <= deadzone){
            mStrafeSpeed = 0;
        }
        else{
            mStrafeSpeed = Xbox1.getLeftX();
        }
        return mStrafeSpeed;
    }
    double mRotateSpeed = 0;
    double mRotate(){
        if (Math.abs(Xbox1.getRightX()) <= deadzone){
            mRotateSpeed = 0;
        }
        else{
            mRotateSpeed = Xbox1.getRightX();
        }
        return mRotateSpeed;
    }
    //arcade drive values
    double aForward(){
        return Xbox1.getLeftY();
    }
    double aRotate(){
        return Xbox1.getRightX();
    }

    boolean climb = Xbox2.getBButton();
















    double winchSpeed(){
        if(Xbox2.getRightTriggerAxis() >=0.5){
            return -0.5;
        }
        else if(Xbox2.getLeftTriggerAxis() >=0.5){
            return 1;
        }
        else{
            return 0;
        }
        // return 0;
    }
    boolean winchUp = false;
    boolean winchUp(){
       
        if(Xbox2.getXButtonReleased()){
            winchUp = !winchUp;
        }
        return winchUp;
    }
    boolean clampOut = false;
    boolean clampOut(){
        if(Xbox2.getAButtonReleased()){
            clampOut = !clampOut;
        }
        return clampOut;
    }

    double yellowMotorSpeed(){
        if(Xbox2.getRightTriggerAxis() >= 0.5)
        {
            return 0.75;
        }
        else if (Xbox2.getLeftTriggerAxis() >=0.5)
        {
            return -0.5;
        }
        else{
            return 0; 
        }
    }
    boolean climbStepBool1 = false;
    boolean climbStepBool2 = false;
    boolean climbStepBool3 = false;
    public boolean climbStep(){
        return Xbox2.getBButtonPressed();

        // if(climbStepBool2 == true && climbStepBool1 == false){
        //     climbStepBool2 = false;
        // }
        // if(Xbox2.getBButtonReleased() && climbStepBool1 != true){
        //     climbStepBool1 = true;
        // }
        // else if(Xbox2.getBButtonReleased() && climbStepBool1 == true){
        //     climbStepBool2 = true;
        //     climbStepBool1 = false;
        // }
        // if(climbStepBool2){
        //     return true;

        // }
        // else{
        //     return false;
        // }
    }

    boolean winchPosBool = false;
    double winchPos(){
        if(Xbox2.getBButtonReleased()){
            winchPosBool = !winchPosBool;
        }
        if(winchPosBool){
            return -80;
        }
        else{
            return 0;
        }
    }

    boolean resetClimber(){
        if(Xbox2.getPOV()==180) {
            return true;
        }
        else {
            return false;
        }
    }
   
   
   
   
   
   
   
   
    double intakeRunSpeed = -1;
    double intakeSpeed(){
        if(Xbox1.getRightTriggerAxis() >=0.9){
            return intakeRunSpeed;
        }
        // else if(Xbox1.getRightBumper()){
        //     return 0.5;
        // }
        else{
            return 0;
        }
      }

    double translateRunSpeed = -0.5;
    double translateSpeed(){
        if(Xbox2.getLeftBumper()){
            return translateRunSpeed;
        }  
        // else if(Xbox1.getRightBumper()){
        //     return 1;
        // }
        else{
            return 0;
        }
      //  return 0;
    }
    boolean intakeOut = true;
    boolean intakeSolenoid(){
        if(Xbox1.getRightTriggerAxis() >=0.9){
            intakeOut = false;
        }
        else{
            intakeOut = true;
        }
        return intakeOut;
        // return Xbox1.getAButton();
    }

    double speed = -13250;
    double targetSpeedManual(){
     SmartDashboard.putNumber("Target Shooter Speed", speed);
        if(Xbox2.getBackButtonReleased()){
            speed += 250;
        }
        if(Xbox2.getStartButtonReleased()){
            speed += -250;
        }
        if(speed > 0){
            speed = 0;
        }
        if(Xbox2.getRightBumper()){
              return speed;
        }
        else{
               return 0;
        }
      }
      
    double hoodSpeed(){
        if (Xbox1.getLeftTriggerAxis() == 1){
            return 0.15;
        }
        else if (Xbox1.getRightTriggerAxis() == 1){
            return -0.15;
        }
        else{
            return 0;
        }
        //return 0;
    }

    
    int shooterConfigInt = 1;
    public void shooterTeleopConfigSwitch(){
        switch (Xbox2.getPOV()){
            case 270:
            shooterConfigInt = 1;
            break;
            case 0:
            shooterConfigInt = 2;
            break;
            case 90:
            shooterConfigInt = 3;
            break;
        }
        switch (shooterConfigInt){
            case 1: //at the edge of tarmac
                hoodAutomatedPos = 0;
                shootAutomatedSpeed = -13250;
                SmartDashboard.putString("Shooter Configuration", "Edge of Tarmack");
            break;
            case 2: //from launchpad
                hoodAutomatedPos = 1322;
                shootAutomatedSpeed = -15750;
                SmartDashboard.putString("Shooter Configuration", "Launchpad");
            break;
            case 3: //from back wall
                hoodAutomatedPos = 1830;
                shootAutomatedSpeed = -20000;            
                SmartDashboard.putString("Shooter Configuration", "Back Wall");

            break;
        }
    }

    public double hoodConfigs(){
    return hoodAutomatedPos;

    }
    public double shootConfigs(){
        if(Xbox2.getRightBumper()){
            return shootAutomatedSpeed;
            }
            else{
                return 0;
            }
    }








    boolean limeLightTurn = false;
    boolean limeLightTurn(){
    if(Xbox1.getAButton()){
        limeLightTurn = true;
    }    
    else{
        limeLightTurn = false;
    }
    return limeLightTurn;
    
    }



    boolean cargoTurn = false;
    boolean cargoTurn(){
    if(Xbox1.getYButton()){
        cargoTurn = true;
    }    
    else{
        cargoTurn = false;
    }
    return cargoTurn;
    
    }

 





    

}

    


















