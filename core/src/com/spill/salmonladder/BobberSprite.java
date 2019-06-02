package com.spill.salmonladder;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

public class BobberSprite extends Sprite {
    //
    private Array<EventFisher> arr;
    private EventFisher current;
    private int index = 0, fishermanX, fishermanY;
    private boolean direction = false, inAnimation = false;
    private Timer.Task deathX, deathY;
    private Array<Timer.Task> movement = new Array<Timer.Task>(4);

    public BobberSprite(Array<EventFisher> arr, Fisherman fisherman) {
        super(new Texture("Sprites/Bobber.png"));
        this.arr = arr;
        this.current = arr.get(index);
        this.fishermanX = fisherman.getX();
        this.fishermanY = fisherman.getY();

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

        deathX = new Timer.Task() {
            @Override
            public void run() {
                translateX(1f);
            }
        };

        deathY = new Timer.Task() {
            @Override
            public void run() {
                translateY(1f);
            }
        };
    }

    @Override
    public void draw(Batch batch) {
        if (!inAnimation) {
            if ((getX() - 13) / SalmonLadderConstants.PIXEL_PER_METER % 32 == 0 && (getY() - 11) / SalmonLadderConstants.PIXEL_PER_METER % 32 == 0) {
                movement();
            }
        }
        super.draw(batch);
    }

    private void movement() {
        EventFisher next;
        if (direction) {
            next = arr.get(--index);
            if (index == 0) {
                direction = false;
            }
        } else {
            next = arr.get(++index);
            if (index == arr.size - 1) {
                direction = true;
            }
        }

        if (next.getY() > current.getY()) {
            Timer.schedule(movement.get(0), 0, 1 / 16f, 16);
        } else if (next.getX() > current.getX()) {
            Timer.schedule(movement.get(1), 0, 1 / 16f, 16);
        } else if (next.getY() < current.getY()) {
            Timer.schedule(movement.get(2), 0, 1 / 16f, 16);
        } else if (next.getX() < current.getX()) {
            Timer.schedule(movement.get(3), 0, 1 / 16f, 16);
        }
    }

    public int getEventX(int x) {
        return arr.get(x).getX();
    }

    public int getEventY(int y) {
        return arr.get(y).getY();
    }

    public int getFishermanX() {
        return fishermanX;
    }

    public int getFishermanY() {
        return fishermanY;
    }

    public void animate() {
        inAnimation = true;

        if (getX() / SalmonLadderConstants.PIXEL_PER_METER != fishermanX) {
            Timer.schedule(deathX, 0, 1 / 100f, Math.abs((int) (getX() / (fishermanX * SalmonLadderConstants.PIXEL_PER_METER))) - 1);
        }
        if (getY() / SalmonLadderConstants.PIXEL_PER_METER != fishermanY) {
            Timer.schedule(deathY, 0, 1 / 100f, Math.abs((int) (getY() / (fishermanY * SalmonLadderConstants.PIXEL_PER_METER))) - 1);
        }
    }
}