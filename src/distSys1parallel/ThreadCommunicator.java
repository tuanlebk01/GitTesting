package distSys1parallel;

public class ThreadCommunicator {

	private Integer counter;
	private int x = -1; // some fault by one in requestBlock
	private int y = 0;

	private int xBound;
	private int yBound;

	private boolean reachedEnd = false;

	/**
	 * 
	 * @param xBound
	 * @param yBound
	 * @param frameBound
	 */
	public ThreadCommunicator(int xBound, int yBound) {
		this.xBound = xBound;
		this.yBound = yBound;
		counter = -1;
	}

	/**
	 * 
	 * @return
	 */
	public int[] requestBlock() {
		synchronized (counter) {
			counter++;
			int[] results = new int[2];
			if (!reachedEnd) {
				x++;
				if (x == xBound) {
					x = 0;
					y++;
					if (y == yBound) {
						System.err.println("Reached end");
						reachedEnd = true;
						results[0] = -2;
						results[1] = -2;

					}

				}
				results[0] = x;
				results[1] = y;
				return results;
			}
			results[0] = -2;
			results[1] = -2;
			return results;
		}
	}
}
