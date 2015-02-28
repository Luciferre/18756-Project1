package NetworkElements;

import DataTypes.*;

import java.util.*;

import com.sun.org.apache.bcel.internal.generic.IREM;

public class SONETRouter extends SONETRouterTA{
	public boolean flag = false;
	
	/**
	 * Construct a new SONET router with a given address
	 * @param	address the address of the new SONET router
	 */
	public SONETRouter(String address){
		super(address);
	}
	
	/**
	 * This method processes a frame when it is received from any location (including being created on this router
	 * from the source method). It either drops the frame from the line, or forwards it around the ring
	 * @param	frame the SONET frame to be processed
	 * @param	wavelength the wavelength the frame was received on
	 * @param	nic the NIC the frame was received on
	 */
	public void receiveFrame(SONETFrame frame, int wavelength, OpticalNICTA nic){
		/*If a frame is on the routers drop frequency it takes it off of the line (it might be for that router, or has to be forwarded to somewhere off of the ring)
		If a frame is on the routers drop frequency, and is also the routers destination frequency, the frame is forwarded to the sink(frame, wavelength) method. In real life the sink method would be sending the data to the layer above)
		If a frame is not on the routers drop frequency the frame is forwarded on all interfaces, except the interface the frame was received on (sendRingFrame())*/
		if(this.dropFrequency.contains(wavelength))
		{
			if( this.destinationFrequencies.containsValue(wavelength))
			{
				flag = true;
				//5) The sink should only receive one SPE and it should be the working path
				if(nic.getIsSentWorkingNIC())
					this.sink(frame, wavelength);
			
			}
			else {
				
			}
		}
		else {
			//the data of unknown frequencies are never sent
			if(this.destinationFrequencies.containsValue(wavelength))
				this.sendRingFrame(frame, wavelength, nic);//to do
			
		}	
		
	}
	
