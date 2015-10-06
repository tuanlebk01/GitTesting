package distSys1parallel;

import se.umu.cs._5dv147.a1.client.FrameAccessor;
import se.umu.cs._5dv147.a1.client.StreamServiceClient;

public class ParallelFactory implements FrameAccessor.Factory {

	public ParallelFactory(String username, int timeout, String string) {

	}

	public ParallelFactory() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public FrameAccessor getFrameAccessor(StreamServiceClient client, String streamName) {
		return null;
	}

	@Override
	public FrameAccessor getFrameAccessor(StreamServiceClient[] clients, String streamName) {
		ParallelFrameAccessor accessor = null;

		accessor = new ParallelFrameAccessor(clients, streamName);

		return accessor;
	}

}
