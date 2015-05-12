package com.mygdx.Pool;
import com.sun.prism.Texture;

import java.awt.Color;

/**
 * Created by Owen Li on 5/6/2015.
 */
public class Behavior
{
    private float x, y;
    private int radius;
    private Vector2 velo;
    private Color col;
    Texture ball;

    public Behavior(float x, float y, int radius, Color c)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
        col = c;
    }
    public Vector2 velocity(float x, float y)
    {
       return new Vector2(x, y);
    }

    public boolean isMoving(Texture a)
    {
        return a.velo.x != 0 || a.velo.y != 0;
    }

    /**
     * When Ball(Texture) A is hit by Ball(Texture) B, going at a Vector of Additional.
     * @param additional
     *          The vector at which ball b is traveling
     * @param a
     *          The first ball (the one that's being hit.)
     * @param b
     *          The ball hitting the first ball.
     */

    public Vector2 hit( Vector2 additional, Texture a, Texture b )
    {
        a.velo.add(additional.x / 2, additional.y / 2);  //this will be the impact reduction for now.
        b.velo.setZero(); // b.velo.set(-additional/2);
    }


//    public void deccelerate( float val )
//    {
//        if (val >= velo)
//        {
//            velo.
//        }
//    }
//    public int collide()
//    {
//        if (ball.collides(wall))
//        {
//            velocity.x = -velocity.x*2;
//            velocity.y = -velocity.y*2;
//        }
//    }

}
