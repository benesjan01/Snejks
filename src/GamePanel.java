import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int [GAME_UNITS];
    final int y[] = new int [GAME_UNITS];
    int bodyParts = 6;
    int appleEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    private boolean playAgainButtonAdded = false;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent (Graphics g){
        super.paintComponent(g);         // preddefinovani metody
        draw(g);
    }
    public void draw(Graphics g){
        if(running) {
           /* for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_HEIGHT, i * UNIT_SIZE);
            }*/
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.yellow);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + appleEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + appleEaten)) / 2, g.getFont().getSize());
        } else {
            this.add(new JButton("try again"), BorderLayout.NORTH);
            gameOVer(g);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)( SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)( SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i = bodyParts; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
        if((x[0] == appleX) && (y[0] ==appleY)){
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
        //kontroluje jestli se had nesrazil s vlastnim telem
        for(int i = bodyParts; i>0; i--){
            if(x[0]== x[i] && (y[0]== y[i])){
                running=false;
            }
        }
        //kontoluje jestli se hlava dotkla leveho okraje
        if(x[0] < 0){
            running=false;
        }
        //kontoluje jestli se hlava dotkla praveho okraje
        if(x[0] > SCREEN_WIDTH){
            running=false;
        }
        //kontoluje jestli se hlava dotkla dolniho ukraje
        if(y[0] > SCREEN_HEIGHT){
            running=false;
        }
        //kontoluje jestli se hlava dotkla horniho okraje
        if(y[0] < 0){
            running=false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOVer(Graphics g){
        //score
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Your score is: " + appleEaten, (SCREEN_WIDTH - metrics1.stringWidth("Your score is: " + appleEaten)) / 2, g.getFont().getSize());

        //gameover text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER")) / 2,  SCREEN_HEIGHT/2);


        System.out.println("Game over");

     /*   JButton bt = new JButton();
        bt.setFont(new Font("Futura", Font.BOLD, 75));
        bt.setText("Play again");

        this.add(bt, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();*/
        if(this.playAgainButtonAdded){
             return;
        }
        if (!this.playAgainButtonAdded) {
            this.playAgainButtonAdded = true;
            this.add(new JButton("try again"), BorderLayout.NORTH);
            this.revalidate();
        }
    }
    private void zobraz(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JButton("try again"), BorderLayout.NORTH);
        add(panel);
    }

    private JPanel playAgain() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton bt = new JButton("try again");
     //   bt.setFont(new Font("Futura", Font.BOLD, 75));
        panel.add(bt);


    return panel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                if(direction!='R'){
                    direction='L';
                }
                break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction!='D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='U'){
                        direction='D';
                    }
                    break;
            }
        }
    }
}

