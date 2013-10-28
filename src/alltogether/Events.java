package alltogether;

import java.util.HashMap;

public class Events{

	public static void main(String args[]) throws InterruptedException{
		findAndSendEvents();
	}

	public static void findAndSendEvents() {
		while(true){
			HashMap<String, ProjectInfo> projects = ProjectsDAO.getProjectsToBeMonitored(CommandExecutor.runCommand());
			HashMap<String, ProjectInfo> projectsToBeNotified = ProjectsDAO.getProjectsToBeNotified(projects);

			for(ProjectInfo project : projects.values()){
				if("STARTED".equalsIgnoreCase(project.getStatus())){
					ProjectsDAO.updateNotificationState(project.getId(), false);
				} else if ("STOPPED".equalsIgnoreCase(project.getStatus())){
					OnmsEvent event = new OnmsEvent("testEnventwithparams",2,"localhost", "ServerLogMonitor", project.getStoppedState());
					event.addParm("ProjectName", project.getProjectName().substring(project.getProjectName().lastIndexOf("/") + 1));
					System.out.println(project.getProjectName().substring(project.getProjectName().lastIndexOf("/") + 1));
					event.addParm("Status", project.getStatus());
					event.addParm("Runtime Server", project.getRuntimeServer());
					event.addParm("Feed Server", project.getFeedServer());
					event.addParm("Release Mode", project.getReleaseMode());
					event.addParm("Deploy Time", project.getDeploytime());
					event.sendEvent(event.toXml());
					for(ProjectInfo projectInfo : projectsToBeNotified.values()){
						if(project.getId() == projectInfo.getId()){
							for(String address : projectInfo.getEmails().split(",")){
								System.out.println("Sending mail to " + address);
								MailClient.sendMail(address, projectInfo.getProjectName(), projectInfo.getStoppedState());
								ProjectsDAO.updateNotificationState(projectInfo.getId(), true);
							}

							if("true".equalsIgnoreCase(PropertyReader.getString("SalesForce.raiseIssue")))
								new SalesForceIntegration().raiseIssue(projectInfo.getProjectName(), projectInfo.getStoppedState());
							continue;
						}
					}
				}
			}


			if(!CommandExecutor.checkEsmsStatus()){
				OnmsEvent event = new OnmsEvent("testEnventwithparams",2,"localhost", "ServerLogMonitor", "Major");
				event.addParm("ProjectName", "ESMS");
				event.addParm("ESMS","is not running");
				event.sendEvent(event.toXml());
			}

			//sleep for 10 mins
			System.out.println("Waiting for 10 mins");
			try {
				Thread.sleep(600000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Executing after 10 mins");
		}
	}
}
