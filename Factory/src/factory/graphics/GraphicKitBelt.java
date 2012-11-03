package factory.graphics;

import java.awt.Color;
import java.awt.Graphics;


public class GraphicKitBelt {
	
	/*GraphicKitBelt.java (220x600)
	 * This displays and animates the conveyer belts,
	 * as well as any kits entering/exiting the factory (1 of each)
	 */
	
	private int x, y; //Coordinates where belt is to be drawn
	private int t; //Time for animation purposes
	private GraphicKit kitIn; //The Kit entering the factory
	private GraphicKit kitOut; //The Kit exiting the factory
	private boolean pickUp; //When a Kit has arrived
	
	public GraphicKitBelt(int m, int n) {
		//Constructor
		x = m;
		y = n;
		t = 0;
		pickUp = false;
		kitIn = null;
		kitOut = null;
	}
	
	public void paint(Graphics g) {
		//Paints the conveyer belt
		
		//Main belt
		g.setColor(new Color(47, 41, 32));
		g.fillRect(x, y, 100, 600);
		
		//Side exit belt
		g.fillRect(x+120, y+300, 100, 100);
		g.fillRect(x+100, y+400, 20, 100);
		g.fillArc(x+20, y+300, 200, 200, 270, 90);
		
		//Lines to animate
		//Main conveyer belt
		g.setColor(new Color(224, 224, 205));
		for (int i = t; i < 600; i += 50) {
			g.drawLine(x, i, x+100, i);
		}
		
		//Side conveyer belts
		for (int i = t + 300; i < 400; i += 50)
			g.drawLine(x+120, i, x+220, i);  
		g.drawLine(x+120, y+400, x+120+(int)(100*Math.cos(3.14*t/100)), y+400+(int)(100*Math.sin(3.14*t/100)));
		if (t < 20)
			g.drawLine(x+120-t, y+400, x+120-t, y+500);
		
		//The diagonal to push kit into ready station
		g.setColor(new Color(27, 21, 12));
		for (int i = 0; i < 5; i++)
			g.drawLine(x, 90+i, x+100, 190+i);
		
		//The ready station for a robot to take
		g.setColor(new Color(10, 5, 0));
		g.fillRoundRect(x+100, y+110, 50, 100, 20, 20);
		g.fillRect(x+100, y+110, 20, 100);
		
		//Draws the kit moving into the factory
		if (kitin())
			drawKitIn(g);
		
		//Draws the kit moving out of the factory
		if (kitout())
			drawKitOut(g);
	}
	
	public void inKit() {
		//Has a new kit enter the factory
		kitIn = new GraphicKit(x+25, x-80);
		pickUp = false;
	}
	
	public void outKit() {
		//Has a kit exit the factory
		kitOut = new GraphicKit(x+150, y+300);
	}
	
	public void drawKitIn(Graphics g) {
		//Draws the kit that's entering the factory
		kitIn.paint(g);
	}
	
	public void drawKitOut(Graphics g) {
		//Draws the kit that's exiting the factory
		kitOut.paint(g);
	}
	
	public void moveBelt(int v) {
		//Increments t for animation
		
		t += v;
		if (t >= 50)
			t = 0;
		
		//Moves the incoming kit along a path
		if (kitin()) {
			if (kitIn.getY() <= y+115)
			kitIn.move(v);
			if (kitIn.getY() >= y+40 && kitIn.getX() <= x+100)
				kitIn.moveX(v);
			if (kitIn.getX() >= x+105 && kitIn.getY() >= y+115)
				pickUp = true;
			if (kitIn.getY() >= y+600)
				kitIn = null;
		}
		
		//Moves the outgoing kit along a path
		if (kitout()) {
			if (kitOut.getY() <= y+370)
				kitOut.move(v);
			else if (kitOut.getY() <= y+400){
				kitOut.moveX(-v);
				kitOut.move(v);
			}
			else if (kitOut.getX() > x+80)
				kitOut.moveX(-v);
			else if (kitOut.getX() > x+25) {
				kitOut.moveX(-v);
				kitOut.move(v);
			}
			else
				kitOut.move(v);
			
			if (kitOut.getY() >= y+600)
				kitOut = null;
		}
	}
	
	public boolean kitin() {
		//Returns if a kit is entering the factory
		return kitIn != null;
	}
	
	public boolean kitout() {
		//Returns if a kit is exiting the factory
		return kitOut != null;
	}
	
	public boolean pickUp() {
		//Returns if a kit that's entering the factory is ready to be picked up
		return pickUp;
	}
	
}