package distSys1parallel;

import java.io.IOException;
import java.net.SocketTimeoutException;

import ki.types.ds.Block;
import se.umu.cs._5dv147.a1.client.FrameAccessor.Frame;

/**
 * A frame consisting of blocks.
 */
public class SimpleFrame implements Frame {

	private int width;
	private int height;

	private Block[][] frameBlocks;

	public SimpleFrame(int width, int height) {
		this.width = width;
		this.height = height;

		frameBlocks = new Block[width][height];
	}

	public Block getBlock(int blockX, int blockY) throws IOException, SocketTimeoutException {

		if (blockX >= 0 && blockX < width && blockY >= 0 && blockY < height) {
			/* TODO Consult the teacher about null */
			return frameBlocks[blockX][blockY];
		} else {
			return null;
		}

	}

	public void setBlock(Block block, int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			frameBlocks[x][y] = block;
		}
	}

}