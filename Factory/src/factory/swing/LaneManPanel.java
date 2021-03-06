//Stephanie Reagle, Joey Huang, Marc Mendiola
//CS 200
// last edited: 10:24 pm 11/28/12
package factory.swing;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import factory.managers.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class LaneManPanel extends JPanel{
	
		// ID number
	private static final long serialVersionUID = -4485912622490446254L;

// not needed?
	String [] stringLanes = { "1", "2", "3", "4", "5", "6", "7", "8"};
	JComboBox lane = new JComboBox(stringLanes);
	//ImageIcon red = new ImageIcon("red.png");
	JButton redButton = new JButton("Red");
	JButton yellowButton = new JButton("Yellow");
	JButton greenButton = new JButton("Green");
	JButton powerButton = new JButton("Power");





	LaneManager laneManager; // instance of lane manager this panel is contained in
	JTabbedPane tabbedPane; 
	JPanel preferencesPanel; // normative controls
	LaneNonNormPanel nonnormativePanel; // nonnormative controls

// constructor
	public LaneManPanel(LaneManager l){
		laneManager = l;

		preferencesPanel = new LanePreferencesPanel();
		nonnormativePanel = new LaneNonNormPanel();
		tabbedPane = new JTabbedPane();

		GridBagConstraints c = new GridBagConstraints();
		setLayout(new GridBagLayout());

		tabbedPane.addTab("Preferences", preferencesPanel);
		tabbedPane.addTab("Non-Normative", nonnormativePanel);

		this.add(tabbedPane);


	}

// not used?
	public int getSelectedLane(){
		return (Integer) lane.getSelectedItem();

	}



// normative controls panel 
// allows changing lane speed and amplitude, and lane/feeder power

	public class LanePreferencesPanel extends JPanel implements ActionListener{

		// labels
		JLabel title;
		JLabel laneLabel;
		JLabel laneSpeedLabel;
		JLabel laneAmplitudeLabel;
		JLabel lanePowerLabel;
		JLabel feederLabel;
		JLabel feederPowerLabel;

		//power switches
		JButton laneOn;
		JButton laneOff;
		JButton feederOn;
		JButton feederOff;

		// sliders and selectors
		JSlider laneSpeed;
		JSlider laneAmplitude;
		JComboBox laneSelect;
		JComboBox feederSelect;

		// containers
		JPanel feederSection;
		JPanel laneSection;

		//limits
		int speedMin;
		int speedMax;
		int amplitudeMin;
		int amplitudeMax;

		// constructor
		public LanePreferencesPanel(){

			speedMin = 1;
			speedMax = 8;
			amplitudeMin = 1;
			amplitudeMax = 8;
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			title = new JLabel("Lane Manager");
			title.setAlignmentX(Component.CENTER_ALIGNMENT);
			title.setFont(new Font("Serif", Font.BOLD, 16));
			laneLabel = new JLabel("Lane : ");
			lanePowerLabel = new JLabel("Power : ");
			laneSpeedLabel = new JLabel("Speed : "); 
			laneAmplitudeLabel = new JLabel("Amplitude : ");
			laneSpeed = new JSlider(speedMin, speedMax);
			laneSpeed.setMinorTickSpacing(1);
			laneSpeed.setMajorTickSpacing(7);
			laneSpeed.setPaintTicks(true);
			laneSpeed.setSnapToTicks(true);
			laneSpeed.setPaintLabels(true);
			laneSpeed.setValue(1);
			laneSpeed.addChangeListener(new SliderDetection());
			laneAmplitude = new JSlider(amplitudeMin, amplitudeMax);
			laneAmplitude.setMinorTickSpacing(1);
			laneAmplitude.setMajorTickSpacing(7);
			laneAmplitude.setPaintTicks(true);
			laneAmplitude.setSnapToTicks(true);
			laneAmplitude.setPaintLabels(true);
			laneAmplitude.setValue(1);
			laneAmplitude.addChangeListener(new SliderDetection());
			laneOn = new JButton("ON");
			laneOff = new JButton("OFF");
			laneOn.addActionListener(this);
			laneOff.addActionListener(this);
			laneSelect = new JComboBox();
			for(int i = 1; i <= 8; i++){
				laneSelect.addItem(i);
			}
			laneSelect.addActionListener(this);

			feederLabel = new JLabel("Feeder : ");
			feederSelect = new JComboBox();
			for(int i = 1; i <= 4; i++){
				feederSelect.addItem(i);
			}
			feederSelect.addActionListener(this);
			feederPowerLabel = new JLabel("Power : ");
			feederOn = new JButton("ON");
			feederOff = new JButton ("OFF");
			feederOn.addActionListener(this);
			feederOff.addActionListener(this);

			TitledBorder feederBorder = BorderFactory.createTitledBorder("Feeder Control");

			feederSection = new JPanel();
			feederSection.setLayout(new GridBagLayout());
			feederSection.setBorder(feederBorder);
			GridBagConstraints c = new GridBagConstraints();

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 0;
			feederSection.add(feederLabel, c);
			c.gridx = 1;
			c.gridy = 0;
			feederSection.add(feederSelect, c);
			c.gridx = 0;
			c.gridy = 1;
			feederSection.add(feederPowerLabel, c);
			c.gridx = 1;
			c.gridy = 1;
			feederSection.add(feederOn, c);
			c.gridx = 2;
			c.gridy = 1;
			feederSection.add(feederOff, c);


			TitledBorder laneBorder = BorderFactory.createTitledBorder("Lane Control");
			laneSection = new JPanel();
			laneSection.setBorder(laneBorder);
			laneSection.setLayout(new GridBagLayout());

			c.gridx = 0;
			c.gridy = 0;
			laneSection.add(laneLabel, c);
			c.gridx = 1;
			c.gridy = 0;
			laneSection.add(laneSelect, c);
			c.gridx = 0;
			c.gridy = 1;
			laneSection.add(lanePowerLabel, c);
			c.gridx = 1;
			c.gridy = 1;
			laneSection.add(laneOn, c);
			c.gridx = 2;
			c.gridy = 1;
			laneSection.add(laneOff, c);
			c.gridx = 0;
			c.gridy = 2;
			laneSection.add(laneSpeedLabel, c);
			c.gridwidth = 3;
			c.gridx = 1;
			c.gridy = 2;
			laneSection.add(laneSpeed, c);
			c.gridwidth = 1;
			c.gridx = 0;
			c.gridy = 3;
			laneSection.add(laneAmplitudeLabel, c);
			c.gridwidth = 3;
			c.gridx = 1;
			c.gridy = 3;
			laneSection.add(laneAmplitude, c);




			this.add(title);
			this.add(feederSection);
			this.add(laneSection);
		}

		@Override
		public void actionPerformed(ActionEvent ae) {
			// TODO Auto-generated method stub
			if(ae.getSource() == laneOn){ // turn lane one
				int lanenum = (Integer)laneSelect.getSelectedItem();
				String set = "lm lm set lanepower on " + (lanenum-1);
				try {
					laneManager.sendCommand(set);
				} catch (Exception e) {
					System.out.println("An error occurred trying to send message to power on lane " + lanenum + ".");
				} 
			}else if (ae.getSource() == laneOff){ // turn lane off
				int lanenum = (Integer)laneSelect.getSelectedItem();
				String set = "lm lm set lanepower off " + (lanenum-1);
				try {
					laneManager.sendCommand(set);
				} catch (Exception e) {
					System.out.println("An error occurred trying to send message to power off lane " + lanenum + ".");
				} 
			}else if (ae.getSource() == feederOn){ // turn feeder on
				int feedernum = (Integer)feederSelect.getSelectedItem();
				String set = "lm lm set feederpower on " + (feedernum-1);
				try {
					laneManager.sendCommand(set);
				} catch (Exception e) {
					System.out.println("An error occurred trying to send message to power on feeder " + feedernum + ".");
				} 
			}else if (ae.getSource() == feederOff){ // turn feeder off
				int feedernum = (Integer)feederSelect.getSelectedItem();
				String set = "lm lm set feederpower off " + (feedernum-1);
				try {
					laneManager.sendCommand(set);
				} catch (Exception e) {
					System.out.println("An error occurred trying to send message to power off feeder " + feedernum + ".");
				} 
			}else{  // updates speed and amplitude when JComboBox changes
				JComboBox cb = (JComboBox)ae.getSource();
				System.out.println("Get");
				if(cb == laneSelect){
					System.out.println(laneManager.getLaneSpeed((Integer)cb.getSelectedItem()));
					laneSpeed.setValue(laneManager.getLaneSpeed((Integer)cb.getSelectedItem()));
					laneAmplitude.setValue(laneManager.getLaneAmplitude((Integer)cb.getSelectedItem()));
				}
			}

		}

		public class SliderDetection implements ChangeListener{

			@Override
			public void stateChanged(ChangeEvent ce) {
				// TODO Auto-generated method stub
				JSlider source = (JSlider) ce.getSource();
				if(source == laneSpeed){
					if (!source.getValueIsAdjusting()) {
						int speed = (int)source.getValue();
						// send speed to server
						int lanenum = (Integer)laneSelect.getSelectedItem();
						String set = "lm lm set lanespeed " + (lanenum-1) + " " + speed;
						try {
							laneManager.sendCommand(set);
						} catch (Exception e) {
							System.out.println("An error occurred trying to send message to change lanespeed.");
						} 
						System.out.println("lane speed : " + speed);
					}
				}else if(source == laneAmplitude){
					if (!source.getValueIsAdjusting()) {
						int amplitude = (int)source.getValue();
						// send amplitude to server
						int lanenum = (Integer)laneSelect.getSelectedItem();
						String set = "lm lm set guilaneamplitude " + (lanenum-1) + " " + amplitude;
						try {
							laneManager.sendCommand(set);
						} catch (Exception e) {
							System.out.println("An error occurred trying to send message to change lane amplitude.");
						} 
						System.out.println("lane amplitude : " + amplitude);
					}
				}
			}

		}

	}

	public class LaneNonNormPanel extends JPanel implements ActionListener { // Panel containing non-normative case controls
		//labels
		JLabel badPartsLabel; 
		JLabel diverterSpeedLabel;

		// selectors
		JComboBox laneBoxList;
		JComboBox feederBoxList;

		// containers
		JPanel laneContainer;
		JPanel feederContainer;

		// components / controls
		JButton laneJamButton; // initiates non-normative 2.2: Lane Jam
		JButton diverterButton; // initiates non-normative 2.6: slower diverter
		JButton badPartsButton; // initiates non-normative 3.1:
		JButton blockingRobotButton; // initiates non-normative 3.
		JTextArea messageBox;
		JSlider badPartsPercentage;
		int badPartsPercentageMin;
		int badPartsPercentageMax;
		JSlider diverterSpeed;
		int diverterSpeedMin;
		int diverterSpeedMax;

		public LaneNonNormPanel() { // constructor
			
			badPartsLabel = new JLabel("% Bad Parts");
			laneJamButton = new JButton("Lane Jam");
			diverterButton = new JButton("Diverter Too Slow");
			badPartsButton = new JButton("Bad Parts in Feeder");
			blockingRobotButton = new JButton("Robot Blocking Camera");
			diverterSpeedLabel = new JLabel("Diverter Speed");
			messageBox = new JTextArea("Actions...\n");
			messageBox.setLineWrap(true);
			badPartsPercentageMin = 0;
			badPartsPercentageMax = 100;
			badPartsPercentage = new JSlider(badPartsPercentageMin, badPartsPercentageMax);
			
			// hash table for bad parts percentage slider for easy access
			Hashtable labelTable = new Hashtable();
			for(int i = 0; i <=100; i+=25){
				labelTable.put( new Integer( i ), new JLabel(i + "%") );
			}
			badPartsPercentage.setLabelTable( labelTable );
			badPartsPercentage.setMinorTickSpacing(5);
			badPartsPercentage.setMajorTickSpacing(25);
			badPartsPercentage.setPaintTicks(true);
			badPartsPercentage.setSnapToTicks(true);
			badPartsPercentage.setPaintLabels(true);
			badPartsPercentage.setValue(0);
			
			diverterSpeedMin = 0;
			diverterSpeedMax = 20;
			diverterSpeed = new JSlider(diverterSpeedMin, diverterSpeedMax);
			labelTable = new Hashtable();
			labelTable.put( new Integer(diverterSpeedMin), new JLabel("Slow") );
			labelTable.put( new Integer(diverterSpeedMax), new JLabel("Fast"));
			diverterSpeed.setLabelTable( labelTable );
			diverterSpeed.setMinorTickSpacing(1);
			diverterSpeed.setMajorTickSpacing(5);
			diverterSpeed.setPaintTicks(true);
			diverterSpeed.setSnapToTicks(true);
			diverterSpeed.setPaintLabels(true);
			diverterSpeed.setValue(diverterSpeedMax);
			diverterSpeed.addChangeListener(new SliderDetection());
			laneJamButton.addActionListener(this);
			diverterButton.addActionListener(this);
			badPartsButton.addActionListener(this);
			blockingRobotButton.addActionListener(this);

			laneJamButton.setPreferredSize(new Dimension(200,25));
			diverterButton.setPreferredSize(new Dimension(200,25));
			badPartsButton.setPreferredSize(new Dimension(200,25));
			blockingRobotButton.setPreferredSize(new Dimension(200,25));

			setLayout(new FlowLayout());
			Box boxContainer = Box.createVerticalBox();
			laneBoxList = new JComboBox();
			for (int i = 1; i < 9;i++) {
				laneBoxList.addItem("Lane "+i);
			}
			laneBoxList.setSelectedIndex(0);
			feederBoxList = new JComboBox();
			for (int i = 1; i < 5; i++) {
				feederBoxList.addItem("Feeder "+i);
			}

			feederBoxList.setPreferredSize(new Dimension(200,25));
			laneBoxList.setPreferredSize(new Dimension(200,25));
			laneContainer = new JPanel();
			feederContainer = new JPanel();


			laneContainer.setPreferredSize(new Dimension(250,120));
			feederContainer.setPreferredSize(new Dimension(250,330));
			
			laneContainer.setLayout(new GridBagLayout());
			feederContainer.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			TitledBorder title = BorderFactory.createTitledBorder("Lanes / Nests");
			laneContainer.setBorder(title);	

			title = BorderFactory.createTitledBorder("Feeders");
			feederContainer.setBorder(title);
			c.gridx = 0;
			c.gridy = 0;
			laneContainer.add(laneBoxList,c);
			c.gridy = 1;
			c.insets = new Insets(10,0,0,0);
			laneContainer.add(laneJamButton,c);
			//laneContainer.add(diverterButton);
			
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			feederContainer.add(feederBoxList,c);
			c.gridy = 1;
			c.insets = new Insets(30,0,0,0);
			feederContainer.add(badPartsLabel,c);
			c.insets = new Insets(0,0,0,0);
			c.gridy = 2;
			feederContainer.add(badPartsPercentage,c);
			c.gridy = 3;
			c.insets = new Insets(3,0,0,0);
			feederContainer.add(badPartsButton,c);
			//feederContainer.add(blockingRobotButton);
			c.insets = new Insets(30,0,0,0);
			c.gridy = 4;
			feederContainer.add(diverterSpeedLabel,c);
			c.insets = new Insets(0,0,0,0);
			c.gridy = 5;
			feederContainer.add(diverterSpeed,c);
			//boxContainer.add(Box.createRigidArea(new Dimension(0,30)));
			JLabel label = new JLabel("Non-Normative Cases");
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			boxContainer.add(label);
			//boxContainer.add(Box.createRigidArea(new Dimension(0,30)));
			//boxContainer.add(laneBoxList);
			boxContainer.add(Box.createRigidArea(new Dimension(0,20)));
			boxContainer.add(laneContainer);
			boxContainer.add(Box.createRigidArea(new Dimension(0,30)));
			boxContainer.add(feederContainer);
			boxContainer.add(Box.createRigidArea(new Dimension(0,10)));
			JScrollPane scrollPane = new JScrollPane(messageBox);
			scrollPane.setPreferredSize(new Dimension(200,100));
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			boxContainer.add(scrollPane);
			add(boxContainer);
		}

		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == laneJamButton) {
				messageBox.append("Lane jam initated in " + laneBoxList.getSelectedItem() + ".\n");
				messageBox.setCaretPosition(messageBox.getDocument().getLength());
				int lanenum = laneBoxList.getSelectedIndex();
				String set = "lm va cmd missingparts " + lanenum/2 + " " + lanenum%2;
				try {
					laneManager.sendCommand(set);
					
					if (lanenum%2 == 0)
						laneManager.sendCommand("lm lm cmd jamtoplane " + lanenum/2);
					else
						laneManager.sendCommand("lm lm cmd jambottomlane " + lanenum/2);
				} catch (Exception e) {
					System.out.println("An error occurred trying to initiate non-normative case: lane jam.");
				} 
			} else if (ae.getSource() == diverterButton) {
				messageBox.append("Diverter was too slow switching to " + laneBoxList.getSelectedItem() + ".\n");
				messageBox.setCaretPosition(messageBox.getDocument().getLength());
				int feedernum = feederBoxList.getSelectedIndex();
				String set = "lm va cmd missingparts " + feedernum + " -1";
				try {
					laneManager.sendCommand(set);
					//TODO: missingparts command appends a nest # (0-7), but slowdiverter is feeder-based (0-3).
					//Figure out a way to determine how to do this.
					laneManager.sendCommand("lm fa cmd slowdiverter " + feedernum);
				} catch (Exception e) {
					System.out.println("An error occurred trying initiate non-normative case: slow diverter change.");
				} 
			} else if (ae.getSource() == badPartsButton) {
				messageBox.append("Bad parts found in " + laneBoxList.getSelectedItem() + "'s nest.\n");
				messageBox.setCaretPosition(messageBox.getDocument().getLength());
				int feedernum = feederBoxList.getSelectedIndex();
				String set = "lm lm cmd badparts " + feedernum + " "+ badPartsPercentage.getValue();
				try {
					laneManager.sendCommand(set);
				} catch (Exception e) {
					System.out.println("An error occurred trying initiate non-normative case: bad parts in nest.");
				} 
			} else if (ae.getSource() == blockingRobotButton) {
				messageBox.append("A robot is blocking the camera at " + laneBoxList.getSelectedItem() + "'s nest.\n");
				messageBox.setCaretPosition(messageBox.getDocument().getLength());
				int lanenum = laneBoxList.getSelectedIndex();
				String set = "lm va cmd blockingrobot " + lanenum;
				try {
					laneManager.sendCommand(set);
				} catch (Exception e) {
					System.out.println("An error occurred trying to initiate non-normative case: robot blocking camera.");
				} 
			}

		}
		
		public class SliderDetection implements ChangeListener{

			@Override
			public void stateChanged(ChangeEvent ce) {
				// TODO Auto-generated method stub
				JSlider source = (JSlider) ce.getSource();
				if(source == diverterSpeed){
					if (!source.getValueIsAdjusting()) {
						int speed = (int)source.getValue();
						// send amplitude to server
						int feederNumber = (Integer)feederBoxList.getSelectedIndex();
						String set = "lm fa set diverterspeed " + (feederNumber) + " " + (diverterSpeedMax-speed);
						try {
							laneManager.sendCommand(set);
							laneManager.sendCommand("lm fpm set diverterspeed " + feederNumber + " " + (speed));
						} catch (Exception e) {
							System.out.println("An error occurred trying to send message to change lane amplitude.");
						} 
						System.out.println("Feeder : " + feederNumber + " going at " + speed);
					}
				}
						
			}

		}
		

	}	
}
