package main;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

import board.*;

@SuppressWarnings("serial")
public class RunGameDouble extends RunGame implements KeyListener {
	
	private static RunGameDouble Instance = null;
	
	private Color life2 = new Color(233, 150, 150);
	
	private Player p2;

	protected boolean swap;
	private boolean soul,
					eternal;
	private BoardFactory soulF,
						eternalF;
	
	protected void init(){
		super.init();
		
		swap = false;
		
		p2 = new Player("player2",(Global.WIDTH)/2+Player.width,(Global.HEIGHT)/2-Player.height);

		if(soul)soulF = SoulBoardFactory.getInstance();
		else soulF = boardF;
		if(eternal)eternalF = EternalBoardFactory.getInstance();
		else eternalF = boardF;
	}
	protected boolean over(){
		if(p1.dead && p2.dead)return true;
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
	     g2d.setColor(life2);
	     String p2life=String.valueOf(p2.life);
	     g2d.drawString(p2life, Global.WIDTH-Global.MARGIN-5-25*(p2life.length()), 40+Global.MARGIN);
	     
	     //boards
	     Iterator<Board> iter = boards.iterator();
	     while(iter.hasNext()){
			Board b = (Board)iter.next();
			b.draw(g2d);
		 }
			
	     //players
	     p1.draw(g2d);
	     p2.draw(g2d);
	     
	     //edges
	     g2d.setColor(Color.black);
	     g2d.fillRect(0, 0, Global.WIDTH, Global.MARGIN);
	     g2d.fillRect(0, Global.HEIGHT - Global.MARGIN, Global.WIDTH, Global.MARGIN);
	     if(swap){
	    	 g2d.setColor(Color.magenta);
	    	 swap=false;
	     }
	     else g2d.setColor(Color.gray);
	     g2d.fillRect(0, 0, Global.MARGIN, Global.HEIGHT);
	     g2d.fillRect(Global.WIDTH - Global.MARGIN + 1, 0, Global.MARGIN, Global.HEIGHT);
	     g2d.setColor(Color.black);
	     g2d.drawRect(Global.MARGIN,Global.MARGIN,Global.WIDTH - 2*Global.MARGIN, Global.HEIGHT - 2*Global.MARGIN);

	     //draw
	     g.drawImage(screen, 0, 0, null);
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
		p2.update();
		
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
			b.checkPlayer(p2);
		}
		
		//do swap if swap triggered
		if(swap){
			Player.swap(p1, p2);
		}
		
		//player-player hit check
		Player.checkHit(p1,p2);
		
		//check if dead
		p1.death();p2.death();
		
		ticks++;
	}
	public void setSwap() {
		swap=true;
	}
	
	private void newBoard() {
		Board b;
		double x = Math.random();
		if(x<0.20)b = spikeF.produce();
		else if(x<0.30)b=bounceF.produce();
		else if(x<0.40)b=sparkF.produce();
		else if(x<0.50)b=twinF.produce();
		else if(x<0.53)b=soulF.produce();
		else if(x<0.56)b=eternalF.produce();
		else b = boardF.produce();
		boards.add(b);
	}

	public static RunGameDouble getInstance(){
		if(Instance == null)Instance = new RunGameDouble();
		return Instance;
	}
	
	protected RunGameDouble() {

		try{
			SoulBoard.readResource();
			EternalBoard.readResource();
		}
		catch(Exception e){
			
		}

	}
	public void setSoul(boolean b) {
		soul=b;
	}
	public void setEternal(boolean b) {
		eternal=b;
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
			case KeyEvent.VK_LEFT:
				p2.setL();
				break;
			case KeyEvent.VK_RIGHT:
				p2.setR();
				break;
			case KeyEvent.VK_UP:
			case KeyEvent.VK_ENTER:
				p2.jump();
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
		case KeyEvent.VK_LEFT:
			p2.unsetL();
			break;
		case KeyEvent.VK_RIGHT:
			p2.unsetR();
			break;
		}
	}
	
}
