package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class PagedPane extends ScrollPane {

    private boolean panDragFling = false;

    private Table levels;

    PagedPane() {

        super(null);

        setPrefs();

    }

    private void setPrefs() {

        levels = new Table();

        levels.defaults().space(50);

        super.setActor(levels);

    }

    void addPage(Actor page) {

        levels.add(page).expandY().fillY();

    }

    @Override
    public void act(float delta) {

        super.act(delta);

        if (panDragFling && !isPanning() && !isDragging() && !isFlinging()) {

            panDragFling = false;

            scrollToPage();

        } else {

            if (isPanning() || isDragging() || isFlinging()) {

                panDragFling = true;

            }
        }
    }

    @Override
    public void setWidth(float width) {

        super.setWidth(width);

        if (levels != null) {

            for (Cell cell : levels.getCells()) {

                cell.width(width);

            }

            levels.invalidate();

        }
    }

    void setPageSpacing(float pageSpacing) {

        if (levels != null) {

            levels.defaults().space(pageSpacing);

            for (Cell cell : levels.getCells()) {

                cell.space(pageSpacing);

            }

            levels.invalidate();

        }
    }

    private void scrollToPage() {

        final float width = getWidth();
        final float scrollX = getScrollX();
        final float maxX = getMaxX();

        if (scrollX >= maxX || scrollX <= 0) {

            return;

        }

        Array<Actor> pages = levels.getChildren();

        float pageX = 0;
        float pageWidth = 0;

        if (pages.size > 0) {

            for (Actor a : pages) {

                pageX = a.getX();

                pageWidth = a.getWidth();

                if (scrollX < (pageX + pageWidth * 0.5)) {

                    break;

                }

            }

            setScrollX(MathUtils.clamp(pageX - (width - pageWidth) / 2, 0, maxX));

        }
    }
}
