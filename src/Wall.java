import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Wall {
    private int wall_x;
    private int wall_y;
    private int wall_Width;
    private int wall_Height;
    
	TankClient tc;
	public Wall(TankClient tc, int x , int y, int width, int height){
		this.tc = tc;
		this.wall_x = x;
		this.wall_y = y;
		this.wall_Width = width;
		this.wall_Height = height;
	}
	
	public void drawMe(Graphics g){
		Color c = g.getColor();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(wall_x - 5, wall_y, wall_Width + 10, wall_Height);
		g.setColor(c);
		
	}
	
	public Rectangle getRect(){
	   	  return new Rectangle(wall_x - 10, wall_y, wall_Width + 10, wall_Height);
	     }
}
