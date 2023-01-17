
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Application extends JPanel {

    public static final String fileName = "mazes/one.txt";
    public static final int screenWidth = 800;
    public static final int screenHeight = 700;
    public int paintcans = 10;
    public int batterylife = 100;
    public int hammer = 3;

    private static final long serialVersionUID = 1L;

    private JFrame frame;

    private Maze maze;

    private boolean show3d = true;

    public Application() {
        frame = new JFrame();

        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth * 50 / 49, screenHeight * 50 / 49);
        setBoard();
        frame.setVisible(true);

        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent event) {
                // System.out.println("Pressed: " + event.getKeyCode());
                if (!maze.isDone()) {
                    switch (event.getKeyCode()) {
                        case 38:
                            maze.getExplorer().move();
                            if (maze.get(maze.getExplorer().nextLocation(maze.getExplorer().getDirection())) == null
                                    && maze.getExplorer().getVision() == 7)
                                batterylife--;
                            break;
                        case 40:
                            maze.getExplorer().moveBack();
                            if (maze.get(maze.getExplorer()
                                    .nextLocation(maze.getExplorer().flip(maze.getExplorer().getDirection()))) == null
                                    && maze.getExplorer().getVision() == 7)
                                batterylife--;
                            break;
                        case 72:
                            // NOTE: If a breaking a wall is going to create a 2-wide path, the game will
                            // not allow you to break it.
                            if (hammer > 0
                                    && maze.get(
                                            maze.getExplorer().nextLocation(maze.getExplorer().getDirection())) != null
                                    && maze.get(maze.getExplorer().nextLocation(
                                            maze.getExplorer().turnRight(maze.getExplorer().getDirection()),
                                            maze.getExplorer().nextLocation(maze.getExplorer().getDirection()))) != null
                                    && maze.get(maze.getExplorer().nextLocation(
                                            maze.getExplorer().turnLeft(maze.getExplorer().getDirection()),
                                            maze.getExplorer()
                                                    .nextLocation(maze.getExplorer().getDirection()))) != null) {
                                maze.set(maze.getExplorer().nextLocation(maze.getExplorer().getDirection()), null);
                                hammer--;
                            }
                            break;
                        case 83:
                            if (paintcans > 0
                                    && maze.get(
                                            maze.getExplorer().nextLocation(maze.getExplorer().getDirection())) != null
                                    && !(maze.get(maze.getExplorer()
                                            .nextLocation(maze.getExplorer().getDirection())) instanceof PaintedWall)) {
                                maze.set(maze.getExplorer().nextLocation(maze.getExplorer().getDirection()),
                                        new PaintedWall(
                                                maze.getExplorer().nextLocation(maze.getExplorer().getDirection())));
                                paintcans--;
                            }
                            break;
                    }
                    if (batterylife <= 0) {
                        maze.getExplorer().setVision(5);
                        batterylife = 0;
                    }
                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent event) {
                System.out.println("Released: " + event.getKeyCode());
                if (!maze.isDone()) {
                    switch (event.getKeyCode()) {
                        case 37:
                            maze.getExplorer().turnLeft();
                            break;
                        case 39:
                            maze.getExplorer().turnRight();
                            break;
                        case 77:
                            show3d = !show3d;
                            break;
                        case 70:
                            if (batterylife > 0 && maze.getExplorer().getVision() == 5) {
                                maze.getExplorer().setVision(7);
                            } else {
                                maze.getExplorer().setVision(5);
                            }
                            break;
                    }
                    repaint();
                }
            }

            public void keyTyped(KeyEvent event) {

            }

        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, screenWidth, screenHeight);
        try {
            if (maze.isDone()) {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, 900, 690);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Courier New", Font.PLAIN, 40));
                g.drawString("Congratulations!", 220, 150);
                g.drawString("You win!", 310, 300);
                g.drawString("Moves: " + maze.getExplorer().getSteps(), 290, 450);
            } else {
                maze.draw(g);
                g.setColor(Color.WHITE);
                g.drawString("[F] Flashlight Battery: " + batterylife + "%", 595, 620);
                g.drawString("[H] Hammer Uses: " + hammer, 635, 650);
                if (show3d) {
                    maze.draw3d(g);
                    g.setColor(Color.WHITE);
                    g.drawString("[S] Sprays Left: " + paintcans, 645, 590);
                    g.drawString("[F] Flashlight Battery: " + batterylife + "%", 595, 620);
                    g.drawString("[H] Hammer Uses: " + hammer, 635, 650);
                }
                g.drawString("Press [M] for map", 350, screenHeight - 50);
            }
        } catch (Exception e) {
        }

    }

    public void setBoard() {
        maze = Maze.fromFile(fileName);
    }

    public static void main(String[] args) {
        Application application = new Application();
    }
}