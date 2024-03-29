package com.spill.salmonladder;

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
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class ScreenLevelSelect implements Screen {

    // ADD THE CAMERA
    private OrthographicCamera camera;

    // ADD THE SKIN FOR BUTTON TEXTURE
    private Skin skin;

    // ADD THE STAGE FOR USER INPUT
    private Stage stage;

    // ADD THE CONTAINER TO HOLD LEVELS
    private Table container = new Table();

    ScreenLevelSelect() {

        // INSTANTIATE THE CAMERA TO THE SIZE OF THE PHONE SCREEN AND UPDATE IT
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        camera.update();

        // SET THE STAGE TO THE SIZE OF THE PHONE SCREEN
        stage = new Stage(new FitViewport(SalmonLadderConstants.VIRTUAL_WIDTH, SalmonLadderConstants.VIRTUAL_HEIGHT));

        // ADD A TABLE TO CONTAIN ALL THE PAGES
        stage.addActor(container);

        // SET THE CONTAINER TO SCREEN SIZE
        container.setFillParent(true);

        // ENABLE OUR STAGE TO BE ABLE TO ACCEPT TOUCH PRESSES AS A FORM OF INPUT
        Gdx.input.setInputProcessor(stage);

        // ENABLE THE APP TO LISTEN TO IF THE BACK KEY IS PRESSED ON ANDROID
        Gdx.input.setCatchBackKey(true);

        // GET THE SKIN TEXTURES FOR THE BUTTON
        skin = SalmonLadderConstants.SKIN;

        skin.add("Level-Locked", skin.newDrawable("ButtonLevel", new Color(150 / 255f, 150 / 255f, 150 / 255f, 1)), Drawable.class);

        skin.add("Level-Unlocked", skin.newDrawable("ButtonLevel", Color.SKY), Drawable.class);

        skin.add("Level-Padlock", skin.newDrawable("ImagePadlock", Color.WHITE), Drawable.class);

        skin.add("Star-Filled", skin.newDrawable("ImageStar", Color.YELLOW), Drawable.class);
        
        skin.add("Star-Unfilled", skin.newDrawable("ImageStarEmpty", Color.GRAY), Drawable.class);

        // MOVE IN THE LEVELS
        stage.addAction(sequence(alpha(0f), moveTo(Gdx.graphics.getWidth() + stage.getWidth(), 0, 0f), alpha(1f), moveTo(0, 0, 0.5f)));

    }

    @Override
    public void show() {

        SalmonLadderConstants.PREFERENCES.setStatus(1, "Unlocked");

        int levelLabel = 1;

        // NEW SCROLL PANE THAT CONTROLS SCROLLING
        PagedPane scroll = new PagedPane();
        scroll.setFlingTime(0.1f);
        scroll.setPageSpacing(25);

        // MAKE THE PAGES WITH THE BUTTONS TO SELECT LEVELS

        for (int i = 0; i < SalmonLadderConstants.PAGES; i++) {

            Table Levels = new Table().pad(25);

            Levels.defaults().pad(20, 40, 20, 40);

            for (int y = 0; y < SalmonLadderConstants.ROWS; y++) {

                Levels.row();

                for (int x = 0; x < SalmonLadderConstants.COLUMNS; x++) {

                    Levels.add(getLevelButton(levelLabel++)).width(SalmonLadderConstants.VIRTUAL_WIDTH / (SalmonLadderConstants.COLUMNS * 2f)).height(SalmonLadderConstants.VIRTUAL_HEIGHT / (SalmonLadderConstants.ROWS * 1.5f)).fill();

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

        // IF THE PHYSICAL BACK BUTTON IS PRESSED ON THE PHONE, GO BACK TO START
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {

            stage.addAction(sequence(alpha(1), moveTo(Gdx.graphics.getWidth() + stage.getWidth(), 0, 0.5f), run(new Runnable() {

                @Override
                public void run() {

                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenStart());

                }

            })));

        }

        if (SalmonLadderConstants.MUSIC_AMBIANT.isPlaying()) {

            SalmonLadderConstants.MUSIC_AMBIANT.stop();

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
        label.setFontScale(1.5f);
        label.setAlignment(Align.center);

        // PUT THE LABEL INSIDE THE BUTTON
        if (SalmonLadderConstants.PREFERENCES.getStatus(level).equals("Unlocked")) {

            button.stack(new Image(skin.getDrawable("Level-Unlocked")), label).expand().fill();

        } else {

            Table tempTable = new Table();

            tempTable.add(new Image(skin.getDrawable("Level-Padlock"))).width(75f).height(100f);

            button.stack(new Image(skin.getDrawable("Level-Locked")), tempTable).expand().fill();

        }

        int stars = SalmonLadderConstants.PREFERENCES.getStars(level);

        // CREATE A TABLE BELOW THE BUTTON THAT RESPERESNTS STARS THE PLAYER GOT ON THE LEVEL
        Table starTable = new Table();
        starTable.defaults().pad(5);
        if (stars >= 0) {

            for (int star = 0; star < 3; star++) {

                if (stars > star) {

                    starTable.add(new Image(skin.getDrawable("Star-Filled"))).width(50).height(50);

                } else {

                    starTable.add(new Image(skin.getDrawable("Star-Unfilled"))).width(50).height(50);

                }
            }
        }

        // CREATE A NEW ROW AND ADD THE STARS TO THE BUTTON
        button.row();
        button.add(starTable).height(30);

        // SET THE NAME OF THE BUTTON TO BE THE LEVEL IT REPRESENTS
        button.setName(Integer.toString(level));
        button.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (SalmonLadderConstants.PREFERENCES.getStatus(Integer.parseInt(event.getListenerActor().getName())).equals("Unlocked")) {

                    if (SalmonLadderConstants.PREFERENCES.isSoundEnabled()) {

                        SalmonLadderConstants.SOUND_CLICK.play();

                    }

                    ((Game) Gdx.app.getApplicationListener()).setScreen(new LevelParser(Integer.parseInt(event.getListenerActor().getName())));

                }
            }

        });

        return button;

    }
}
