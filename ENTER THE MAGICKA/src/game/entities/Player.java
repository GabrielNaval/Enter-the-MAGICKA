package game.entities;

import game.InputHandler;
import game.gfx.Colors;
import game.gfx.Screen;
import game.level.Level;
//TODO: FIX PLAYER SPRITE - HE SHOULD BE SYMMETRICAL SO THAT WAY FLIPPING THE SPRITE WORKS CORRECTLY

public class Player extends Mob {

    private InputHandler input;
    private int color = Colors.get(-1, 111, 145, 543);
    protected boolean isSwimming = false;
    private int tickCount = 0;

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

        if(input.moveX - x > 0){
            xa++;
            System.out.printf("playerX: %5d, playerY: %5d\n", x, y);
        }
        if(input.moveX - x < 0){
            xa--;
        }
        if(input.moveY - y > 0){
            ya++;
        }
        if(input.moveY - y < 0){
            ya--;
        }
        

        if(xa != 0 || ya != 0){
            move(xa, ya);
            isMoving = true;
        }
        else{
            isMoving = false;
        }
        if (level.getTile(this.x >> 3, this.y >>3).getId() == 3){
            isSwimming = true;
        }
        if(isSwimming && level.getTile(this.x >> 3, this.y >> 3).getId() != 3){
            isSwimming = false;
        }
        tickCount ++;
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
        int flipTop = (movingDir ) % 2;
        int flipBottom = (movingDir) % 2;

        // This was previously used to make the player to "jitter" when they walk
        // int flipTop = (numSteps >> walkingSpeed) & 1;
        // int flipBottom = (numSteps >> walkingSpeed) & 1; 
        
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
        
        if(isSwimming) {
            int waterColor = 0;
            yOffset += 4;
            if (tickCount % 60 < 15) {
                waterColor = Colors.get(-1, -1, 225, -1);
            }
            else if ( 15 <= tickCount % 60 && tickCount % 60 < 30){
                yOffset -=1;
                waterColor = Colors.get(-1, 225, 115, -1);
            }
            else if (30 <= tickCount % 60 && tickCount % 60 < 45){
                waterColor = Colors.get(-1, 115, -1, 225);
            }
            else{
                yOffset -=1;
                waterColor = Colors.get(-1, 225, 115, -1);
            }
            screen.render(xOffset, yOffset + 3, 0 + 27 * 32, waterColor, 0x00, 1);
            screen.render(xOffset + 8, yOffset + 3, 0 + 27 * 32, waterColor, 0x01, 1);
        }

        //We use modifier * flipTop to correct the sprite when we flip it (because when we mirror, we only
        //flip IN PLACE, so we want to move it so it flips across the player's y-axis)
        /**Upper body */
        screen.render(xOffset + (modifier * flipTop), yOffset, xTile + yTile * 32, color, flipTop, scale);
        screen.render(xOffset + modifier - (modifier * flipTop), yOffset, xTile + 1 + yTile * 32, color, flipTop, scale);


        if(!isSwimming){
            /**Lower body */
            screen.render(xOffset + (modifier * flipBottom), yOffset + modifier, xTile + (yTile + 1) * 32, color, flipBottom, scale);
            screen.render(xOffset + modifier - (modifier * flipBottom), yOffset + modifier, xTile + 1 + (yTile + 1)* 32, color, flipBottom, scale);
        }
    }

    /**
     * This method determines collisions with SOLID tiles.
     */
    @Override
    public boolean hasCollided(int xa, int ya) {
        /**These will determine the hitbox(rectangle) of our player */
        int xMin = 0;
        int xMax = 7;
        int yMin = 3;
        int yMax = 7;

        //NOTE THIS COULD BE MADE MORE EFFICIENT BUT IM LAZY
        //4 for loops correspond to the sides of the rectangle hitbox
        for(int x = xMin; x < xMax; x++){
            if(isSolidTile(xa, ya, x, yMin)){
                return true;
            }
        }

        for(int x = xMin; x < xMax; x++){
            if(isSolidTile(xa, ya, x, yMax)){
                return true;
            }
        }

        for(int y = yMin; y < yMax; y++){
            if(isSolidTile(xa, ya, xMin, y)){
                return true;
            }
        }

        for(int y = yMin; y < yMax; y++){
            if(isSolidTile(xa, ya, xMax, y)){
                return true;
            }
        }

        return false;
    }
    
}