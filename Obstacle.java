import java.awt.Color;
import java.awt.Graphics;

public class Obstacle extends GameObject implements Drawable {

    public Obstacle(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x * 20, (y * 20) + 120, 20, 20);
    }
}