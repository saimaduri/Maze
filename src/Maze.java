
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.List;

public class Maze {

    private GameObject[][] gameObjects;
    private Explorer explorer;
    private Location endPos;

    public static final int wMargin3d = (int) (0.05 * Application.screenWidth);
    public static final int hMargin3d = (int) (0.05 * Application.screenHeight);
    public static final int screenWidth3d = Application.screenWidth - 2 * wMargin3d;
    public static final int screenHeight3d = Application.screenHeight - 2 * hMargin3d;
    public static final Wall tempWall = new Wall(0, 0);

    public static final Color floorColor = Color.BLUE;
    public static final Color ceilingColor = Color.WHITE;
    public static final Color wallColor = Color.GRAY;
    public static final Color lineColor = Color.BLACK;
    public static final Color paintedColor = Color.MAGENTA;

    public Maze(int w, int h) {

        gameObjects = new GameObject[w][h];

        explorer = new Explorer(0, 0);
        gameObjects[0][0] = explorer;
        explorer.setMaze(this);

    }

    public Maze(GameObject[][] gameObjects, int explorerX, int explorerY, Location endPos) {
        int w = gameObjects.length;
        int h = gameObjects[0].length;

        System.out.println(Application.screenWidth);
        Wall.width = Application.screenWidth / w > 30 ? 30 : Application.screenWidth / w;
        Wall.height = Application.screenHeight / h > 30 ? 30 : Application.screenHeight / h;

        this.gameObjects = gameObjects;
        explorer = (Explorer) gameObjects[explorerX][explorerY];
        explorer.setMaze(this);
        this.endPos = endPos;
    }

    public GameObject[][] getGameObjects() {
        return gameObjects;
    }

    public Explorer getExplorer() {
        return explorer;
    }

    public int getWidth() {
        return gameObjects.length;
    }

    public int getHeight() {
        return gameObjects[0].length;
    }

    public Color applyFilter(Color orig, int dist) {
        return new Color((int) (orig.getRed() * (1 - (double) dist / explorer.getVision())),
                (int) (orig.getGreen() * (1 - (double) dist / explorer.getVision())),
                (int) (orig.getBlue() * (1 - (double) dist / explorer.getVision())));
    }

    public GameObject get(int x, int y) {
        if (!isValid(x, y))
            return tempWall;
        return gameObjects[x][y];
    }

    public boolean isValid(int x, int y) {
        return !(x >= getWidth() || x < 0 || y >= getHeight() || y < 0);
    }

    public boolean isValid(Location loc) {
        return isValid(loc.getX(), loc.getY());
    }

    public GameObject get(Location l) {
        return get(l.getX(), l.getY());
    }

    public void set(int x, int y, GameObject g) {
        gameObjects[x][y] = g;
        System.out.println(x + ", " + y);
    }

    public void set(Location l, GameObject g) {
        set(l.getX(), l.getY(), g);
    }

    public static Maze fromFile(String fileName) {
        try {
            File file = new File(fileName);
            List<String> list = Files.readAllLines(file.toPath());

            int w = list.get(0).length();
            int h = list.size();

            GameObject[][] gameObjects = new GameObject[w][h];

            int explorerX = 0;
            int explorerY = 0;

            Location endPos = new Location(0, 0);

            for (int y = 0; y < h; y++)
                for (int x = 0; x < w; x++)
                    switch (list.get(y).charAt(x)) {
                        case 'X':
                            gameObjects[x][y] = new Wall(x, y);
                            break;
                        case '*':
                            gameObjects[x][y] = new Explorer(x, y);
                            explorerX = x;
                            explorerY = y;
                            break;
                        case 'E':
                            endPos = new Location(x, y);
                    }

            return new Maze(gameObjects, explorerX, explorerY, endPos);
        } catch (IOException e) {
            System.err.println("File error");
        }
        return null;
    }

