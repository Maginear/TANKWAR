package tankWar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

    /**
     * �ڵ���Bullet������̹�˻�ǽ��ʱ������ı�ը��Explode����
     * @author Magy
     *
     */
public class Explode {
	  
	  /**
	   * ��ը��������ԭ������ꡣ
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
       * ��ը����ֱ����С�仯�����顣
       */
      private int step = 0;
      private boolean live = true;
      
      /**
       * ��ʼ����ը��λ�á�
       * @param b   ������ը���ڵ�
       * @param tc  ��Ϸ����
       */
      public Explode(BulletImpl b, TankClient tc){
    	  explode_x = b.bullet_x;
    	  explode_y = b.bullet_y;
    	  this.b = b;
    	  this.tc = tc;
      }
      
      /**
       * �����ը��û���������Ʊ�ը������
       * @param g  ����
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
