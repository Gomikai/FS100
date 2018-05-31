package main;

import gameOver.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements MouseListener, MouseMotionListener, 
													ActionListener, KeyListener, GameOverListener{
	
	private int xOld;
	private int yOld;
	
	private int state;
	private RunGame run;
	
	private JPanel menu;
	private JButton sing,dual;
	private JCheckBox spike,bounce,spark,twin,soul,eternal;
	
	MainWindow(){
		Global.WIDTH = 750;
		Global.HEIGHT = 720;
		Global.MARGIN = 15;
		
		state=0;
		
		setTitle("FSRun");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, Global.WIDTH, Global.HEIGHT); 
		setSize(Global.WIDTH, Global.HEIGHT);
		setUndecorated(true);
		setLayout(null);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		menu = getStartPanel();
		menu.addKeyListener(this);
		add(menu);
		menu.setVisible(true);
		
		run = RunGame.getInstance();
		run.init();
		add(run);
		run.setVisible(true);
		
		setVisible(true);

	}

	private void start() {
		menu.setVisible(false);
		add(run);
		run.setVisible(true);
		run.setFocusable(true);
		run.requestFocus();
		run.init();
		run.start();
	}
	
	//stop game and show startPanel
	@Override
	public void gameOverEvent() {
		state=0;
		run.stop();	
		run.setFocusable(false);
		menu.setVisible(true);
		menu.requestFocus();
	}
	
	//startPanel
	protected static int width = 200;
	protected static int height = 200;
	private JPanel getStartPanel() {
		JPanel t = new JPanel(new BorderLayout());
		t.setBounds((Global.WIDTH-width)/2, (Global.HEIGHT-height)/2, width, height);
		t.setSize(width,height);
		t.setBackground(new Color(123,134,156));
		
		
		JPanel l = new JPanel();
		l.setLayout(new BoxLayout(l, BoxLayout.Y_AXIS));
		l.setPreferredSize(new Dimension(width/2,height));
		l.setOpaque(false);
		
		l.add(Box.createHorizontalStrut(30));
		l.add(Box.createVerticalStrut(15));
		spike = new JCheckBox("钉板",true);
		spike.setFocusable(false);
		l.add(spike);		
		l.add(Box.createVerticalStrut(10));
		bounce = new JCheckBox("弹跳板",true);
		bounce.setFocusable(false);
		l.add(bounce);		
		l.add(Box.createVerticalStrut(10));
		spark = new JCheckBox("虚板",true);
		spark.setFocusable(false);
		l.add(spark);		
		l.add(Box.createVerticalStrut(10));
		twin = new JCheckBox("移动板",true);
		twin.setFocusable(false);
		l.add(twin);		
		l.add(Box.createVerticalGlue());
		
		sing = new JButton("Single");
		sing.setFocusable(false);
		sing.addActionListener(this);
		l.add(sing);		
		l.add(Box.createVerticalStrut(15));
		
		
		JPanel r = new JPanel();
		r.setLayout(new BoxLayout(r, BoxLayout.Y_AXIS));
		r.setPreferredSize(new Dimension(width/2,height));
		r.setOpaque(false);
		
		//r.add(Box.createHorizontalStrut(15));
		r.add(Box.createVerticalStrut(50));
		soul = new JCheckBox("交换板",true);
		soul.setFocusable(false);
		r.add(soul);	
		r.add(Box.createVerticalStrut(30));
		eternal = new JCheckBox("急救板",true);
		eternal.setFocusable(false);
		r.add(eternal);
		r.add(Box.createVerticalGlue());
		
		dual = new JButton("Dual");
		dual.setFocusable(false);
		dual.addActionListener(this);
		r.add(dual);		
		r.add(Box.createVerticalStrut(16));
		
		
		t.add(l,BorderLayout.WEST);
		t.add(r,BorderLayout.EAST);
		
		return t;
	}
	
	static public void main(String[] args){
		JFrame window = new MainWindow();
	}
	
	//make window dragable
		@Override  
	    public void mouseDragged(MouseEvent e) {  
	        int xOnScreen = e.getXOnScreen();  
	        int yOnScreen = e.getYOnScreen();  
	        int xx = xOnScreen - xOld;  
	        int yy = yOnScreen - yOld;  
	        this.setLocation(xx, yy);  
	    }  

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		//make window dragable
		@Override  
	    public void mousePressed(MouseEvent e) {  
	        xOld = e.getX();  
	        yOld = e.getY(); 
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			remove(run);
			if(e.getSource()==dual){
				state=2;
				run = RunGameDouble.getInstance();
				((RunGameDouble) run).setSoul(soul.isSelected());
				((RunGameDouble) run).setEternal(eternal.isSelected());
			}
			else{
				state=1;
				run = RunGame.getInstance();
			}
			run.setSpike(spike.isSelected());
			run.setBounce(bounce.isSelected());
			run.setSpark(spark.isSelected());
			run.setTwin(twin.isSelected());
			
			run.addGameOverListener(this);
			
			start();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE)System.exit(0);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

}
