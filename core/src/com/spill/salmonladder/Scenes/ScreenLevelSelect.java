package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ScreenLevelSelect implements Screen {

    // ADD THE CAMERA
    private OrthographicCamera camera;

    // ADD THE SKIN FOR BUTTON TEXTURE
    private Skin skin;

    // ADD THE STAGE FOR USER INPUT
    private Stage stage;
    private ClickListener levelClickListener = new ClickListener() {

        @Override
        public void clicked(InputEvent event, float x, float y) {

            // WHEN A BUTTON IS CLICKED, GRAB THE NAME AND SET THE CORRECT LEVEL
            ((Game) Gdx.app.getApplicationListener()).setScreen(new LevelParser(Integer.parseInt(event.getListenerActor().getName())));

        }
    };

    ScreenLevelSelect() {

        // INSTANTIATE THE CAMERA TO THE SIZE OF THE PHONE SCREEN AND UPDATE IT
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        camera.update();

        // SET THE STAGE TO THE SIZE OF THE PHONE SCREEN
        stage = new Stage(new ScreenViewport());

        // ENABLE THE APP TO LISTEN TO IF THE BACK KEY IS PRESSED ON ANDROID
        Gdx.input.setCatchBackKey(true);

        // GET THE SKIN TEXTURES FOR THE BUTTON
        skin = new Skin(Gdx.files.internal("Skins/uiskin.json"));
        skin.add("top", skin.newDrawable("default-round", Color.RED), Drawable.class);
        skin.add("star-filled", skin.newDrawable("white", Color.YELLOW), Drawable.class);
        skin.add("star-unfilled", skin.newDrawable("white", Color.GRAY), Drawable.class);

        // ENABLE OUR STAGE TO BE ABLE TO ACCEPT TOUCH PRESSES AS A FORM OF INPUT
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void show() {

        // VARIABLES TO CONTROL LEVEL SELECT BUTTONS
        int rows = 7;
        int columns = 4;
        int pages = 1;
        int levelLabel = 1;

        // ADD A TABLE TO CONTAIN ALL THE PAGES
        Table container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        // NEW SCROLL PANE THAT CONTROLS SCROLLING
        PagedPane scroll = new PagedPane();
        scroll.setFlingTime(0.1f);
        scroll.setPageSpacing(25);

        // MAKE THE PAGES WITH THE BUTTONS TO SELECT LEVELS
        for (int i = 0; i < pages; i++) {

            Table Levels = new Table().pad(25);

            Levels.defaults().pad(20, 40, 20, 40);

            for (int y = 0; y < rows; y++) {

                Levels.row();

                for (int x = 0; x < columns; x++) {

                    Levels.add(getLevelButton(levelLabel++)).width(Gdx.graphics.getWidth() / (columns * 2f)).height(Gdx.graphics.getHeight() / (rows * 1.5f)).fill();

                }

            }

            scroll.addPage(Levels);
        }

        container.add(scroll).expand().fill();

    }

    @Override
    public void resize(int width, int height) {

        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void render(float delta) {

        // SET THE BACKGROUND COLOUR OF THE TITLE SCREEN
        Gdx.gl.glClearColor(255f, 255f, 255f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // EVERY FRAME, UPDATE THE STAGE FRAME
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // IF THE PHYSICAL BACK BUTTON IS PRESSED ON THE PHONE, EXIT THE APP
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {

            ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenStart());

        }

    }

    @Override
    public void dispose() {

        stage.dispose();

        skin.dispose();

    }

    private Button getLevelButton(int level) {

        // CREATE A NEW BUTTON AND SET ITS STYLE
        Button button = new Button(skin);
        Button.ButtonStyle style = button.getStyle();
        style.up = style.down = null;

        // CREATE A LABEL THAT IS THE NUMBER INSIDE THE ICON
        Label label = new Label(Integer.toString(level), skin);
        label.setFontScale(2f);
        label.setAlignment(Align.center);

        // PUT THE LABEL INSIDE THE BUTTON
        button.stack(new Image(skin.getDrawable("top")), label).expand().fill();

        // PARSE THE XML FILE
        XmlReader.Element root = new XmlReader().parse(Gdx.files.internal("Stars.xml"));

        // GRAB THE ROOT OF THE SPECIFIC LEVEL
        XmlReader.Element LevelAttributes = root.getChildByName("Level" + level);

        // GRAB THE STAR COUNT PER LEVEL
        int stars = LevelAttributes.getInt("Stars");

        // CREATE A TABLE BELOW THE BUTTON THAT RESPERESNTS STARS THE PLAYER GOT ON THE LEVEL
        Table starTable = new Table();
        starTable.defaults().pad(5);
        if (stars >= 0) {

            for (int star = 0; star < 3; star++) {

                if (stars > star) {

                    starTable.add(new Image(skin.getDrawable("star-filled"))).width(20).height(20);

                } else {

                    starTable.add(new Image(skin.getDrawable("star-unfilled"))).width(20).height(20);

                }
            }
        }

        // CREATE A NEW ROW AND ADD THE STARS TO THE BUTTON
        button.row();
        button.add(starTable).height(30);

        // SET THE NAME OF THE BUTTON TO BE THE LEVEL IT REPRESENTS
        button.setName(Integer.toString(level));
        button.addListener(levelClickListener);
        return button;

    }
}
