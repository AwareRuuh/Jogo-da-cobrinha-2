import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

  static final int LARGURA_TELA = 1000;
  static final int ALTURA_TELA = 720;
  static final int TAMANHO_UNIDADE = 50;
  static final int GAME_UNITS = (LARGURA_TELA * ALTURA_TELA) / TAMANHO_UNIDADE;
  static final int DELAY = 200;//175
  final int x[] = new int[GAME_UNITS];
  final int y[] = new int[GAME_UNITS];
  int partesCorpo = 6;
  int frutaMaca;
  int macaX;
  int macaY;
  char direction = 'R';
  boolean running = false;
  Timer timer;
  Random random;

  GamePanel() {
    random = new Random();
    this.setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
    this.setBackground(new Color(231,209,255));
    //fundo
    this.setFocusable(true);
    this.addKeyListener(new MyKeyAdapter());
    startGame();
  }

  public void startGame() {
    newApple();
    running = true;
    timer = new Timer(DELAY, this);
    timer.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {

    if (running) {
      for (int i = 0; i < ALTURA_TELA / TAMANHO_UNIDADE; i++) {
        g.drawLine(i * TAMANHO_UNIDADE, 0, i * TAMANHO_UNIDADE, ALTURA_TELA);
        g.drawLine(0, i * TAMANHO_UNIDADE, LARGURA_TELA, i * TAMANHO_UNIDADE);
      }
      g.setColor(new Color (244,67,54));
      g.fillOval(macaX, macaY, TAMANHO_UNIDADE, TAMANHO_UNIDADE);

      for (int i = 0; i < partesCorpo; i++) {
        if (i == 0) {
          g.setColor(new Color(112,179,193));
          g.fillRect(x[i], y[i], TAMANHO_UNIDADE, TAMANHO_UNIDADE);
        } else {
          g.setColor(new Color(84,177,197));
          // g.setColor(new
          // Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
          g.fillRect(x[i], y[i], TAMANHO_UNIDADE, TAMANHO_UNIDADE);
        }
      }
      g.setColor(new Color(80,145,203));
      g.setFont(new Font("Ink Free", Font.BOLD, 40));
      FontMetrics metrics = getFontMetrics(g.getFont());
      g.drawString("Pontuação: " + frutaMaca, (LARGURA_TELA - metrics.stringWidth("Pontuação: " + frutaMaca)) / 2,
          g.getFont().getSize());
    } else {
      gameOver(g);
    }

  }

  public void newApple() {
    macaX = random.nextInt((int) (LARGURA_TELA / TAMANHO_UNIDADE)) * TAMANHO_UNIDADE;
    macaY = random.nextInt((int) (ALTURA_TELA / TAMANHO_UNIDADE)) * TAMANHO_UNIDADE;
  }

  public void move() {
    for (int i = partesCorpo; i > 0; i--) {
      x[i] = x[i - 1];
      y[i] = y[i - 1];
    }

    switch (direction) {
      case 'U':
        y[0] = y[0] - TAMANHO_UNIDADE;
        break;
      case 'D':
        y[0] = y[0] + TAMANHO_UNIDADE;
        break;
      case 'L':
        x[0] = x[0] - TAMANHO_UNIDADE;
        break;
      case 'R':
        x[0] = x[0] + TAMANHO_UNIDADE;
        break;
    }

  }

  public void checkApple() {
    if ((x[0] == macaX) && (y[0] == macaY)) {
      partesCorpo++;
      frutaMaca++;
      newApple();
    }
  }

  public void checkCollisions() {
    // checks if head collides with body
   for (int i = partesCorpo; i > 0; i--) {
      if ((x[0] == x[i]) && (y[0] == y[i])) {
        running = false;
      }
    }
    // check if head touches left border
    if (x[0] < 0) {
      running = false;
    }
    // check if head touches right border
    if (x[0] > LARGURA_TELA) {
      running = false;
    }
    // check if head touches top border
    if (y[0] < 0) {
      running = false;
    }
    // check if head touches bottom border
    if (y[0] > ALTURA_TELA) {
      running = false;
    }

    if (!running) {
      timer.stop();
    }
  }

  public void gameOver(Graphics g) {
    // Score
    g.setColor(new Color(80,145,203));
    g.setFont(new Font("Ink Free", Font.BOLD, 40));
    FontMetrics metrics1 = getFontMetrics(g.getFont());
    g.drawString("Pontuação: " + frutaMaca, (LARGURA_TELA - metrics1.stringWidth("Pontuação: " + frutaMaca)) / 2,
        g.getFont().getSize());
    // Game Over text
    g.setColor(new Color(255,255,127));
    g.setFont(new Font("Ink Free", Font.BOLD, 75));
    FontMetrics metrics2 = getFontMetrics(g.getFont());
    g.drawString("Game Over", (LARGURA_TELA - metrics2.stringWidth("Game Over")) / 2, ALTURA_TELA / 2);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    if (running) {
      move();
      checkApple();
      checkCollisions();
    }
    repaint();
  }

  public class MyKeyAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
          if (direction != 'R') {
            direction = 'L';
          }
          break;
        case KeyEvent.VK_RIGHT:
          if (direction != 'L') {
            direction = 'R';
          }
          break;
        case KeyEvent.VK_UP:
          if (direction != 'D') {
            direction = 'U';
          }
          break;
        case KeyEvent.VK_DOWN:
          if (direction != 'U') {
            direction = 'D';
          }
          break;
      }
    }
  }
}