package com.mygdx.Pool;

/**
 * Created by Owen Li on 5/27/2015.
 */
public class JUnit
{
    final Pool gam;
    public void GameScreenRender()
    {
        GameScreen game = new GameScreen(gam);
        int i = 3;
        Ball b = new Ball(i);
        double length = game.poolBalls.getLength();
        game.pocketed(b);
        assertEquals("Ball should be removed from Array", length-1 , (double)game.poolBalls.getLength());
    }
    public void WrongBallFirstContactSwitch()
    {

       //        if ((b.getNum() < 8 && currPlayer.getType().equals("striped") || (b.getNum() > 8 && currPlayer.getType().equals("solid")))) {
//            Player temp = currPlayer;
//            currPlayer = watchPlayer;
//            watchPlayer = temp;
//            System.out.println("switched because wrong contact");
//            System.out.println("Current Player" + currPlayer.getPlayerNumber());
//            firstContact = true;
//        }
        Ball b = new Ball(3);
        GameScreen game = new GameScreen(gam);
        Player currPlayer = new Player("striped", 8);
        boolean firstContact = true;
        if (b.getNum() < 8 && currPlayer.getType().equals("striped"))
        {
            assertTrue(firstContact);
        }
        if (b.getNum()>8 && currPlayer.getType().equals("solid"))
        {
            assertFalse(firstContact);
        }
    }
    public void WrongBallPocketed() {
        GameScreen game = new GameScreen(gam);
        int ball = 3;
        Ball b = new Ball(ball);
        double length = game.poolBalls.getLength();
        Player currPlayer = new Player("striped", )
        game.pocketed(b);
    }

}
