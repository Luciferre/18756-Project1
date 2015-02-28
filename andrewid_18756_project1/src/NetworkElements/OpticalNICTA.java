package NetworkElements;

import DataTypes.*;

public class OpticalNICTA implements INICTA{
	private IOpticalRouterTA parent = null;
	private OtoOLinkTA inLink = null, outLink = null;
	private Boolean trace = true;
	private Boolean isOnRing = true;
	private Boolean isWorking = false;
	private int id = -1;
	private OpticalNICTA workingNIC = null;
	private	OpticalNICTA protectionNIC = null;
	private Boolean hasError = false;
	private Boolean isClockwise = false;
	private Boolean isSentWorking = false;
	/**
	 * Creates a new Optical NIC
	 * @param	parent the parent router for this NIC
	 */
	public OpticalNICTA(IOpticalRouterTA parent){
		if(parent==null)
			System.err.println("Error: You cannot create an interface with no parent");
		
		this.parent = parent;
		this.parent.addNIC(this);
		
	}
	
	/**
	 * Set the incoming optical link to this optical NIC
	 * @param	inLink the incoming optical link
	 */
	public void setInLink(OtoOLinkTA inLink){
		if(inLink.getSource() != this && inLink.getDest() == this)
			this.inLink = inLink;
		else
			System.err.println("Error (OpticalNIC): You tried to assign an Optical in link that was not created for this interface");
	}
	public OtoOLinkTA getInLink() {
		return inLink;
	}
	/**
	 * Set the outgoing optical link to this optical NIC
	 * @param	outLink the outgoing optical link
	 */
	public void setOutLink(OtoOLinkTA outLink){
		if(outLink.getSource() == this && outLink.getDest() != this)
			this.outLink = outLink;
		else
			System.err.println("Error (OpticalNIC): You tried to assign an Optical out link that was not created for this interface");
	}
	
	public OtoOLinkTA getOutLink(){
		return this.outLink;
	}
	
	/**
	 * Sets the ID for this optical NIC. Each NIC in a router should have a unique ID so that it can
	 * be distinguished from other NICs in the router
	 * @param	id the id of this NIC
	 */
	public void setID(int id){
		this.id = id;
	}
	
	/**
	 * Returns the ID of this NIC
	 * @return	ID of the NIC
	 */
	public int getID(){
		return this.id;
	}
	
	/**
	 * Returns the router that this NIC is in
	 * @return parent NIC
	 */
	public IOpticalRouterTA getParent(){
		return this.parent;
	}
	
	/**
	 * Set if this NIC goes in a clockwise direction. Could be one way of telling the shortest path since
	 * we know the network design
	 * @param	clockwise does the NIC go clockwise
	 */
	public void setClockwise(Boolean clockwise){
		this.isClockwise = clockwise;
	}
	
	/**
	 * Returns whether this NIC goes in a clockwise direction
	 * @return	does this NIC go clockwise
	 */
	public Boolean getIsClockwise(){
		return this.isClockwise;
	}
	
	/**
	 * Set of this NIC is on a SONET ring. If the NIC is part of the ring wavelengths will be forwarded on
	 * it regardless of the wavelength destination
	 * @param	isOnRing is this NIC on a SONET ring
	 */
	public void setIsOnRing(Boolean isOnRing){
		this.isOnRing = isOnRing;
	}
	
	/**
	 * Returns whether or no this NIC is part of a SONET ring. If it is part of a ring then all wavelengths
	 * should be forwarded onto this NIC
	 * @return	is this NIC on a SONET ring
	 */
	public Boolean getIsOnRing(){
		return this.isOnRing;
	}
	
	/**
	 * Sets whether or not this NIC is a protection NIC for a working NIC
	 * @param	workingNIC The working NIC for this backup NIC
	 */
	public void setIsProtection(OpticalNICTA workingNIC){
		if(this.parent != workingNIC.parent)
			System.err.println("Error (OpticalNIC): The working NIC and protection must be in the same router");
				
	}
	
	/**
	 * Returns the working NIC of this NIC, or null if this card is not a protection NIC
	 * @return	the working NIC for this protection NIC
	 */
	public OpticalNICTA getWorkingNIC(){
		return this.workingNIC;
	}
	
	public Boolean getIsSentWorkingNIC() {
		return isSentWorking;
	}

	public void setIsSentWorkingNIC(Boolean isSentWorking) {
		this.isSentWorking = isSentWorking;
	}
	
	/**
	 * Sets whether or not this NIC has a protection NIC
	 * @param	protectionNIC The protection NIC for this working NIC
	 */
	public void setIsWorking(OpticalNICTA protectionNIC){
		if(this.parent != protectionNIC.parent)
			System.err.println("Error (OpticalNIC): The working NIC and protection must be in the same router");
		
		this.protectionNIC = protectionNIC;
		this.isWorking = true;
	}
	
	public Boolean getIsWorking()
	{
		return this.isWorking;
	}
	
	/**
	 * Returns the protection NIC of this NIC, or null if this card does not have a protection NIC
	 * @return	the protection NIC for this working NIC
	 */
	public OpticalNICTA getProtectionNIC(){
		return this.protectionNIC;
	}
	
	/**
	 * Sets whether or not this NIC is in an error state for some reason
	 * @param	hasError does this NIC have an error
	 */
	public void setHasError(Boolean hasError){
		System.out.println("(OpticalNIC) " + this.parent.getAddress() + " " + this.getID() + ": Set error state: " + hasError);
		this.hasError = hasError;
	}
	
	/**
	 * Gets whether or not this NIC is in an error state
	 * @return	whether this NIC is in an error state
	 */
	public Boolean getHasError(){
		return this.hasError;
	}
	
	/**
	 * Tells this NIC if it is sensing light through the cable
	 * @param	light does the link have light
	 */
	public void senseLight(Boolean light){
		if(light==true)
			this.hasError = false;
		else
			this.hasError = true;
	}
	
	/**
	 * Sends a frame to the link that is joined to the output of this NIC
	 * @param	frame the frame to be sent
	 * @param	wavelength the wavelength the frame will be sent on
	 */
	public Boolean sendFrame(SONETFrame frame, int wavelength){
		if(this.outLink == null){
			System.err.println("Error (OpticalNIC): you are trying to send a frame on an interface with no out connection:");
			return false;
		}
		if(this.getHasError()){
			System.err.println("Error (OpticalNIC): you are trying to send a frame on an interface that is in an error state");
			return false;
		}
		else{
			outLink.sendData(frame, wavelength, this);
			return true;
		}
	}
	
	/**
	 * This method is run when a frame is received from the input link joined to this NIC. It is then
	 * passed to the SONET router for routing
	 * @param	frame the frame received
	 * @param	wavelength the wavelength it was received on
	 */
	public void receiveData(SONETFrame frame, int wavelength){
		if(this.trace)
			System.out.println("Trace (OpticalNIC): router:" + this.parent.getAddress() + " NICid: " + this.getID() + " received data");
	
		if(this.trace)
		System.out.println("Trace (OpticalNIC): " + this.parent.getAddress() + " has received a frame on " + wavelength
				+ " frame delay: " + frame.getDealy() + " SPE delay: " + frame.getSPE().getDealy());
		
		this.parent.receiveFrame(frame, wavelength, this);
	}
}
