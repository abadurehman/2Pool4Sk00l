package com.mygdx.Pool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;


import java.util.HashMap;

/**
 * Created by SrinjoyMajumdar on 5/12/15.
 */
public class PoolTable extends Stage {
    /**
     * Your table
     */
    public Table table;
    /**
     * Array of pocket locations
     */
    private Vector2[] pockets;
    /**
     * Slider for power
     */
    Slider slider;
    /**
     * Skin of the screen
     */
    Skin skin;
    /**
     * Region of the packed image
     */
    TextureAtlas atlas;
    /**
     * Style of the slider
     */
    Slider.SliderStyle sliderStyle;
    /**
     * Sets the top of the left-side rail
     */
    private final Vector2 topLeft = new Vector2(97, 354);
    /**
     * Sets the width of the pocket
     */
    private int widthPockets = 294;
    /**
     * Sets the height of the pocket
     */
    private int heightPockets = 312;
    /**
     * Creates a rectangle inside the table, along the rails indicated by the picture.
     */
    private Rectangle tableInside = new Rectangle(118, 47, 704, 351);

    /**
     * Creates the pool table, with all the different regions, the table, the pockets, the knob, slider, profile pictures.
     */
    public PoolTable() {
        table = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("gamebackround.png")))));
        table.setFillParent(true);
        table.setDebug(true);
        super.addActor(table);
        atlas = new TextureAtlas("button.txt");
        skin = new Skin();
        skin.addRegions(atlas);
        sliderStyle = new Slider.SliderStyle();
        sliderStyle.knob = skin.getDrawable("knob");
        sliderStyle.background = skin.getDrawable("slider");
        slider = new Slider((float) 0.0, (float) 1.0, (float) 0.01, true, sliderStyle);
        float x2 = 86 * (Gdx.graphics.getWidth() / getCamera().viewportWidth);
        float y2 = getTableInside().getY() * (getCamera().viewportHeight / 480);
        float height = getTableInside().getHeight() * (540 / 480);
        slider.setValue(1);

        table.align(Align.bottomLeft);
        table.padBottom(y2);
        table.row().expandX().align(Align.left).padLeft(getCamera().viewportWidth / 25).height(height * (getHeight() / 540));
//        System.out.println(getTableInside().getHeight() * (540 / 480));
        table.add(slider);





        pockets = new Vector2[6];
        for (int i = 0; i < 3; i++) {
            pockets[i] = new Vector2((topLeft.x + widthPockets * (i)), topLeft.y);
        }
        for (int i = 3; i < 6; i++) {
            pockets[i] = new Vector2((topLeft.x + widthPockets * (i)), topLeft.y + heightPockets);
        }
    }


//    public void setPosition(int index, Vector2 position)
//    {
//        positions[index] = position;
//    }

    /**
     * Returns which pocket this is/location
     * @param index index of the pocket in the pockets[] array
     * @return the location of the pocket
     */
    public Vector2 getPocketIndex(int index) {
        return pockets[index];
    }

    /**
     * Returns all the pockets.
     * @return Vector2 array of all the pocket locations
     */
    public Vector2[] getPockets() {
        return pockets;
    }

    /**
     * Returns the table drawn inside the picture
     * @return Rectangle inside the picture
     */
    public Rectangle getTableInside() {
        return tableInside;
    }

    /**
     * Slider for power
     * @return Slider for power on the left hand side.
     */
    public Slider getSlider() {
        return slider;
    }

}
