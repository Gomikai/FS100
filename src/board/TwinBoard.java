package board;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import main.Global;
import main.Player;

public class TwinBoard extends Board {
	public static BufferedImage[] img = new BufferedImage[80];
	public static int width = 105;
	public static int range = 205;
	public static int halft = 41;
	public static int step = range/(halft-1);
	private int a,b;
	private float imgNo;
	private int imgOffset;
	private int timer;
	
	public static void readResource(){
		try{
			img[0] = ImageIO.read(new File("./imgs/twinboard.gif"));
		}
		catch(Exception e){
			
		}
		for(int i=1;i<40;i++){
			img[40+i] = img[0].getSubimage(width,i*(height+1),width,height);
			img[i] = img[0].getSubimage(0,i*(height+1),width,height);
		}
		img[40] = img[0].getSubimage(width,0,width,height);
		img[0] = img[0].getSubimage(0,0,width,height);
	}
	TwinBoard(){
		imgOffset = timer%2*20;
		timer = (int)(Math.random()*(halft*2-1)) - halft + 1;
		a=x-range+Math.abs(timer)*step;b=x+range+width-Math.abs(timer)*step;
		imgNo = (1-(float)Math.abs(timer)/(float)halft)*19;
	}
	
	protected boolean ground(Player p){
		if(y<Global.MARGIN + Player.height)return false;
		if((p.x>=a-p.width&&p.x<=a+width&&p.y>=y-p.height || p.x>=b-p.width&&p.x<=b+width&&p.y>=y-p.height)
				&& p.y<=y-p.height+Global.BOARDSPEED+Global.MAXSPEEDY)return true;
		return false;
	}
	public void checkPlayer(Player p) {
		if(ground(p)){
			p.y=y-p.height;
			p.setGround(-Global.BOARDSPEED,0);
		}
	}
	
	public void update() {
		y = y-Global.BOARDSPEED;

		if(timer<0){
			a-=step;
			b+=step;
			imgNo+=19/(float)halft;
		}
		else if(timer>0){
			a+=step;
			b-=step;
			imgNo-=19/(float)halft;
		}
		timer++;
		if(timer==halft){
			timer=1-halft;
			imgOffset ^= 20;
		}
	}
	
	public void draw(Graphics2D g2d) {
		//g2d.drawRect(a,y,width,height);
		//g2d.drawRect(b,y,width,height);		
		g2d.drawImage(img[(int)imgNo+imgOffset], a, y, null);
		g2d.drawImage(img[40 + (int)imgNo + imgOffset], b, y, null);
	}
}