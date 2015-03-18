import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {
	Image offScreeenImage = null;
    public static final int frameWidth = 1200;
    public static final int frameHeight = 700;
    Tank myTank = new Tank(40, 100, true, this);
    List<Tank> tanks = new ArrayList<Tank>();
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    ArrayList<Explode> explodes = new ArrayList<Explode>();
    
	public static void main(String[] args) {
		TankClient tc = new TankClient();
        tc.launchFrame();
	}
	
	private void launchFrame() {
		for (int i = 0; i < 10; i++) {
			tanks.add(new Tank((int) ((frameWidth - 5 - Tank.WIDTH) * Math.random()) + 5,
					(int) ((frameHeight - 30 - Tank.HEIGHT)* Math.random() + 30) , false, this));
		}
		
		this.setTitle("TankWar");
		this.setLocation(50, 50);
		this.setSize(frameWidth, frameHeight);
		this.setVisible(true);
		this.setResizable(false);
		this.setBackground(Color.green);
		
		this.addKeyListener(new KeyMonitorPress());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		new Thread(new PaintThread()).start();
	}

	private class KeyMonitorPress extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			myTank.KeyPressed(e);
		}
		
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
   
   
	}
	

    public void update(Graphics g) {
	    if(offScreeenImage == null){
	    	offScreeenImage = this.createImage(frameWidth, this.getHeight());
	    }
	    
	    Graphics goffscreen = offScreeenImage.getGraphics();
	    Color c = goffscreen.getColor();
	    goffscreen.setColor(Color.GREEN);
	    goffscreen.fill3DRect(0, 0,frameWidth, frameHeight, true);
	    goffscreen.setColor(c);
	    
	    paint(goffscreen);
	    g.drawImage(offScreeenImage, 0, 0, null);
	}
 
    public void paint(Graphics g) {
    	myTank.drawMe(g);
    	
    	g.drawString("Tank    conut: " + tanks.size(), 40, 60);
    	g.drawString("Bullet  count: " + bullets.size(), 40, 75);
		
		for (int a = 0; a < tanks.size(); a++) {
			tanks.get(a).drawMe(g);
		}
		for (int a = 0; a < bullets.size(); a++) {
			bullets.get(a).drawMe(g);
		}
		for (int a = 0; a < explodes.size(); a++) {
			explodes.get(a).drawMe(g);
		}
		
	}
    
	private class PaintThread implements Runnable {
		public void run() {
			while (true) {
				repaint();
				
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
			}
		}
	}
    
}
