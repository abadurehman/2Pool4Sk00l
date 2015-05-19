//package com.mygdx.Pool;
//
//import com.badlogic.gdx.math.Vector2;
//
///**
// * Created by Owen Li on 5/11/2015.
// */
//public class AI {
//    /**
//     * @param balls the set of all the balls in the game
//     * @param player which player the AI is 0 for solids, 1 for stripes
//     * @param tab the table within which all this is occuring
//     * Return Vector2 direction cue ball goes
//     */
//    public Vector2 choice(Ball[] balls, int player, aifj tab)
//    {
//
//        Ball cueBall = balls[0];
////        aifj gameTable = tab;
//
//        Ball closestBall = null; // what ball to start with?
//        if (player == 0)
//        {
//            closestBall = balls[1];
//        }
//        else
//        {
//            closestBall = balls[15];
//        }
//
//        double minDist = cueBall.getDistance(closestBall);
//        for (Ball i : balls) // finds which ball is closest
//        {
//            if (isMyBall(player, i.getNum()))
//            {
//                if (cueBall.getDistance(i) < minDist)
//                {
//                    minDist = cueBall.getDistance(i);
//                    closestBall = i;
//                }
//            }
//        }
//
//        Vector2 closestPocket = gameTable.getPosition(0);
//        minDist = closestBall.getDistance(closestPocket);
//        Vector2 positions[] = gameTable.getArray();
//        for (Vector2 i : positions)
//        {
//            if (closestBall.getDistance(i) < minDist)
//            {
//                minDist = closestBall.getDistance(i);
//            }
//        }
//
//
//
//
//
//        return null; // FIX THIS
//    }
//
//    public boolean isMyBall(int player, int ballNum)
//    {
//        if (player == 0 && ballNum < 8)
//        {
//            return true;
//        }
//        else if (player == 1 && ballNum > 8)
//        {
//            return true;
//        }
//        return false;
//    }
//
//}
