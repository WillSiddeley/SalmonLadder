package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.spill.salmonladder.Scenes.LevelParser;

    /*
    LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE
    LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE
    LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE
    LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE LEGACY CODE
    */

public class Fish extends Actor {

    private int orientation = 0, xPos, yPos;
    private Sprite sprite;

    public Vector2 position = new Vector2();

    public Fish(String img) {

        this.sprite = new Sprite(new Texture(Gdx.files.internal(img)));

        this.sprite.setScale(LevelParser.tiledMapRenderer.getUnitScale());
    }

    @Override
    public void draw(Batch batch, float alpha) {

        sprite.setPosition(getX(), getY());
        sprite.draw(batch);

    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getOrientation(){
        return orientation;
    }

    public void setOrientation(int o){
        orientation = o;
    }
}
