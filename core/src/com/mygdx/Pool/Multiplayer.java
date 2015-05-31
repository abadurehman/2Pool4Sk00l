package com.mygdx.Pool;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by SrinjoyMajumdar on 5/4/15.
 */
public class Multiplayer extends GameScreen {
//    final Pool game;
//    Texture background;
//    Sprite bgSprite;
//    OrthographicCamera camera;
//    TextButton quit;
//    Stage stage;
//    Skin skin;
//    TextureAtlas atlas;


    private static final float BUTTON_WIDTH = 300f;
    private static final float BUTTON_HEIGHT = 60f;
    private static final float BUTTON_SPACING = 10f;
    private String playerName1 = "";
    private String playerName2 = "";

    TextButton.TextButtonStyle textButtonStyle;

    public Multiplayer(final Pool gam) {
        super(gam);

    }


}

