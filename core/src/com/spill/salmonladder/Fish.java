package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.spill.salmonladder.Scenes.LevelParser;

public class Fish extends Actor {

    private int orientation = 0, xPos, yPos;
    private Sprite sprite;

    public Fish(String img) {

        this.sprite = new Sprite(new Texture(Gdx.files.internal(img)));

        this.sprite.setScale(LevelParser.tiledMapRenderer.getUnitScale());
    }

    @Override
    public void draw(Batch batch, float alpha) {

        sprite.setPosition(getX(), getY());
        sprite.draw(batch);

    }

    public int getOrientation(){
        return orientation;
    }

    public void setOrientation(int o){
        orientation = o;
    }
}
