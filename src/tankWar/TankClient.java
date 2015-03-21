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
	 * 设定整个坦克游戏程序的宽度
	 */
	public static final int frameWidth = 1200;
	
	/**
	 * 设定整个坦克游戏程序的高度
	 */
	public static final int frameHeight = 700;
	
	private Image offScreeenImage = null;
	public Wall wall;
	public Tank myTank = new Tank(70, 120, true, this); 
	
	/*
	 * 声明了一个敌方AI坦克的集合，方便在对每一个敌方AI坦克引用
	 * 实例化了创建玩家坦克对象
	 * 声明了所有坦克的生命值以及发射的炮弹、爆炸的集合
	 */
	public List<Tank> tanks = new ArrayList<Tank>();               
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>();            
	public ArrayList<Explode> explodes = new ArrayList<Explode>();        
    public ArrayList<TankBlood> TankBloods = new ArrayList<TankBlood>();  
    
    /**
     * 实例化坦克游戏窗口
     * @param args 运行配置参数
     */
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();
	}
    
	/**
	 * 注册键盘监听器和window关闭监听器，
	 * 并完成界面初始化和开始重绘的线程
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
	 * 用于监听键盘按下和释放事件的内部类
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
	 * 在整个窗口范围内随机生成20个敌方AI坦克
	 */
	public void makeEnemyTank(){
		for (int i = 0; i < 20; i++) {
			tanks.add(new Tank((int) ((frameWidth - 5 - Tank.WIDTH) * Math
					.random()) + 5, (int) ((frameHeight - 30 - Tank.HEIGHT)
					* Math.random() + 30), false, this));
		}
	}
    
    /**
     * 在重绘前截断,在后台将所有图像画到一张图片上，再一次性的将图片绘制出来。这样能够保证前端画面的流畅性。
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
	 * 在窗口左上角打印出敌方AI坦克数量、窗口内炮弹总数。
	 * 也将画出所有的敌方AI坦克、所有炮弹、和玩家坦克以及墙。
	 * 	 
	 */
	public void paint(Graphics g) {
		g.drawString("EnemyTank conut: " + tanks.size(), 40, 50);
		g.drawString("Bullet    count: " + bullets.size(), 40, 65);
		g.drawString("按F1增加20个敌人，按F2重新复活。", 40, 80);
		g.drawString("按方向键进行八个方向移动，按空格键可发射炮弹。", 40, 100);
		
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
     * 控制重绘频率的线程内部类
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
