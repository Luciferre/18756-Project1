package NetworkElements;

import DataTypes.SONETFrame;

public abstract class OtoOLinkTA implements ILinkTA{
	protected OpticalNICTA source=null, dest=null;
	protected Boolean linkCut = false;
	
	/**
	 * Constructs a new Optical to Optical link from a source to a destination
	 * @param	source the source the link is connected to
	 * @param	dest the destination the link is connected to
	 */
	public OtoOLinkTA(OpticalNICTA source, OpticalNICTA dest){	
		if(source == dest)
			System.out.println("Error: You cannot create a link to your own interface");
		if(source == null)
			System.out.println("Error: You cannot create a link that doesn't have a source");
		if(dest == null)
			System.out.println("Error: You cannot create a link that doesn't have a destination");
		
		this.source = source;
		this.dest = dest;
		
		this.source.setOutLink(this);
		this.dest.setInLink(this);
	}
	
	/**
	 * Returns the source NIC for this link. i.e. the only NIC that can put data onto this link
	 * @return	source NIC of this link
	 */
	public OpticalNICTA getSource(){
		return this.source;
	}
	
	/**
	 * Returns the destination NIC for this link. i.e. the NIC where data put on this link will be delivered to
	 * @return	destination NIC for this link
	 */
	public OpticalNICTA getDest(){
		return this.dest;
	}
	
	/**
	 * Sets that this link has been cut. Making both ends senSe there have been a loss of light
	 * (as SONET sends frames even when there is no data to be sent)
	 * 
	 */
	public void cutLink(){
		this.source.senseLight(false);
		this.dest.senseLight(false);
		this.linkCut = true;
	}
	
	/**
	 * Sets the link as being uncut. i.e. the link is now working fine
	 */
	public void uncutLink(){
		this.source.senseLight(true);
		this.dest.senseLight(true);
		this.linkCut = false;
	}
	
	/**
	 * Sends data from the source NIC joined to this link, to the dest NIC joined to this link
	 * @param	frame the frame to be sent
	 * @param	wavelength the wavelength the frame is sent on
	 * @param	nic the source NIC, to ensure the NIC is joined to this link
	 */
	public abstract void sendData(SONETFrame frame, int wavelength, OpticalNICTA nic);
}
