package alltogether;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author apaxson
 */
public class OnmsEvent {
	private String uei;
	private String source;
	private int nodeid;
	private String time;
	private String host;
	private String severity;
	private Map<String, String> parms = new HashMap<String, String>();
	public OnmsEvent(String uei, int nodeid, String host, String source, String severity) {
		setUei(uei);
		setNodeid(nodeid);
		setHost(host);
		setSource(source);
		setSeverity(severity);
		Date currentTime = Calendar.getInstance().getTime();
		DateFormat dformat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		//dformat.setTimeZone(TimeZone.getTimeZone("GMT"));
		setTime(dformat.format(currentTime));
	}
	protected String getSeverity() {
		return severity;
	}
	protected void setSeverity(String severity) {
		this.severity = severity;
	}
	protected String getUei() {
		return uei;
	}
	protected void setUei(String uei) {
		this.uei = uei;
	}
	protected String getSource() {
		return source;
	}
	protected void setSource(String source) {
		this.source = source;
	}
	protected int getNodeid() {
		return nodeid;
	}
	protected void setNodeid(int nodeid) {
		this.nodeid = nodeid;
	}
	protected String getTime() {
		return time;
	}
	protected void setTime(String time) {
		this.time = time;
	}
	protected String getHost() {
		return host;
	}
	protected void setHost(String host) {
		this.host = host;
	}
	public void addParm(String key, String value) {
		parms.put(key, value);
	}
	protected Map<String, String> getParms() {
		return parms;
	}
	public String toXml() {
		StringBuffer data = new StringBuffer();
		data.append("<log>");
		data.append("<events>");
		data.append("<event>");
		data.append("<uei>" + getUei() + "</uei>");
		data.append("<source>" + getSource() + "</source>");
		data.append("<nodeid>" + getNodeid() + "</nodeid>");
		data.append("<time>" + getTime() + "</time>");
		data.append("<host>" + getHost() + "</host>");
		data.append("<severity>" + getSeverity() + "</severity>");
		data.append("<parms>");
		// Cycle through each parameter
		for (Map.Entry<String, String> e : getParms().entrySet()) {
			data.append("<parm>");
			data.append("<parmName>");
			data.append("<![CDATA[" + e.getKey() + "]]></parmName>");
			data.append("<value type=\"string\" encoding=\"text\"><![CDATA[" + e.getValue() + "]]></value>");
			data.append("</parm>");
		}
		data.append("</parms>");
		data.append("</event>");
		data.append("</events>");
		data.append("</log>");
		return data.toString();
	}
	
	public void sendEvent(String xmlData) {
		//TODO check to make sure xmlData isn't null before creating socket
		//TODO make some useful logging functions here, rather than println
		Socket socket = null;
		try {
			System.out.println("Sending data");
			socket = new Socket(PropertyReader.getString("opennmsServer"), 5817);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(xmlData);
			System.out.println("Sending data complete");
		} catch (UnknownHostException ex) {
			System.out.println("Unknown host");
		} catch (IOException ex) {
			System.out.println("I/O Exception while creating socket");
		} finally {
			try {
				socket.close();
			} catch (IOException ex) {
				System.out.println("I/O Exception when closing socket");
			}
		}
	}
}
