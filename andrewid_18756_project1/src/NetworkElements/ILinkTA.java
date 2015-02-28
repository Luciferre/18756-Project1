package NetworkElements;

import DataTypes.*;

public interface ILinkTA {
	/**
	 * Returns the source NIC of this link
	 * @return	source NIC
	 */
	public INICTA getSource();
	
	/**
	 * Returns the destination NIC of this link
	 * @return	destination NIC
	 */
	public INICTA getDest();
	
	/**
	 * Sends data from the source NIC joined to this link, to the dest NIC joined to this link
	 * @param	frame the frame to be sent
	 * @param	wavelength the wavelength the frame is sent on
	 * @param	nic the source NIC, to ensure the NIC is joined to this link
	 */
	public void sendData(SONETFrame frame, int wavelength, OpticalNICTA nic);
}
