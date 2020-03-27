package game.entities;

import game.InputHandler;
import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;

public class Player extends Mob {

    private InputHandler input;
    private int color = Colors.get(-1, 111, 145, 543);

    public Player(Level level, int x, int y, InputHandler input) {
        super(level, "Player", x, y, 1);
        this.input = input;
    }

    

    @Override
    public void tick() {
        int xa = 0;
        int ya = 0;
        /**This is for input testing purposes. Remove later */
        if (input.up.isPressed()){ 
            ya--;
        }
        if (input.down.isPressed()){ 
            ya++;
        }
        if (input.left.isPressed()){ 
            xa--;
        }
        if (input.right.isPressed()){ 
            xa++;
        }

        if(xa != 0 || ya != 0){
            move(xa, ya);
            isMoving = true;
        }
        else{
            isMoving = false;
        }
    }

    @Override
    public void render(Screen screen) {
        int xTile = 0;
        int yTile = 28;
        
        /**
         * walking speed determines how fast the animation should take.
         * flipTop and flipBottom are used to determine the direction
         * the sprite should be facing. If going left, mirror the sprite 
         * so it faces left 
        */
        int walkingSpeed = 4;
        int flipTop = (numSteps >> walkingSpeed) & 1;
        int flipBottom = (numSteps >> walkingSpeed) & 1;
        
        //if we're moving down, change to the appropriate sprite
        if (movingDir == 1){
            xTile += 2;
        }
        //if we're moving left or right, figure out which sprite to show and 
        //the direction we want the sprite to be facing
        else if (movingDir > 1) {
            xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
            flipTop = (movingDir - 1) % 2;
            flipBottom = (movingDir - 1) % 2;
        }

        int modifier = 8 * scale;
        // xOffset and yOffset determine where we want the components of the sprite to be
        // relative to the center of the Player sprite.
        int xOffset = x - modifier/2;
        int yOffset = y - modifier/2 -4; //-4 so that the waist of the player is the center of the y 
        
        //We use modifier * flipTop to correct the sprite when we flip it (because when we mirror, we only
        //flip IN PLACE, so we want to move it so it flips across the player's y-axis)
        /**Upper body */
        screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, color, flipTop, scale);
        screen.render(xOffset + modifier - (modifier * flipTop), yOffset, xTile + 1 + yTile * 32, color, flipTop, scale);

        /**Lower body */
        screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, color, flipBottom, scale);
        screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, xTile + 1 + (yTile + 1)* 32, color, flipBottom, scale);
    }

    @Override
    public boolean hasCollided(int xa, int ya) {
        return false;
    }
    
}