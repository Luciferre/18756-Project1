
import NetworkElements.*;
import DataTypes.*;

public class ExampleTA {
	public void twoRouter(){
		/*
		 * Setup the network
		 */
		System.out.println("Setting up two routers");
		// Create two SONET routers
		SONETRouter router1 = new SONETRouter("00:11:22");
		SONETRouter router2 = new SONETRouter("88:77:66");
		
		// tell routers a wavelength to add/drop on (in this case their own frequencies)
		router1.addDropWavelength(1310);
		router2.addDropWavelength(1490);
		
		// tell router 1 the wavelength each router is add/dropping on
		router1.addDestinationFrequency("00:11:22", 1310);
		router1.addDestinationFrequency("88:77:66", 1490);
		
		// tell router 2 the wavelength each router is add/dropping on
		router2.addDestinationFrequency("00:11:22", 1310);
		router2.addDestinationFrequency("88:77:66", 1490);
		
		// Create an interface for each router
		OpticalNICTA nicRouter11 = new OpticalNICTA(router1);
		nicRouter11.setID(11);
		OpticalNICTA nicRouter12 = new OpticalNICTA(router1);
		nicRouter12.setID(12);
		OpticalNICTA nicRouter21 = new OpticalNICTA(router2);
		nicRouter21.setID(21);
		OpticalNICTA nicRouter22 = new OpticalNICTA(router2);
		nicRouter22.setID(22);
		
		// Create two-uni directional links between the routers
		OtoOLink OneToTwo1 = new OtoOLink(nicRouter11, nicRouter21);
		OtoOLink TwoToOne1 = new OtoOLink(nicRouter21, nicRouter11);
		OtoOLink OneToTwo2 = new OtoOLink(nicRouter12, nicRouter22);
		OtoOLink TwoToOne2 = new OtoOLink(nicRouter22, nicRouter12);
		
		/*
		 * Sent a frame on the network
		 */
		//router1.source(new SONETFrame(new SPE(0)), 1490);
		//router1.source(new SONETFrame(new SPE(0)), 1310);
		router1.source(new SONETFrame(new SPE(0)), 1320);
	}
	
	public static void main(String args[]){
		ExampleTA go = new ExampleTA();
		go.twoRouter();
	}
}
