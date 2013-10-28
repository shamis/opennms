package alltogether;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class CommandExecutor {
	public static void main(String args[]){
		System.out.println(checkEsmsStatus());
	}
	
	public static boolean checkEsmsStatus(){
		try {
			String[] cmd = {
					"/bin/sh",
					"-c",
					PropertyReader.getString("esmsCommand")
					};
			Process p = Runtime.getRuntime().exec(cmd); //$NON-NLS-1$
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = reader.readLine();
			while (line != null && !"".equals(line)) { //$NON-NLS-1$
				System.out.println(line);
				if(null != line && !"".equals(line) && line.contains(PropertyReader.getString("esmsCommandMatcher")))
					return true;
				line = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static HashMap<String, ProjectInfo> runCommand(){
		Process p;
		HashMap<String, ProjectInfo> projectList = new HashMap<String, ProjectInfo>();
		ArrayList<String> lines = new ArrayList<String>();
		try {
			p = Runtime.getRuntime().exec(PropertyReader.getString("command")); //$NON-NLS-1$
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = reader.readLine();
			while (line != null && !"".equals(line)) { //$NON-NLS-1$
				line = reader.readLine();
				lines.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i = 0; i < lines.size(); i++){
			String line = lines.get(i);
			if (null != line && line.contains("Project Name:")) //$NON-NLS-1$
			{
				ProjectInfo tmp = new ProjectInfo();
				tmp.setProjectName(line.replaceAll("Project Name:", "").trim()); //$NON-NLS-1$ //$NON-NLS-2$
				line = lines.get(++i);
				tmp.setStatus(line.replaceAll("Status:", "").trim()); //$NON-NLS-1$ //$NON-NLS-2$
				line = lines.get(++i);
				tmp.setRuntimeServer(line.replaceAll("Runtime Server:", "").trim()); //$NON-NLS-1$ //$NON-NLS-2$
				line = lines.get(++i);
				tmp.setFeedServer(line.replaceAll("Feed Server:", "").trim()); //$NON-NLS-1$ //$NON-NLS-2$
				line = lines.get(++i);
				tmp.setReleaseMode(line.replaceAll("Release Mode:", "").trim()); //$NON-NLS-1$ //$NON-NLS-2$
				line = lines.get(++i);
				tmp.setDeploytime(line.replaceAll("Deploy Time:", "").trim()); //$NON-NLS-1$ //$NON-NLS-2$
				projectList.put(tmp.getProjectName(), tmp);
			}
		}
		System.out.println(projectList.size());
		return projectList;
	}
}