	/**
	 * Sends a frame out onto the ring that this SONET router is joined to
	 * @param	frame the frame to be sent
	 * @param	wavelength the wavelength to send the frame on
	 * @param	nic the wavelength this frame originally came from (as we don't want to send it back to the sender)
	 */
	public void sendRingFrame(SONETFrame frame, int wavelength, OpticalNICTA nic){
		// Loop through the interfaces sending the frame on interfaces that are on the ring
		// except the one it was received on. Basically what UPSR does
		/*take the shortest path if available		
		3) Traffic will start taking the shortest path again if a link that was unavailable becomes available again
		4) The router should receive each SPE twice if there are two full distinct paths available (see the last example picture, only one SPE arrives)*/
		this.getPaths(nic, wavelength);
		
		if(path1.size() == 1 && flag == false){
			//2) Working path takes preference over protection path if only one shortest path is available
			path1.get(0).setIsWorking(path1.get(0));
			path1.get(0).setClockwise(true);
			if(nic == null){
				OtoOLink w = (OtoOLink) path1.get(0).getOutLink();
				w.dest.setIsSentWorkingNIC(true);
				w.source.setIsSentWorkingNIC(true);	
				path1.get(0).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
			}else {
				if (path1.get(0).getOutLink().getDest().getParent().getAddress() != nic.getInLink().getSource().getParent().getAddress()){
					OtoOLink w = (OtoOLink) path1.get(0).getOutLink();
					w.dest.setIsSentWorkingNIC(nic.getInLink().getSource().getIsSentWorkingNIC());
					w.source.setIsSentWorkingNIC(nic.getInLink().getSource().getIsSentWorkingNIC());
					if(nic.getIsClockwise() == path1.get(0).getIsClockwise())
						path1.get(0).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
				}
			}
			if(path2.size() >= 1 && flag == false ){

				//2) Working path takes preference over protection path if only one shortest path is available
				path2.get(0).setIsWorking(path2.get(0));
				path2.get(0).setClockwise(false);
				if(path2.get(1)!=null)
					path2.get(1).setClockwise(true);
				if(nic == null){
					OtoOLink w = (OtoOLink) path2.get(0).getOutLink();
					w.dest.setIsSentWorkingNIC(true);
					w.source.setIsSentWorkingNIC(true);
					path2.get(0).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
				}else {
					if (path2.get(0).getOutLink().getDest().getParent().getAddress() != nic.getInLink().getSource().getParent().getAddress()){
						OtoOLink w = (OtoOLink) path2.get(0).getOutLink();
						w.dest.setIsSentWorkingNIC(true);
						w.source.setIsSentWorkingNIC(true);
						if(nic.getIsClockwise() == path2.get(0).getIsClockwise())
							path2.get(0).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
					}
				}
			}
		}
		else if (path1.size() == 2 && flag == false){		
			path1.get(0).setIsWorking(path1.get(1));
			path1.get(0).setClockwise(true);
			path1.get(1).setIsProtection(path1.get(0));
			path1.get(1).setClockwise(false);
			if(nic == null){
				OtoOLink w = (OtoOLink) path1.get(0).getOutLink();
				w.dest.setIsSentWorkingNIC(true);
				w.source.setIsSentWorkingNIC(true);
				path1.get(0).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
				path1.get(1).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
			}else {
				if (path1.get(0).getOutLink().getDest().getParent().getAddress() != nic.getInLink().getSource().getParent().getAddress()){
					OtoOLink w = (OtoOLink) path1.get(0).getOutLink();
					w.dest.setIsSentWorkingNIC(nic.getInLink().getSource().getIsSentWorkingNIC());
					w.source.setIsSentWorkingNIC(nic.getInLink().getSource().getIsSentWorkingNIC());
					if(nic.getIsClockwise() == path1.get(0).getIsClockwise())
						path1.get(0).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
				}
				if (path1.get(1).getOutLink().getDest().getParent().getAddress() != nic.getInLink().getSource().getParent().getAddress()){
					OtoOLink w = (OtoOLink) path1.get(0).getOutLink();
					w.dest.setIsSentWorkingNIC(nic.getInLink().getSource().getIsSentWorkingNIC());
					w.source.setIsSentWorkingNIC(nic.getInLink().getSource().getIsSentWorkingNIC());
					if(nic.getIsClockwise() == path1.get(0).getIsClockwise())
						path1.get(1).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
				}
				
				
			}
			
		}
		else{
		if(path2.size() == 1 && flag == false ){

			//2) Working path takes preference over protection path if only one shortest path is available
			path2.get(0).setIsWorking(path2.get(0));
			if(nic == null){
				OtoOLink w = (OtoOLink) path2.get(0).getOutLink();
				w.dest.setIsSentWorkingNIC(true);
				w.source.setIsSentWorkingNIC(true);
				path2.get(0).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
			}else {
				if (path2.get(0).getOutLink().getDest().getParent().getAddress() != nic.getInLink().getSource().getParent().getAddress()){
					OtoOLink w = (OtoOLink) path2.get(0).getOutLink();
					w.dest.setIsSentWorkingNIC(true);
					w.source.setIsSentWorkingNIC(true);
					if(nic.getIsClockwise() == path2.get(0).getIsClockwise())
						path2.get(0).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
				}
			}
		}
		else if (path2.size() == 2 && flag == false){		
			path2.get(0).setIsWorking(path2.get(1));
			path2.get(0).setClockwise(true);
			path2.get(1).setClockwise(false);
			path2.get(1).setIsProtection(path2.get(0));
			if(nic == null){
				OtoOLink w = (OtoOLink) path2.get(0).getOutLink();
				w.dest.setIsSentWorkingNIC(true);
				w.source.setIsSentWorkingNIC(true);
				path2.get(0).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
				path2.get(1).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
			}else {
				if (path2.get(0).getOutLink().getDest().getParent().getAddress() != nic.getInLink().getSource().getParent().getAddress()){
					OtoOLink w = (OtoOLink) path2.get(0).getOutLink();
					w.dest.setIsSentWorkingNIC(nic.getInLink().getSource().getIsSentWorkingNIC());					
					w.source.setIsSentWorkingNIC(nic.getInLink().getSource().getIsSentWorkingNIC());
					if(nic.getIsClockwise() == path2.get(0).getIsClockwise())
						path2.get(0).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
				}
				if (path2.get(1).getOutLink().getDest().getParent().getAddress() != nic.getInLink().getSource().getParent().getAddress()){
					OtoOLink w = (OtoOLink) path2.get(0).getOutLink();
					w.dest.setIsSentWorkingNIC(nic.getInLink().getSource().getIsSentWorkingNIC());
					w.source.setIsSentWorkingNIC(nic.getInLink().getSource().getIsSentWorkingNIC());
					if(nic.getIsClockwise() == path2.get(0).getIsClockwise())
						path2.get(1).sendFrame(new SONETFrame(frame.getSPE().clone()), wavelength);
				}
			}
		}
		}
	}
	
	
	public void getPaths(OpticalNICTA sourceNIC, int wavelength){
		//build a table of wavelength and nics
		for (Map.Entry<String, Integer> dF : this.destinationFrequencies.entrySet()) {  
            if(this.destinationNextHop.containsKey(dF.getValue())){
            	nextHop = this.destinationNextHop.get(dF.getValue());
            }
            else {
				nextHop = new ArrayList<>();
			}
            for(int i=0; i<this.NICs.size();i++)
            {
            	if(dF.getKey() == NICs.get(i).getOutLink().dest.getParent().getAddress())
            		nextHop.add(NICs.get(i).getID());        			
            }
            this.addDestinationHopCount(dF.getValue(), nextHop);
        }  
		
		path1 = new ArrayList<>();
		path2 = new ArrayList<>();
		//add paths
		for(int i = 0; i < NICs.size();i++){
        	if((!NICs.get(i).equals(sourceNIC)) && NICs.get(i).getIsOnRing() 
        			&& (!NICs.get(i).getOutLink().linkCut)   		
        		    && ( !isSameParent(NICs.get(i), sourceNIC))
        			&& this.destinationNextHop.get(wavelength).contains(NICs.get(i).getID()))
        	{
        		path1.add(NICs.get(i));       		
        	}
        }
		
		for(int i = 0; i < NICs.size();i++){
        	if(!(NICs.get(i).equals(sourceNIC))  && NICs.get(i).getIsOnRing() 
        			&& !NICs.get(i).getOutLink().linkCut 
        		    && !isSameParent(NICs.get(i), sourceNIC))
        		if( this.destinationNextHop.get(wavelength).contains(NICs.get(i).getID()))
        	{       		  		
        	}
        	else {
        			path2.add(NICs.get(i));     
			}
        }
			
	}
	private boolean isSameParent(OpticalNICTA out, OpticalNICTA in){
		if (out==null || in ==null)
			return false;
		return out.getOutLink().getDest().getParent().getAddress() == in.getInLink().getSource().getParent().getAddress();
	}
}
