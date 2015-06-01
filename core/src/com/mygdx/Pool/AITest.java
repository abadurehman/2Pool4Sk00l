package com.mygdx.Pool;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by SrinjoyMajumdar on 6/1/15.
 */
public class AITest {
    AI ai = new AI();

    @Test
    public void testChoice() throws Exception {
        ArrayList<Ball> balls = new ArrayList<Ball>();
        balls.add(new Ball(0));
        balls.add(new Ball(1));
        Player player = new Player("", 1);
        player.setType("striped");
        PoolTable table = new PoolTable();
        ai.choice(balls, player, table);
        Assert.assertEquals(ai.closestBall, balls.get(1));
    }
}