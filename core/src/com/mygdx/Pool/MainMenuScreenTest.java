package com.mygdx.Pool;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by SrinjoyMajumdar on 6/1/15.
 */
public class MainMenuScreenTest {
    Pool pool = new Pool();
    MainMenuScreen menuScreen = new MainMenuScreen(pool);

    @Test
    public void testRender() throws Exception {

    }

    @Test
    public void testButtons() throws Exception {
        Assert.assertEquals(menuScreen.table.getCells(), 3);
    }

    @Test
    public void testTitle() throws Exception {
//        Assert.assertEquals(menuScreen);
    }
}