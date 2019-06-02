package com.spill.salmonladder;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;


public class BearSprite extends Sprite {

    private float elapsedTime = 0;

    private int eventX, eventY;

    private boolean inAnimation;

    private Animation<Texture> kill;

    private Array<Texture> killTextures = new Array<Texture>(3);


    BearSprite(int eventX, int eventY, boolean orientation) {

        super(new Texture("Sprites/BearTextures/BearStagnant.png"));

        flip(orientation, false);


        this.eventX = eventX;

        this.eventY = eventY;


        for (int i = 0;
             i < 3;
             i++) {

            killTextures.add(new Texture("Sprites/BearTextures/BearKill_" + (i + 1) + ".png"));


        }


        kill = new Animation<Texture>(1 / 10f, killTextures, Animation.PlayMode.LOOP);


    }


    @Override
    public void draw(Batch batch) {

        if (inAnimation) {

            elapsedTime += Gdx.graphics.getDeltaTime();

            if (kill.isAnimationFinished(elapsedTime)) {

                setTexture(new Texture("Sprites/BearTextures/BearKill_3.png"));

                inAnimation = false;

                if (SalmonLadderConstants.SETTINGS.isSoundEnabled()) {

                    SalmonLadderConstants.SOUND_DEATH_BEAR.play();

                    SalmonLadderConstants.SOUND_MENU_OPEN.play();

                }

                LevelParser.inDeath = true;


            } else {

                setTexture(kill.getKeyFrame(elapsedTime, true));


            }


        }


        super.draw(batch);


    }


    int getEventX() {

        return eventX;


    }


    int getEventY() {

        return eventY;


    }


    void animate() {

        inAnimation = true;


    }


}