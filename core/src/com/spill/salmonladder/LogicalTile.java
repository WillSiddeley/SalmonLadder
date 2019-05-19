package com.spill.salmonladder;
public class LogicalTile {

    private int x, y;
    private boolean up, right, down, left;
    private boolean containsFish;

    public LogicalTile(int x, int y){

        this.x = x;
        this.y = y;
        up = false;
        right = false;
        down = false;
        left = false;
        containsFish = false;

    }

    LogicalTile(int x, int y, boolean up, boolean right, boolean down, boolean left) {

        this.x = x;
        this.y = y;
        this.up = up;
        this.right = right;
        this.down = down;
        this.left = left;
        containsFish = false;

    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean getCurWall(int i){

        switch(i){

            case 0: return up;
            case 1: return right;
            case 2: return down;
            case 3: return left;

        }
        return false;
    }

    public boolean getNextWall(int i){

        switch(i){

            case 0: return down;
            case 1: return left;
            case 2: return up;
            case 3: return right;

        }

        return false;
    }

    public boolean getContainsFish() {

        return containsFish;

    }

    public void setContainsFish(boolean fish) {

        this.containsFish = fish;

    }

}