    public void draw(Graphics g) {
        for (int x = 0; x < getWidth(); x++)
            for (int y = 0; y < getHeight(); y++)
                if (get(x, y) != null)
                    get(x, y).draw(g);

        int x = endPos.getX();
        int y = endPos.getY();
        g.setColor(Color.GREEN);
        g.fillRect(x * Wall.width, y * Wall.height, Wall.width, Wall.height);
        g.setColor(Color.GREEN);
        g.drawRect(x * Wall.width, y * Wall.height, Wall.width, Wall.height);

        Graphics2D g2 = (Graphics2D) g;

        Area outer = new Area(new Rectangle(0, 0, Application.screenWidth, Application.screenHeight));

        if (explorer.getVision() == 5) {
            Shape inner = new Ellipse2D.Float(explorer.getLocation().getX() * Wall.width - Wall.width * 5,
                    explorer.getLocation().getY() * Wall.height - Wall.height * 6, Wall.width * 10, Wall.height * 12);
            outer.subtract(new Area(inner));
            g2.setColor(new Color(0, 0, 0, 245));
            g2.fill(outer);
        } else if (explorer.getVision() == 7) {
            Shape inner = new Ellipse2D.Float(explorer.getLocation().getX() * Wall.width - Wall.width * 10,
                    explorer.getLocation().getY() * Wall.height - Wall.height * 12, Wall.width * 20, Wall.height * 24);
            outer.subtract(new Area(inner));
            g2.setColor(new Color(0, 0, 0, 245));
            g2.fill(outer);
        }

        g.setColor(Color.LIGHT_GRAY);
        g.fillOval((int) (0.955 * Application.screenWidth), (int) (0.02 * Application.screenHeight),
                (int) (0.03 * Application.screenWidth), (int) (0.03 * Application.screenWidth));
        g.setColor(Color.DARK_GRAY);

        float thickness = 5;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(thickness));
        g2.drawOval((int) (0.955 * Application.screenWidth), (int) (0.02 * Application.screenHeight),
                (int) (0.03 * Application.screenWidth), (int) (0.03 * Application.screenWidth));
        g2.setStroke(oldStroke);
        g.setColor(Color.RED);
        switch (explorer.getDirection()) {
            case Explorer.UP:
                g.fillOval((int) ((0.968) * Application.screenWidth), (int) (0.023 * Application.screenHeight),
                        (int) (0.006 * Application.screenWidth), (int) (0.016 * Application.screenWidth));
                break;
            case Explorer.RIGHT:
                g.fillOval((int) ((0.965) * Application.screenWidth),
                        (int) (0.023 * Application.screenHeight + 0.01 * Application.screenWidth),
                        (int) (0.018 * Application.screenWidth), (int) (0.006 * Application.screenWidth));
                break;
            case Explorer.DOWN:
                g.fillOval((int) ((0.968) * Application.screenWidth), (int) (0.033 * Application.screenHeight),
                        (int) (0.006 * Application.screenWidth), (int) (0.016 * Application.screenWidth));
                break;
            case Explorer.LEFT:
                g.fillOval((int) ((0.958) * Application.screenWidth),
                        (int) (0.023 * Application.screenHeight + 0.01 * Application.screenWidth),
                        (int) (0.018 * Application.screenWidth), (int) (0.006 * Application.screenWidth));
                break;
        }
    }

    public void draw3d(Graphics g) {

        g.setColor(lineColor);
        g.fillRect(wMargin3d, hMargin3d, screenWidth3d, screenHeight3d);

        int x = wMargin3d;
        int y = hMargin3d;
        int y2 = hMargin3d + screenHeight3d;
        int w = screenWidth3d / 4;
        int h = screenHeight3d;

        double rw = 2 / 3.0;
        double rh = 2 / 3.0;

        Location location = explorer.getLocation();

        for (int i = 0; i < explorer.getVision(); i++) {
            Location left = Explorer.nextLocation(Explorer.turnLeft(explorer.getDirection()), location);
            Location right = Explorer.nextLocation(Explorer.turnRight(explorer.getDirection()), location);

            int nw = (int) (rw * w);
            int nh = (int) (rh * h);
            int nx = x + nw;
            int ny = y + (int) ((1 - rh) / 2.0 * nh);
            int ny2 = y + (int) (((1 - rh) / 2.0 + 1) * nh);

            location = Explorer.nextLocation(explorer.getDirection(), location);

            int[] xc = new int[] { x, nx, flipX(nx), flipX(x) };
            int[] yc = new int[] { y2, ny2, ny2, y2 };
            g.setColor(applyFilter(floorColor, i));
            g.fillPolygon(xc, yc, 4);
            g.setColor(lineColor);
            g.drawPolygon(xc, yc, 4);

            int[] xf = new int[] { x, nx, flipX(nx), flipX(x) };
            int[] yf = new int[] { y, ny, ny, y };
            g.setColor(applyFilter(ceilingColor, i));
            g.fillPolygon(xf, yf, 4);
            g.setColor(lineColor);
            g.drawPolygon(xf, yf, 4);

            if (get(left) instanceof PaintedWall) {
                int[] xs = new int[] { x, nx, nx, x };
                int[] ys = new int[] { y, ny, ny2, y2 };
                g.setColor(applyFilter(paintedColor, i));
                g.fillPolygon(xs, ys, 4);
                g.setColor(lineColor);
                g.drawPolygon(xs, ys, 4);
            } else if (get(left) instanceof Wall) {
                int[] xs = new int[] { x, nx, nx, x };
                int[] ys = new int[] { y, ny, ny2, y2 };
                g.setColor(applyFilter(wallColor, i));
                g.fillPolygon(xs, ys, 4);
                g.setColor(lineColor);
                g.drawPolygon(xs, ys, 4);
            } else {

                int[] xwfc = new int[] { x, x, nx };
                int[] ywc = new int[] { y, ny, ny };
                int[] ywf = new int[] { y2, ny2, ny2 };

                g.setColor(lineColor);
                g.drawPolygon(xwfc, ywc, 3);
                g.setColor(applyFilter(ceilingColor, i));
                g.fillPolygon(xwfc, ywc, 3);

                g.setColor(lineColor);
                g.drawPolygon(xwfc, ywf, 3);
                g.setColor(applyFilter(floorColor, i));
                g.fillPolygon(xwfc, ywf, 3);

                if (get(Explorer.nextLocation(explorer.getDirection(), left)) instanceof PaintedWall) {
                    int[] xs = new int[] { x, nx, nx, x };
                    int[] ys = new int[] { ny, ny, ny2, ny2 };
                    g.setColor(applyFilter(paintedColor, i));
                    g.fillPolygon(xs, ys, 4);
                    g.setColor(lineColor);
                    g.drawPolygon(xs, ys, 4);
                } else if (get(Explorer.nextLocation(explorer.getDirection(), left)) instanceof Wall) {
                    int[] xs = new int[] { x, nx, nx, x };
                    int[] ys = new int[] { ny, ny, ny2, ny2 };
                    g.setColor(applyFilter(wallColor, i));
                    g.fillPolygon(xs, ys, 4);
                    g.setColor(lineColor);
                    g.drawPolygon(xs, ys, 4);
                } else {
                    int hh = (h - nh) / 2;
                    int hw = (w - nw) / 2;
                    int hy = ny;
                    int hy2 = ny2;
                    Location l = Explorer.nextLocation(explorer.getDirection(), left);
                    for (int j = i + 1; j < explorer.getVision(); j++) {
                        hh = (int) (hh * rh);
                        if (get(l) instanceof PaintedWall) {
                            int[] rectX = new int[] { x, nx, nx, x };
                            int[] rectY = new int[] { hy, hy, hy2, hy2 };
                            g.setColor(lineColor);
                            g.drawPolygon(rectX, rectY, 4);
                            g.setColor(applyFilter(paintedColor, j));
                            g.fillPolygon(rectX, rectY, 4);
                            break;
                        } else if (get(l) instanceof Wall) {
                            int[] rectX = new int[] { x, nx, nx, x };
                            int[] rectY = new int[] { hy, hy, hy2, hy2 };
                            g.setColor(lineColor);
                            g.drawPolygon(rectX, rectY, 4);
                            g.setColor(applyFilter(wallColor, j));
                            g.fillPolygon(rectX, rectY, 4);
                            break;
                        } else if (l.equals(endPos)) {
                            int[] rectX = new int[] { x, nx, nx, x };
                            int[] rectY = new int[] { hy, hy, hy2, hy2 };
                            g.setColor(lineColor);
                            g.drawPolygon(rectX, rectY, 4);
                            g.setColor(Color.GREEN);
                            g.fillPolygon(rectX, rectY, 4);
                            break;
                        } else
                            l = Explorer.nextLocation(explorer.getDirection(), l);

                        int[] xs = new int[] { x, nx, nx, x };
                        int[] ys = new int[] { hy2, hy2, hy2 - hh, hy2 - hh };
                        int[] ys2 = new int[] { hy, hy, hy + hh, hy + hh };
                        hy2 -= hh;
                        hy += hh;
                        g.setColor(lineColor);
                        g.drawPolygon(xs, ys, 4);
                        g.drawPolygon(xs, ys2, 4);
                        g.setColor(applyFilter(floorColor, j));
                        g.fillPolygon(xs, ys, 4);
                        g.setColor(applyFilter(ceilingColor, j));
                        g.fillPolygon(xs, ys2, 4);

                    }
                }
            }

            if (get(right) instanceof PaintedWall) {
                int[] xs = flipX(new int[] { x, nx, nx, x });
                int[] ys = new int[] { y, ny, ny2, y2 };
                g.setColor(applyFilter(paintedColor, i));
                g.fillPolygon(xs, ys, 4);
                g.setColor(lineColor);
                g.drawPolygon(xs, ys, 4);
            } else if (get(right) instanceof Wall) {
                int[] xs = flipX(new int[] { x, nx, nx, x });
                int[] ys = new int[] { y, ny, ny2, y2 };
                g.setColor(applyFilter(wallColor, i));
                g.fillPolygon(xs, ys, 4);
                g.setColor(lineColor);
                g.drawPolygon(xs, ys, 4);
            } else {

                int[] xwfc = new int[] { flipX(x), flipX(x), flipX(nx) };
                int[] ywc = new int[] { y, ny, ny };
                int[] ywf = new int[] { y2, ny2, ny2 };

                g.setColor(lineColor);
                g.drawPolygon(xwfc, ywc, 3);
                g.setColor(applyFilter(ceilingColor, i));
                g.fillPolygon(xwfc, ywc, 3);

                g.setColor(lineColor);
                g.drawPolygon(xwfc, ywf, 3);
                g.setColor(applyFilter(floorColor, i));
                g.fillPolygon(xwfc, ywf, 3);

                if (get(Explorer.nextLocation(explorer.getDirection(), right)) instanceof PaintedWall) {
                    int[] xs = flipX(new int[] { x, nx, nx, x });
                    int[] ys = new int[] { ny, ny, ny2, ny2 };
                    g.setColor(applyFilter(paintedColor, i));
                    g.fillPolygon(xs, ys, 4);
                    g.setColor(lineColor);
                    g.drawPolygon(xs, ys, 4);
                } else if (get(Explorer.nextLocation(explorer.getDirection(), right)) instanceof Wall) {
                    int[] xs = flipX(new int[] { x, nx, nx, x });
                    int[] ys = new int[] { ny, ny, ny2, ny2 };
                    g.setColor(applyFilter(wallColor, i));
                    g.fillPolygon(xs, ys, 4);
                    g.setColor(lineColor);
                    g.drawPolygon(xs, ys, 4);
                } else {
                    int hh = (h - nh) / 2;
                    int hw = (w - nw) / 2;
                    int hy = ny;
                    int hy2 = ny2;
                    Location l = Explorer.nextLocation(explorer.getDirection(), right);
                    for (int j = i + 1; j < explorer.getVision() + 1; j++) {
                        hh = (int) (hh * rh);
                        if (get(l) instanceof PaintedWall) {
                            int[] rectX = flipX(new int[] { x, nx, nx, x });
                            int[] rectY = new int[] { hy, hy, hy2, hy2 };
                            g.setColor(lineColor);
                            g.drawPolygon(rectX, rectY, 4);
                            g.setColor(applyFilter(paintedColor, j));
                            g.fillPolygon(rectX, rectY, 4);
                            break;
                        } else if (get(l) instanceof Wall) {
                            int[] rectX = flipX(new int[] { x, nx, nx, x });
                            int[] rectY = new int[] { hy, hy, hy2, hy2 };
                            g.setColor(lineColor);
                            g.drawPolygon(rectX, rectY, 4);
                            g.setColor(applyFilter(wallColor, j));
                            g.fillPolygon(rectX, rectY, 4);
                            break;
                        } else if (l.equals(endPos)) {
                            int[] rectX = flipX(new int[] { x, nx, nx, x });
                            int[] rectY = new int[] { hy, hy, hy2, hy2 };
                            g.setColor(lineColor);
                            g.drawPolygon(rectX, rectY, 4);
                            g.setColor(Color.GREEN);
                            g.fillPolygon(rectX, rectY, 4);
                            break;
                        } else
                            l = Explorer.nextLocation(explorer.getDirection(), l);

                        System.out.println(hh);
                        int[] xs = flipX(new int[] { x, nx, nx, x });
                        int[] ys = new int[] { hy2, hy2, hy2 - hh, hy2 - hh };
                        int[] ys2 = new int[] { hy, hy, hy + hh, hy + hh };
                        hy2 -= hh;
                        hy += hh;
                        g.setColor(lineColor);
                        g.drawPolygon(xs, ys, 4);
                        g.drawPolygon(xs, ys2, 4);
                        g.setColor(applyFilter(floorColor, j));
                        g.fillPolygon(xs, ys, 4);
                        g.setColor(applyFilter(ceilingColor, j));
                        g.fillPolygon(xs, ys2, 4);

                    }
                }
            }

            if (get(location) instanceof PaintedWall) {
                int[] xs = new int[] { nx, nx, flipX(nx), flipX(nx) };
                int[] ys = new int[] { ny, ny2, ny2, ny };
                g.setColor(applyFilter(paintedColor, i));
                g.fillPolygon(xs, ys, 4);
                g.setColor(lineColor);
                g.drawPolygon(xs, ys, 4);
                break;
            } else if (get(location) instanceof Wall) {
                int[] xs = new int[] { nx, nx, flipX(nx), flipX(nx) };
                int[] ys = new int[] { ny, ny2, ny2, ny };
                g.setColor(applyFilter(wallColor, i));
                g.fillPolygon(xs, ys, 4);
                g.setColor(lineColor);
                g.drawPolygon(xs, ys, 4);
                break;
            } else if (location.equals(endPos)) {
                int[] xs = new int[] { nx, nx, flipX(nx), flipX(nx) };
                int[] ys = new int[] { ny, ny2, ny2, ny };
                g.setColor(Color.GREEN);
                g.fillPolygon(xs, ys, 4);
                g.setColor(lineColor);
                g.drawPolygon(xs, ys, 4);
                break;
            }

            if (i == explorer.getVision() - 1) {
                int[] xs = new int[] { nx, nx, flipX(nx), flipX(nx) };
                int[] ys = new int[] { ny, ny2, ny2, ny };
                g.setColor(lineColor);
                g.fillPolygon(xs, ys, 4);
            }

            x = nx;
            y = ny;
            y2 = ny2;
            w = nw;
            h = nh;

        }

    }

    public int flipX(int x) {
        return Application.screenWidth - x;
    }

    public int[] flipX(int[] x) {
        int[] x2 = new int[x.length];
        for (int i = 0; i < x.length; i++)
            x2[i] = flipX(x[i]);
        return x2;
    }

    public boolean isDone() {
        return getExplorer().getLocation().equals(endPos);
    }

}