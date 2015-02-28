package DataTypes;

public class SONETFrame extends SONETFrameTA{
	/**
	 * Create a new SONET frame to be sent on the network
	 * @param	spe the spe inside the frame
	 */
	public SONETFrame(SPE spe){
		super(spe);
	}

	/**
	 * Increases the delay that this frame has encountered during it's travel
	 * @param	delay the additional delay to be added
	 */
	public void addDelay(int delay){
		this.delay += delay;
	}
}
