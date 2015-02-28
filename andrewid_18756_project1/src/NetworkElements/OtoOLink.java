package NetworkElements;

import DataTypes.*;

public class OtoOLink extends OtoOLinkTA{
	/**
	 * Constructs a new Optical to Optical link from a source to a destination
	 * @param	source the source the link is connected to
	 * @param	dest the destination the link is connected to
	 */
	public OtoOLink(OpticalNICTA source, OpticalNICTA dest){
		super(source,dest);
	}
	
	/**
	 * Sends data from the source NIC joined to this link, to the dest NIC joined to this link
	 * @param	frame the frame to be sent
	 * @param	wavelength the wavelength the frame is sent on
	 * @param	source the source NIC, to ensure the NIC is joined to this link
	 * (non-Javadoc)
	 * @see NetworkElements.OtoOLinkTA#sendData(DataTypes.SONETFrame, int, NetworkElements.OpticalNICTA)
	 */
	public void sendData(SONETFrame frame, int wavelength, OpticalNICTA source){
		if(dest==null)
			System.err.println("Error (OtoOLink): You tried to send data down a line that doesn't go anywhere");
		else if(this.source != source)
			System.err.println("Error (OtoOLink: You tried to send data down a line you are not connected to");
		else if(this.linkCut)
			System.err.println("Error (OtoOLink: You tried to send data down a line that is cut");
		else{
			frame.addDelay(5);
			frame.getSPE().addDelay(5);
			this.dest.receiveData(frame, wavelength);
		}
	}
	
}
