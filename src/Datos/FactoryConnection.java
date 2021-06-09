package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FactoryConnection {
	
	private static FactoryConnection instancia;
	
	private String driver="com.mysql.cj.jdbc.Driver";
	private String host="jdbc:mysql://node30397-env-6222023.es-1.axarnet.cloud/telar";
	private String port="3306";
	private String user="user";
	private String user2="root";
	private String password="tGqBxmR6zTXkIGqs";
	private String password2="lucas123";
	private String db="/telar?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private String host2="jdbc:mysql://localhost:"+port+db;
	private int conectados=0;
	protected static Connection con = null;

	private FactoryConnection() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static FactoryConnection getInstancia() {
		if (instancia == null) {
			instancia = new FactoryConnection();
		}
		return instancia;
	}
	
	public Connection getConnection() {
		try {
		   if(con==null || con.isClosed()) {
			   //con=DriverManager.getConnection(host+":"+port+"/"+db, user, password);
			   con=DriverManager.getConnection(host2, user2, password2);
			   conectados=0;
		   }
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		conectados++;
		return con;
	}
	
	public void closeConnection() throws SQLException {
		conectados--;
		try {
			if (conectados<=0)
				con.close();
		} 
		catch (SQLException e) {
			throw new SQLException(e);
		}
		catch (NullPointerException e) {}
	}

}
