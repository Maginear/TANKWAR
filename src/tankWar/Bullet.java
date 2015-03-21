package tankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * ̹�˷������ڵ���
 * @author lx
 *
 */
public class Bullet implements Runnable {
	/**
	 * �����ڵ���̹�ˡ�
	 */
	public Tank tank;
	
	/**
	 * �����ڵ���̹�˵ķ��� ��
	 */
	public Tank.Direction dir;
	
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
	
	private boolean live = true;
	private Explode exp;
	enum Direction { L, LU, U, RU, R, RD, D, LD, STOP};
	int hitwall = 0;
	int hittank = 0;
	int outofframe = 0;
	
   /**
    * ʵ�����ڵ�����ʼ��λ�úͷ���
    * @param tank        �����ڵ���̹��
    * @param tc          ��Ϸ����
    * @param dir    ����
    */
	public Bullet(Tank tank, TankClient tc, Tank.Direction dir) {
		this.dir = dir;	
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
			bulletMove();
		}
	}

	/**
	 * ���ݷ����ƶ��ڵ������ڵ����뿪���ڷ�Χ�򽫴��ڵ��Ƴ�Bullets���ϣ��������ػ档
	 */
	private void bulletMove() {
		switch (dir) {
		case L:
			bullet_x -= SPEED;
			break;
		case LU:
			bullet_x -= SPEED;
			bullet_y -= SPEED;
			break;
		case U:
			bullet_y -= SPEED;
			break;
		case RU:
			bullet_x += SPEED;
			bullet_y -= SPEED;
			break;
		case R:
			bullet_x += SPEED;
			break;
		case RD:
			bullet_x += SPEED;
			bullet_y += SPEED;
			break;
		case D:
			bullet_y += SPEED;
			break;
		case LD:
			bullet_x -= SPEED;
			bullet_y += SPEED;
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
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.red);
			}
			g.fillOval(bullet_x + Tank.WIDTH / 2, bullet_y + Tank.HEIGHT / 2,
					WIDTH, HEIGHT);
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
	private boolean isHitTank(Tank t) {
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
