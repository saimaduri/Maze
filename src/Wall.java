
import java.awt.Graphics;
import java.awt.Color;

public class Wall extends GameObject {

    public static int width;
    public static int height;

    public Wall(int x, int y) {
        super(x, y);
    }

    public Wall(Location location) {
        super(location);
    }

    public void draw(Graphics g) {
        int x = location.getX();
        int y = location.getY();
        g.setColor(Color.GRAY);
        g.fillRect(x * width, y * height, width, height);
        g.setColor(Color.WHITE);
        g.drawRect(x * width, y * height, width, height);
    }

}