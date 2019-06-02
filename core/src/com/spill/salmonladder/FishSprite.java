package com.spill.salmonladder;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import java.util.Random;


public class FishSprite extends Sprite implements GestureDetector.GestureListener {

    private Random random;

    private float elapsedTime = 0, alpha = 0.8f, underwaterTime = 0;

    private int orientation = 0;

    private String event;

    private boolean isWaitingStart = false, isWaitingJump = false, isWaitingJumpDone = false, isWaitingWinDone, inBobber = false, flipped = false;

    static boolean entranceAnimation = false;

    private Timer.Task[] movement = new Timer.Task[4], jumpMovement = new Timer.Task[4];

    private Timer.Task dive = new Timer.Task() {
        @Override
        public void run() {
            alpha -= 0.1f;
        }
    };

    private Timer.Task resurface = new Timer.Task() {
        @Override
        public void run() {
            alpha += 0.1f;
        }
    };

    private Array<Texture> fishTextures = new Array<Texture>();

    private Array<Texture> jumpTextures = new Array<Texture>();

    private TiledMapTileLayer map;

    private BobberSprite bobber;

    private Animation<Texture> show, swim, jump;

    FishSprite(int skin, TiledMapTileLayer tiledMap) {

        super(new Texture("Sprites/Textures/ChinookStagnant_1.png"));

        random = new Random();

        switch (skin) {

            case 0:
                for (int i = 0; i < 13; i++) {

                    fishTextures.add(new Texture("Sprites/Textures/ChinookStagnant_" + (i + 1) + ".png"));

                }


                for (int i = 0; i < 21; i++) {

                    jumpTextures.add(new Texture("Sprites/Textures/ChinookJump_" + (i + 1) + ".png"));

                }

                break;

        }


        setTexture(fishTextures.first());

        swim = new Animation<Texture>(1 / 20f, fishTextures, Animation.PlayMode.LOOP);

        jump = new Animation<Texture>(1 / 20f, jumpTextures, Animation.PlayMode.LOOP);

        show = swim;

        init();

        if (LevelParser.levelNumber != 1) {

            Timer.schedule(movement[0], 0, 1 / 20f, 95);

            entranceAnimation = true;

            LevelParser.inAnimation = true;

        }

        this.map = tiledMap;

    }


    private void init() {

        movement[0] = new Timer.Task() {

            @Override
            public void run() {

                translate(0f, 4f);


            }


        }

        ;


        movement[1] = new Timer.Task() {

            @Override
            public void run() {

                translate(4f, 0f);


            }


        }

        ;


        movement[2] = new Timer.Task() {

            @Override
            public void run() {

                translate(0f, -4f);


            }


        }

        ;


        movement[3] = new Timer.Task() {

            @Override
            public void run() {

                translate(-4f, 0f);


            }


        }

        ;


        jumpMovement[0] = new Timer.Task() {

            @Override
            public void run() {

                translate(0f, 2f);


            }


        }

        ;


        jumpMovement[1] = new Timer.Task() {

            @Override
            public void run() {

                translate(2f, 0f);


            }


        }

        ;


        jumpMovement[2] = new Timer.Task() {
            @Override
            public void run() {
                translate(0f, -2f);
            }
        };


        jumpMovement[3] = new Timer.Task() {
            @Override
            public void run() {
                translate(-2f, 0f);
            }
        };


    }


    @Override
    public void draw(Batch batch) {

        setAlpha(alpha);

        if (!entranceAnimation) {

            if (inBobber) {

                setPosition(bobber.getX() - 13, bobber.getY() - 11);

            } else {

                elapsedTime += Gdx.graphics.getDeltaTime();

                setTexture(show.getKeyFrame(elapsedTime, true));

                if (show.isAnimationFinished(elapsedTime) && show.equals(swim)) {
                    flip(true, false);
                    elapsedTime = 0;
                    flipped = !flipped;
                }

                if (isWaitingStart && !dive.isScheduled()) {

                    underwaterTime += Gdx.graphics.getDeltaTime();

                }

                if (underwaterTime >= 0.75f) {

                    underwaterTime = 0;

                    isWaitingStart = false;

                    Timer.schedule(resurface, 0, 1 / 16, 3);

                    isWaitingJump = true;

                }

                if (isWaitingJump && !resurface.isScheduled()) {

                    isWaitingJump = false;

                    elapsedTime = 0;

                    show = jump;

                    Timer.schedule(jumpMovement[orientation], 0, 1 / 15f, 15);

                    alpha = 1f;

                    isWaitingJumpDone = true;
                }

                if (isWaitingJumpDone && jump.isAnimationFinished(elapsedTime)) {

                    alpha = 0.8f;

                    isWaitingJumpDone = false;

                    show = swim;

                    elapsedTime = 9 / 20f;

                    if (SalmonLadderConstants.SETTINGS.isSoundEnabled()) {

                        SalmonLadderConstants.SOUND_WATER_SPLASH.play();

                    }

                    if (event.equals("EventWin")) {

                        isWaitingWinDone = true;

                        LevelParser.winCameraLock = true;

                        Timer.schedule(movement[0], 0, 1 / 20f, 103);

                    } else if (event.substring(0, 11).equals("EventLadder") || event.substring(0, 14).equals("EventWaterfall")) {

                        LevelParser.inAnimation = false;

                    }

                }

                if (isWaitingWinDone && !movement[0].isScheduled()) {

                    isWaitingWinDone = false;

                    LevelParser.unlockNext();

                    int stars = LevelParser.awardStars();

                    switch (stars) {

                        case 0:
                            SalmonLadderConstants.SOUND_STAR_0.play();
                            break;

                        case 1:
                            SalmonLadderConstants.SOUND_STAR_1.play();
                            break;

                        case 2:
                            SalmonLadderConstants.SOUND_STAR_2.play();
                            break;

                        case 3:
                            SalmonLadderConstants.SOUND_STAR_3.play();
                            break;

                    }

                    LevelParser.WinTable.updateStarDrawable(stars);

                    LevelParser.inWin = true;

                }
            }

        } else if (!movement[0].isScheduled()) {

            entranceAnimation = false;

            LevelParser.inAnimation = false;

            LevelParser.winCameraLock = false;

        } else {

            elapsedTime += Gdx.graphics.getDeltaTime();

            setTexture(show.getKeyFrame(elapsedTime, true));

            if (show.isAnimationFinished(elapsedTime) && show.equals(swim)) {
                flip(true, false);
                elapsedTime = 0;
                flipped = !flipped;
            }

        }

        super.draw(batch);

    }


