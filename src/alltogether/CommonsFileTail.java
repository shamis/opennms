package alltogether;
import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

public class CommonsFileTail {

	private static ExecutorService executor;
	
	public static void main(String args[]){
		CommonsFileTail tail = new CommonsFileTail();
		tail.checkJBossServLog("/home/shamis/tomcat/logs");
//		tail.postRequest("http://help.vitria.com/content/o_report_a_problem.asp");
	}

	public void checkJBossServLog(String logFilePath) {

		// Check all the files related to BServ under the same path. 

		System.out.println("Log file Path : " + logFilePath);
		System.out.println("");
		File f = new File(logFilePath);
		if(f.isDirectory()){
			System.out.println("File is a diretory");
			FileFilter ff = this.new JBossFileFilter();
			File[] fileList = f.listFiles(ff);
			System.out.println("Files being monitored : ");
			executor = Executors.newFixedThreadPool(fileList.length);
			for(int i =0; i < fileList.length; i++){
				File curFile = fileList[i];
				System.out.println("File : " + curFile.getAbsolutePath());
					Tailer tailer = new Tailer(curFile, new TestTailerListener(curFile.getName()), 1000, true);
					Thread worker = new Thread(tailer);
					worker.setName(curFile.getName());
					executor.execute(worker);
			}
		}

	}
	
	public void cleanup(){
//		try {
			executor.shutdownNow();
//			executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
//			 while (!executor.isTerminated()) {
//		        }
//			System.out.println("Finished all threads");
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	class JBossFileFilter implements java.io.FileFilter {
		public boolean accept(File f) {
			if (f.isDirectory()) return false;
			String name = f.getName().toLowerCase();
			//			return name.toLowerCase().startsWith("server") && (name.toLowerCase().endsWith("log") || name.toLowerCase().endsWith("bak"));
			return name.toLowerCase().endsWith("log") || name.toLowerCase().endsWith("out");
		}//end accept
	}

}

class TestTailerListener extends TailerListenerAdapter {
	private String fileName;
	private StringBuffer error = new StringBuffer("");
	private int counter; //to print that many lines after the error;
	TestTailerListener(String fileName){
		this.fileName = fileName;
	}

	public void handle(String line) {
		if(line.toLowerCase().indexOf("error")>-1) {
			//			System.out.println("\n\nError in file : " + fileName +" Error is :: \n" + line);
			counter = 7;
		} if (--counter > 0){
			//			System.out.println(line);
			error.append("<br>" + line);
		} if (counter == 0){
			OnmsEvent event = new OnmsEvent("testEnventwithparams",2,"localhost", "ServerLogMonitor", "Major");
			event.addParm("File Name", this.fileName);
			event.addParm("ERORR", error.toString());
			event.sendEvent(event.toXml());
			error = new StringBuffer("");
		}
	}
}