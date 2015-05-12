package com.mygdx.Pool;

/**
 * Created by Owen Li on 5/11/2015.
 */
public class AI {
    /**
     * @param set the set of all the balls in the game
     * @param player which player the AI is 0 for solids, 1 for stripes
     * Returns ball, angle, power.
     */
    public Object[] choice(Ball[] balls, int player, Pocket[] pockets)
    {

        Ball cueBall = balls[0];

        Ball closestBall = null; // what ball to start with?
        if (player == 0)
        {
            closestBall = balls[1];
        }
        else
        {
            closestBall = balls[15];
        }

        int minDist = cueBall.getDistance(closestBall);
        for (Ball i : set) // finds which ball is closest
        {
            if (ismyBall(player,i.getNum()))
            {
                if (cueBall.getDistance(i) < minDist)
                {
                    minDist = cueBall.getDistance(i);
                    closestBall = i;
                }
            }
        }

        Pocket closestPocket = pockets[0];
        minDist = closestBall.getDistance(closestPocket);
        for (Pocket i : pockets)
        {
            if (closestBall.getDistance(i))
            {

            }
        }


        return null; // FIX THIS
    }

    public boolean isMyBall(int player, int ballNum)
    {
        if (player == 0 && ballNum < 8)
        {
            return true;
        }
        else if (player == 1 && ballNum > 8)
        {
            return true;
        }
        return false;
    }

}
