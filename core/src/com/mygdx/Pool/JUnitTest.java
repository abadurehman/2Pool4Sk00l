package com.mygdx.Pool;

import org.junit.Assert;

/**
 * Created by SrinjoyMajumdar on 6/1/15.
 */
public class JUnitTest {
    Pool gam = new Pool();
    GameScreen game = new GameScreen(gam);

    public void testGameScreenRender() {


    }

    public void testWrongBallFirstContactSwitch() {
        Ball a = new Ball(0);

        Ball b = new Ball(12);
        Ball c = new Ball(5);
        Player temp = new Player("p1", 1);
        temp.setType("striped");
        game.currPlayer = temp;
        boolean firstContact = true;
        Assert.assertTrue("", game.checkSwitchFirstContact(a, b));
        Assert.assertTrue("", game.checkSwitchFirstContact(a, c));

    }

    public void testWrongBallPocketed() {
        GameScreen game = new GameScreen(gam);
        int ball = 3;
        Ball b = new Ball(ball);
        double length = game.poolBalls.size();
        Player currPlayer = new Player("striped", 1);
        currPlayer.setType("striped");
        game.pocketed(b);
    }
}