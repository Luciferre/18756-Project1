package NetworkElements;

import java.util.ArrayList;
import java.util.TreeMap;

import DataTypes.SONETFrame;

public abstract class SONETRouterTA implements IOpticalRouterTA{
	protected String address = null;
	protected ArrayList<OpticalNICTA> NICs = new ArrayList<OpticalNICTA>();
	protected TreeMap<String, Integer> destinationFrequencies = new TreeMap<String, Integer>();
	protected TreeMap<Integer, ArrayList<Integer>> destinationNextHop = new TreeMap<Integer, ArrayList<Integer>>();
	protected Boolean trace = true;
	protected ArrayList<Integer> dropFrequency = new ArrayList<Integer>();
	protected Boolean cwHopCanSendCW = true;
	protected Boolean acwHopCanSendACW = true;
	
	protected ArrayList<Integer> nextHop = new ArrayList<>();
	ArrayList<OpticalNICTA> path1 = new ArrayList<>();
	ArrayList<OpticalNICTA> path2 = new ArrayList<>();

	/**
	 * Construct a new SONET router with a given address
	 * @param	address the address of the new SONET router
	 */
	public SONETRouterTA(String address){
		if(address==null || address.equals(""))
			System.err.println("Error (SONETRouter): The router must have an address");
		
		this.address = address;
	}
	
	/**
	 * This method processes a frame when it is received from any location (including being created on this router
	 * from the source method). It either drops the frame from the line, or forwards it around the ring
	 * @param	frame the SONET frame to be processed
	 * @param	wavelength the wavelength the frame was received on
	 * @param	nic the NIC the frame was received on
	 */
	public abstract void receiveFrame(SONETFrame frame, int wavelength, OpticalNICTA nic);
	
	/**
	 * Sends a frame out onto the ring that this SONET router is joined to
	 * @param	frame the frame to be sent
	 * @param	wavelength the wavelength to send the frame on
	 * @param	nic the wavelength this frame originally came from (as we don't want to send it back to the sender)
	 */
	public abstract void sendRingFrame(SONETFrame frame, int wavelength, OpticalNICTA nic);
	
	/**
	 * Sends a frame from this router on a given wavelength from a null interface. This is not a real SONET
	 * feature. We are using this to input frames into our network as we don't have any users creating data.
	 * @param	frame the frame to be sent
	 * @param	wavelength the wavelength to send the frame on
	 */
	public void source(SONETFrame frame, int wavelength){
		this.receiveFrame(frame, wavelength, null);
	}
	
	/**
	 * Destroys a frame and prints an output with the frames details. SONET doesn't really do this, we use this
	 * feature to remove frames as we don't have anywhere to really send them
	 * @param	frame the frame to be destroyed and printed
	 * @param	wavelength the wavelength the frame was received on
	 */
	public void sink(SONETFrame frame, int wavelength){
		System.out.println("(SONETRouter) " + address + " has received a frame at the sink on wavelength " + wavelength
				+ " frame delay: " + frame.getDealy() + " SPE delay: " + frame.getSPE().getDealy());
	}
	
	/**
	 * Adds a wavelength that will be dropped by this router instead of being forwarded on the ring
	 * @param	wavelength the wavelength to be dropped
	 */
	public void addDropWavelength(int wavelength){
		this.dropFrequency.add(wavelength);
	}
	
	/**
	 * Adds a NIC to this router
	 * @param	nic the nic to be added to the router
	 */
	public void addNIC(OpticalNICTA nic){
		if(nic.getParent() != this)
			System.err.println("Error (SONETRouter): You cannot add an NIC to a router it was not made for");
		
		NICs.add(nic);
	}
	
	/**
	 * Gets the string address of this router
	 * @return	the address of this router
	 */
	public String getAddress(){
		return this.address;
	}
	
	/**
	 * Tells the router a wavelength that another router can be contacted on. Adding the routers own
	 * address and a wave length will let the router know that a wavelength is to be sent to the sink
	 * @param	destAddress the destination address that the wavelength will go to
	 * @param	wavelength the wavelength the destination can be contacted on
	 */
	public void addDestinationFrequency(String destAddress, int wavelength){
		destinationFrequencies.put(destAddress, wavelength);
	}
	
	/**
	 * Tells the router the next interface it should use to get to the destination. There is a list as
	 * The most preferred interface may be down
	 * @param	destAddress the destination address that the wavelength will go to
	 * @param	wavelength the wavelength the destination can be contacted on
	 */
	public void addDestinationHopCount(Integer wavelength, ArrayList<Integer> nextHop){
		destinationNextHop.put(wavelength, nextHop);
	}
	
	/**
	 * Sets all of the destination frequencies at once, so that they don't have to be repeatedly created for every
	 * router
	 * @param	destinationFrequencies the TreeMap of all of the destination frequencies
	 */
	public void setDestinationFrequencies(TreeMap<String, Integer> destinationFrequencies){
		this.destinationFrequencies = destinationFrequencies;
	}
}
