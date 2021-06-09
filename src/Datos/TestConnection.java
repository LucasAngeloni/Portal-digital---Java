package Datos;

import java.sql.*;

public class TestConnection
{
   static String login = "root";
   static String password = "lucas123";
   static String url = "jdbc:mysql://localhost:3306/telar?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

   public static void main(String[] args) throws Exception
   {
      Connection conn = null;

      try
      {
         Class.forName("com.mysql.jdbc.Driver").newInstance();

         conn = DriverManager.getConnection(url,login,password);

         if (conn != null)
         {
            System.out.println("Conexión a base de datos "+url+" ... Ok");
            conn.close();
         }
      }
      catch(SQLException ex)
      {
         System.out.println(ex);
      }
      catch(ClassNotFoundException ex)
      {
         System.out.println(ex);
      }

   }
}
