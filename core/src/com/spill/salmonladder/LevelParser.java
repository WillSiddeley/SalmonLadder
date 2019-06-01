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

    private static SalmonLadderStars prefStars;

    static HUDTable HudTable;
    static MenuDeath DeathTable;
    static MenuPause PauseTable;

    private MapLayer propertyLayer;
    static MenuWin WinTable;
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

    private Array<BearSprite> bearSprites = new Array<BearSprite>();

    private Array<Array<EventFisher>> eventFishers = new Array<Array<EventFisher>>();

    private Array<bobberSprite> bobberSprites = new Array<bobberSprite>();

    private ShapeRenderer DimRectangle;

    private Stage stage;

    private String event;

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

        if (levelNumber != SalmonLadderConstants.LEVEL_COUNT) {

            prefStars.setStatus(levelNumber + 1, "Unlocked");

        }

    }

    public static void resetBooleans(boolean reset) {

        inAnimation = reset;

        inMenu = reset;

        inDeath = reset;

        inWin = reset;

    }

    @Override
    public void render(float delta) {

        // BACKGROUND SET TO ALL WHITE
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(255f, 255f, 255f, 0);

        // POSITION CAMERA TO THE CENTER OF THE FISH SPRITE
        camera.position.set(fish.getX() + SalmonLadderConstants.PIXEL_PER_METER / 2, fish.getY() + SalmonLadderConstants.PIXEL_PER_METER / 2, 0);
        camera.update();

        if (((TiledMapTileLayer) propertyLayer).getCell((int) (fish.getX() / SalmonLadderConstants.PIXEL_PER_METER), (int) (fish.getY() / SalmonLadderConstants.PIXEL_PER_METER)).getTile().getProperties().get("Name", String.class).substring(0, 5).equals("Event")) {
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

        // SET THE CAMERA TO RENDER THE TILEMAP
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // DRAW THE FISH WITH RESPECT TO THE TILEMAP
        tiledMapRenderer.getBatch().begin();
        fish.draw(tiledMapRenderer.getBatch());
        for (BearSprite i : bearSprites) {
            i.draw(tiledMapRenderer.getBatch());
        }
        tiledMapRenderer.getBatch().end();

        if (inMenu) {

            dimRectangle();

            PauseTable.bringToCenter(0.3f, "pause");

        }

        if (inDeath) {

            dimRectangle();

            DeathTable.bringToCenter(0.3f, "die");

        }

        if (inWin) {

            dimRectangle();

            WinTable.bringToCenter(0.3f, "win");

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
        propertyLayer = tiledMap.getLayers().get(1);

        // SET THE RENDERER TO RENDER THE TILEMAP
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // SET CAMERA TO ORTHOGRAPHIC (TOP-DOWN)
        camera = new OrthographicCamera();

        // VARIABLE FOR SKIN OF THE FISH
        int fishSkin = 0;

        // CREATE NEW FISH SPRITE TO PLACE ON THE MAP
        fish = new FishSprite(fishSkin, (TiledMapTileLayer) propertyLayer);

        // SET STARTING POSITION OF FISH USING VARIABLES FROM XML FILE
        fish.setPosition(startX * SalmonLadderConstants.PIXEL_PER_METER, startY * SalmonLadderConstants.PIXEL_PER_METER);

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
                    Array<EventFisher> arr = new Array<EventFisher>();
                    arr = bobberPath(arr, i, j);
                    eventFishers.add(arr);
                }
            }
        }

        for (int i = 0; i < eventFishers.size; i++) {
            bobberSprite bobber = new bobberSprite(eventFishers.get(i));
            bobber.setPosition(bobber.getEventX(0) * SalmonLadderConstants.PIXEL_PER_METER + 13, bobber.getEventY(0) * SalmonLadderConstants.PIXEL_PER_METER + 11);
            bobberSprites.add(bobber);
        }

        stage = new Stage(new FitViewport(1080, 1920));

        DimRectangle = new ShapeRenderer();

        HudTable = new HUDTable(0);

        DeathTable = new MenuDeath(2f, 1.5f);

        PauseTable = new MenuPause(2f, 1.5f);

        WinTable = new MenuWin(2f, 1.5f);

        DeathTable.setNinePatchBG(SalmonLadderConstants.BACKGROUND_DIE);

        PauseTable.setNinePatchBG(SalmonLadderConstants.BACKGROUND_PAUSE);

        WinTable.setNinePatchBG(SalmonLadderConstants.BACKGROUND_WIN);

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
        boolean added = false;
        ((TiledMapTileLayer) propertyLayer).getCell(i, j).getTile().getProperties().put("Name", "");
        if (((TiledMapTileLayer) propertyLayer).getCell(i - 1, j) != null) {
            if (((TiledMapTileLayer) propertyLayer).getCell(i - 1, j).getTile().getProperties().get("Name", String.class).equals("EventFisher")) {
                arr = bobberPath(arr, i - 1, j);
                added = true;
            }
        }

        if (!added && (((TiledMapTileLayer) propertyLayer).getCell(i + 1, j) != null)) {
            if (((TiledMapTileLayer) propertyLayer).getCell(i + 1, j).getTile().getProperties().get("Name", String.class).equals("EventFisher")) {
                arr = bobberPath(arr, i + 1, j);
                added = true;
            }
        }

        if (!added && (((TiledMapTileLayer) propertyLayer).getCell(i, j - 1) != null)) {
            if (((TiledMapTileLayer) propertyLayer).getCell(i, j - 1).getTile().getProperties().get("Name", String.class).equals("EventFisher")) {
                arr = bobberPath(arr, i, j - 1);
                added = true;
            }
        }

        if (!added && (((TiledMapTileLayer) propertyLayer).getCell(i, j + 1) != null)) {
            if (((TiledMapTileLayer) propertyLayer).getCell(i, j + 1).getTile().getProperties().get("Name", String.class).equals("EventFisher")) {
                arr = bobberPath(arr, i, j + 1);
            }
        }

        arr.add(new EventFisher(i, j));
        return arr;
    }
}
