package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.spill.salmonladder.Scenes.LevelParser;

public class FishSprite extends Sprite implements GestureDetector.GestureListener {

    private int orientation = 0;

    private Timer.Task[] movement = new Timer.Task[4];

    private TiledMapTileLayer collisionCheck;

    private Animation<Texture> swim;

    private float elapsedTime = 0;

    private float unitScale;

    public FishSprite(Texture[] fishTextures, TiledMapTileLayer tiledMap, float unitScale) {

        super(fishTextures[0]);

        init();

        swim = new Animation(1 / 20f, fishTextures);

        this.collisionCheck = tiledMap;

        this.unitScale = unitScale;
    }

    private void init(){
        movement[0] = new Timer.Task() {
            @Override
            public void run() {
                translate(0f, 4f);
            }
        };

        movement[1] = new Timer.Task() {
            @Override
            public void run() {
                translate(4f, 0f);
            }
        };

        movement[2] = new Timer.Task() {
            @Override
            public void run() {
                translate(0f, -4f);
            }
        };

        movement[3] = new Timer.Task() {
            @Override
            public void run() {
                translate(-4f, 0f);
            }
        };
    }

    @Override
    public void draw(Batch batch) {

        elapsedTime += Gdx.graphics.getDeltaTime();

        setTexture(swim.getKeyFrame(elapsedTime, true));

        super.draw(batch);

    }

    private boolean CheckCollision(int direction) {

        if (direction == 0) {

            return collisionCheck.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) ((getY() + getHeight()) / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoOn", boolean.class);

        } else if (direction == 2) {

            return collisionCheck.getCell((int) (getX() / SalmonLadder.PIXEL_PER_METER), (int) ((getY() - getHeight()) / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoOn", boolean.class);

        } else if (direction == 1) {

            return collisionCheck.getCell((int) ((getX() + getWidth()) / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoOn", boolean.class);

        } else {

            return collisionCheck.getCell((int) ((getX() - getWidth()) / SalmonLadder.PIXEL_PER_METER), (int) (getY() / SalmonLadder.PIXEL_PER_METER)).getTile().getProperties().get("CanGoOn", boolean.class);

        }

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        if (!LevelParser.panMode && !LevelParser.screenLock) {

            if (Math.abs(velocityY) > Math.abs(velocityX)) {

                if (velocityY < 0) {

                    this.orientation = 0;

                    this.setRotation(0);

                } else {

                    this.orientation = 2;

                    this.setRotation(180);

                }

            } else {

                if (velocityX > 0) {

                    this.orientation = 1;

                    this.setRotation(270);

                } else {

                    this.orientation = 3;

                    this.setRotation(90);

                }
            }

            if(CheckCollision(orientation)){
                Timer.schedule(movement[orientation], 0, 1/64f, 7);
            }
        }

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;

    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        return false;

    }

    @Override
    public boolean zoom(float initialDistance, float distance) {

        return false;

    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {

        return false;

    }

    @Override
    public void pinchStop() {

    }

}
