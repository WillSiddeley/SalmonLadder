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

public class HUDTable extends Table {

    private static Label movesLabel;

    private static int moves;

    // ADD THE CLICK LISTENER FOR PAUSING
    private ClickListener pauseClickListener = new ClickListener() {

        @Override
        public void clicked(InputEvent event, float x, float y) {

            if (!LevelParser.inAnimation && !LevelParser.inWin) {

                if (LevelParser.PauseTable.isVisible()) {

                    LevelParser.PauseTable.bringUp(0.3f);

                } else {

                    LevelParser.PauseTable.bringToCenter(0.3f);

                }
            }
        }
    };

    HUDTable(int moves) {

        HUDTable.moves = moves;

        this.setFillParent(true);

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

        Drawable gearUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseMenuGear.png"))));

        ImageButton button = new ImageButton(gearUp);

        button.addListener(pauseClickListener);

        return button;

    }


}
