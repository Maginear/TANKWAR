package tankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * ̹�˷������ڵ���
 * @author lx
 *
 */
public class BulletImpl implements Runnable, MoveAndDrawInterface {
	/**
	 * �����ڵ���̹�ˡ�
	 */
	public TankImpl tank;
	
	/**
	 * �����ڵ���̹�˵ķ��� ��
	 */
	public Direction ptDir;
	
	public TankClient tc;
	
	/**
	 * �ڵ���X�����ꡣ
	 */
	public int bullet_x;
	
	/**
	 * �ڵ���Y������
	 */
	public int bullet_y;
	
	/**
	 * �ڵ��Ŀ��
	 */
	public static final int WIDTH = 10;
	
	/**
	 * �ڵ��ĸ߶�
	 */
	public static final int HEIGHT = 10;
	
	/**
	 * �ڵ��ƶ����ٶ�
	 */
	private static final int SPEED = 3;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] imgs = {
		tk.getImage(BulletImpl.class.getClassLoader().getResource("images/missileL.gif")),
		tk.getImage(BulletImpl.class.getClassLoader().getResource("images/missileLU.gif")),
		tk.getImage(BulletImpl.class.getClassLoader().getResource("images/missileU.gif")),
		tk.getImage(BulletImpl.class.getClassLoader().getResource("images/missileRU.gif")),
		tk.getImage(BulletImpl.class.getClassLoader().getResource("images/missileR.gif")),
		tk.getImage(BulletImpl.class.getClassLoader().getResource("images/missileRD.gif")),
		tk.getImage(BulletImpl.class.getClassLoader().getResource("images/missileD.gif")),
		tk.getImage(BulletImpl.class.getClassLoader().getResource("images/missileLD.gif")),
	};
	private int bulletImages = 0;
	
	private boolean live = true;
	private Explode exp;
	int hitwall = 0;
	int hittank = 0;
	int outofframe = 0;
	
   /**
    * ʵ�����ڵ�����ʼ��λ�úͷ���
    * @param tank        �����ڵ���̹��
    * @param tc          ��Ϸ����
    * @param dir    ����
    */
	public BulletImpl(TankImpl tank, TankClient tc, Direction ptDir) {
		this.ptDir = ptDir;	
		this.tank = tank;
		this.tc = tc;
		bullet_x = tank.tank_x;
		bullet_y = tank.tank_y;
	}


	public void run() {
		while (live) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			move();
		}
	}

	/**
	 * ���ݷ����ƶ��ڵ������ڵ����뿪���ڷ�Χ�򽫴��ڵ��Ƴ�Bullets���ϣ��������ػ档
	 */
	public void move() {
		switch (ptDir) {
		case L:
			bullet_x -= SPEED;
			bulletImages = 0; 
			break;
		case LU:
			bullet_x -= SPEED;
			bullet_y -= SPEED;
			bulletImages = 1;
			break;
		case U:
			bullet_y -= SPEED;
			bulletImages = 2;
			break;
		case RU:
			bullet_x += SPEED;
			bullet_y -= SPEED;
			bulletImages = 3;
			break;
		case R:
			bullet_x += SPEED;
			bulletImages = 4;
			break;
		case RD:
			bullet_x += SPEED;
			bullet_y += SPEED;
			bulletImages = 5;
			break;
		case D:
			bullet_y += SPEED;
			bulletImages = 6;
			break;
		case LD:
			bullet_x -= SPEED;
			bullet_y += SPEED;
			bulletImages = 7;
			break;
		case STOP:
			break;
		}

		if (bullet_x <= 0 || bullet_y <= 0 || bullet_x >= TankClient.frameWidth
				|| bullet_y >= TankClient.frameHeight) {
			live = false;
//if(tank.bePlayerTank) {  outofframe++;
//System.out.println("OutOfFrame : " + outofframe);}
			tc.bullets.remove(this);
			
		}
	}

	public void drawMe(Graphics g) {
		this.isHitTanks();
        this.isHitWall();
		if (live) {
			Color c = g.getColor();
			if (tank.bePlayerTank) {
				g.drawImage(imgs[bulletImages], bullet_x + TankImpl.WIDTH / 2 , 
						bullet_y + TankImpl.HEIGHT / 2  , null);
			} else {
				g.setColor(Color.red);
				g.fillOval(bullet_x + TankImpl.WIDTH / 2, bullet_y + TankImpl.HEIGHT / 2,
						WIDTH, HEIGHT);
			}
			
			g.setColor(c);
		} 
	}

	private Rectangle getRect() {
		return new Rectangle(bullet_x, bullet_y, WIDTH, HEIGHT);
	}

	/**
	 * �ж��Ƿ����ĳһ�ض�̹��Tank���������̹�˻���һ����۳�������ֵ2�㣬���ڵз�AI̹�������һ�ο۳�������ֵ5�㡣
	 * @param t         ������̹�ˡ�
	 * @return boolean  ����true������ˡ�
	 */
	private boolean isHitTank(TankImpl t) {
		if (this.getRect().intersects(t.getRect())) {
			if(tank.bePlayerTank)
			    t.life -= 5;
			else
				t.life -= 2; 
			return true;
		}
		return false;
	}

    /**
     * �ж��ڵ��Ƿ��������̹�ˣ��������������ը��Ȼ�󽫴��ڵ��Ƴ�Bullets���ϣ��������ػ档
     * ͬʱ�жϱ�����̹���Ƿ�����ֵС�ڵ���0��������������live��boolean����Ϊfalse��
     * @return boolean  ����true������ˡ�
     */
	private boolean isHitTanks() {
		if (tank.bePlayerTank) {
			for (int a = 0; a < tc.tanks.size(); a++) {
				if (this.isHitTank(tc.tanks.get(a))) {
					if(tc.tanks.get(a).life <= 0){
						tc.tanks.get(a).setLive(false);
					}
					this.live = false;
					tc.bullets.remove(this);
//if(tank.bePlayerTank)  { hittank--;
//System.out.println("���Д���̹�� " + hittank);}
					exp = new Explode(this, tc);
					tc.explodes.add(exp);
					return true;
				}
			}
			return false;
		} else {
			if (this.isHitTank(tc.myTank)) {
				if(tc.myTank.life <= 0){
					tc.myTank.setLive(false);
				}
				this.live = false;
				tc.bullets.remove(this);
//if(tank.bePlayerTank)  { tank.bulletTest--;
//System.out.println("�������̹�� ��  " + tank.bulletTest);}
				exp = new Explode(this, tc);
				tc.explodes.add(exp);
				return true;
			}
			return false;
		}
	}
	
	/**
	 * �ж��ڵ��Ƿ����ǽ�棬�������������ը��Ȼ�󽫴��ڵ��Ƴ�Bullet���ϣ��������ػ档
	 * @return boolean
	 */
	public boolean isHitWall(){
		if(this.getRect().intersects(tc.wall.getRect())){
			this.live = false;
			tc.bullets.remove(this);
//if(tank.bePlayerTank)  { hitwall--;
//System.out.println("hit wall: " + hitwall);}
			exp = new Explode(this, tc);
			tc.explodes.add(exp);
			return true;
		}
		return false;
	}
}
