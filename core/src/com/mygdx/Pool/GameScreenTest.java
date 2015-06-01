package com.mygdx.Pool;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by SrinjoyMajumdar on 6/1/15.
 */
public class GameScreenTest {
    Pool pool;
    GameScreen gameScreen;


    @Before
    public void initialize() {
        pool = new Pool();
        gameScreen = new GameScreen(pool);
    }

    @Test
    public void testCheckSwitchFirstContact() throws Exception {

        Ball a = new Ball(0);
        Ball b = new Ball(12);
        Ball c = new Ball(5);
        Player temp = new Player("p1", 1);
        temp.setType("striped");
        GameScreen.currPlayer = temp;
        Assert.assertTrue("", GameScreen.checkSwitchFirstContact(a, b));
        Assert.assertTrue("", GameScreen.checkSwitchFirstContact(a, c));

    }

    @Test
    public void testCreateBall() throws Exception {
        int i = 3;
        Ball b = new Ball(i);
        double length = gameScreen.poolBalls.size();
        gameScreen.pocketed(b);
        Assert.assertEquals(gameScreen.poolBalls.size(), (int) length - 1);
    }

    @Test
    public void testPocketed() throws Exception {

        int ball = 3;
        Ball b = new Ball(ball);
        double length = gameScreen.poolBalls.size();
        Player currPlayer = new Player("striped", 1);
        currPlayer.setType("striped");
        gameScreen.pocketed(b);
        int temp = gameScreen.numSwitched;
        Assert.assertEquals(temp + 1, gameScreen.numSwitched);
    }

    @Test
    public void testDrawRack() {
        Assert.assertTrue(gameScreen.drawRack());
    }

    @Test
    public void testDrawUserBox() {
        Player player1 = new Player("", 1);
        Assert.assertEquals(gameScreen.drawUserBox(player1), -355);
    }

    @Test
    public void testEightBallScored() {
        int i = 8;
        Ball b = new Ball(8);
        GameScreen.pocketed(b);
        Assert.assertTrue(GameScreen.done);
    }


}