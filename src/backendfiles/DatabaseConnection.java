/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backendfiles;

/**
 *
 * @author lightway
 */
import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseConnection {

    Connection con = null;

    public static Connection Connect() {
      try {
            
         String dbFile = System.getenv("APPDATA") + "\\ChequeBook\\Database\\ChequeBook.db";
         Class.forName("org.sqlite.JDBC");
         Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dbFile+"");
        
         return connection;
         } catch (Exception e) {
         e.printStackTrace();
         return null;
         }
         
/*
        try {

            Class.forName("com.mysql.jdbc.Driver");
            String unicode = "?useUnicode=true&characterEncoding=UTF-8";
            Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/Chequebook" + unicode, "root", "123");

            return con;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

            return null;
       }
*/
    }

}
