package com.mygdx.Pool;

import java.util.Stack;

/**
 * Created by SrinjoyMajumdar on 5/28/15.
 */
public class Player {
    private String name;
    private String type;
    private int playerNumber;
    private Stack<Ball> scoredBalls;

    public Player(String name, int n) {
        this.name = name;
        playerNumber = n;
        scoredBalls = new Stack<Ball>();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void scoredBall(Ball b) {
        scoredBalls.push(b);
    }

    public Stack<Ball> getScoredBalls() {
        return scoredBalls;
    }

    public String getType() {
        return type;
    }

    public int get
}
