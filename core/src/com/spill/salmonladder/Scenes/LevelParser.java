package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.XmlReader;
import com.spill.salmonladder.FishSprite;
import com.spill.salmonladder.SalmonLadder;

public class LevelParser implements Screen {

    // CREATE A TILED MAP VARIABLE THAT IS USED TO GENERATE THE LEVEL'S GRAPHICS
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    // CREATE A NEW CAMERA
    private OrthographicCamera camera;

    // BOOLEANS FOR LOCKING SCREEN AND PANNING
    public static boolean panMode = false, screenLock = false;

    // CREATE A FISH SPRITE
    private FishSprite fish;

    // LEVEL NUMBER THAT IS PARSED IN
    private int levelNumber;

    // DEFAULT CONSTRUCTOR
    LevelParser(int LevelNumber) {

        this.levelNumber = LevelNumber;

    }

    @Override
    public void show() {

        // PARSE THE XML FILE
        XmlReader.Element root = new XmlReader().parse(Gdx.files.internal("Levels.xml"));

        // GRAB THE ROOT OF THE ENTIRE LEVEL, ALLOWS EASY ACCESS TO LEVEL ATTRIBUTES
        XmlReader.Element LevelAttributes = root.getChildByName("Level" + levelNumber);

        // GRAB START COORDINATES FROM THE XML FILE
        int startX = LevelAttributes.getChildByName("StartCoords").getInt("x");
        int starty = LevelAttributes.getChildByName("StartCoords").getInt("y");

        // LOAD MAP INTO VARIABLE
        tiledMap = new TmxMapLoader().load(LevelAttributes.get("path"));

        // SET THE RENDERER TO RENDER THE TILEMAP
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // SET CAMERA TO ORTHOGRAPHIC (TOP-DOWN)
        camera = new OrthographicCamera();

        // CREATE NEW FISH SPRITE TO PLACE ON THE MAP
        Texture[] fishTextures = new Texture[26];
        for(int i = 0; i < fishTextures.length; i++){
            fishTextures[i] = new Texture("Sprites/Textures/ChinookStagnant_" + (i+1) + ".png");
        }

        fish = new FishSprite(fishTextures, (TiledMapTileLayer) tiledMap.getLayers().get(0), tiledMapRenderer.getUnitScale());

        // SET STARTING POSITION OF FISH USING VARIABLES FROM XML FILE
        fish.setPosition(startX * SalmonLadder.PIXEL_PER_METER, starty * SalmonLadder.PIXEL_PER_METER);

        // SET THE INPUT PROCESSOR TO THE FISH
        Gdx.input.setInputProcessor(new GestureDetector(fish));

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
}
