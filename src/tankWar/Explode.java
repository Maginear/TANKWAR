package tankWar;
import java.awt.Color;
import java.awt.Graphics;

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
      
      private Bullet b;
      private TankClient tc;
      
      /**
       * ��ը����ֱ����С�仯�����顣
       */
      int[] diament = {3, 13, 15, 18, 25, 35, 20, 18, 13, 5, 2};
      
      private int step = 0;
      private boolean live = true;
      
      /**
       * ��ʼ����ը��λ�á�
       * @param b   ������ը���ڵ�
       * @param tc  ��Ϸ����
       */
      public Explode(Bullet b, TankClient tc){
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
