package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class LevelParser implements Screen {

    static MenuTutorial TutorialTable;

    static HUDTable HudTable;
    static MenuDeath DeathTable;
    static MenuPause PauseTable;
    static MenuWin WinTable;
    static boolean inTutorial = false;
    static boolean winCameraLock = false;

    private MapLayer propertyLayer;
    static boolean inAnimation = false;
    static boolean inDeath = false;
    static boolean inMenu = false;
    static boolean inWin = false;
    static int levelNumber;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private FishSprite fish;

    private BearSprite bear;

    private static Array<BearSprite> bearSprites = new Array<BearSprite>();

    private static Array<Array<EventFisher>> eventFishers = new Array<Array<EventFisher>>();

    private static Array<BobberSprite> bobberSprites = new Array<BobberSprite>();

    private ShapeRenderer DimRectangle;

    private Stage stage;

    private String event;

    private float soundTime;

    // DEFAULT CONSTRUCTOR
    LevelParser(int LevelNumber) {

        levelNumber = LevelNumber;

    }

    public static int awardStars() {

        int MinMoves = getXMLRoot().getChildByName("MinMoves").getInt("Best");

        int starsToAward;

        if (HUDTable.getMoves() <= MinMoves) {

            if (SalmonLadderConstants.PREFERENCES.getStars(levelNumber) < 3) {

                SalmonLadderConstants.PREFERENCES.setStars(levelNumber, 3);

            }

            starsToAward = 3;

        } else if (HUDTable.getMoves() < (int) (MinMoves * 1.5)) {

            if (SalmonLadderConstants.PREFERENCES.getStars(levelNumber) < 2) {

                SalmonLadderConstants.PREFERENCES.setStars(levelNumber, 2);

            }

            starsToAward = 2;

        } else if (HUDTable.getMoves() < MinMoves * 2) {

            if (SalmonLadderConstants.PREFERENCES.getStars(levelNumber) < 1) {

                SalmonLadderConstants.PREFERENCES.setStars(levelNumber, 1);

            }

            starsToAward = 1;

        } else {

            if (SalmonLadderConstants.PREFERENCES.getStars(levelNumber) == 0) {

                SalmonLadderConstants.PREFERENCES.setStars(levelNumber, 0);

            }

            starsToAward = 0;

        }

        if (HUDTable.getMoves() < SalmonLadderConstants.PREFERENCES.getBestMoves(levelNumber)) {

            SalmonLadderConstants.PREFERENCES.setBestMoves(levelNumber, HUDTable.getMoves());

        }

        return starsToAward;

    }

    public static void unlockNext() {

        if (levelNumber != SalmonLadderConstants.LEVEL_COUNT) {

            SalmonLadderConstants.PREFERENCES.setStatus(levelNumber + 1, "Unlocked");

        }

    }

    public static void resetBooleans(boolean reset) {

        inAnimation = reset;

        inMenu = reset;

        inTutorial = reset;

        inDeath = reset;

        inWin = reset;

        bobberSprites = new Array<BobberSprite>();

        bearSprites = new Array<BearSprite>();

        eventFishers = new Array<Array<EventFisher>>();

    }

    @Override
    public void render(float delta) {

        // BACKGROUND SET COLOUR
        Gdx.gl.glClearColor(112 / 255f, 166 / 255f, 130 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!FishSprite.entranceAnimation && !getXMLRoot().getChildByName("Tutorial").get("Type").equals("None") && !SalmonLadderConstants.PREFERENCES.getTutorialCompeted(levelNumber)) {

            SalmonLadderConstants.PREFERENCES.setTutorialCompleted(levelNumber, true);

            TutorialTable = new MenuTutorial(1.75f, 1.25f, SalmonLadderConstants.BACKGROUND_PAUSE, getXMLRoot().getChildByName("Tutorial").get("Type"));

            stage.addActor(TutorialTable);

            inTutorial = true;

        }

        // POSITION CAMERA TO THE CENTER OF THE FISH SPRITE
        if (!winCameraLock) {
            camera.position.set(fish.getX() + SalmonLadderConstants.PIXEL_PER_METER / 2, fish.getY() + SalmonLadderConstants.PIXEL_PER_METER / 2, 0);
        }
        camera.update();

        if (!inAnimation && ((TiledMapTileLayer) propertyLayer).getCell((int) (fish.getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (fish.getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class).substring(0, 5).equals("Event")) {
            event = ((TiledMapTileLayer) propertyLayer).getCell((int) (fish.getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (fish.getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class);
            if (event.equals("EventBear")) {
                inAnimation = true;
                for (BearSprite i : bearSprites) {
                    if (i.getEventX() == fish.getX() / SalmonLadderConstants.PIXEL_PER_METER && i.getEventY() == fish.getY() / SalmonLadderConstants.PIXEL_PER_METER) {
                        bear = i;
                        bear.animate();
                    }
                }
            }
        }

        for (BobberSprite i : bobberSprites) {
            if (i.getBoundingRectangle().overlaps(fish.getBoundingRectangle()) && !inAnimation && !inMenu && !inDeath) {
                inAnimation = true;
                i.animate();
                fish.animate(i);
            }
        }

        int[] arr = {0, 1, 2};
        int[] arr2 = {3, 4};

        // SET THE CAMERA TO RENDER THE TILEMAP
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render(arr);

        // DRAW THE FISH WITH RESPECT TO THE TILEMAP
        tiledMapRenderer.getBatch().begin();
        fish.draw(tiledMapRenderer.getBatch());
        tiledMapRenderer.getBatch().end();

        tiledMapRenderer.render(arr2);

        tiledMapRenderer.getBatch().begin();
        for (BearSprite i : bearSprites) {
            i.draw(tiledMapRenderer.getBatch());
        }

        for (BobberSprite i : bobberSprites) {
            i.draw(tiledMapRenderer.getBatch());
        }
        tiledMapRenderer.getBatch().end();

        if (inMenu) {

            dimRectangle();

            PauseTable.bringToCenter(SalmonLadderConstants.MENU_TYPE_PAUSE);

        }

        if (inDeath) {

            dimRectangle();

            DeathTable.bringToCenter(SalmonLadderConstants.MENU_TYPE_DEATH);

        }

        if (inTutorial) {

            dimRectangle();

            TutorialTable.bringToCenter(SalmonLadderConstants.MENU_TYPE_TUTORIAL);

        }

        if (inWin) {

            dimRectangle();

            WinTable.bringToCenter(SalmonLadderConstants.MENU_TYPE_WIN);

        }

        stage.act();
        stage.draw();

        if (SalmonLadderConstants.PREFERENCES.isMusicEnabled()) {

            SalmonLadderConstants.MUSIC_AMBIANT.play();

        } else {

            SalmonLadderConstants.MUSIC_AMBIANT.stop();

        }

        if (SalmonLadderConstants.PREFERENCES.isSoundEnabled()) {

            soundTime += Gdx.graphics.getDeltaTime();

            if (soundTime > 3f) {

                soundTime = 0f;

                SalmonLadderConstants.SOUND_WATER_1.play();

            }

        }

    }

    @Override
    public void resize(int width, int height) {

        camera.viewportWidth = width / SalmonLadderConstants.SCREEN_CONSTANT;
        camera.viewportHeight = height / SalmonLadderConstants.SCREEN_CONSTANT;

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

        // GRAB START COORDINATES FROM THE XML FILE
        int startX = getXMLRoot().getChildByName("StartCoords").getInt("x");
        int startY = getXMLRoot().getChildByName("StartCoords").getInt("y");

        // LOAD MAP INTO VARIABLE
        tiledMap = new TmxMapLoader().load(getXMLRoot().get("LevelFile"));
        propertyLayer = tiledMap.getLayers().get(1);

        // SET THE RENDERER TO RENDER THE TILEMAP
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // SET CAMERA TO ORTHOGRAPHIC (TOP-DOWN)
        camera = new OrthographicCamera();

        if (levelNumber != 1) {
            winCameraLock = true;
            camera.position.set(startX * SalmonLadderConstants.PIXEL_PER_METER + SalmonLadderConstants.PIXEL_PER_METER / 2, startY * SalmonLadderConstants.PIXEL_PER_METER + SalmonLadderConstants.PIXEL_PER_METER / 2, 0);
            camera.update();
        } else {
            winCameraLock = false;
        }

        // VARIABLE FOR SKIN OF THE FISH
        int fishSkin = 0;

        // CREATE NEW FISH SPRITE TO PLACE ON THE MAP
        fish = new FishSprite(fishSkin, (TiledMapTileLayer) propertyLayer);

        // SET STARTING POSITION OF FISH USING VARIABLES FROM XML FILE
        if (levelNumber != 1) {
            fish.setPosition(startX * SalmonLadderConstants.PIXEL_PER_METER, 0);
        } else {
            fish.setPosition(startX * SalmonLadderConstants.PIXEL_PER_METER, startY * SalmonLadderConstants.PIXEL_PER_METER);
        }

        for (int i = 0; i < ((TiledMapTileLayer) propertyLayer).getWidth(); i++) {
            for (int j = 0; j < ((TiledMapTileLayer) propertyLayer).getHeight(); j++) {
                boolean added = false;
                if (((TiledMapTileLayer) propertyLayer).getCell(i, j).getTile().getProperties().get("Name", String.class).equals("EventBear")) {
                    if (((TiledMapTileLayer) propertyLayer).getCell(i + 1, j) != null) {
                        if (((TiledMapTileLayer) propertyLayer).getCell(i + 1, j).getTile().getProperties().get("Name", String.class).equals("Rock")) {
                            BearSprite bear = new BearSprite(i, j, false);
                            bear.setPosition((i + 1) * SalmonLadderConstants.PIXEL_PER_METER - 13, j * SalmonLadderConstants.PIXEL_PER_METER + 16);
                            bearSprites.add(bear);
                            added = true;
                        }
                    }
                    if (!added && ((TiledMapTileLayer) propertyLayer).getCell(i - 1, j) != null) {
                        if (((TiledMapTileLayer) propertyLayer).getCell(i - 1, j).getTile().getProperties().get("Name", String.class).equals("Rock")) {
                            BearSprite bear = new BearSprite(i, j, true);
                            bear.setPosition((i - 1) * SalmonLadderConstants.PIXEL_PER_METER - 5, j * SalmonLadderConstants.PIXEL_PER_METER + 16);
                            bearSprites.add(bear);
                        }
                    }
                } else if (((TiledMapTileLayer) propertyLayer).getCell(i, j).getTile().getProperties().get("Name", String.class).equals("EventFisher")) {
                    boolean exists = false;
                    for (int x = 0; x < eventFishers.size; x++) {
                        for (int y = 0; y < eventFishers.get(x).size; y++) {
                            if (eventFishers.get(x).get(y).getX() == i && eventFishers.get(x).get(y).getY() == j) {
                                exists = true;
                            }
                        }
                    }
                    if (!exists) {
                        Array<EventFisher> arr = new Array<EventFisher>();
                        arr = bobberPath(arr, i, j);
                        eventFishers.add(arr);
                    }
                }
            }
        }

        for (int i = 0; i < eventFishers.size; i++) {
            BobberSprite bobber = new BobberSprite(eventFishers.get(i), findFisher(eventFishers.get(i)));
            bobber.setPosition(bobber.getEventX(0) * SalmonLadderConstants.PIXEL_PER_METER + 13, bobber.getEventY(0) * SalmonLadderConstants.PIXEL_PER_METER + 11);
            bobberSprites.add(bobber);
        }

        stage = new Stage(new FitViewport(SalmonLadderConstants.VIRTUAL_WIDTH, SalmonLadderConstants.VIRTUAL_HEIGHT));

        DimRectangle = new ShapeRenderer();

        HudTable = new HUDTable(0);

        DeathTable = new MenuDeath(2f, 1.5f, SalmonLadderConstants.BACKGROUND_DIE);

        PauseTable = new MenuPause(2f, 1.5f, SalmonLadderConstants.BACKGROUND_PAUSE);

        WinTable = new MenuWin(2f, 1.5f, SalmonLadderConstants.BACKGROUND_WIN);

        stage.addActor(HudTable);

        stage.addActor(DeathTable);

        stage.addActor(PauseTable);

        stage.addActor(WinTable);

        InputMultiplexer multiplexer = new InputMultiplexer();

        Gdx.input.setInputProcessor(multiplexer);

        multiplexer.addProcessor(new GestureDetector(fish));

        multiplexer.addProcessor(stage);

    }

    private void dimRectangle() {

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        DimRectangle.begin(ShapeRenderer.ShapeType.Filled);
        DimRectangle.setColor(0, 0, 0, 0.75f);
        DimRectangle.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        DimRectangle.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

    }

    private Array<EventFisher> bobberPath(Array<EventFisher> arr, int i, int j) {
        boolean added = false, exists = false;
        arr.add(new EventFisher(i, j));
        if (((TiledMapTileLayer) propertyLayer).getCell(i - 1, j) != null) {
            if (((TiledMapTileLayer) propertyLayer).getCell(i - 1, j).getTile().getProperties().get("Name", String.class).equals("EventFisher")) {
                for (EventFisher x : arr) {
                    if (x.getX() == i - 1 && x.getY() == j) {
                        exists = true;
                    }
                }
                if (!exists) {
                    arr = bobberPath(arr, i - 1, j);
                    added = true;
                }
                exists = false;
            }
        }

        if (!added && (((TiledMapTileLayer) propertyLayer).getCell(i + 1, j) != null)) {
            if (((TiledMapTileLayer) propertyLayer).getCell(i + 1, j).getTile().getProperties().get("Name", String.class).equals("EventFisher")) {
                for (EventFisher x : arr) {
                    if (x.getX() == i + 1 && x.getY() == j) {
                        exists = true;
                    }
                }
                if (!exists) {
                    arr = bobberPath(arr, i + 1, j);
                    added = true;
                }
                exists = false;
            }
        }

        if (!added && (((TiledMapTileLayer) propertyLayer).getCell(i, j - 1) != null)) {
            if (((TiledMapTileLayer) propertyLayer).getCell(i, j - 1).getTile().getProperties().get("Name", String.class).equals("EventFisher")) {
                for (EventFisher x : arr) {
                    if (x.getX() == i && x.getY() == j - 1) {
                        exists = true;
                    }
                }
                if (!exists) {
                    arr = bobberPath(arr, i, j - 1);
                    added = true;
                }
                exists = false;
            }
        }

        if (!added && (((TiledMapTileLayer) propertyLayer).getCell(i, j + 1) != null)) {
            if (((TiledMapTileLayer) propertyLayer).getCell(i, j + 1).getTile().getProperties().get("Name", String.class).equals("EventFisher")) {
                for (EventFisher x : arr) {
                    if (x.getX() == i && x.getY() == j + 1) {
                        exists = true;
                    }
                }
                if (!exists) {
                    arr = bobberPath(arr, i, j + 1);
                }
            }
        }
        return arr;
    }

    private Fisherman findFisher(Array<EventFisher> arr) {
        if (((TiledMapTileLayer) propertyLayer).getCell(arr.get(arr.size / 2).getX() + 2, arr.get(arr.size / 2).getY()).getTile().getProperties().get("Name", String.class).equals("Fisherman")) {
            return new Fisherman(arr.get(arr.size / 2).getX() + 2, arr.get(arr.size / 2).getY());
        } else if (((TiledMapTileLayer) propertyLayer).getCell(arr.get(arr.size / 2).getX() - 2, arr.get(arr.size / 2).getY()).getTile().getProperties().get("Name", String.class).equals("Fisherman")) {
            return new Fisherman(arr.get(arr.size / 2).getX() - 2, arr.get(arr.size / 2).getY());
        } else if (((TiledMapTileLayer) propertyLayer).getCell(arr.get(arr.size / 2).getX(), arr.get(arr.size / 2).getY() + 2).getTile().getProperties().get("Name", String.class).equals("Fisherman")) {
            return new Fisherman(arr.get(arr.size / 2).getX(), arr.get(arr.size / 2).getY() + 2);
        } else if (((TiledMapTileLayer) propertyLayer).getCell(arr.get(arr.size / 2).getX(), arr.get(arr.size / 2).getY() - 2).getTile().getProperties().get("Name", String.class).equals("Fisherman")) {
            return new Fisherman(arr.get(arr.size / 2).getX(), arr.get(arr.size / 2).getY() - 2);
        } else return null;
    }

    public static void releaseBobbers() {
        for (BobberSprite i : bobberSprites) {
            i.reposition();
        }
    }
}

/* TODO:
 * Final Win Screen
 * Levels
 * */
