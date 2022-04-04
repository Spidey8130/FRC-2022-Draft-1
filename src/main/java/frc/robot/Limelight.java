package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.controller.PIDController;


public class Limelight extends TimedRobot {
    //Drive drive = new Drive(); 
    OI oi = new OI();
    PIDController limelightPID = new PIDController(0.05, 0.0, 0.0);

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry tv = table.getEntry("tv");
    
    //read values periodically


    

    // tv	Whether the limelight has any valid targets (0 or 1)
    // tx	Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
    // ty	Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
    // ta	Target Area (0% of image to 100% of image)
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);
    double v = tv.getDouble(0.0);

    // double absx = x/Math.abs(tx.getDouble(0.0));

    // double integral = 0;
    // double derivative = 0;
    // double PID = 0;
    // double PIDC = 0;
    // double P = 1;
    // double I = 1;
    // double D = 1;
    // double previous_error = 0;






    public double limeLightTurn() {
        double turn;
        turn = -limelightPID.calculate(tx.getDouble(-0.5));
        return turn;
    }







    // public double PIDC(){
    //     //error is self explanatory
    //     //constant is the number the PID value is multiplied by to make it managable
    //     double error = x;
    //     double constant = 0.0005;

    //     if (oi.limeLightTurn == false){
    //         integral = 0;
    //         derivative = 0;
    //     //Resets these two values after each time the limelight turn is used.
    //     }
    
    //     double sign = Math.abs(error)/error;
    //     /*Gets the sign that the PID needs to be multiplied by so that the 
    //       motors will turn in the right direction*/
    
    //     /*Using this "If" statement so that these values are only changed
    //       whenever the limelight turning is being used*/
    //     if (oi.limeLightTurn()){
    //     /*Integral is "area of the curve", so it just adds up all the past values */
    //     integral += (Math.abs(error)*.02); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
    //     /*Derivative is the slope, so that's self explanatory for calc students */
    //     derivative = (error - previous_error) / .02;
        
    //     /*Getting the PID value.*/
    //     PID = 50*P*Math.abs(error) + I*integral + D*Math.abs(derivative);
    
    //     //Keeps our error from before so that we can use the derivative. 
    //     previous_error = error;
    
    //     //Multiples our PID by a constant we can control and our direction
    //     PIDC = PID * constant *sign;
    
    
    
    //         if (Math.abs(PIDC) <= 0.1 && Math.abs(error) >= 5)
    //         {
    //             //So that we are always moving
    //             PIDC = 0.1 * sign;
    //         }
    //         else if (Math.abs(error) <= 5)
    //         {
    //             //A small buffer so it doesn't overcorrect.
    //             PIDC = 0;
    //         }
    //     }
    
    //     else {
    //     //if the limelight drive isn't being used, returns a value of 0.
    //     PIDC = 0;
    //     }
    //     // SmartDashboard.putNumber("P", error);
    //     // SmartDashboard.putNumber("I", integral);
    //     // SmartDashboard.putNumber("D", derivative);
    //     // SmartDashboard.putNumber("PID", PID);    
    
    //     SmartDashboard.putNumber("PIDC", PIDC);
    //     return PIDC;
    // }


    public double limeLightDistanceInches(){
        double targetOffsetAngle_Vertical = ty.getDouble(0.0);
    
    // how many degrees back is your limelight rotated from perfectly vertical?
    double limelightMountAngleDegrees = 26.0 ;
    
    // distance from the center of the Limelight lens to the floor
    double limelightLensHeightInches = 32.0;
    
    // distance from the target to the floor
    double goalHeightInches = 104.0;
    
    double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
    double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);
    
    //calculate distance
    oi.distanceFromLimelightToGoalInches = (goalHeightInches - limelightLensHeightInches)/Math.tan(angleToGoalRadians);
        SmartDashboard.putNumber("Distance To Goal", oi.distanceFromLimelightToGoalInches);
       
        return oi.distanceFromLimelightToGoalInches;
        // SmartDashboard.putNumber("test", ty.getDouble(0.0));
    }
    
    


}