package distSys1parallel;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import ki.types.ds.Block;
import ki.types.ds.StreamInfo;
import se.umu.cs._5dv147.a1.client.DefaultStreamServiceClient;
import se.umu.cs._5dv147.a1.client.FrameAccessor;
import se.umu.cs._5dv147.a1.client.StreamServiceClient;

public class ParallelFrameAccessor implements FrameAccessor {

	private ServerInfo info;
	private StreamServiceClient[] clients;
	private String streamName;
	private SimpleFrame frame;
	private ThreadCommunicator communicator;
	private StreamInfo streamInfo;

	public ParallelFrameAccessor(StreamServiceClient[] clients, String streamName) {

		try {
			for (StreamInfo info : clients[0].listStreams()) {
				if (info.getName().equals(streamName)) {
					this.streamInfo = info;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.clients = clients;
		this.streamName = streamName;

	}

	@Override
	public Frame getFrame(int frameID) throws IOException, SocketTimeoutException {
		ThreadCommunicator com = new ThreadCommunicator(streamInfo.getWidthInBlocks(), streamInfo.getHeightInBlocks());
		SimpleFrame frame = new SimpleFrame(streamInfo.getWidthInBlocks(), streamInfo.getHeightInBlocks());
		for (StreamServiceClient streamServiceClient : clients) {
			Downloader downloader = new Downloader(com, frameID, frame, streamServiceClient, streamName);
			Initiation.threads.add(downloader);
			downloader.start();
		}
		return frame;
	}

	@Override
	public PerformanceStatistics getPerformanceStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StreamInfo getStreamInfo() throws IOException, SocketTimeoutException {
		// TODO Auto-generated method stub
		return null;
	}
}
