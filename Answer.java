import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Answer extends GameObject implements Drawable {

    public String text;
    public boolean isCorrect;

    //  FITUR TAMBAHAN
    private static final int TILE = 20;
    private static final int OFFSET_Y = 120;

    public Answer(String text, int x, int y, boolean isCorrect) {
        super(x, y);
        this.text = text;
        this.isCorrect = isCorrect;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 14));

        // 🔥 lebih rapi (tidak hardcode)
        g.drawString(text, x * TILE, (y * TILE) + OFFSET_Y);
    }
}