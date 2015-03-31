package tankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class TankImpl implements MoveAndDrawInterface{
	
	/**
	 * 坦克的X坐标
	 */
    public int tank_x = 50;

	/**
	 * 坦克的X坐标
	 */
    public int tank_y = 50;
    public int old_x = 50; 
    public int old_y = 50; 
    
	/**
	 * 坦克的宽度常量
	 */
    public static final int WIDTH = 30;

    /**
	 * 坦克的高度常量
	 */
    public static final int HEIGHT = 30;
    
    /**
	 * 坦克的移动速度常量
	 */
    private static final int SPEED = 5;
    
    private int BulletNum;
    private int step = 0; 
    public boolean bePlayerTank; 
    public boolean live = true;
    public int life = 10;
//static int bulletTest = 0;
    
    private boolean bL = false, bU = false, bR = false, bD = false;    //将坦克的方向用枚举的方式形成集合，方便各个类规范使用。
    
    Direction dir = Direction.STOP;
    Direction ptDir = Direction.L;
    
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] tankImages = null;
    private static Map<String, Image> images = new HashMap<String, Image> ();
    private static Random r = new Random();
    
    static {
    	tankImages = new Image[]{
    	tk.getImage(TankImpl.class.getClassLoader().getResource("Images/tankL.gif")),
    	tk.getImage(TankImpl.class.getClassLoader().getResource("Images/tankLU.gif")),
    	tk.getImage(TankImpl.class.getClassLoader().getResource("Images/tankU.gif")),
    	tk.getImage(TankImpl.class.getClassLoader().getResource("Images/tankRU.gif")),
    	tk.getImage(TankImpl.class.getClassLoader().getResource("Images/tankR.gif")),
    	tk.getImage(TankImpl.class.getClassLoader().getResource("Images/tankRD.gif")),
    	tk.getImage(TankImpl.class.getClassLoader().getResource("Images/tankD.gif")),
    	tk.getImage(TankImpl.class.getClassLoader().getResource("Images/tankLD.gif"))
    	};
    	
    	images.put("L", tankImages[0]);
    	images.put("LU", tankImages[1]);
    	images.put("U", tankImages[2]);
    	images.put("RU", tankImages[3]);
    	images.put("R", tankImages[4]);
    	images.put("RD", tankImages[5]);
    	images.put("D", tankImages[6]);
    	images.put("LD", tankImages[7]);
    }
    
    
    ArrayList<Integer> oldKey = new ArrayList<Integer>();
    
    public TankClient tc;
    private TankBloodImpl tb;
    
	
    /**
     * 初始化坦克的位置，并开始限制坦克发射炮弹频率的线程MakeBullets
     * @param tank_x 坦克X轴位置
     * @param tank_y 坦克X轴位置
     * @param bePlayerTank 是否为玩家坦克
     * @param tc   游戏窗口
     */
	public TankImpl(int tank_x, int tank_y, boolean bePlayerTank, TankClient tc) {
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		this.bePlayerTank = bePlayerTank;
		this.tc = tc;
		new Thread(new MakeBullet()).start();
	}
	
	/**
	 * 1、为每个坦克New一个血条类
	 * 2、若当前坦克还活着（live =true），则根据是否是玩家坦克画出区别颜色的坦克及其炮筒,再让坦克前进一步。
	 * 否则将此炮弹移除Tanks集合，并不再重绘。
	 * @param g 画笔
	 */
	public void drawMe(Graphics g) {
		if (tb == null) {
			tb = new TankBloodImpl(this, tc);
			tc.TankBloods.add(tb);
		}
		
		if (live) {
			switch (ptDir) {
			case L:
				g.drawImage(images.get("L"), tank_x,
						tank_y, null);
				break;
			case LU:
				g.drawImage(images.get("LU"), tank_x,
						tank_y, null);
				break;
			case U:
				g.drawImage(images.get("U"), tank_x ,
						tank_y, null);
				break;
			case RU:
				g.drawImage(images.get("RU"), tank_x,
						tank_y, null);
				break;
			case R:
				g.drawImage(images.get("R"), tank_x ,
						tank_y, null);
				break;
			case RD:
				g.drawImage(images.get("RD"), tank_x,
						tank_y, null);
				break;
			case D:
				g.drawImage(images.get("D"), tank_x,
						tank_y, null);
				break;
			case LD:
				g.drawImage(images.get("LD"), tank_x,
						tank_y, null);
				break;
			}
			move();
		} else {
			tc.TankBloods.remove(tb);
			tc.tanks.remove(this);
		}
	}

	/**
	 * 根据坦克的方向，让坦克在窗口边界内并不与其余坦克、墙发生碰撞的前提下前进。
	 */
	public void move(){
		old_x = tank_x;
		old_y = tank_y;
		switch(dir){
		case L:
			tank_x -= SPEED;
			break;
		case LU:
			tank_x -= SPEED;
			tank_y -= SPEED;
			break;
		case U:
			tank_y -= SPEED;
			break;
		case RU:
			tank_x += SPEED;
			tank_y -= SPEED;
			break;
		case R:
			tank_x += SPEED;
			break;
		case RD:
			tank_x += SPEED;
			tank_y += SPEED;
			break;
		case D:
			tank_y += SPEED;
			break;
		case LD:
			tank_x -= SPEED;
			tank_y += SPEED;
			break;
		case STOP:
			break;
	
		}
		
		if(tank_x < 5) tank_x = 5;
		if(tank_y < 30) tank_y = 30;
		if(tank_x + WIDTH > TankClient.frameWidth - 5) tank_x = TankClient.frameWidth - WIDTH - 5;
		if(tank_y + HEIGHT > TankClient.frameHeight - 5) tank_y = TankClient.frameHeight - HEIGHT - 5;
	    
		if(!bePlayerTank){
			Direction[] dire = Direction.values();
			if(step == 0){
				do {
					ptDir = dir = dire[r.nextInt(dire.length)];
				} while (dir == Direction.STOP);
		        step = r.nextInt(12) + 3;   
			}
			step--;
		}
		
		if(isMeetTanks() || isMeetWall()){
		     this.tank_x = this.old_x;
		     this.tank_y = this.old_y;
		}
	}
	
	/**
	 * 在按键在释放后重新设定坦克方向，实现两个方向键同时按下的效果，并在完全释放时将方向设定为STOP。
	 * @param e 键盘事件事件
	 */
	public void keyReleased(KeyEvent e) {
		int Key = e.getKeyCode();
		switch (Key) {
		case KeyEvent.VK_RIGHT:
			bR = false;
			oldKey.remove(new Integer(Key));
//			System.out.println(oldKey.size());
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			oldKey.remove(new Integer(Key));
//			System.out.println(oldKey.size());
			break;
		case KeyEvent.VK_UP:
			bU = false;
			oldKey.remove(new Integer(Key));
//			System.out.println(oldKey.size());
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			oldKey.remove(new Integer(Key));
//			System.out.println(oldKey.size());
			break;
	
		}
		LocalDirection();
	}
	
	private boolean isOldKey(int i){
		boolean flag = false;
		for(int a = 0; a < oldKey.size(); a++){
			if(i == oldKey.get(a)){
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 在按键被按下时，改变方向(dir)或调用相应的方法。
	 * @param e  键盘事件
	 */
	public void KeyPressed(KeyEvent e){
		int Key = e.getKeyCode();
			if (!isOldKey(Key) && oldKey.size() <= 2) {
				switch (Key) {
				case KeyEvent.VK_RIGHT:
					bR = true;
					oldKey.add(Key); 
					break;
				case KeyEvent.VK_LEFT:
					bL = true;
					oldKey.add(Key);
					break;
				case KeyEvent.VK_UP:
					bU = true;
					oldKey.add(Key);
					break;
				case KeyEvent.VK_DOWN:
					bD = true;
					oldKey.add(Key);
					break;
				case KeyEvent.VK_A:
					superFire();
					break;
				case KeyEvent.VK_SPACE:
					   fire();
						break;
				case KeyEvent.VK_F1:
					tc.makeEnemyTank();
					break;
				case KeyEvent.VK_F2:
					tc.TankBloods.remove(this.tb);
					tc.myTank = new TankImpl(70, 120, true, tc);
					break;
				}
				LocalDirection();
			}
			 move();
		
		/*
		if((Key == KeyEvent.VK_RIGHT)){
			Tank_x += 5;
		}
		else if (Key == KeyEvent.VK_LEFT) {
			Tank_x += -5;
		}	
		else if (Key == KeyEvent.VK_UP) {
		    Tank_y += 5;
		}
		else if (Key == KeyEvent.VK_DOWN) {
			Tank_y += -5;
		}
		*/	
	}
    
	/**
	 * 向坦克的炮筒方向（ptDir）发射一发炮弹。
	 */
	private void fire() {
		if (live && BulletNum <= 2) {
			BulletImpl b = new BulletImpl(this, tc, ptDir);
			BulletNum++;
			new Thread(b).start();
			tc.bullets.add(b);
		}
	}
	
	public void fire(Direction dire){
		BulletImpl b = new BulletImpl(this, tc, dire);
		new Thread(b).start();
		tc.bullets.add(b);
//		System.out.println(tc.bullets.size());
//		System.out.println("BulletTest : "+bulletTest);
//		bulletTest++;
	}
	
	/**
	 * 发射一次八个方向的超级炮弹。
	 */
	private void superFire(){
		if (live) {
			Direction[] dire = Direction.values();
			for (int i = 0; i < 8; i++) {
				fire(dire[i]);
//				System.out.println(dire[i]);
			}
		}
	}
	
	/**
	 * 根据按键的状态设定坦克的方向。
	 */
	private void LocalDirection() {
		if (!bL && !bU && !bR && !bD)
			dir = Direction.STOP;
		else {
			if (bL && !bU && !bR && !bD) {
				dir = Direction.L;
			} else if (bL && bU && !bR && !bD) {
				dir = Direction.LU;
			} else if (!bL && bU && !bR && !bD) {
				dir = Direction.U; 
			} else if (!bL && bU && bR && !bD) {
				dir = Direction.RU; 
			} else if (!bL && !bU && bR && !bD) {
				dir = Direction.R; 
			} else if (!bL && !bU && bR && bD) {
				dir = Direction.RD;
			} else if (!bL && !bU && !bR && bD) {
				dir = Direction.D; 
			} else if (bL && !bU && !bR && bD) {
				dir = Direction.LD; 
			}	
		}
		
		if(dir != Direction.STOP){
			ptDir = dir;
		}
		

	}
	
	private class MakeBullet implements Runnable{
		public void run(){
			while (true) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BulletNum = 0;
				if(!bePlayerTank && live){
					if(r.nextInt(40) > 30)
					fire();
				}
				
			}
		}
	}
	
	public void setLive(boolean live){
 		this.live = live;
	}
	
	public Rectangle getRect(){
   	  return new Rectangle(tank_x, tank_y,tankImages[0].getWidth(null), tankImages[0].getHeight(null));
     }
	 
	/**
	 * 判断当前坦克是否碰撞到了其他坦克。
	 * @return boolean true则碰到了。
	 */
	public boolean isMeetTanks() {
		for (int a = 0; a < tc.tanks.size(); a++) {
			TankImpl t = tc.tanks.get(a);
			if (!this.bePlayerTank) {
				if (t != this) {
					if (this.getRect().intersects(t.getRect()) || this.getRect().intersects(tc.myTank.getRect())) {
						return true;
					}
				}
			} else {
				if (this.getRect().intersects(t.getRect())) {
					return true;
				}
			}

		}
		return false;
	}
    
	/**
	 * 判断当前坦克是否碰撞到了墙。
	 * @return boolean true则碰到了。
	 */
    public boolean isMeetWall(){
    	if(this.getRect().intersects(tc.wall.getRect())){
    		return true;
    	}
    	return false;
    }
}
