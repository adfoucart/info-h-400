/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.lhist.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrien Foucart
 */
public class Database {
    
    private String driver = "com.mysql.jdbc.Driver";
    private String userName = "student";
    private String password = "1234";
    private String url = "jdbc:mysql://192.168.1.110/medical";
    private Connection conn = null;
    private static Database instance = null;
    
    private Database(){
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Database getInstance(){
        if( Database.instance == null ){
            Database.instance = new Database();
        }
        
        return Database.instance;
    }
    
    public Connection getConnection(){
        return conn;
    }
}
