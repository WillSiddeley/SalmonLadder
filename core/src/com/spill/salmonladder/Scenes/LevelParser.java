package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.XmlReader;
import com.spill.salmonladder.FishSprite;

public class LevelParser implements Screen {

    // CREATE A TILED MAP VARIABLE THAT IS USED TO GENERATE THE LEVEL'S GRAPHICS
    private TiledMap tiledMap;
    public static OrthogonalTiledMapRenderer tiledMapRenderer;

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

        XmlReader.Element root = new XmlReader().parse(Gdx.files.internal("Levels.xml"));

        XmlReader.Element LevelAttributes = root.getChildByName("Level" + levelNumber);

        tiledMap = new TmxMapLoader().load(LevelAttributes.get("path"));

        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        camera = new OrthographicCamera();

        fish = new FishSprite(new Sprite(new Texture("Sprites/salmon.png")), (TiledMapTileLayer) tiledMap.getLayers().get(0));

        fish.setPosition(9 * 32f, 2 * 32f);

        Gdx.input.setInputProcessor(new GestureDetector(fish));

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(255f, 255f, 255f, 0);

        camera.position.set(fish.getX() + 16f, fish.getY() + 16f, 0);
        camera.update();

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

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
