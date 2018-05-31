package board;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.Global;
import main.Player;
import musicPlayer.MusicPlayer;

public class SparkBoard extends Board {
	public static BufferedImage[] img = new BufferedImage[10];
	
	private boolean ready;
	private int timer;
	
	private MusicPlayer sound;
	
	public static void readResource(){
		
		try{
			img[0] = ImageIO.read(new File("./imgs/sparkboard.gif"));
		}
		catch(Exception e){
			
		}
		for(int i=1;i<10;i++){
			img[i] = img[0].getSubimage(0,i*(height+1),width,height);
		}
		img[0] = img[0].getSubimage(0,0,width,height);
	}
	
	public void checkPlayer(Player p) {
		if(timer>0)return;
		if(ground(p)){
			sound.play();
			p.y=y-p.height;
			p.setGround(-Global.BOARDSPEED,-1);
			ready=false;
		}
	}
	
	SparkBoard(){
		sound = new MusicPlayer("./sounds/spark.wav");
		ready=true;
		timer=0;
	}
	
	public void update() {
		y = y-Global.BOARDSPEED;
		if(!ready)timer++;
		if(timer==20){
			timer=0;
			ready=true;
		}
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(img[timer/3], x, y, null);
	}
}