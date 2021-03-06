package game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.Point;
import javax.swing.JFrame;

import game.entities.Player;
import game.gfx.Colors;
import game.gfx.Font;
import game.gfx.Screen;
import game.gfx.SpriteSheet;
import game.level.Level;

public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 160;
    public static final int HEIGHT = WIDTH / 12 * 9;
    public static final int SCALE = 6;
    public static final String NAME = "ENTER THE MAGICKA WOAH";

    private JFrame frame;
    public boolean running = false;
    public int tickCount = 0;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private int[] colors = new int[6 * 6 * 6];

    private Screen screen;
    public InputHandler input;
    public Level level;
    public Player player;
    
    /**Setting dimension of the canvas */
    public Game() {
        setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame(NAME);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(this, BorderLayout.CENTER);
        frame.pack(); /* Keeps everything sized correctly*/

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Toolkit toolkit = Toolkit.getDefaultToolkit();
        // // Transparent 16 x 16 pixel cursor image.
        // Image cursorImg = toolkit.getImage("res/cursor.png");

        // // Create a new blank cursor.
        // Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        //     cursorImg, new Point(0, 0), "blank cursor");
        
        // // Set the blank cursor to the JFrame.
        // frame.getContentPane().setCursor(blankCursor);
    }

    public void init(){
        int index = 0;
        for (int r = 0; r < 6; r++){
            for (int g = 0; g < 6; g++){
                for (int b = 0; b < 6; b++){
                    int rr = (r * 255/5);
                    int gg = (g * 255/5);
                    int bb = (b * 255/5);

                    colors[index++] = rr << 16 | gg << 8 | bb;
                }
            }
        }
        
        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/sprite_sheet.png"));
        input = new InputHandler(this);
        level = new Level("/levels/water_test_level.png");
        player = new Player(level, 0, 0, input);
        level.addEntity(player);
    }

    public synchronized void start(){
        running = true;
        new Thread(this).start();
    }

    public synchronized void stop(){
        running = false;
    }

    /**Where updating of each frame occurs */
    public void run(){
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D/60D; /*How many nanoseconds per tick */

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        init();

        while (running){
            long now = System.nanoTime();
            delta += (now - lastTime)/ nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while(delta >= 1){
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }
            
            if(shouldRender){
                frames ++;
                render();
            }
            
            if(System.currentTimeMillis() - lastTimer > 1000){
                lastTimer += 1000;    
                System.out.println(ticks  + " ticks, " + frames + " frames");
                frames = 0;
                ticks = 0;
            } 
        }
    }

    /**
     * Update the logic of the game
    */
    public void tick(){
        tickCount++;
        level.tick();
    }

    /**Used to actually display images on the canvas*/
    public void render(){
        BufferStrategy bs = getBufferStrategy();
        if (bs == null){
            createBufferStrategy(3);
            return;
        }

        int xOffset = player.x - (screen.width/2);
        int yOffset = player.y - (screen.height/2);

        level.renderTiles(screen, xOffset, yOffset);

        /*
        //Font things
        for(int x = 0; x < level.width; x++){
            int color = Colors.get(-1, -1, -1, 000);
            if (x %10 == 0 && x != 0){
                color = Colors.get(-1, -1, -1, 500);
            }
            Font.render((x%10) + "", screen, 0 + (x * 8), 0, color);
        }
        */
        level.renderEntities(screen);
        // String msg = "Hello World! 0157";
        // Font.render(msg, screen, screen.xOffset + screen.width/2 - (msg.length()*8/2), screen.yOffset + screen.height/2, Colors.get(-1, -1, -1, 0));

        for(int y = 0; y< screen.height; y++){
            for(int x = 0; x< screen.width; x++){
                int colorCode = screen.pixels[x + y * screen.width];
                if (colorCode <255){
                    pixels[x + y * WIDTH] = colors[colorCode];
                }
            }
        }
        
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

    /** Creating a thread and starting it*/
    public static void main(String[] args){
        new Game().start();
    }
}