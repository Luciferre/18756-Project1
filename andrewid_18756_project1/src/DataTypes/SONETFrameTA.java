package DataTypes;

public abstract class SONETFrameTA {
	private SPE spe = null;
	private String OAMFlags= "";
	protected int delay = 0;
	
	/**
	 * Create a new SONET frame to be sent on the network
	 * @param	spe the spe inside the frame
	 */
	public SONETFrameTA(SPE spe){
		this.spe = spe;
	}
	
	/**
	 * Gets the SPE out of this frame
	 * @return	the SPE inside this frame
	 */
	public SPE getSPE(){
		return this.spe;
	}
	
	/**
	 * Returns the total amount of delay this frame has experienced during it's travel
	 * @return	 the total delay this frame experienced
	 */
	public int getDealy(){
		return this.delay;
	}
	
	/**
	 * Sets some info in the OAM bytes of the Frame. Could be useful if we needed to pass some message
	 * to the other routers
	 * @param flags the info that is in the flags
	 */
	public void setOAMFlags(String flags){
		this.OAMFlags = flags;
	}
	
	/**
	 * Gets the info in the OAM bytes of the frame
	 * @return the OAM info from the frame
	 */
	public String getOAMFlags(){
		return this.OAMFlags;
	}
}
