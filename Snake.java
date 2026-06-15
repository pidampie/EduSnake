import java.awt.Point;
import java.util.ArrayList;

public class Snake {

    private ArrayList<Point> body;
    private int direction;

    // 🔥 TAMBAHAN UNTUK GROW
    private boolean grow = false;

    public Snake() {
        body = new ArrayList<>();
        body.add(new Point(10, 10));
        body.add(new Point(9, 10));
        body.add(new Point(8, 10));
        direction = 1;
    }

    public void move() {
        Point head = body.get(0);
        Point newHead = new Point(head);

        if (direction == 0) newHead.y--;
        if (direction == 1) newHead.x++;
        if (direction == 2) newHead.y++;
        if (direction == 3) newHead.x--;

        body.add(0, newHead);

        // 🔥 INI KUNCI GROW
        if (!grow) {
            body.remove(body.size() - 1);
        } else {
            grow = false; // reset setelah grow
        }
    }

    // 🔥 METHOD GROW
    public void grow() {
        grow = true;
    }

    public ArrayList<Point> getBody() {
        return body;
    }

    public void setDirection(int dir) {
        this.direction = dir;
    }
}