package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spill.salmonladder.FishSprite;
import com.spill.salmonladder.SalmonLadder;
import com.spill.salmonladder.SalmonLadderStars;

public class LevelParser implements Screen {

    // CREATE A TILED MAP VARIABLE THAT IS USED TO GENERATE THE LEVEL'S GRAPHICS
    private TiledMap tiledMap;

    // TABLE FOR PAUSE MENU
    public static PopUpMenu PauseTable;

    // TABLE FOR WIN MENU
    public static PopUpMenu WinTable;

    // BOOLEANS FOR LOCKING WHEN IN ANIMATION
    public static boolean inAnimation = false;

    // BOOLEANS FOR LOCKING WHEN IN MENU
    public static boolean inMenu = false;

    // BOOLEANS FOR LOCKING SCREEN
    public static boolean screenLock = false;

    // BOOLEANS FOR LOCKING WHEN IN WIN
    public static boolean inWin = false;

    private static SalmonLadderStars prefStars;

    // CREATE A NEW CAMERA
    private OrthographicCamera camera;

    // RECTANGLE FOR DIMMING
    private ShapeRenderer dimmer;

    // CREATE A TILED MAP RENDERER THAT RENDERS THE MAP
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    // STAGE FOR THE HUD
    private Stage stage;
    // TABLE FOR HUD
    private static HUDTable HudTable;

    // CREATE A FISH SPRITE
    private FishSprite fish;

    // LEVEL NUMBER THAT IS PARSED IN
    static int levelNumber;

    // DEFAULT CONSTRUCTOR
    LevelParser(int LevelNumber) {

        levelNumber = LevelNumber;

    }

    public static int awardStars() {

        int MinMoves = getXMLRoot().getChildByName("MinMoves").getInt("Best");

        int starsToAward;

        if (HUDTable.getMoves() == MinMoves) {

            if (prefStars.getStars(levelNumber) < 3) {

                prefStars.setStars(levelNumber, 3);

            }

            starsToAward = 3;

        } else if (HUDTable.getMoves() < MinMoves + 3) {

            if (prefStars.getStars(levelNumber) < 2) {

                prefStars.setStars(levelNumber, 2);

            }

            starsToAward = 2;

        } else if (HUDTable.getMoves() < MinMoves + 6) {

            if (prefStars.getStars(levelNumber) < 1) {

                prefStars.setStars(levelNumber, 1);

            }

            starsToAward = 1;

        } else {

            if (prefStars.getStars(levelNumber) == 0) {

                prefStars.setStars(levelNumber, 0);

            }

            starsToAward = 0;

        }

        return starsToAward;

    }

    public static void unlockNext() {

        if (levelNumber != ScreenLevelSelect.levelCount) {

            prefStars.setStatus(levelNumber + 1, "Unlocked");

        }

    }

    public static void resetBooleans(boolean reset) {

        inAnimation = reset;

        inMenu = reset;

        inWin = reset;

        screenLock = reset;

    }

    @Override
    public void render(float delta) {

        // BACKGROUND SET TO ALL WHITE
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(255f, 255f, 255f, 0);

        // POSITION CAMERA TO THE CENTER OF THE FISH SPRITE
        camera.position.set(fish.getX() + SalmonLadder.PIXEL_PER_METER / 2, fish.getY() + SalmonLadder.PIXEL_PER_METER / 2, 0);
        camera.update();

        // SET THE CAMERA TO RENDER THE TILEMAP
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // DRAW THE FISH WITH RESPECT TO THE TILEMAP
        tiledMapRenderer.getBatch().begin();
        fish.draw(tiledMapRenderer.getBatch());
        tiledMapRenderer.getBatch().end();

        if (inMenu) {

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            dimmer.begin(ShapeRenderer.ShapeType.Filled);
            dimmer.setColor(0, 0, 0, 0.75f);
            dimmer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            dimmer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);

        }

        stage.act();
        stage.draw();


    }

    @Override
    public void resize(int width, int height) {

        camera.viewportWidth = width / 2.5f;
        camera.viewportHeight = height / 2.5f;

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

        dispose();

    }

    @Override
    public void dispose() {

        tiledMap.dispose();

        tiledMapRenderer.dispose();

    }

    private static XmlReader.Element getXMLRoot() {

        return new XmlReader().parse(Gdx.files.internal("Levels.xml")).getChildByName("Level" + levelNumber);

    }

    @Override
    public void show() {

        prefStars = new SalmonLadderStars();

        // GRAB START COORDINATES FROM THE XML FILE
        int startX = getXMLRoot().getChildByName("StartCoords").getInt("x");
        int startY = getXMLRoot().getChildByName("StartCoords").getInt("y");

        // LOAD MAP INTO VARIABLE
        tiledMap = new TmxMapLoader().load(getXMLRoot().get("LevelFile"));

        // SET THE RENDERER TO RENDER THE TILEMAP
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // SET CAMERA TO ORTHOGRAPHIC (TOP-DOWN)
        camera = new OrthographicCamera();

        // VARIABLE FOR SKIN OF THE FISH
        int fishSkin = 0;

        // CREATE NEW FISH SPRITE TO PLACE ON THE MAP
        fish = new FishSprite(fishSkin, (TiledMapTileLayer) tiledMap.getLayers().get(1), tiledMapRenderer.getUnitScale());

        // SET STARTING POSITION OF FISH USING VARIABLES FROM XML FILE
        fish.setPosition(startX * SalmonLadder.PIXEL_PER_METER, startY * SalmonLadder.PIXEL_PER_METER);

        /*
        for (int i = 0; i < ((TiledMapTileLayer) tiledMap.getLayers().get(1)).getHeight(); i++) {
            for (int j = 0; j < ((TiledMapTileLayer) tiledMap.getLayers().get(1)).getWidth(); i++) {
                if (((TiledMapTileLayer) tiledMap.getLayers().get(1)).getCell(i, j).getTile().getProperties().get("Name", String.class).equals("EventBear")) {
                    if (((TiledMapTileLayer) tiledMap.getLayers().get(1)).getCell(i + 1, j) != null) {
                        if (((TiledMapTileLayer) tiledMap.getLayers().get(1)).getCell(i + 1, j).getTile().getProperties().get("Name", String.class).equals("Rock")) {
                            
                        }
                    }
                }
            }
        }
        */

        stage = new Stage(new FitViewport(1080, 1920));

        dimmer = new ShapeRenderer();

        HudTable = new HUDTable(0);

        PauseTable = new PopUpMenu(2f, 1.5f, "up");

        WinTable = new PopUpMenu(2f, 1.5f, "up");

        PauseTable.setNinePatchBG("Images/PauseMenuBackground.png");

        WinTable.setNinePatchBG("Images/WinMenuBackground.png");

        PauseTable.createPauseMenu();

        stage.addActor(HudTable);

        stage.addActor(PauseTable);

        stage.addActor(WinTable);

        InputMultiplexer multiplexer = new InputMultiplexer();

        Gdx.input.setInputProcessor(multiplexer);

        multiplexer.addProcessor(new GestureDetector(fish));

        multiplexer.addProcessor(stage);

    }

}
