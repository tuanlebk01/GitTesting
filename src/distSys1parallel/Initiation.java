package distSys1parallel;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import ki.types.ds.Block;
import ki.types.ds.StreamInfo;
import se.umu.cs._5dv147.a1.client.DefaultStreamServiceClient;
import se.umu.cs._5dv147.a1.client.FrameAccessor;
import se.umu.cs._5dv147.a1.client.FrameAccessor.Frame;
import se.umu.cs._5dv147.a1.client.FrameAccessor.PerformanceStatistics;
import se.umu.cs._5dv147.a1.client.StreamServiceClient;
import se.umu.cs._5dv147.a1.client.StreamServiceDiscovery;

public class Initiation {

	public static SimpleFrame[] data;
	public static ArrayList<Thread> threads;
	private static int timeout = 500;
	private static String username = "c12osn";
	public static String[] hosts;
	private static String streamName = "stream7";
	public static ThreadCommunicator communicator;
	public static SimpleFrame[] database;
	private static StreamServiceClient[] clients;
	private static int xSize;
	private static int ySize;
	public static LinkedBlockingQueue<PerformanceStatistics>[] statsArray;

	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();
		threads = new ArrayList<Thread>();
		StreamServiceClient[] clients = getMetaData();

		StreamInfo streamInfo = null;
		for (StreamServiceClient streamServiceClient : clients) {
			System.out.println(streamServiceClient.getHost());
			try {
				for (StreamInfo info : clients[0].listStreams()) {
					if (info.getName().equals(streamName)) {
						streamInfo = info;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		statsArray = new LinkedBlockingQueue[hosts.length];
		for (int i = 0; i < statsArray.length; i++) {
			statsArray[i] = new LinkedBlockingQueue<PerformanceStatistics>();
		}
		
		
		//database = new SimpleFrame[streamInfo.getLengthInFrames()];
		database = new SimpleFrame[3];
		
		
		xSize = streamInfo.getWidthInBlocks();
		ySize = streamInfo.getHeightInBlocks();

		ParallelFactory factory = new ParallelFactory();
		FrameAccessor accessor = factory.getFrameAccessor(clients, streamName);

		for (int i = 0; i < database.length; i++) {
			try {
				database[i] = (SimpleFrame) accessor.getFrame(i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		
		
		double maximumDifference = 0;
		double lastResult = 0;
		double result = 0;
		while (true) {
			lastResult = result;
			result = calculateBuffer();
			double difference = result - lastResult;
			if (maximumDifference < difference) {
				maximumDifference = difference;
			}
			System.out.println("Buffer filled to: " + result * 100 + "%");
			if (result == 1) {
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Time taken = " + (endTime - startTime));
		// all tasks have finished or the time has been reached.
		System.out.println(
				"Maxiumum speed kB/s: " + (maximumDifference * database.length * xSize * ySize * 16 * 16 * 3) / 1000);
		
		for(int i = 0; i < statsArray.length; i++){
			double averageLatency = 0;
			for(int j = 0; j < statsArray[i].size(); j++){
				
				try {
					PerformanceStatistics stat = statsArray[i].take();
					averageLatency += stat.getPacketLatency("");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.err.println(e.getMessage());
				}
			}
			averageLatency = averageLatency/statsArray[i].size();
			int resLatency = (int)averageLatency; 
			System.out.println("Latency for "+clients[i].getHost() + " " +resLatency + "ms");
		}
	}

	private static StreamServiceClient[] getMetaData() {
		hosts = StreamServiceDiscovery.SINGLETON.findHosts();
		StreamServiceClient[] clients = new StreamServiceClient[hosts.length];
		int i = 0;
		for (String host : hosts) {
			try {
				clients[i] = DefaultStreamServiceClient.bind(host, timeout, username);
				i++;
			} catch (SocketException | UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return clients;
	}

	private static double calculateBuffer() {
		double empty = database.length * xSize * ySize;
		double filled = 0;
		for (int i = 0; i < database.length; i++) {
			for (int x = 0; x < xSize; x++) {
				for (int y = 0; y < ySize; y++) {
					try {
						if (database[i].getBlock(x, y) != null) {
							filled++;
						}
					} catch (Exception e) {
						// do nothing
					}
				}
			}
		}
		return filled / empty;
	}

	private static void printStatistics() {

	}
}
