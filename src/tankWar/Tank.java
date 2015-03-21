package tankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;


public class Tank {
	
	/**
	 * ̹�˵�X����
	 */
    public int tank_x = 50;
    

	/**
	 * ̹�˵�X����
	 */
    public int tank_y = 50;
    public int old_x = 50; 
    public int old_y = 50; 
    
	/**
	 * ̹�˵Ŀ�ȳ���
	 */
    public static final int WIDTH = 50;

    /**
	 * ̹�˵ĸ߶ȳ���
	 */
    public static final int HEIGHT = 50;
    
    /**
	 * ̹�˵��ƶ��ٶȳ���
	 */
    private static final int SPEED = 5;
    private int BulletNum;
    private int step = 0; 
    public boolean bePlayerTank; 
    private boolean live = true;
    public int life = 10;
static int bulletTest = 0;
    
    private boolean bL = false, bU = false, bR = false, bD = false;    //��̹�˵ķ�����ö�ٵķ�ʽ�γɼ��ϣ����������淶ʹ�á�
    enum Direction { L, LU, U, RU, R, RD, D, LD, STOP};
    
    Direction dir = Direction.STOP;
    Direction ptDir = Direction.R;
    
    ArrayList<Integer> oldKey = new ArrayList<Integer>();
  
    private static Random r = new Random(); 
    public TankClient tc;
    private boolean down = false;
    TankBlood tb;
	
    /**
     * ��ʼ��̹�˵�λ�ã�����ʼ����̹�˷����ڵ�Ƶ�ʵ��߳�MakeBullets
     * @param tank_x ̹��X��λ��
     * @param tank_y ̹��X��λ��
     * @param bePlayerTank �Ƿ�Ϊ���̹��
     * @param tc   ��Ϸ����
     */
	public Tank(int tank_x, int tank_y, boolean bePlayerTank, TankClient tc) {
		this.tank_x = tank_x;
		this.tank_y = tank_y;
		this.bePlayerTank = bePlayerTank;
		this.tc = tc;
		new Thread(new MakeBullet()).start();
	}
	
	/**
	 * 1��Ϊÿ��̹��Newһ��Ѫ����
	 * 2������ǰ̹�˻����ţ�live =true����������Ƿ������̹�˻���������ɫ��̹�˼�����Ͳ,����̹��ǰ��һ����
	 * ���򽫴��ڵ��Ƴ�Tanks���ϣ��������ػ档
	 * @param g ����
	 */
	public void drawMe(Graphics g){
		if (tb == null) {
			tb = new TankBlood(this, tc);
			tc.TankBloods.add(tb);
		}
		
		if(live) {
	     	Color c = g.getColor();
		    
		    if(bePlayerTank)
		    	g.setColor(Color.RED);
	     	else
	     		g.setColor(Color.blue);
		
		    g.fillOval(tank_x,tank_y, WIDTH, HEIGHT);
	     	g.setColor(c);
		
		switch(ptDir){
		case L:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x , tank_y + Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x , tank_y);
			break;
		case U:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x + Tank.WIDTH/2, tank_y);
			break;
		case RU:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x + Tank.WIDTH, tank_y);
			break;
		case R:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x + Tank.WIDTH , tank_y + Tank.HEIGHT/2);
			break;
		case RD:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x + Tank.WIDTH , tank_y + Tank.HEIGHT);
			break;
		case D:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x + Tank.WIDTH/2 , tank_y + Tank.HEIGHT);
			break;
		case LD:
			g.drawLine(tank_x + Tank.WIDTH/2, tank_y + Tank.HEIGHT/2 , tank_x , tank_y + Tank.HEIGHT);
			break;
	
		}
		move();
	}
		else 	{
			tc.TankBloods.remove(tb);
			tc.tanks.remove(this);
		}
}	
	/**
	 * ����̹�˵ķ�����̹���ڴ��ڱ߽��ڲ���������̹�ˡ�ǽ������ײ��ǰ����ǰ����
	 */
	private void move(){
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
	 * �ڰ������ͷź������趨̹�˷���ʵ�����������ͬʱ���µ�Ч����������ȫ�ͷ�ʱ�������趨ΪSTOP��
	 * @param e �����¼��¼�
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
	 * �ڰ���������ʱ���ı䷽��(dir)�������Ӧ�ķ�����
	 * @param e  �����¼�
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
					tc.myTank = new Tank(40, 100, true, tc);
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
	 * ��̹�˵���Ͳ����ptDir������һ���ڵ���
	 */
	private void fire() {
		if (live && BulletNum <= 2) {
			Bullet b = new Bullet(this, tc, ptDir);
			BulletNum++;
			new Thread(b).start();
			tc.bullets.add(b);
		}
	}
	
	public void fire(Direction dire){
		Bullet b = new Bullet(this, tc, dire);
		new Thread(b).start();
		tc.bullets.add(b);
//		System.out.println(tc.bullets.size());
//		System.out.println("BulletTest : "+bulletTest);
		bulletTest++;
	}
	
	/**
	 * ����һ�ΰ˸�����ĳ����ڵ���
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
	 * ���ݰ�����״̬�趨̹�˵ķ���
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
   	  return new Rectangle(tank_x, tank_y, WIDTH, HEIGHT);
     }
	 
	/**
	 * �жϵ�ǰ̹���Ƿ���ײ��������̹�ˡ�
	 * @return boolean true�������ˡ�
	 */
	private boolean isMeetTanks() {
		for (int a = 0; a < tc.tanks.size(); a++) {
			Tank t = tc.tanks.get(a);
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
	 * �жϵ�ǰ̹���Ƿ���ײ����ǽ��
	 * @return boolean true�������ˡ�
	 */
    private boolean isMeetWall(){
    	if(this.getRect().intersects(tc.wall.getRect())){
    		return true;
    	}
    	return false;
    }
}
