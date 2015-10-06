package distSys1parallel;

public class ServerInfo {
	String username;
	int timeout;
	String hostname;
	String streamName;

	public ServerInfo(String username, int timeout, String hostname, String streamname) {
		this.username = username;
		this.timeout = timeout;
		this.hostname = hostname;
		this.streamName = streamname;
	}
}
