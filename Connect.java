package Students;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Connect
{
	static Connection getConnection()
	{
	Connection con=null;
	try {
		con=DriverManager.getConnection("jdbc:mysql://localhost/project2021javafx","root","");
		System.out.println("Connected");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return con;
	
}
	public static void main(String args[])
	{
		getConnection();
	}
}