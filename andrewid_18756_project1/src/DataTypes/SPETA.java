package DataTypes;

public abstract class SPETA {
	private int VTPointer;
	protected int delay = 0;
	
	/**
	 * Creates a new SPE, with a virtual pointer set
	 * @param	VTPointer the VT number to be set for this SPE
	 */
	public SPETA(int VTPointer){
		this.VTPointer = VTPointer;
	}
	
	/**
	 * Gets the Virtual pointer that is set for this SPE
	 * @return	the VT of this SPE
	 */
	public int getVTPointer(){
		return this.VTPointer;
	}
	
	/**
	 * Returns the total amount of delay this SPE has experienced during it's travel
	 * @return	 the total delay this SPE experienced
	 * @deprecated
	 */
	public int getDealy(){
		return this.delay;
	}
	
	/**
	 * Returns the total amount of delay this SPE has experienced during it's travel
	 * @return	 the total delay this SPE experienced
	 */
	public int getDelay(){
		return this.delay;
	}
}
