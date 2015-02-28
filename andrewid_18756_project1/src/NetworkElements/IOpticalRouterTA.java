package NetworkElements;

import DataTypes.*;

public interface IOpticalRouterTA {
	public String getAddress();
	public void receiveFrame(SONETFrame frame, int wavelength, OpticalNICTA nic);
	public void addNIC(OpticalNICTA nic);
}
