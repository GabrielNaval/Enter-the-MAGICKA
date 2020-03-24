package game.entities;

import game.level.Level;

public abstract class Mob extends Entity {

    protected String name;
    protected int speed;
    protected int numSteps;
    protected boolean isMoving;
    protected int scale = 1;

    /**
     * 0 is up, 1 is down, 2 is left, 3 is right  
     * */
    protected int movingDir = 1;

    public Mob(Level level, String name, int x, int y, int speed) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        
    }


    public void move(int xa, int ya){
        if(xa != 0 && ya != 0){
            move(xa, 0);
            move(0, ya);
            numSteps--;
            return;
        }
        numSteps++;
        if(!hasCollided(xa, ya)){
            if (ya < 0) {
                movingDir = 0;
            }
            if (ya > 0){
                movingDir = 1;
            }
            if(xa < 0){
                movingDir = 2;
            }
            if(xa > 0){
                movingDir = 3;
            }
            x += xa * speed;
            y += ya * speed;
        }
    }

    public abstract boolean hasCollided(int xa, int ya);

    public String getName(){
        return name;
    }

}