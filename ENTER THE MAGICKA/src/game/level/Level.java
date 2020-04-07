package game.level;

import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import game.entities.Entity;
import game.gfx.Screen;
import game.level.tiles.Tile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Level {

	private byte[] tiles;
	public int width;
	public int height;
	public List<Entity> entities = new ArrayList<Entity>();
	private String imagePath;
	private BufferedImage image;
	

	public Level(String imagePath) {
		if(imagePath != null){
			this.imagePath = imagePath;
			this.loadLevelFromFile();
		}
		else {
			tiles = new byte[width * height];
			this.width = 64;
			this.height = 64;
			this.generateLevel();
		}
	}

	private void loadLevelFromFile(){
		try {
			this.image = ImageIO.read(Level.class.getResource(this.imagePath));
			this.width = image.getWidth();
			this.height = image.getHeight();
			tiles = new byte[width * height];
			this.loadTiles();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**Parse out color of image to get the tile ids */
	private void loadTiles(){
		int[] tileColors = this.image.getRGB(0, 0, this.width, this.height, null, 0, width);
		for (int y = 0; y < height; y ++){
			for (int x = 0; x < width; x++){
				tileCheck: for(Tile t: Tile.tiles){
					if(t != null && t.getLevelColor() == tileColors[x + y * width]){
						this.tiles[x + y * width] = t.getId();
						break tileCheck;
					}
				}
			}
		}
	}	

	/**Saves the level to the same level file if the level has been altered */
	private void saveLevelToFile(){
		try{
			ImageIO.write(image, "png", new File(Level.class.getResource(this.imagePath).getFile()));
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}

	/**Alters and changes a tile (both in the tiles array and image) */
	/**TODO: This could be used to make exits appear on the screen */
	public void alterTile(int x, int y, Tile newTile){
		this.tiles[x + y * width] = newTile.getId();
		image.setRGB(x, y, newTile.getLevelColor());
	}

	/**Default level generator */
	public void generateLevel() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//tiles[x + y * width] = Tile.GRASS.getId();
				
				if (x * y % 10 < 7) {
					tiles[x + y * width] = Tile.GRASS.getId();
				} else {
					tiles[x + y * width] = Tile.STONE.getId();
				}
				
			}
		}
	}

	public void tick() {
		for (Entity e: entities) {
			e.tick();
		}

		for(Tile t: Tile.tiles){
			if (t == null) {
				break;
			}
			t.tick();
		}
	}

	public void renderTiles(Screen screen, int xOffset, int yOffset) {
		int scale = 2; // change << 2 to scale down
		if (xOffset < 0)
			xOffset = 0;
		if (xOffset > ((width << 2) - screen.width))
			xOffset = ((width << 2) - screen.width);
		if (yOffset < 0)
			yOffset = 0;
		if (yOffset > ((height << 2) - screen.height))
			yOffset = ((height << 2) - screen.height);

		screen.setOffset(xOffset, yOffset);

		for (int y = (yOffset >> 2); y < (yOffset + screen.height >> 2) + 1; y++) {
			for (int x = (xOffset >> 2); x < (xOffset + screen.width >> 2) + 1; x++) {
				getTile(x, y).render(screen, this, x << 2, y << 2);
			}
		}
	}

	public void renderEntities(Screen screen){
		for (Entity e: entities) {
			e.render(screen);
		}
	}

    public Tile getTile(int x, int y) {
        if (0 > x || x >= width || 0 > y || y >= height)
            return Tile.VOID;
        return Tile.tiles[tiles[x + y * width]];
	}
	
	public void addEntity(Entity entity){
		this.entities.add(entity);
	}
}