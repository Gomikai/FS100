package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import musicPlayer.MusicPlayer;

public class Player {

	public int x;
	public int y;
	public int lasty;
	
	private float vx;
	private float vy;

	private float ax;
	private float ay;
	
	private int state;//going left (1) or right (2)
	private boolean ground;//standing on board/player
	private boolean groundBefore;//standing on board/player last frame?
	public boolean dead;
	
	public int life;
	
	public static int width = 30;
	public static int height = 45;
	protected BufferedImage[] img = new BufferedImage[2];
	private MusicPlayer hitSound;
	private MusicPlayer deathSound;
	private MusicPlayer groundSound;
	private MusicPlayer bounceSound;
	private MusicPlayer eternalSound;
	
	private int maxLife;
	
	public void draw(Graphics2D g2d) {
		g2d.drawImage(img[dead?1:0], x, y, null);
	}
	
	public void update() {
		
		//update acceleration
		if(state==0||dead)ax = -vx*0.30f;
		else{
			if(state==1)ax=-Global.RUNACC;
			else if(state==2)ax=Global.RUNACC;
		}
		
		if(groundBefore==false){//free drop
			ground=false;
			ay=Global.GRAVITY;
		}
		else{//unset groundBefore for next frame. 
			 //player update is before board-player hit check in tick() so it will be set correctly 
			 //		later in this frame if player still ground.
			groundBefore=false;	
		}
		
		
		//update velocity
		vx+=ax;
		if(vx>=Global.MAXSPEEDX)vx=Global.MAXSPEEDX;
		if(-vx>=Global.MAXSPEEDX)vx=-Global.MAXSPEEDX;
		
		vy+=ay;
		if(vy>=Global.MAXSPEEDY)vy=Global.MAXSPEEDY;
		if(-vy>=Global.MAXSPEEDY)vy=-Global.MAXSPEEDY;
		
		
		//update location
		x = (int)(x+vx+0.5f);
		if(x>Global.WIDTH-Global.MARGIN-width){
			x=Global.WIDTH-Global.MARGIN-width;
			vx=ax=0;
		}
		if(x<Global.MARGIN){
			x=Global.MARGIN;
			vx=ax=0;
		}
		
		lasty=y;
		y = (int)(y+vy+0.5f);
		if(y<Global.MARGIN){
			y=Global.MARGIN;
			this.hit(4);
			vy=0;
		}

	}

	Player(String name,int x,int y){
		this.x=x;
		this.y=y;
		
		vx=vy=ax=0;
		ay=Global.GRAVITY;
		state=0;
		ground = true;
		groundBefore = true;
		dead=false;
		
		maxLife=life=10;
		
		try{
			img[0] = ImageIO.read(new File("./imgs/"+name+".gif"));
		}
		catch(Exception e){
			
		}
		img[1] = img[0].getSubimage(width, 0, width, height);
		img[0] = img[0].getSubimage(0, 0, width, height);
		hitSound = new MusicPlayer("./sounds/hit.wav");
		deathSound = new MusicPlayer("./sounds/"+name+".wav");
		groundSound = new MusicPlayer("./sounds/board.wav");
		bounceSound = new MusicPlayer("./sounds/bounce.wav");
		eternalSound = new MusicPlayer("./sounds/eternal.wav");
		
	}

	public void setL() {
		state |= 1;
	}
	public void setR() {
		state |= 2;
	}
	public void unsetL() {
		state &= 2;
	}
	public void unsetR() {
		state &= 1;
	}
	//set player ground (standing on board/player)
	public void setGround(float speed, int type){
		if(ground==false){
			if(type==0){
				if(life<maxLife)
				life++;
				groundSound.play();
			}
			else if(type==1)hit(4);
			else if(type==2){
				life+=5;
				maxLife+=1;
				eternalSound.play();
			}
		}
		if(type==3){
			if(life<maxLife)
			life++;
			bounceSound.play();
		}
		vy=speed;
		ay=0;
		ground = groundBefore = true;
	}
	public void jump() {
		if(ground && !dead)vy-=Global.MAXSPEEDY*0.62f;
	}
	public void hit(int x){
		hitSound.play();
		life-=x;
	}
	public void death(){
		if(!dead && (y>=Global.HEIGHT || life<=0)){
			dead = true;
			deathSound.play();
		}
	}

	//check player-player hit
	public static void checkHit(Player p1, Player p2) {
        float x1 = Math.max(p1.x,p2.x);
        float y1 = Math.max(p1.y,p2.y);
        float x2 = Math.min(p1.x+width,p2.x+width);
        float y2 = Math.min(p1.y+height,p2.y+height);
        
        if(x2>x1&&y2>y1){//fix player location and swap velocity (no energy loss in hit)
        	
        	float slope=(y2-y1)/(x2-x1);
        	
        	if(slope<=1.5 && slope>=-1.5){//divide players vertically
        		if(p1.y<p2.y){
        			p1.y = p2.y-height-1;
        			if(p2.ground){
        				p1.setGround(p2.vy, -1);
        				return;
        			}
        		}
        		else{
        			p2.y = p1.y-height-1;
        			if(p1.ground){
        				p2.setGround(p1.vy, -1);
        				return;
        			}
        		}
        		y1=p1.vy;
        		p1.vy=p2.vy;
        		p2.vy=y1;
        	}
        	else{//Horizontally
        		if(p1.x<p2.x){
        			p2.x += (x2-x1)/2;
        			p1.x = p2.x-width-1;
        		}
        		else{
        			p1.x += (x2-x1)/2;
        			p2.x = p1.x-width-1;
        		}
        		x1=p1.vx;
        		p1.vx=p2.vx;
        		p2.vx=x1;
        	}
        }
	}
	
	//swap players
	public static void swap(Player p1, Player p2) {
		int temp;
		temp = p1.x;p1.x = p2.x;p2.x = temp;
		temp = p1.y;p1.y = p2.y;p2.y = temp;
		temp = p1.lasty;p1.lasty = p2.lasty;p2.lasty = temp;
		temp = p1.life;p1.life = p2.life;p2.life = temp;
		temp = p1.maxLife;p1.maxLife = p2.maxLife;p2.maxLife = temp;
		
		float tempf;
		tempf = p1.vx;p1.vx = p2.vx;p2.vx = tempf;
		tempf = p1.vy;p1.vy = p2.vy;p2.vy = tempf;
		tempf = p1.ax;p1.ax = p2.ax;p2.ax = tempf;
		tempf = p1.ay;p1.ay = p2.ay;p2.ay = tempf;
		
		p1.ground^=p2.ground;p2.ground^=p1.ground;p1.ground^=p2.ground;
		p1.groundBefore^=p2.groundBefore;
			p2.groundBefore^=p1.groundBefore;
			p1.groundBefore^=p2.groundBefore;
		p1.dead^=p2.dead;p2.dead^=p1.dead;p1.dead^=p2.dead;	
	}
}
