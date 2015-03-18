import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Bullet implements Runnable{
	  Tank tank;
	  Tank.Direction dir;
	  TankClient tc;
	  public int bullet_x;
	  public int bullet_y;
	  public static final int WIDTH  = 10;
	  public static final int HEIGHT = 10;
	  private static final int SPEED = 3;
	  private boolean live = true;
	  private Explode exp;
	  static int i = 0;	  
	  public Bullet(Tank tank, TankClient tc){
		  this.dir = tank.ptDir;
		  this.tank = tank;
		  this.tc = tc;
		  bullet_x = tank.tank_x;
		  bullet_y = tank.tank_y;
	  }
      
	  public void run(){
		  while (live) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			bulletMove();
		}
	  }
	  
	  public void bulletMove(){
			switch(dir){
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
			
			if(bullet_x < 0 || bullet_y < 0 || bullet_x > TankClient.frameWidth || bullet_y > TankClient.frameHeight){
				live = false;
				tank.tc.bullets.remove(this);
			}
	  }
	  
	void drawMe(Graphics g) {
        this.isHitTanks();
    
		if(live) {

			Color c = g.getColor();
			g.setColor(Color.RED);
			g.fillOval(bullet_x + Tank.WIDTH / 2, bullet_y + Tank.HEIGHT / 2,
					WIDTH, HEIGHT);
			g.setColor(c);
		}
		else{
			exp.drawMe(g);
			System.out.println(i++);
		}
		
	}
      
      public Rectangle getRect(){
    	  return new Rectangle(bullet_x, bullet_y, WIDTH, HEIGHT);
      }
      
      
	public boolean isHitTank(Tank t) {
    
		if (this.getRect().intersects(t.getRect())) {
			t.setLive(false);
			return true;
		}
		return false;
	}
	
	public boolean isHitTanks() {
		if (tank.bePlayerTank) {
			for (int a = 0; a < tc.tanks.size(); a++) {
				if (this.isHitTank(tc.tanks.get(a))) {
					this.live = false;
					tank.tc.bullets.remove(this);
					exp = new Explode(this, tc);
					tc.explodes.add(exp);
					return true;
				}
				
			}
			return false;
		}
		else {
			if (this.getRect().intersects(tc.myTank.getRect())) {
				tc.myTank.setLive(false);
				return true;
			}
			return false;
		}
	}
}
