package main;

import gameOver.GameOverListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import board.*;

@SuppressWarnings("serial")
public class RunGame extends Component implements KeyListener {
	
	protected int ticks;
	protected boolean pause;
	private static RunGame Instance = null;
	
	protected static Font font = new Font("Arial", Font.BOLD, 50);
	protected Color bg = new Color(233, 233, 233);
	protected Color floor = new Color(100, 100, 100);
	protected Color life1 = new Color(100, 233, 233);
	
	protected Player p1;
	protected List<Board> boards;
	
	protected BufferedImage screen;
	protected Thread game;
	
	protected int layer;
	protected boolean spike,
					bounce,
					spark,
					twin;
	protected BoardFactory boardF,
						spikeF,
						bounceF,
						sparkF,
						twinF;
	
	protected GameOverListener listener;
	
	protected void init(){
		Global.SLEEPTIME = 50;
		Global.BOARDSPEED = 6;
		Global.MAXSPEEDX = 7;
		Global.MAXSPEEDY = 27;
		layer=0;
		pause = true;
		ticks = 0;
		p1 = new Player("player1",(Global.WIDTH)/2-2*Player.width,(Global.HEIGHT)/2-Player.height);
		boards = new ArrayList<Board>();
		boards.add(new Board(Global.WIDTH/2,Global.HEIGHT/2));
		
		boardF = BoardFactory.getInstance();
		if(spike)spikeF = SpikeBoardFactory.getInstance();
		else spikeF = boardF;
		if(bounce)bounceF = BounceBoardFactory.getInstance();
		else bounceF = boardF;
		if(spark)sparkF = SparkBoardFactory.getInstance();
		else sparkF = boardF;
		if(twin)twinF = TwinBoardFactory.getInstance();
		else twinF = boardF;
	}
	protected void start() {
		// TODO Auto-generated method stub
		pause=false;
		game = new Thread(new ThreadUpadte());
		game.start();
	}
	@SuppressWarnings("deprecation")
	protected void restart() {
		// TODO Auto-generated method stub
		game.stop();
		init();
		pause=false;
		game = new Thread(new ThreadUpadte());
		game.start();
	}
	@SuppressWarnings("deprecation")
	protected void stop(){
		game.stop();
		pause=true;
	}
	protected boolean over(){
		if(p1.dead)return true;
		else return false;
	}
	
	@Override
	public void paint(Graphics g){
		 Graphics2D g2d = (Graphics2D) screen.getGraphics();  
	     g2d.setColor(bg);  
	     //background
	     g2d.fillRect(0, 0, Global.WIDTH, Global.HEIGHT);      
	    	 
	     //life and layers
	     g2d.setFont(font);
	     g2d.setColor(floor);
	     g2d.drawString(String.valueOf(layer), Global.MARGIN, Global.HEIGHT-Global.MARGIN-2);
	     g2d.setColor(life1);
	     g2d.drawString(String.valueOf(p1.life), Global.MARGIN, 40+Global.MARGIN);
	     
	     //boards
	     Iterator<Board> iter = boards.iterator();
	     while(iter.hasNext()){
			Board b = (Board)iter.next();
			b.draw(g2d);
		 }
			
	     //players
	     p1.draw(g2d);
	     
	     //edges
	     g2d.setColor(Color.black);
	     g2d.fillRect(0, 0, Global.WIDTH, Global.MARGIN);
	     g2d.fillRect(0, Global.HEIGHT - Global.MARGIN, Global.WIDTH, Global.MARGIN);
	     g2d.setColor(Color.gray);
	     g2d.fillRect(0, 0, Global.MARGIN, Global.HEIGHT);
	     g2d.fillRect(Global.WIDTH - Global.MARGIN + 1, 0, Global.MARGIN, Global.HEIGHT);
	     g2d.setColor(Color.black);
	     g2d.drawRect(Global.MARGIN,Global.MARGIN,Global.WIDTH - 2*Global.MARGIN, Global.HEIGHT - 2*Global.MARGIN);

	     //draw
	     g.drawImage(screen, 0, 0, null);
	}
    class ThreadUpadte implements Runnable {  
        public void run() {  
            while (true) {  
                try {  
                	if(pause)wait();
                    Thread.sleep(Global.SLEEPTIME); 
                    tick();
                    repaint();  
            		if(over())stop();
                } catch (Exception e) {  
                    //e.printStackTrace();  
                }  
            }  
        }  
    }  
	public void tick(){
		
		if(ticks%20==0)newBoard();
		if(ticks%60==0){
			layer++;
			if(layer%25==0){
				//speed up
				Global.SLEEPTIME-=1;Global.MAXSPEEDX+=1;Global.BOARDSPEED+=1;Global.MAXSPEEDY+=1;
			}
		}
		
		//update player location
		p1.update();
		
		//update board location
		Iterator<Board> iter = boards.iterator();
		while(iter.hasNext()){
			Board b = (Board)iter.next();
			b.update();
			//check if removable
			if(b.death()){
				iter.remove();
			}
			//player-board hit check
			b.checkPlayer(p1);
		}
		
		//check if dead
		p1.death();
		
		ticks++;
	}
	
	private void newBoard() {
		Board b;
		double x = Math.random();
		if(x<0.20)b = spikeF.produce();
		else if(x<0.30)b=bounceF.produce();
		else if(x<0.40)b=sparkF.produce();
		else if(x<0.50)b=twinF.produce();
		else b = boardF.produce();
		boards.add(b);
	}

	public static RunGame getInstance(){
		if(Instance == null)Instance = new RunGame();
		return Instance;
	}
	
	protected RunGame() {

		Global.GRAVITY = 2.7f;
		Global.RUNACC = 1.8f;
		
		screen = new BufferedImage(Global.WIDTH, Global.HEIGHT,BufferedImage.TYPE_3BYTE_BGR);
		
		try{
			Board.readResource();
			SpikeBoard.readResource();
			BounceBoard.readResource();
			SparkBoard.readResource();
			TwinBoard.readResource();
		}
		catch(Exception e){
			
		}
		
		setSize(Global.WIDTH, Global.HEIGHT);
		
		addKeyListener(this);

	}
	public void addGameOverListener(GameOverListener L){
		listener = L;
	}
	public void setSpike(boolean b) {
		spike=b;
	}
	public void setBounce(boolean b) {
		bounce=b;
	}
	public void setSpark(boolean b) {
		spark=b;
	}
	public void setTwin(boolean b) {
		twin=b;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				p1.setL();
				break;
			case KeyEvent.VK_D:
				p1.setR();
				break;
			case KeyEvent.VK_W:
			case KeyEvent.VK_SPACE:
				p1.jump();
				break;
			case KeyEvent.VK_P:
				if(pause==false)pause = true;
				else pause=false;
				break;
			case KeyEvent.VK_R:
				restart();
				break;
			case KeyEvent.VK_ESCAPE:
				listener.gameOverEvent();
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			p1.unsetL();
			break;
		case KeyEvent.VK_D:
			p1.unsetR();
			break;
		}
	}
	
}
