package com.spill.salmonladder;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spill.salmonladder.Scenes.ScreenStart;

public class
SalmonLadder extends Game implements ApplicationListener {

    // SET UP VARIABLES FOR SPRITES AND FONT
    private SpriteBatch batch;
    private BitmapFont font;

    // THIS IS THE NUMBER OF PIXELS PER TILE / SPRITES.  ALL ASSETS MUST BE 32 x 32 OR MULTIPLE OF 32
    public static final float PIXEL_PER_METER = 32f;

    @Override
    public void create() {

        // INSTANTIATE BATCH AND FONT
        batch = new SpriteBatch();
        font = new BitmapFont();

        // AS SOON AS THE GAME LOADS UP, PUTS USER INTO THE START SCREEN
        this.setScreen(new ScreenStart());

    }

    @Override
    public void render() {

        super.render();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

        // DISPOSE OF BATCH AND FONT SO THEY DON'T TAKE UP MEMORY
        batch.dispose();
        font.dispose();

    }
}
