package tankWar;
import java.awt.Color;
import java.awt.Graphics;

    /**
     * 炮弹（Bullet）打中坦克或墙的时候产生的爆炸（Explode）。
     * @author Magy
     *
     */
public class Explode {
	  
	  /**
	   * 爆炸动画绘制原点的坐标。
	   */
      private int explode_x, explode_y;
      
      private Bullet b;
      private TankClient tc;
      
      /**
       * 爆炸动画直径大小变化的数组。
       */
      int[] diament = {3, 13, 15, 18, 25, 35, 20, 18, 13, 5, 2};
      
      private int step = 0;
      private boolean live = true;
      
      /**
       * 初始化爆炸的位置。
       * @param b   产生爆炸的炮弹
       * @param tc  游戏窗口
       */
      public Explode(Bullet b, TankClient tc){
    	  explode_x = b.bullet_x;
    	  explode_y = b.bullet_y;
    	  this.b = b;
    	  this.tc = tc;
      }
      
      /**
       * 如果爆炸还没结束，绘制爆炸动画。
       * @param g  画笔
       */
      public void drawMe(Graphics g){
    	  if(!live) return;
    	  
    	  if(step == diament.length){
    		  live = false;
    		  tc.explodes.remove(this);
    		  return;
    	  }
    	  
    	  Color c = g.getColor();
    	  g.setColor(Color.BLACK);
    	  g.fillOval(explode_x + diament[step]/2, explode_y, diament[step], diament[step]);
    	  g.setColor(c);
    	  step++;
      }
}
