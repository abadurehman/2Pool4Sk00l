package com.mygdx.Pool;
import com.sun.prism.Texture;

import java.awt.Color;
import java.util.Vector;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Owen Li on 5/6/2015.
 */
public class Behavior
{
    private float x, y;
    private int radius;
    private Vector2 velo, velo2;
    private Color col;
    Ball bal;
    private static float VERT, HORIZ;
    private static float FRICTION = 0.75;
    private static float COLLISION = 0.5;

    public Behavior(Vector2 v, Ball bal)
    {
        velo = v;
        this.bal = bal;
    }
    public Behavior(Vector2 v1, Vector2 v2)
    {
        velo = v1;
        velo2 = v2;
    }
    public boolean isMoving()
    {
        return bal.velo.x != 0 || bal.velo.y != 0;
    }

    /**
     *
     */
//    public Vector2 ballHit(Ball a, Vector2 aVelo, Ball b, Vector2 bVelo) {
//        float magA = Math.sqrt(Math.pow(aVelo.x, 2) + Math.pow(aVelo.y, 2));
//        Vec2D v12 = ( new Vec2D( this, b ) ).unitVec();
//        Vec2D v1c = vel.copy().unitVec();
//        float cos = v1c.dotProd( v12 );
//        vel.subVec( v12.mulVec( cos * mv ) ).addVec( b.vel );
//        b.vel.addVec( v12 );
//    }
    public Vector2 magnitude(Ball a, Ball b)
    {
        return new Vector2(Math.sqrt(Math.pow(a.getCenter().getX - b.getCenter().getX),2) + Math.pow(a.getCenter().getY - b.getCenter().getY, 2));
    }
    public Vector2 hitWall(float wall, Vector2 velo)
    {
        if (wall == VERT)
        {
            velo.x = -velo.x;
        }
        if (wall == HORIZ)
        {
            velo.y = -velo.y;
        }

    }
    //angle and power of ball1
    //vector of ball2
    public Vector2 hit(double angle, double pow, double angleOpp, double powOpp)
    {
        Vector2 vect = new Vector2((float)pow*Math.cos(angle), (float)pow*Math.sin(angle));
        float collisionAngle = vect.angle(ball2);
        return new Vector2((float)powOpp*Math.sin(angleOpp)*COLLISION, (float)powOpp*Math.cos(angleOpp)*COLLISION);
    }
//    public Vector2 unit(Vector2 adsf, double pow)
//    {
//        return new Vector2(adsf.x / pow, adsf.y/pow);
//    }


//    public void deccelerate( float val )
//    {
//        while(b.isMoving())
//        {
//            velo.set(velo.x * FRICTION , velo.y * FRICTION);
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
