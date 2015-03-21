package tankWar;
import java.awt.Color;
import java.awt.Graphics;

/**
 * 坦克的生命值
 * @author Magy
 *
 */
public class TankBlood {
	   private Tank t ;
	   private TankClient tc;
	   
	  
       public TankBlood(Tank t, TankClient tc){
    	   this.t =t;
    	   this.tc = tc;
       }
       
	public void drawMe(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		g.fillRect(t.tank_x, t.tank_y -15, 50, 10);
		g.setColor(Color.GREEN);
		g.fillRect(t.tank_x, t.tank_y - 15, this.t.life * 5, 10);
		g.setColor(Color.white);
	    g.drawRect(t.tank_x, t.tank_y -15, 50, 10);
		g.setColor(c);
	}
}
