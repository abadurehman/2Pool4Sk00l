package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by SrinjoyMajumdar on 5/11/15.
 */
public class Stick extends Sprite {
    /**
     * Creates a new sprite for the stick
     */
    private Sprite stick;
    /**
     * Makes the texture for the stick.
     */
    private Texture stickTexture;

    /**
     * Makes the stick from a stock picture
     */
    public Stick() {
        stickTexture = new Texture(Gdx.files.internal("stick.png"));
        stick = new Sprite(stickTexture);
        super.set(stick);
    }
}
