package factory.graphics;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GraphicKitAssemblyManager extends JPanel implements ActionListener{
	
	/*GraphicKitAssemblyManager.java (600x600) - Tobias Lee
	 * This is the graphical display of the Kit Assembly Manager
	 * Currently, this displays a conveyer belt and its kits, and a kitting station
	 * and animates the two.
	 * 
	 * TODO: (��)
	 * 		[��] Rework GraphicKittingStation to hold ArrayList of GraphicKits 
	 * 			>Give them positions<, or have them slide down to the bottom?
	 * 				Second option is easier, but no way of confirming that bottom-most kit will be filled first. If Robot needs to target completed kits, then first option is just as viable
	 * 		[��] Update GraphicKittingRobot movement to add kit to GraphicKittingStation
	 * 		[��] Add functionality of GraphicKittingRobot to move from kitting station back to belt
	 * 		[ ] Create GraphicItems class to display rudimentary items
	 * 		[ ] Rework GraphicKit to be able to hold GraphicItems
	 * 		[ ] Rotation of GraphicKit
	 * 		[ ] Rework GraphicKittingRobot to actually hold kits rather than using an image with a blank kit
	 * 		[ ] Add functionality of GraphicKittingStation to randomly add items to a kit
	 * 		[ ] Create buttons for each potential command in ControlPanel
	 * 		[ ] Update GraphicKittingStation so that the positions of the kits are not as autistic
	 * 				GraphicKittingRobot must be able to pick which kit it removes
	 * 		[X] ROBOT COMMANDS ROBOT COMMANDS ROBOT COMMANDS ROBOT COMMANDS ROBOT COMMANDS ROBOT COMMANDS ROBOT COMMANDS
	 * 				It can take 3 kits from the station at once...
	 * 				TOO MANY BRUTE FORCE SOLUTIONS. NEED ELEGANCE
	 * 				Queue of Command Strings would be kinda cool
	 * 				!- HANDLED BY 201 TEAM
	 * 		{ } (Potentially) have GraphicKittingRobot know where to move to put items where they belong on GraphicKittingStation, instead of moving to a fixed spot
	 * 		{ } (Perhaps) Create a generic moveTo(int x, int y) function for GraphicKittingRobot to move to a location
	 * 
	 * CURRENT ISSUES:
	 * 		[X] Hierarchy of commands: Robot will prioritize getting new kit from belt above all else
	 * 			- HARDFIXED FOR NOW. FIX LATER
	 * 			- HANDLE WHAT TO DO WHEN TOO MANY KITS ARE CIRCULATING (4TH KIT FROM BELT TRANSFERRING TO STATION)
	 * 			Are hardfixes really a bad thing?
	 * 			!- HANDLED BY 201 TEAM
	 */
	
	//int x; //This was just for testing purposes, uncomment the x-related lines to watch a square move along a sin path
	private FrameKitAssemblyManager am; //The JFrame that holds this. Will be removed when gets integrated with the rest of the project
	private GraphicKitBelt belt; //The conveyer belt
	private GraphicKittingStation station; //The kitting station
	private GraphicKittingRobot robot;
	private boolean fromBelt;
	private boolean toStation;
	private boolean fromStation;
	private boolean toBelt;
	
	public GraphicKitAssemblyManager(FrameKitAssemblyManager FKAM) {
		//Constructor
		//x = 0;
		am = FKAM;
		belt = new GraphicKitBelt(0, 0);
		station = new GraphicKittingStation(400, 130);
		robot = new GraphicKittingRobot(this, 250, 150);
		fromBelt = false;
		toStation = false;
		fromStation = false;
		toBelt = false;
		(new Timer(50, this)).start();
		this.setPreferredSize(new Dimension(600, 600));
	}
	
	public void addInKit() {
		//Adds a kit into the factory via conveyer belt
		if (belt.kitin())
			return;
		belt.inKit();
	}
	
	public void addOutKit() {
		//Sends a kit out of the factory via conveyer belt
		/*if (belt.kitout())
			return;
		belt.outKit(new GraphicKit(150, 300));*/
		if (station.hasKits() && !robot.kitted())
			fromStation = true;
	}
	
	public void robotFromBelt() {
		//Sends robot to pick up kit from belt
		if (belt.pickUp() && !robot.kitted() && !station.maxed())
			fromBelt = true;
	}
	
	public void paint(Graphics g) {
		//Paints all the objects
		g.setColor(new Color(200, 200, 200));
		g.fillRect(0, 0, 600, 600);
		//g.setColor(Color.black);
		//g.fillRect(x, (int)(20*Math.sin(x/90.0*3.1415))+100, 5, 5);
		belt.paint(g);
		station.paint(g);
		robot.paint(g);
		
		belt.moveBelt(5);
		moveRobot();
		//x += 1;
	}
	
	public void moveRobot() {
		//Moving path control into separate method
		
		//robotFromBelt();
		
		if (fromBelt) {
			if (robot.moveFromBelt(5)) {
				fromBelt = false;
				robot.setKit(belt.unKitIn());
				toStation = true;
			}
		}
		else if (toStation) {
			if (robot.moveToStation(5)) {
				toStation = false;
				station.addKit(robot.unkit());
				am.fromBeltDone();
			}
		}
		else if (fromStation) {
			if (robot.moveFromStation(5)) {
				fromStation = false;
				robot.setKit(station.popKit(0));
				toBelt = true;
			}
		}
		else if (toBelt) {
			if (robot.moveToBelt(5)) {
				toBelt = false;
				belt.outKit(robot.unkit());
				am.outKitDone();
			}
		}
		else
			robot.moveToStartX(5);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		//etc.
		repaint();
	}
	
	public boolean getFromBelt() {
		return fromBelt;
	}
	
	public boolean getToStation() {
		return toStation;
	}
	
	public boolean getFromStation() {
		return fromStation;
	}
	
	public boolean getToBelt() {
		return toBelt;
	}

}
