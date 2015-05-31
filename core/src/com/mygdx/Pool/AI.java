package com.mygdx.Pool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.Pool.Ball;
import com.mygdx.Pool.PoolTable;


/**
 * Created by Owen Li on 5/11/2015.
 */
public class AI {
    double minDist = 0;
    Vector2 targetDirection;
    double angleOff = 0;
    Ball closestBall;

    /**
     * @param balls  the set of all the balls in the game
     * @param player which player the AI is 0 for solids, 1 for stripes
     * @param tab    the table within which all this is occuring Return Vector2
     *               direction cue ball goes
     */
    public Vector2 choice(Ball[] balls, int player, PoolTable tab, double FRICTION, double energyTransfer) {

        Ball cueBall = balls[0];
        PoolTable gameTable = tab;

        closestBall = null; // what ball to start with?
        if (player == 0) {
            closestBall = balls[1];
        } else {
            closestBall = balls[15];
        }

        double minDist = cueBall.getDistance(closestBall);
        for (Ball i : balls) // finds which ball is closest
        {
            if (isMyBall(player, i.getNum())) {
                if (cueBall.getDistance(i) < minDist) {
                    minDist = cueBall.getDistance(i);
                    closestBall = i;
                }
            }
        }

        Vector2 closestPocket = evaluatePockets(closestBall, balls, gameTable);

        targetDirection = new Vector2((float) (closestPocket.x - closestBall.getCenterX()), (float) (closestPocket.y - closestBall.getCenterY()));
        double radius = closestBall.getWidth() / 2;
        double bigR = (targetDirection.x * targetDirection.x) + (targetDirection.y * targetDirection.y);
        double ratio = radius / bigR;
//        Vector2 targetSpot = new Vector2(closestBall.getX()-(ratio*targetDirection.getX()),closestBall.getY()-(ratio*targetDirection.getY()));
//        this is the spot I want it to be tangent

        Vector2 targetCueCenter = new Vector2((float) (closestBall.getX() - (2 * ratio * targetDirection.x)), (float) (closestBall.getY() - (2 * ratio * targetDirection.y)));

        Vector2 targetDisplacement = new Vector2(targetCueCenter.x - cueBall.getX(), targetCueCenter.y - cueBall.getY());
        // how much we want the ball to change in X and Y according to center

        Vector2 neededSpeedTransfer;
        Vector2 calculatedStrike = neededTrajectory(targetDisplacement.x, targetDisplacement.y, 0, 0.5);


        return calculatedStrike;
    }


    public boolean isMyBall(int player, int ballNum) {
        if (ballNum == 0) {
            return false;
        }
        if (player == 0 && ballNum < 8) {
            return true;
        } else if (player == 1 && ballNum > 8) {
            return true;
        }
        return false;
    }

    public Vector2 neededTrajectory(double targetDisX, double targetDisY, double leftOverSpeed, double FRICTION) {
        double distance = (targetDisX * targetDisX) + (targetDisY * targetDisY);
        double time = -2 * leftOverSpeed + Math.sqrt((leftOverSpeed * leftOverSpeed) + (4 * FRICTION * 2 * distance));
        time /= 2 * FRICTION;

        double startingSpeed = (2 * distance / time) - leftOverSpeed;

        double bigR = (targetDirection.x * targetDirection.x) + (targetDirection.y * targetDirection.y);
        double ratio = startingSpeed / bigR;

        angleOff = Math.atan(targetDisY / targetDisX) - Math.atan(closestBall.getY() / closestBall.getX());
        angleOff = Math.abs(angleOff);
        double energyTransfer = Math.cos(angleOff);

        return new Vector2((float) (ratio * targetDisX), (float) (ratio * targetDisY / energyTransfer));
    }

    public Vector2 evaluatePockets(Ball closestBall, Ball[] balls, PoolTable gameTable) {
        Ball cueBall = balls[0];
        Vector2 closestPocket = gameTable.getPocketIndex(0);
        minDist = closestBall.getDistance(closestPocket);
        Vector2 positions[] = gameTable.getPockets();
        for (Vector2 i : positions) {
            if (closestBall.getDistance(i) < minDist) {
                if (isLinedUp(cueBall.getY(),
                        closestBall.getY(),
                        closestPocket.y)
                        && isLinedUp(cueBall.getX(),
                        closestBall.getX(),
                        closestPocket.x)) {
                    minDist = closestBall.getDistance(i);
                }

            }
        }
        return closestPocket;
    }

    public boolean isLinedUp(double cueVar, double ballVar, double pocketVar) {
        if (cueVar < ballVar && ballVar < pocketVar) {
            return true;
        }
        if (cueVar > ballVar && ballVar > pocketVar) {
            return true;
        }
        return false;
    }

}