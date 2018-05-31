package board;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.Global;
import main.Player;
import main.RunGameDouble;
import musicPlayer.MusicPlayer;

public class SoulBoard extends Board {
	public static BufferedImage[] img = new BufferedImage[2];
	
	private int ready;
	
	private static MusicPlayer sound;
	
	public static void readResource(){
		sound = new MusicPlayer("./sounds/soul.wav");
		
		try{
			img[0] = ImageIO.read(new File("./imgs/soulboard.gif"));
		}
		catch(Exception e){
			
		}
		img[1] = img[0].getSubimage(0, 0, width, height);
		img[0] = img[0].getSubimage(0, height+1, width, height);
	}
	
	public void checkPlayer(Player p) {
		if(ground(p)){
			p.y=y-p.height;
			
			if(ready==1){
				sound.play();
				ready=0;
				RunGameDouble.getInstance().setSwap();
				p.setGround(-Global.BOARDSPEED,-1);
			}
			else p.setGround(-Global.BOARDSPEED,0);
		}
	}
	
	SoulBoard(){
		ready=1;
	}
	
	public void update() {
		y = y-Global.BOARDSPEED;
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(img[ready], x, y, null);
	}
}
