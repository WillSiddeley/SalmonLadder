/*package com.spill.salmonladder;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

public class bobberSprite extends Sprite {

    private Array<EventFisher> arr;
    private EventFisher current;
    private int index = 0;
    private Array<Timer.Task> movement = new Array<Timer.Task>(4);

    public bobberSprite(Array<EventFisher> arr) {
        super(new Texture("Sprites/Bobber.png"));
        this.arr = arr;
        this.current = arr.get(index);

        movement.add(new Timer.Task() {
            @Override
            public void run() {
                translate(0, 2f);
            }
        });

        movement.add(new Timer.Task() {
            @Override
            public void run() {
                translate(2f, 0);
            }
        });

        movement.add(new Timer.Task() {
            @Override
            public void run() {
                translate(0, -2f);
            }
        });

        movement.add(new Timer.Task() {
            @Override
            public void run() {
                translate(-2f, 0);
            }
        });
    }

    @Override
    public void draw(Batch batch) {
        if ((getX() - 13) / SalmonLadderConstants.PIXEL_PER_METER % 32 == 0 && (getY() - 11) / SalmonLadderConstants.PIXEL_PER_METER % 32 == 0) {
            movement();
        }
        super.draw(batch);
    }


    private int movement() {
        EventFisher next = arr.get(++index);
        if (next.getY() > current.getY()) {
            Timer.schedule(movement.get(0), 0, 1 / 16f, 16);
        } else if (next.getX() > current.getX()) {
            Timer.schedule(movement.get(1), 0, 1 / 16f, 16);
        }
    }


    public int getEventX(int x) {
        return arr.get(x).getX();
    }

    public int getEventY(int y) {
        return arr.get(y).getY();
    }
}
*/