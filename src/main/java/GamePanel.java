import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 50;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        this.random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.startGame();
    }

    public void startGame() {
        this.newApple();
        this.running = true;
        this.timer = new Timer(DELAY, this);
        this.timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.draw(g);
    }

    public void draw(Graphics g) {

        //grid
        for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
            g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH, i*UNIT_SIZE);
        }

        g.setColor(Color.red);
        g.fillOval(this.appleX, this.appleY, UNIT_SIZE, UNIT_SIZE);

        for(int i = 0; i < this.bodyParts; i++) {
            if(i == 0) {
                g.setColor(Color.green);
                g.fillRect(this.x[i], this.y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(45,180,0));
                g.fillRect(this.x[i], this.y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }

    }

    public void newApple() {
        this.appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        this.appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move() {
        for(int i = this.bodyParts; i > 0; i--) {
            this.x[i] = this.x[i-1];
            this.y[i] = this.y[i-1];
        }

        switch (this.direction) {
            case 'U':
                this.y[0] = this.y[0] - UNIT_SIZE;
                break;
            case 'D':
                this.y[0] = this.y[0] + UNIT_SIZE;
                break;
            case 'L':
                this.x[0] = this.x[0] - UNIT_SIZE;
                break;
            case 'R':
                this.x[0] = this.x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {

    }

    public void checkCollisions() {
        //checks if head collides with body
        for(int i = this.bodyParts; i > 0; i--) {
            if((this.x[0] == this.x[i]) && (this.y[0] == this.y[i])) {
                this.running = false;
            }
        }

        //check if head collides with left border
        if(this.x[0] < 0) {
            this.running = false;
        }
        //checks if head collides with right border
        if(this.x[0] > SCREEN_WIDTH) {
            this.running = false;
        }
        //check if head collides with top border
        if(this.y[0] < 0) {
            this.running = false;
        }
        //check if head touches bottom border
        if(this.y[0] > SCREEN_HEIGHT) {
            this.running = false;
        }

        if(!this.running) {
            this.timer.stop();
        }
    }

    public void gameOver(Graphics g) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            this.move();
            this.checkApple();
            this.checkCollisions();
        }
        this.repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
