package distSys1parallel;

import java.io.IOException;

import ki.types.ds.Block;
import se.umu.cs._5dv147.a1.client.FrameAccessor.PerformanceStatistics;
import se.umu.cs._5dv147.a1.client.StreamServiceClient;

public class Downloader extends Thread {
	private ThreadCommunicator communicator;
	private int frameID;
	private SimpleFrame frame;
	private StreamServiceClient streamServiceClient;
	private String streamName;
	private double count;
	private double drops;
	private double avarageLatency;
	private long fastestResponse;

	public Downloader(ThreadCommunicator communicator, int frameID, SimpleFrame frame,
			StreamServiceClient streamServiceClient, String streamName) {
		this.count = 0;
		this.drops = 0;
		this.avarageLatency = 0;
		this.communicator = communicator;
		this.frameID = frameID;
		this.frame = frame;
		this.streamServiceClient = streamServiceClient;
		this.streamName = streamName;
		this.fastestResponse = 0;

	}

	public void run() {
		count = 0;
		for (int i = 0; i > -1; i++) {
			int[] blockNumber = communicator.requestBlock();
			int x = blockNumber[0];
			int y = blockNumber[1];
			if (blockNumber[0] < 0) {
				saveStats();
				System.out.println("packages downloaded: " + count);
				break;
			}
			boolean var = true;
			long startTime = System.currentTimeMillis();
			while (var) {
				try {

					Block block = null;
					block = streamServiceClient.getBlock(streamName, frameID, x, y);

					frame.setBlock(block, x, y);
					var = false;
					count++;

				} catch (IOException e) {
					// System.out.println(e.getMessage());
					drops++;
				}
			}
			long endTime = System.currentTimeMillis();
			long timeTaken = endTime - startTime;

			if (timeTaken < fastestResponse || fastestResponse == 0) {
				fastestResponse = timeTaken;
			}

			if (avarageLatency == 0) {
				avarageLatency = timeTaken;
			} else {
				avarageLatency = (avarageLatency + timeTaken) / 2;
				// System.out.println(avarageLatency);
			}
		}
	}

	private void saveStats() {

		PerformanceStatistics stats = new PerformanceStatistics() {

			@Override
			public double getBandwidthUtilization() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public double getFrameThroughput() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public double getLinkBandwidth(String arg0) {
				return fastestResponse;
			}

			@Override
			public double getPacketDropRate(String arg0) {
				return drops / count;
			}

			@Override
			public double getPacketLatency(String arg0) {
				return avarageLatency;
			}

		};

		String hostname = streamServiceClient.getHost();
		int match = 0;
		for (int i = 0; i < Initiation.hosts.length; i++) {
			if (hostname == Initiation.hosts[i]) {
				match = i;
			}
		}

		Initiation.statsArray[match].add(stats);
	}
}
