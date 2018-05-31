package board;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

import main.Global;
import main.Player;

public class SpikeBoard extends Board {
	public static Image img;
	
	public static void readResource(){
		try{
			img = ImageIO.read(new File("./imgs/spikeboard.jpg"));
		}
		catch(Exception e){
			
		}
	}
	
	public void checkPlayer(Player p) {
		if(ground(p)){
			p.y=y-p.height;
			p.setGround(-Global.BOARDSPEED,1);
		}
	}
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(img, x, y, null);
	}
}
