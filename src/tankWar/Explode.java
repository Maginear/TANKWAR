package tankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

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
      
      private BulletImpl b;
      private TankClient tc;
      
      private static Toolkit tk = Toolkit.getDefaultToolkit();
      
      private static  Image[] imgs = {
    	 tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
    	 tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
    	 tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
    	 tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
    	 tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
    	 tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
    	 tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
    	 tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
    	 tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif"))
      };
      
      /**
       * 爆炸动画直径大小变化的数组。
       */
      private int step = 0;
      private boolean live = true;
      
      /**
       * 初始化爆炸的位置。
       * @param b   产生爆炸的炮弹
       * @param tc  游戏窗口
       */
      public Explode(BulletImpl b, TankClient tc){
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
    	  
    	  if(step == imgs.length){
    		  live = false;
    		  tc.explodes.remove(this);
    		  return;
    	  }
    	  g.drawImage( imgs[step], explode_x, explode_y, null);
    	  step++;
      }
}
