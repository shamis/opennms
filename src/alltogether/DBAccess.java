package alltogether;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBAccess {
	private static Connection conn = null;
	public static Connection getConnection(){
		try{
			Class.forName(PropertyReader.getString("DBAccess.driver")).newInstance(); //$NON-NLS-1$
			conn = DriverManager.getConnection(PropertyReader.getString("DBAccess.url"),PropertyReader.getString("DBAccess.username"),PropertyReader.getString("DBAccess.password")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			System.out.println("Connected to the database"); //$NON-NLS-1$
		} catch(Exception e ){
			e.printStackTrace();
		}
		return conn;
	}

}
