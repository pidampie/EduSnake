import java.awt.Color;
import java.awt.Graphics;

public class Food extends GameObject implements Drawable {

    public Food(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillOval(x * 20, (y * 20) + 120, 15, 15);
    }
}