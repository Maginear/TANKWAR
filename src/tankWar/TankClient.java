package tankWar;
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

/**
 * 
 * @author Magy
 *
 */
public class TankClient extends Frame {
	
	/**
	 * �趨����̹����Ϸ����Ŀ��
	 */
	public static final int frameWidth = 1200;
	
	/**
	 * �趨����̹����Ϸ����ĸ߶�
	 */
	public static final int frameHeight = 700;
	
	private Image offScreeenImage = null;
	public Wall wall;
	public Tank myTank = new Tank(70, 120, true, this); 
	
	/*
	 * ������һ���з�AI̹�˵ļ��ϣ������ڶ�ÿһ���з�AI̹������
	 * ʵ�����˴������̹�˶���
	 * ����������̹�˵�����ֵ�Լ�������ڵ�����ը�ļ���
	 */
	public List<Tank> tanks = new ArrayList<Tank>();               
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>();            
	public ArrayList<Explode> explodes = new ArrayList<Explode>();        
    public ArrayList<TankBlood> TankBloods = new ArrayList<TankBlood>();  
    
    /**
     * ʵ����̹����Ϸ����
     * @param args �������ò���
     */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
    
	/**
	 * ע����̼�������window�رռ�������
	 * ����ɽ����ʼ���Ϳ�ʼ�ػ���߳�
	 * 
	 */
	private void launchFrame() {
		wall = new Wall(this, 500, 150, 50, 450);
        makeEnemyTank();
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

	/**
	 * ���ڼ������̰��º��ͷ��¼����ڲ���
	 * @author Magy
	 *
	 */
	private class KeyMonitorPress extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			myTank.KeyPressed(e);
		}

		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}
	
	/**
	 * ���������ڷ�Χ���������20���з�AI̹��
	 */
	public void makeEnemyTank(){
		for (int i = 0; i < 20; i++) {
			tanks.add(new Tank((int) ((frameWidth - 5 - Tank.WIDTH) * Math
					.random()) + 5, (int) ((frameHeight - 30 - Tank.HEIGHT)
					* Math.random() + 30), false, this));
		}
	}
    
    /**
     * ���ػ�ǰ�ض�,�ں�̨������ͼ�񻭵�һ��ͼƬ�ϣ���һ���ԵĽ�ͼƬ���Ƴ����������ܹ���֤ǰ�˻���������ԡ�
     */
	public void update(Graphics g) {
		if (offScreeenImage == null) {
			offScreeenImage = this.createImage(frameWidth, frameHeight);
		}

		Graphics goffscreen = offScreeenImage.getGraphics();
		Color c = goffscreen.getColor();
		goffscreen.setColor(Color.LIGHT_GRAY);
		goffscreen.fill3DRect(0, 0, frameWidth, frameHeight, true);
		goffscreen.setColor(c);

		paint(goffscreen);
		g.drawImage(offScreeenImage, 0, 0, null);
	}
    
	/**
	 * �ڴ������ϽǴ�ӡ���з�AI̹���������������ڵ�������
	 * Ҳ���������еĵз�AI̹�ˡ������ڵ��������̹���Լ�ǽ��
	 * 	 
	 */
	public void paint(Graphics g) {
		g.drawString("EnemyTank conut: " + tanks.size(), 40, 50);
		g.drawString("Bullet    count: " + bullets.size(), 40, 65);
		g.drawString("��F1����20�����ˣ���F2���¸��", 40, 80);
		g.drawString("����������а˸������ƶ������ո���ɷ����ڵ���", 40, 100);
		
		wall.drawMe(g);
		for (int a = 0; a < bullets.size(); a++) {
			bullets.get(a).drawMe(g);
		}
		for (int a = 0; a < tanks.size(); a++) {
			tanks.get(a).drawMe(g);
		}
		for (int a = 0; a < explodes.size(); a++) {
			explodes.get(a).drawMe(g);
		}
		for (int a = 0; a < TankBloods.size(); a++) {
			TankBloods.get(a).drawMe(g);
		}
		myTank.drawMe(g);
	}
	
    /**
     * �����ػ�Ƶ�ʵ��߳��ڲ���
     * @author Magy
     *
     */
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
