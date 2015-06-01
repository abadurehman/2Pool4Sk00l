package com.mygdx.Pool;

import java.util.Stack;

/**
 * Created by SrinjoyMajumdar on 5/28/15.
 */
public class Player {
    /**
     * Name of the player
     */
    private String name;
    /**
     * If the player is playing stripes or solids
     */
    private String type;
    /**
     * Player 1 or 2
     */
    private int playerNumber;
    /**
     * Stack of scored balls (so if Cue ball is scored, we can just pop it off)
     */
    private Stack<Ball> scoredBalls;

    /**
     * Constructs a new player
     * @param name Username
     * @param n Player number
     */
    public Player(String name, int n) {
        this.name = name;
        playerNumber = n;
        scoredBalls = new Stack<Ball>();
    }

    /**
     * Sets solids/stripes
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets your name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the player number
     * @return int player number
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * pushes a scored ball onto the stack
     * @param b ball scored
     */
    public void scoredBall(Ball b) {
        scoredBalls.push(b);
    }

    /**
     * The stack of scored balls
     * @return Stack of balls
     */
    public Stack<Ball> getScoredBalls() {
        return scoredBalls;
    }

    /**
     * Gets the type of player
     * @return String solid/stripe
     */
    public String getType() {
        return type;
    }

}
