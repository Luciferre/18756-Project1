package NetworkElements;

import java.util.*;

import DataTypes.SONETFrame;

public class OXC implements IOpticalRouterTA{
	private String address;
	private ArrayList<OpticalNICTA> NICs = new ArrayList<OpticalNICTA>();
	private HashMap<OpticalNICTA, OpticalNICTA> forwardRing = new HashMap<OpticalNICTA, OpticalNICTA>();
	private TreeMap<Integer, Integer> frequencyMap = new TreeMap<Integer, Integer>();
	private Boolean trace = true;
	
	public OXC(String address){}
	
	public void receiveFrame(SONETFrame frame, int wavelength, OpticalNICTA nic){}
	
	public void addNIC(OpticalNICTA nic){}
	
	public void addFrequencyMap(Integer oldFrequency, Integer newFrequency){}
	
	public void addForwardRing(OpticalNICTA from, OpticalNICTA to){}

	public String getAddress(){ return null;}
}
