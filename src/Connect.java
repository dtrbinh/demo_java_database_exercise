import java.sql.*;
public class Connect {

	public static void main(String args[]) throws
		ClassNotFoundException,SQLException {
		System.out.println("Dang ket noi CSDL...");
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String urlDB = "jdbc:sqlserver://CATCH-ME:1433;"
		            + "databaseName=javaDataDemo;"
		            + "integratedSecurity=true;"
		            + "encrypt = true;"
		            + "trustServerCertificate=true;";
			Connection cnn = DriverManager.getConnection(urlDB);
			Statement stmt = cnn.createStatement();
			System.out.println("Ket noi thanh cong.");
			
			//them record
			String sql1="INSERT INTO Table1(ID,Name,Address,Total) VALUES('5','NguyenC','HCM','900')";
			stmt.executeUpdate(sql1);
			
			//Cap nhat du lieu
			String sql2="UPDATE Table1 SET Total = Total*1.1";
			int n=stmt.executeUpdate(sql2);
			if (n < 1) 
				System.out.println("Khong co ban ghi nao duoc cap nhat");
			else System.out.println("Co "+n+" ban ghi duoc cap nhat");
			
			String sql="SELECT ID,Name,Address,Total FROM Table1";
			ResultSet rs=stmt.executeQuery(sql);
			while (rs.next())
			{ 
				int id=rs.getInt("ID");
				double l=rs.getDouble("Total");
				String s=rs.getString("Name");
				String d=rs.getString("Address");
				System.out.println("ID=" +id +" " + s+ " " + d + " Total=" + l) ;
			}
			cnn.close();
			System.out.print("Da ngat ket noi CSDL");
		} 
		catch(SQLException e) 
			{
			System.out.println("Error: " + e.toString());
			}
	}
}
			