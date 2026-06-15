import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    // KONSTANTA UKURAN (Sesuai script asli)
    private final int TILE = 20;
    private final int GRID = 25;
    private final int HEADER = 120;
    private final int WIDTH = TILE * GRID;
    private final int HEIGHT = TILE * GRID;

    // ASSET GAMBAR
    private Image latarImg, rumputImg;

    // DELEGASI LOGIKA & DATA (Penerapan SRP & OCP)
    private GameRules rules;
    private Snake snake;
    private Timer timer;
    private List<Question> questions;
    private Question currentQuestion;
    private int currentIndex = 0;
    private List<Answer> answers = new ArrayList<>();

    // SCREEN FLAGS (Sesuai alur asli)
    private boolean showStoryScreen = true;
    private boolean showQuestionScreen = true;
    private boolean startScreen = true;
    private boolean finalWin = false;
    private long questionStartTime = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT + HEADER));
        setBackground(Color.BLACK);
        
        // Load Assets
        latarImg = new ImageIcon(getClass().getResource("/assets/latar.jpg")).getImage();
        rumputImg = new ImageIcon(getClass().getResource("/assets/rumput.png")).getImage();

        // Inisialisasi Modular
        rules = new GameRules();
        snake = new Snake();
        
        // Ambil soal dari Repository (OCP)
        questions = QuestionRepository.getAllQuestions(); 
        loadCurrentQuestion();
        
        SoundPlayer.play("sound/Opening.wav");
        questionStartTime = System.currentTimeMillis();

        timer = new Timer(200, this);
        timer.start();

        setFocusable(true);
        addKeyListener(new GameKeyAdapter());
    }

    private void loadCurrentQuestion() {
        currentQuestion = questions.get(currentIndex);
        answers.clear();
        Random rand = new Random();
        for (String ans : currentQuestion.getAllAnswers()) {
            boolean posisiAman = false;
            int x = 0, y = 0;
            while (!posisiAman) {
                x = rand.nextInt(GRID - 6) + 2;
                y = rand.nextInt(GRID - 10) + 6;
                posisiAman = true;
                for (Answer a : answers) {
                    if (Math.abs(a.x - x) < 4 && Math.abs(a.y - y) < 2) {
                        posisiAman = false; break;
                    }
                }
            }
            answers.add(new Answer(ans, x, y, currentQuestion.isCorrect(ans)));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!rules.gameOver && !rules.showNext && !startScreen && !finalWin) {
            if (showQuestionScreen) {
                if (System.currentTimeMillis() - questionStartTime >= 8000) {
                    showQuestionScreen = false;
                }
            } else if (!showStoryScreen) {
                snake.move();
                checkCollision();
            }
        }
        repaint();
    }

    private void checkCollision() {
        Point head = snake.getBody().get(0);
        
        // Nabrak Tembok
        if (head.x < 0 || head.y < 0 || head.x >= GRID || head.y >= GRID) {
            rules.handleWrongAnswer();
            snake = new Snake();
        }

        // Nabrak Jawaban (Logika Presisi Asli)
        Iterator<Answer> it = answers.iterator();
        while (it.hasNext()) {
            Answer a = it.next();
            int hX = head.x * TILE;
            int hY = (head.y * TILE) + HEADER;
            int tX = a.x * TILE;
            int tY = (a.y * TILE) + HEADER;
            int tWidth = a.text.length() * 8;

            if (hX < tX + tWidth && hX + TILE > tX && hY < tY && hY + TILE > tY - 15) {
                if (a.isCorrect) {
                    rules.handleCorrectAnswer(snake);
                    it.remove();
                } else {
                    rules.handleWrongAnswer();
                }
            }
        }
        rules.checkGameStatus(answers);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. STORY SCREEN (Sesuai Tampilan Asli)
        if (showStoryScreen) {
            drawStory(g);
            return;
        }

        // 2. START SCREEN (Sesuai Tampilan Asli)
        if (startScreen) {
            drawStart(g);
            return;
        }

        // 3. QUESTION SCREEN (Sesuai Tampilan Asli)
        if (showQuestionScreen) {
            drawQuestion(g);
            return;
        }

        // --- GAMEPLAY RENDERING ---
        
        // Header Area
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, getWidth(), HEADER);

        // Arena Background
        if (latarImg != null) g.drawImage(latarImg, 0, HEADER, WIDTH, HEIGHT, null);
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, HEADER, WIDTH, HEIGHT);

        // KOTAK ARENA (Garis Putih Sesuai Script Asli)
        g.setColor(new Color(255, 255, 255, 80));
        g.drawRoundRect(5, HEADER + 5, WIDTH - 10, HEIGHT - 10, 20, 20);

        // Header Teks (Data dari Rules)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g.drawString(currentQuestion.questionText, 20, 65);
        g.setColor(Color.ORANGE);
        g.drawString("SKOR: " + rules.score, 400, 35);
        g.setColor(Color.RED);
        g.drawString("NYAWA: " + rules.lives, 400, 70);

        // Asset Rumput
        if (rumputImg != null) {
            int s = 50; int yR = HEADER + HEIGHT - s;
            g.drawImage(rumputImg, 10, yR, s, s, null);
            g.drawImage(rumputImg, WIDTH - s - 10, yR, s, s, null);
        }

        // Snake
        for (int i = 0; i < snake.getBody().size(); i++) {
            Point p = snake.getBody().get(i);
            g.setColor(i == 0 ? Color.GREEN : new Color(0, 180, 0));
            g.fillOval(p.x * TILE, (p.y * TILE) + HEADER, TILE, TILE);
        }

        // Answers
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
        for (Answer a : answers) a.draw(g);

        // Overlays (Game Over / Next)
        if (rules.gameOver) {
            g.setColor(Color.RED); g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
            g.drawString("GAME OVER", 230, 250);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            g.drawString("PRESS ENTER TO RESTART", 170, 300);
        }
        if (rules.showNext) {
            g.setColor(Color.GREEN); g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            g.drawString("CONGRATULATIONS!", 190, 250);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
            g.drawString("PRESS ENTER FOR NEXT LEVEL", 140, 300);
        }
    }

    // --- HELPER RENDERING (Agar Kode Lebih Modular / SRP) ---

    private void drawStory(Graphics g) {
        // Background Hitam
        g.setColor(new Color(10, 10, 10));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Judul - Sekarang di Y: 80
        g.setColor(Color.GREEN);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        g.drawString("MISI SI ULAR KUTU BUKU", 100, 80);

        // Narasi Cerita - Dinaikkan lagi posisinya
        g.setColor(Color.WHITE);
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        g.drawString("Di perpustakaan tua, hidup seekor ular kecil", 100, 150);
        g.drawString("yang bermimpi ingin menjadi naga pengetahuan.", 100, 180);

        g.drawString("Bantulah dia melahap jawaban-jawaban benar", 100, 250);
        g.drawString("untuk tumbuh besar dan membuka rahasia ilmu.", 100, 280);

        g.drawString("Tapi waspadalah! Jawaban yang salah adalah", 100, 350);
        g.drawString("racun yang bisa membuat energinya melemah...", 100, 380);

        // Pertanyaan Penutup - Y: 440
        g.setFont(new Font("Comic Sans MS", Font.ITALIC, 18));
        g.drawString("Apakah kamu siap membantu Sang Kutu Buku?", 100, 450);

        // Petunjuk Kuning - Y: 500
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        g.drawString("Tekan ENTER untuk memulai petualangan...", 100, 480);
    }

    private void drawStart(Graphics g) {
        g.setColor(Color.WHITE); g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        g.drawString("LEVEL " + (currentIndex + 1), 200, 250);
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g.drawString("PRESS ENTER TO START", 150, 300);
    }

    private void drawQuestion(Graphics g) {
        g.setColor(Color.BLACK); g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.PINK); g.setFont(new Font("Comic Sans MS", Font.BOLD, 28));
        g.drawString("LEVEL " + (currentIndex + 1), 200, 200);
        g.setColor(Color.WHITE); g.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        g.drawString(currentQuestion.questionText, 120, 300);
    }

    // --- KEY ADAPTER ---

    private class GameKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int k = e.getKeyCode();
            if (showStoryScreen && k == KeyEvent.VK_ENTER) showStoryScreen = false;
            else if (startScreen && k == KeyEvent.VK_ENTER) startScreen = false;
            else if (rules.gameOver && k == KeyEvent.VK_ENTER) restartGame();
            else if (rules.showNext && k == KeyEvent.VK_ENTER) nextQuestion();

            if (!startScreen && !showStoryScreen) {
                if (k == KeyEvent.VK_UP) snake.setDirection(0);
                if (k == KeyEvent.VK_RIGHT) snake.setDirection(1);
                if (k == KeyEvent.VK_DOWN) snake.setDirection(2);
                if (k == KeyEvent.VK_LEFT) snake.setDirection(3);
            }
        }
    }

    private void nextQuestion() {
        currentIndex++;
        if (currentIndex >= questions.size()) { currentIndex = 0; }
        rules.showNext = false;
        snake = new Snake();
        loadCurrentQuestion();
        showQuestionScreen = true;
        questionStartTime = System.currentTimeMillis();
    }

    private void restartGame() {
        rules.reset(5);
        currentIndex = 0;
        startScreen = true;
        snake = new Snake();
        loadCurrentQuestion();
        showQuestionScreen = true;
        questionStartTime = System.currentTimeMillis();
    }
}