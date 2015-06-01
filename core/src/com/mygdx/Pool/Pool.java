package com.mygdx.Pool;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


public class Pool extends Game {
	/**
	 * Batch of sprites
	 */
	SpriteBatch batch;
	/**
	 * background textures
	 */
	Texture background;
	/**
	 * font of text
	 */
    BitmapFont font;

	/**
	 * Creates the main menu screen
	 */
	@Override
	public void create () {
        batch = new SpriteBatch();
        // Use LibGDX's default Arial font.
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}

	/**
	 * renders the screen
	 */
	@Override
	public void render () {
		super.render();
	}
}
