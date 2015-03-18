import java.awt.Color;
import java.awt.Graphics;


public class Explode {
      private int explode_x, explode_y;
      Bullet b;
      TankClient tc;
      int[] diament = {3, 13, 15, 18, 25, 28, 35, 20, 18, 13, 5, 2};
      int step = 0;
      private boolean live = true;
    		  
      public Explode(Bullet b, TankClient tc){
    	  explode_x = b.bullet_x;
    	  explode_y = b.bullet_y;
    	  this.b = b;
    	  this.tc = tc;
      }
      
      public void drawMe(Graphics g){
    	  if(!live) return;
    	  //Transfome the Chinaese into English
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
