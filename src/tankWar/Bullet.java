package tankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 坦克发出的炮弹。
 * @author lx
 *
 */
public class Bullet implements Runnable {
	/**
	 * 发出炮弹的坦克。
	 */
	public Tank tank;
	
	/**
	 * 发出炮弹的坦克的方向 。
	 */
	public Tank.Direction dir;
	
	public TankClient tc;
	
	/**
	 * 炮弹的X轴坐标。
	 */
	public int bullet_x;
	
	/**
	 * 炮弹的Y轴坐标
	 */
	public int bullet_y;
	
	/**
	 * 炮弹的宽度
	 */
	public static final int WIDTH = 10;
	
	/**
	 * 炮弹的高度
	 */
	public static final int HEIGHT = 10;
	
	/**
	 * 炮弹移动的速度
	 */
	private static final int SPEED = 3;
	
	private boolean live = true;
	private Explode exp;
	enum Direction { L, LU, U, RU, R, RD, D, LD, STOP};
	int hitwall = 0;
	int hittank = 0;
	int outofframe = 0;
	
   /**
    * 实例化炮弹，初始化位置和方向。
    * @param tank        产生炮弹的坦克
    * @param tc          游戏窗口
    * @param dir    方向
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
	 * 根据方向移动炮弹，若炮弹已离开窗口范围则将此炮弹移除Bullets集合，并不再重绘。
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
	 * 判断是否击中某一特定坦克Tank，对于玩家坦克击中一次则扣除其生命值2点，对于敌方AI坦克则击中一次扣除其生命值5点。
	 * @param t         被测试坦克。
	 * @return boolean  返回true则打中了。
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
     * 判断炮弹是否击中任意坦克，若击中则产生爆炸。然后将此炮弹移除Bullets集合，并不再重绘。
     * 同时判断被击中坦克是否生命值小于等于0，若是则将其生命live（boolean）设为false。
     * @return boolean  返回true则打中了。
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
//System.out.println("打中敵方坦克 " + hittank);}
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
//System.out.println("打中玩家坦克 ：  " + tank.bulletTest);}
				exp = new Explode(this, tc);
				tc.explodes.add(exp);
				return true;
			}
			return false;
		}
	}
	
	/**
	 * 判断炮弹是否击中墙面，若击中则产生爆炸。然后将此炮弹移除Bullet集合，并不再重绘。
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