    private boolean CheckCollisionIn() {


        if (orientation == 0) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() + getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoUpIn", boolean.class);


        } else if (orientation == 2) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() - getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoDownIn", boolean.class);


        } else if (orientation == 1) {


            return map.getCell((int) ((getX() + getWidth()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoRightIn", boolean.class);


        } else {


            return map.getCell((int) ((getX() - getWidth()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoLeftIn", boolean.class);


        }


    }


    private boolean CheckCollisionOut() {


        if (orientation == 0) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoUpOut", boolean.class);


        } else if (orientation == 2) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoDownOut", boolean.class);


        } else if (orientation == 1) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoRightOut", boolean.class);


        } else {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoLeftOut", boolean.class);


        }


    }


    private boolean waterfallJump() {

        if (orientation == 0) {

            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() + getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoDownOut", boolean.class);


        } else if (orientation == 2) {

            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() - getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoUpOut", boolean.class);


        } else if (orientation == 1) {

            return map.getCell((int) ((getX() + getHeight()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoLeftOut", boolean.class);


        } else {

            return map.getCell((int) ((getX() - getHeight()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("CanGoRightOut", boolean.class);


        }


    }


    private String nextTile() {


        if (orientation == 0) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() + getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);


        } else if (orientation == 2) {


            return map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) ((getY() - getHeight()) / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);


        } else if (orientation == 1) {


            return map.getCell((int) ((getX() + getWidth()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);


        } else {


            return map.getCell((int) ((getX() - getWidth()) / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);


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


        if (canMove()) {


            LevelParser.inAnimation = true;


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


            if (CheckCollisionIn() && CheckCollisionOut()) {

                if (SalmonLadderConstants.SETTINGS.isSoundEnabled()) {

                    if (random.nextInt(100) < 20) {

                        if (random.nextInt(2) == 0) {

                            SalmonLadderConstants.SOUND_WATER_1.play();

                        } else {

                            SalmonLadderConstants.SOUND_WATER_2.play();

                        }

                    }

                }

                HUDTable.setMoves(HUDTable.getMoves() + 1);


                if (map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class).substring(0, 5).equals("Event")) {

                    event = map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);

                    if (event.equals("EventLadder")) {

                        Timer.schedule(dive, 0, 1 / 16f, 7);

                        isWaitingStart = true;

                    } else if (!nextTile().substring(0, 5).equals("Event")) {

                        Timer.schedule(movement[orientation], 0, 1 / 64f, 7);

                        LevelParser.inAnimation = false;


                    }


                }


                if (nextTile().substring(0, 5).equals("Event")) {

                    event = nextTile();

                    if (event.equals("EventWin") || event.equals("EventLadder") || (event.equals("EventWaterfall") && !waterfallJump())) {

                        Timer.schedule(dive, 0, 1 / 16f, 7);

                        isWaitingStart = true;

                    } else {

                        Timer.schedule(movement[orientation], 0, 1 / 64f, 7);

                        LevelParser.inAnimation = false;


                    }


                }


                if (!map.getCell((int) (getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class).substring(0, 5).equals("Event") && !nextTile().substring(0, 5).equals("Event")) {

                    Timer.schedule(movement[orientation], 0, 1 / 64f, 7);

                    LevelParser.inAnimation = false;


                }


            } else {

                LevelParser.inAnimation = false;


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


    private boolean canMove() {


        return !LevelParser.inAnimation && !LevelParser.inDeath && !LevelParser.inMenu && !LevelParser.inTutorial && !LevelParser.inWin && getX() % 32 == 0 && getY() % 32 == 0;


    }


    void animate(BobberSprite bobber) {

        inBobber = true;

        this.bobber = bobber;

    }

    public boolean isWaitingWinDone() {

        return isWaitingWinDone;

    }
}