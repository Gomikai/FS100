package board;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

import main.Global;
import main.Player;
import musicPlayer.MusicPlayer;

public class Board{

	protected static Image img;
	protected static int width = 175;
	protected static int height = 23;
	protected int x;
	protected int y;
	
	public static void readResource(){
		try{
			img = ImageIO.read(new File("./imgs/board.jpg"));
		}
		catch(Exception e){
			
		}
	}
	
	public void update() {
		y = y-Global.BOARDSPEED;
	}

	protected boolean ground(Player p){
		if(y<Global.MARGIN + Player.height)return false;
		if(p.x>=x-p.width&&p.x<=x+width&&p.y>=y-p.height&&
				p.lasty<=y-p.height+Global.BOARDSPEED)return true;
		return false;
	}
	
	public void checkPlayer(Player p) {
		if(ground(p)){
			p.y=y-p.height;
			p.setGround(-Global.BOARDSPEED,0);
		}
	}
	
	public Board(){
		y = Global.HEIGHT-Global.MARGIN;
		//generate randomly
		x = Global.MARGIN + (int)(Math.random() * (Global.WIDTH-Global.MARGIN-width));
	}
	public Board(int a,int b){
		y = b;
		x = a - width/2;
	}

	public boolean death() {
		if(y<=Global.MARGIN-height)return true;
		return false;
	}

	public void draw(Graphics2D g2d) {
		g2d.drawImage(img, x, y, null);
	}

}
