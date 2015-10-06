package distSys1parallel;

public class BlockInfo {
	private int x;
	private int y;
	private int frame;
	private int xBound;
	private int yBound;
	private int frameBound;
	private Object lock = new Object();

	/**
	 * Creates a new blockInfo object with the given bounds. THe bounds should
	 * be set using serverinformation gathered from the host streams.
	 * 
	 * @param xBound
	 * @param yBound
	 * @param frameBound
	 */
	public BlockInfo(int xBound, int yBound, int frameBound) {
		x = 0;
		y = 0;
		frame = 0;
		this.xBound = xBound;
		this.yBound = yBound;
		this.frameBound = frameBound;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getFrame() {
		return frame;
	}

	public void increment() {
	}

}
