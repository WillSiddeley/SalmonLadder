package com.spill.salmonladder;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

public class BobberSprite extends Sprite {
    private Array<EventFisher> arr;
    private EventFisher current, next;
    private int index = 0, fishermanX, fishermanY;
    private boolean direction = false, inAnimation = false, reposition = false, inReposition = false, override = false;
    private boolean soundPlayed = false;
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
                translate(0, 1f);
            }
        });

        movement.add(new Timer.Task() {
            @Override
            public void run() {
                translate(1f, 0);
            }
        });

        movement.add(new Timer.Task() {
            @Override
            public void run() {
                translate(0, -1f);
            }
        });

        movement.add(new Timer.Task() {
            @Override
            public void run() {
                translate(-1f, 0);
            }
        });

    }

    @Override
    public void draw(Batch batch) {

        if (inReposition && noTaskScheduled()) {
            inReposition = false;
        }

        if (!LevelParser.inMenu && !reposition && !inReposition) {
            if (!inAnimation || !LevelParser.inAnimation || override) {
                if ((getX() - 13) % 32 == 0 && (getY() - 11) % 32 == 0 && noTaskScheduled()) {
                    movement();
                }
            } else {
                if (noTaskScheduled()) {
                    LevelParser.inAnimation = false;

                    if (SalmonLadderConstants.PREFERENCES.isSoundEnabled() && !soundPlayed) {

                        SalmonLadderConstants.SOUND_DEATH_FISHERMAN.play();

                        soundPlayed = true;

                    }

                    override = true;

                    LevelParser.inDeath = true;
                }
            }
        } else if (!LevelParser.inMenu && reposition) {
            reposition = false;
            if (next.getX() * SalmonLadderConstants.PIXEL_PER_METER != getX() - 13) {
                if (getX() - 13 < next.getX() * SalmonLadderConstants.PIXEL_PER_METER) {
                    Timer.schedule(movement.get(1), 0, 1 / 32f, (int) (next.getX() * SalmonLadderConstants.PIXEL_PER_METER - (getX() - 13)) - 1);
                    inReposition = true;
                } else {
                    Timer.schedule(movement.get(3), 0, 1 / 32f, (int) ((getX() - 13) - next.getX() * SalmonLadderConstants.PIXEL_PER_METER) - 1);
                    inReposition = true;
                }
            } else if (next.getY() * SalmonLadderConstants.PIXEL_PER_METER != getY() - 11) {
                if (getY() - 13 < next.getY() * SalmonLadderConstants.PIXEL_PER_METER) {
                    Timer.schedule(movement.get(0), 0, 1 / 32f, (int) (next.getY() * SalmonLadderConstants.PIXEL_PER_METER - (getY() - 11)) - 1);
                    inReposition = true;
                } else {
                    Timer.schedule(movement.get(2), 0, 1 / 32f, (int) ((getY() - 11) - next.getY() * SalmonLadderConstants.PIXEL_PER_METER) - 1);
                    inReposition = true;
                }
            }
        } else if (inReposition) {

        } else {
            for (Timer.Task i : movement) {
                i.cancel();
            }
        }
        super.draw(batch);
    }

    private void movement() {
        current = arr.get(index);
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
            Timer.schedule(movement.get(0), 0, 1 / 32f, 31);
        } else if (next.getX() > current.getX()) {
            Timer.schedule(movement.get(1), 0, 1 / 32f, 31);
        } else if (next.getY() < current.getY()) {
            Timer.schedule(movement.get(2), 0, 1 / 32f, 31);
        } else if (next.getX() < current.getX()) {
            Timer.schedule(movement.get(3), 0, 1 / 32f, 31);
        }
    }

    public int getEventX(int x) {
        return arr.get(x).getX();
    }

    public int getEventY(int y) {
        return arr.get(y).getY();
    }

    public void animate() {
        inAnimation = true;

        for (Timer.Task i : movement) {
            i.cancel();
        }

        if (getX() / SalmonLadderConstants.PIXEL_PER_METER != fishermanX) {
            if (getX() / SalmonLadderConstants.PIXEL_PER_METER < fishermanX) {
                Timer.schedule(movement.get(1), 0, 1 / 400f, Math.abs((int) (getX() - (fishermanX * SalmonLadderConstants.PIXEL_PER_METER))));
            } else {
                Timer.schedule(movement.get(3), 0, 1 / 400f, Math.abs((int) (getX() - (fishermanX * SalmonLadderConstants.PIXEL_PER_METER))));
            }
        }
        if (getY() / SalmonLadderConstants.PIXEL_PER_METER != fishermanY + 1) {
            if (getY() / SalmonLadderConstants.PIXEL_PER_METER < fishermanY + 1) {
                Timer.schedule(movement.get(0), 0, 1 / 400f, Math.abs((int) (getY() - (fishermanY * SalmonLadderConstants.PIXEL_PER_METER + 32))));
            } else {
                Timer.schedule(movement.get(2), 0, 1 / 400f, Math.abs((int) (getY() - (fishermanY * SalmonLadderConstants.PIXEL_PER_METER + 32))));
            }
        }
    }

    private boolean noTaskScheduled() {
        return !movement.get(0).isScheduled() && !movement.get(1).isScheduled() && !movement.get(2).isScheduled() && !movement.get(3).isScheduled();
    }

    public void reposition() {
        reposition = true;
    }
}