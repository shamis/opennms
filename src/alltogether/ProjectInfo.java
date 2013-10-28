package alltogether;

public class ProjectInfo {

	private String projectName;
	private String status;
	private String runtimeServer;
	private String feedServer;
	private String releaseMode;
	private String deploytime;
	private String emails;
	private int id;
	
	//from the db
	private String stoppedState;
	
	public String getStoppedState() {
		return stoppedState;
	}
	public void setStoppedState(String stopeedState) {
		this.stoppedState = stopeedState;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRuntimeServer() {
		return runtimeServer;
	}
	public void setRuntimeServer(String runtimeServer) {
		this.runtimeServer = runtimeServer;
	}
	public String getFeedServer() {
		return feedServer;
	}
	public void setFeedServer(String feedServer) {
		this.feedServer = feedServer;
	}
	public String getReleaseMode() {
		return releaseMode;
	}
	public void setReleaseMode(String releaseMode) {
		this.releaseMode = releaseMode;
	}
	public String getDeploytime() {
		return deploytime;
	}
	public void setDeploytime(String deploytime) {
		this.deploytime = deploytime;
	}
	public String getEmails() {
		return emails;
	}
	public void setEmails(String emails) {
		this.emails = emails;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
