package board;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.Global;
import main.Player;
import musicPlayer.MusicPlayer;

public class BounceBoard extends Board {
	public static BufferedImage[] img = new BufferedImage[2];
	private int ready;
	private int timer;
	
	public static void readResource(){
		
		try{
			img[0] = ImageIO.read(new File("./imgs/bounceboard.gif"));
		}
		catch(Exception e){
			
		}
		img[1] = img[0].getSubimage(0, 0, width, height);
		img[0] = img[0].getSubimage(0, height+1, width, height);
	}
	
	public void checkPlayer(Player p) {
		
		if(ground(p)){
			p.y=y-p.height;
			if(timer==0){
				p.setGround(-Global.BOARDSPEED-Global.MAXSPEEDY*0.70f,3);
				ready=0;
			}
			else {
				p.setGround(-Global.BOARDSPEED,-1);
			}
		}
	}
	
	BounceBoard(){
		ready=1;
		timer=0;
	}
	
	public void update() {
		y = y-Global.BOARDSPEED;
		if(ready==0)timer++;
		if(timer==12){
			timer=0;
			ready=1;
		}
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(img[ready], x, y, null);
	}
}
