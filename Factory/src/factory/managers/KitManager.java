//Ben Mayeux, Marc Mendiola

package factory.managers;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import factory.client.Client;
import factory.KitConfig;
import factory.swing.KitManPanel;
import factory.Part;

public class KitManager extends Client implements WindowListener{

	private static final long serialVersionUID = -3161852324870948654L;

	private HashMap<String,KitConfig> kitConfigList;
	private HashMap<String,Part> partsList;

	public KitManager() {
		super(Client.Type.km);
		loadData();
		UI = new KitManPanel(this);

		setInterface();
		this.addWindowListener(this);
	}
	public static void main(String[] args){
		KitManager k = new KitManager();
	}

	public void setInterface() {

		add(UI, BorderLayout.LINE_END);
		pack();
		this.setTitle("Kit Manager");
		setVisible(true);
	}


	@Override
	public void doCommand(ArrayList<String> pCmd) {

		//		System.out.println("GOT HERE: ");
		//		for (int i = 0; i < pCmd.size(); i++){
		//			System.out.println(pCmd.get(i));
		//		}

		int size = pCmd.size();
		//parameters lay between i = 2 and i = size - 2
		String action = pCmd.get(0);
		String identifier = pCmd.get(1);
		if(action.equals("cmd"))
		{	
			System.out.println("GOT HEREs");
			boolean hasPart = false;
			for(int i = 0; i < pCmd.size(); i++){
				System.out.println(pCmd.get(i));
			}
			System.out.println();
			if(identifier.equals("addpartname"))// check directions
			{
				System.out.println("YESSSSSS");
				Part part = new Part(pCmd);
				partsList.put(pCmd.get(2),part);
				((KitManPanel) UI).refreshAll();
			}
			else if(identifier.equals("rmpartname"))//check directions
			{
				System.out.println("Removing Part Name "+pCmd.get(2));
				partsList.remove(pCmd.get(2));// remove part from partList
				List<String> kitNames = new ArrayList<String>();
				for(String s: kitConfigList.keySet())
				{
					kitNames.add(s);
				}

				for(int j = 0; j < kitNames.size(); j++)
				{
					KitConfig configToCheck = kitConfigList.get(kitNames.get(j));
					for(int i = 0; i < configToCheck.listOfParts.size(); i++)//iterating through each part in a kit
					{
						hasPart = false;
						Part partToCheck = configToCheck.listOfParts.get(i);
						String name = partToCheck.name;
						if(pCmd.get(2).equals(name))//check if the part name from the KitConfig matches the removedpart name
						{
							hasPart = true;
							break; // once the part is found no more checks needed to be done and can move to next kitConfig
						}
						
					}
					if(hasPart)
					{	//remove kitConfig send confirmations and put hasPart to false 
						KitConfig kitconfig = kitConfigList.remove(kitNames.get(j));
						System.out.println("The KitConfig "+kitconfig.kitName+" required a "+pCmd.get(2)+" so it was deleted");
						((KitManPanel) UI).refreshAll();
					}

				}

			}
		}


		else if(action.equals("req")){
			/*if(identifier.equals(request1))
			 * do(request1);
			 * else if(identifier.equals(request2))
			 * do(request2);
			 */
		}
		else if(action.equals("get")){
			/*if(identifier.equals(get1))
			 * do(get1);
			 * else if(identifier.equals(get2))
			 * do(get2);
			 */
		}
		else if(action.equals("set"))
		{
			//first get the new and old kitnames 
			//then check if the identifier is correct and then remove the old kit from the hashMap
			//make the new kit with the new name and run through a loop to update the parts in the new kitConfig
			//finally add the new kitConfig to the kitCongifList and send confirmations to server
			String oldKitName = pCmd.get(2);
			String newKitName = pCmd.get(3);
			if (identifier.equals("partconfig")) {
				Part part = partsList.get(pCmd.get(2));
				part.name = pCmd.get(3);
				part.id = Integer.parseInt(pCmd.get(4));
				part.imagePath = pCmd.get(5);
				part.nestStabilizationTime = Integer.parseInt(pCmd.get(6));
				part.description = new String();
				for(int i = 7; i < pCmd.size()-1; i++)
					part.description.concat(pCmd.get(i) + " ");
				part.description = pCmd.get(7);
			}
		}
		else if(action.equals("cnf")){
			/*if(identifier.equals(confirm1))
			 * do(confirm1);
			 * else if(identifier.equals(confirm2))
			 * do(confirm2);
			 */
		}
		else if(action.equals("err")){
			String error;
			error = new String();
			for(int i = 1; i<this.parsedCommand.size(); i++)
				error.concat(parsedCommand.get(i));
			System.out.println(error);


		}
		else if(action.equals("mcs")){
			   try {
				this.server.close();
				connected = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
			   this.quit();
		   }
		else 
			System.out.println("Stuff is FU with the server...\n(string does not contain a command type)");


	}

	// Load Data - remember to import the file - FOR EVERYONE
	@SuppressWarnings("unchecked")
	public void loadData(){
		FileInputStream f;
		ObjectInputStream o;
		try{    // loads previously saved player data
			f = new FileInputStream("InitialData/initialParts.ser");
			o = new ObjectInputStream(f);
			setPartsList((HashMap<String,Part>) o.readObject());
			System.out.println("Good");
			o.close();
		}catch(IOException e){
			e.printStackTrace();
		} catch(ClassNotFoundException c){
			c.printStackTrace();
		}
		try{    // loads previously saved player data
			f = new FileInputStream("InitialData/initialKitConfigs.ser");
			o = new ObjectInputStream(f);
			setKitConfigList((HashMap<String,KitConfig>) o.readObject());
			System.out.println("KitConfigList " + kitConfigList.size());
			o.close();
		}catch(IOException e){
			e.printStackTrace();
		} catch(ClassNotFoundException c){
			c.printStackTrace();
		}
	}


	// Parts manager will save the most recent parts list to file
	public void savePartData() {
		FileOutputStream f;
		ObjectOutputStream o;
		try {    

			f = new FileOutputStream("InitialData/initialParts.ser");
			o = new ObjectOutputStream(f);
			o.writeObject(getPartsList());
			o.close();
			System.out.println("It worked");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// kit manager will save the most recent kit configuration list to file
	public void saveKitData() {
		FileOutputStream f;
		ObjectOutputStream o;
		try {  

			f = new FileOutputStream("InitialData/initialKitConfigs.ser");
			o = new ObjectOutputStream(f);
			o.writeObject(getKitConfigList());
			o.close();
			System.out.println("It worked");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		savePartData();
		saveKitData();

	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}
	public HashMap<String,KitConfig> getKitConfigList() {
		return kitConfigList;
	}
	public void setKitConfigList(HashMap<String,KitConfig> kitConfigList) {
		this.kitConfigList = kitConfigList;
	}
	public void addToKitConfigList(KitConfig k) {
		this.kitConfigList.put(k.kitName, k);
	}
	public void removeFromKitConfigList(KitConfig k) {
		this.kitConfigList.remove(k.kitName);
	}
	public HashMap<String,Part> getPartsList() {
		return partsList;
	}
	public void setPartsList(HashMap<String,Part> partsList) {
		this.partsList = partsList;
	}
}
