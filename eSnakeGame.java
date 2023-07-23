import java.awt.event.KeyEvent; 
import java.awt.Graphics; 
import java.awt.Graphics2D; 
import javax.swing.JFrame;  
import javax.swing.JOptionPane;  
import javax.swing.JPanel; 
import java.awt.event.KeyListener;
import java.util.ArrayList;  
import java.util.List;  
import java.awt.Color; 
import java.awt.Font;  

//start
public class SnakeGame extends JPanel implements KeyListener {

    
    private int x, y, dx, dy;  
    private Food food; 
    private List<Food> foods ;  
    int score = 0; 
    private List<SnakeSegment> snake = new ArrayList<>(); 
    
    
    public SnakeGame() {
    	setFocusable(true); 
        addKeyListener(this); 
        x = 10; 
        y = 10; 
        dx = 10;
        dy = 0; 
        foods = new ArrayList<>(); 
        createFood(); 
    }

    //key reading
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode(); 
        if (code == KeyEvent.VK_UP) {  
            dy = -10;
            dx = 0;
        }
        if (code == KeyEvent.VK_DOWN) {  
            dy = 10;
            dx = 0;
        }
        if (code == KeyEvent.VK_LEFT) {  
            dx = -10;
            dy = 0;
        }
        if (code == KeyEvent.VK_RIGHT) {  
            dx = 10;
            dy = 0;
        }
    }

    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}
    

    //position update
    public void move() {
        x += dx;  
        y += dy;  
        if (x < 0 || x > getWidth() || y > getHeight() || y < 0) { 
        	Object[] options = {"Play Again", "Exit"};  
        	int result = JOptionPane.showOptionDialog(this, "Game Over!", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        	if (result == JOptionPane.YES_OPTION) {   
        	    //restart code
        		resetGame();  
        		resetScore();  
        	} else if (result == JOptionPane.NO_OPTION) {  
        	    System.exit(0);  
        	}

        }
        
     //collision check
        for (int i = 1; i < snake.size(); i++) {  
            SnakeSegment segment = snake.get(i);  
            if (x == segment.x && y == segment.y) {  
                Object[] options = {"Play Again", "Exit"}; 
                int result = JOptionPane.showOptionDialog(this, "Game Over!", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                if (result == JOptionPane.YES_OPTION) {  
                	//restart code
                    resetGame(); 
                    resetScore();  
                } else if (result == JOptionPane.NO_OPTION) { 
                    System.exit(0); 
                }
            }
        }
        
        
        
        for (Food food : new ArrayList<>(foods)) {
        	if (x == food.x && y == food.y) {
        	    foods.remove(food);  
        	    score++;  
        	    if (snake.isEmpty()) {
        	        snake.add(new SnakeSegment(x, y));  
        	        createFood();  
        	        SnakeSegment tail = snake.get(snake.size() - 1);
        	        snake.add(new SnakeSegment(tail.x, tail.y)); 
        	    } else {
        	    	
        	    	snake.add(new SnakeSegment(x, y));  
        	    	createFood(); 
        	    }
        	}
        }
        
        if (snake.size() > 0) {  
            SnakeSegment head = snake.get(0);  
            int prevX = head.x;
            int prevY = head.y;
            head.x = x;
            head.y = y;
            for (int i = 1; i < snake.size(); i++) {  
                SnakeSegment segment = snake.get(i); 
                int tempX = segment.x;  
                int tempY = segment.y;
                segment.x = prevX;
                segment.y = prevY;
                prevX = tempX;
                prevY = tempY;
            }
        }
        
        repaint(); 
    }



    
    @Override
    public void paintComponent(Graphics g) {
    	
    	Graphics2D g2 = (Graphics2D) g; 
    	
    	
    	setBackground(Color.BLACK);
    	
    	
    	g.setColor(Color.GREEN);  
    	super.paintComponent(g);  
        g.fillRoundRect(x, y, 10, 10,10,10); 
        for (SnakeSegment segment : snake) { 
            g.drawRoundRect(segment.x, segment.y, 10, 10,10,10);  
        }
        
        g2.setColor(Color.WHITE);   
        g2.setFont(new Font("Helvetica", Font.BOLD, 12)); 
        g2.drawString("Score: " + score, 235, 15);  
        
        
        for (Food food: foods) { 
        	g.setColor(Color.RED); 
        	g.fillRoundRect(food.x, food.y, 10, 10,10,10);  
            food.draw(g);  
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");  
        frame.setSize(305, 308); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setResizable(false);  

        SnakeGame game = new SnakeGame();  
        frame.add(game);  
        game.createFood();  
        frame.setLocationRelativeTo(null);  
        frame.setVisible(true);  
        

        // fps/game speed
        while (true) {
            game.move(); 
            try {
                Thread.sleep(120);  
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    
    public class Food {
        int x, y; 

        public Food(int x, int y) {  
            this.x = x;  
            this.y = y;  
        }

        public void draw(Graphics g) {
            g.fillRoundRect(x, y, 10, 10, 10, 10);  
        }
    }
    
    public void createFood() {
    	foods.clear(); 
        int x = (int)(Math.random() * (getWidth() / 10)) * 10;  
        int y = (int)(Math.random() * (getHeight() / 10)) * 10;  
        food = new Food(x, y);  
        foods.add(food); 
    }
    
    private void resetGame() {
        x = 0;  
        y = 0;  
        dx = 10; 
        dy = 0;  
        foods.clear();  
        createFood();  
        repaint();  
        snake = new ArrayList<>();
    }
    
    public void resetScore() { 
        score = 0; 
        repaint();  
    }

    public class SnakeSegment {
        int x, y;   

        public SnakeSegment(int x, int y) {   
            this.x = x;  
            this.y = y;  
        }
    }   
}