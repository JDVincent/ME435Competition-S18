package edu.rosehulman.golfballdelivery;

import android.os.Handler;
import android.widget.Toast;

import edu.rosehulman.me435.NavUtils;
import edu.rosehulman.me435.RobotActivity;

/**
 * Created by localmgr on 4/24/2018.
 */

public class Scripts {

    private Handler mCommandHandler = new Handler();

    private GolfBallDeliveryActivity mActivity;

    private  int ARM_REMOVAL_TIME = 3000;

    public Scripts(GolfBallDeliveryActivity activity){
        mActivity = activity;
    }

    public void testStraightScript() {
        // TODO: Implement
        mActivity.sendWheelSpeed(mActivity.mLeftStraightPwmValue, mActivity.mRightStraightPwmValue);
        Toast.makeText(mActivity, "Begin Driving", Toast.LENGTH_SHORT).show();
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendWheelSpeed(0,0);
                Toast.makeText(mActivity, "Stop Driving", Toast.LENGTH_SHORT).show();
            }
        }, 8000);
    }


    public void nearBallScript() {
        double distanceToNearball = NavUtils.getDistance(15, 0, 90, 50);
        long driveTimeMs = (long) (distanceToNearball / RobotActivity.DEFAULT_SPEED_FT_PER_SEC * 1000);

        // TODO For testing make it shorter!

        mActivity.sendWheelSpeed(mActivity.mLeftStraightPwmValue, mActivity.mRightStraightPwmValue);
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendWheelSpeed(0,0);
                removeBallAtLocation(mActivity.mNearBallLocation);

            }
        }, driveTimeMs);

        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mActivity.mState == GolfBallDeliveryActivity.State.NEAR_BALL_SCRIPT){
                    mActivity.setState(GolfBallDeliveryActivity.State.DRIVE_TOWARDS_FAR_BALL);
                }
            }
        }, driveTimeMs + ARM_REMOVAL_TIME);


    }

    public void farBallScript() {
        // TODO implement
    }

    private void removeBallAtLocation(final int location){

        mActivity.sendCommand("ATTACH 111111");

        // TODO Really remove a ball with your arm
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendCommand("POSITION 83 90 0 -90 90");
            }
        }, 10);

        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendCommand("POSITION 90 141 -60 -180 169");
            }
        }, 2000);

        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.setLocationToColor(location, GolfBallDeliveryActivity.BallColor.NONE);
            }
        }, ARM_REMOVAL_TIME);
    }

}
