package DataTypes;

public class SPE extends SPETA{
	/**
	 * Creates a new SPE, with a virtual pointer set
	 * @param	VTPointer the VT number to be set for this SPE
	 */
	public SPE(int VTPointer){
		super(VTPointer);
	}
	
	/**
	 * Increases the delay that this SPE has encountered during it's travel
	 * @param	delay the additional delay to be added
	 */
	public void addDelay(int delay){
		this.delay += delay;
	}
	
	/**
	 * Returns a clone of this SPE object
	 */
	public SPE clone(){ 
		SPE cloneSpe = new SPE(this.getVTPointer());
		cloneSpe.delay = this.getDelay();
		return cloneSpe;
	
	}
}
