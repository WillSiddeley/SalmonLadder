package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class HUDTable extends Table {

    private static Label movesLabel;

    private static int moves = 0;

    // ADD THE CLICK LISTENER FOR PAUSING
    private ClickListener pauseClickListener = new ClickListener() {

        @Override
        public void clicked(InputEvent event, float x, float y) {

            float widthCenter = (Gdx.graphics.getWidth() / 2f) - (LevelParser.PauseTable.getWidth() / 2f);

            float heightCenter = (Gdx.graphics.getHeight() / 2f) - (LevelParser.PauseTable.getHeight() / 2f);

            if (LevelParser.PauseTable.isVisible()) {

                LevelParser.PauseTable.addAction(sequence(moveTo(widthCenter, Gdx.graphics.getHeight() * 2f, 0.2f), run(new Runnable() {

                    @Override
                    public void run() {

                        LevelParser.PauseTable.setVisible(false);

                        LevelParser.screenLock = false;

                    }

                })));

            } else {

                LevelParser.PauseTable.setVisible(true);

                LevelParser.PauseTable.addAction(moveTo(widthCenter, heightCenter, 0.2f));

                LevelParser.screenLock = true;

            }
        }
    };

    HUDTable() {

        this.setFillParent(true);

        this.setDebug(true);

        this.pad(50, 50, 50, 50);

        movesLabel = createMovesLabel(new Skin(Gdx.files.internal("Skins/uiskin.json")));

        ImageButton pauseButton = createPauseButton();

        this.row().expand();

        this.add(movesLabel).top().left();

        this.add(pauseButton).top().right();

    }

    public static int getMoves() {

        return moves;

    }

    public static void setMoves(int newMoves) {

        movesLabel.setText("Moves: " + newMoves);

        moves = newMoves;

    }

    private Label createMovesLabel(Skin skin) {

        Label label = new Label("Moves: " + moves, skin);

        label.setStyle(new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Skins/MovesFont.fnt")), Color.BLACK));

        label.setFontScale(1.5f);

        return label;

    }

    private ImageButton createPauseButton() {

        Drawable gearUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseGear.png"))));

        ImageButton button = new ImageButton(gearUp);

        button.addListener(pauseClickListener);

        return button;

    }


}
