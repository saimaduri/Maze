
import java.awt.Graphics;

public abstract class GameObject {

    protected Location location;

    public GameObject(Location location) {
        this.location = location;
    }

    public GameObject(int x, int y) {
        this(new Location(x, y));
    }

    public Location getLocation() {
        return location;
    }

    public abstract void draw(Graphics g);

}