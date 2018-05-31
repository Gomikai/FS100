package board;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.Global;
import main.Player;
import musicPlayer.MusicPlayer;

public class EternalBoard extends Board{
	public static BufferedImage[] img = new BufferedImage[6];
	
	private boolean ready;
	private int timer;
	
	public static void readResource(){
		try{
			img[0] = ImageIO.read(new File("./imgs/eternalboard.gif"));
		}
		catch(Exception e){
			
		}
		for(int i=1;i<6;i++){
			img[i] = img[0].getSubimage(0,i*(height+1),width,height);
		}
		img[0] = img[0].getSubimage(0,0,width,height);
	}
	
	public void checkPlayer(Player p) {
		if(ground(p)){
			p.y=y-p.height;
			if(timer==0){
				p.setGround(-Global.BOARDSPEED,2);
				ready=false;
			}
			else if(timer==5)p.setGround(-Global.BOARDSPEED,1);
			else p.setGround(-Global.BOARDSPEED,0);
		}
	}
	
	EternalBoard(){
		ready=true;
		timer=0;
	}
	
	public void update() {
		y = y-Global.BOARDSPEED;
		if(!ready&&timer<5)timer++;
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(img[timer], x, y, null);
	}
}
