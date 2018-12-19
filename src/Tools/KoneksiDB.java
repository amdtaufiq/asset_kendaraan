/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author TAUFIQ
 */
public class KoneksiDB {
    public Connection getConnection() throws SQLException{
        Connection cnn;
        try {
            String server = "jdbc:mysql://localhost:3306/aset_kendaraan";
            String driver = "com.mysql.jdbc.Driver";
            String username = "root";
            String password = "";
            Class.forName(driver);
            cnn = DriverManager.getConnection(server,username,password);
            return cnn;
        } catch (SQLException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "error koneksi database "+ex);
            return null;
        }
    }
}
