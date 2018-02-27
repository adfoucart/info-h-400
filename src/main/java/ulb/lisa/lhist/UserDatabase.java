/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.lhist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrien Foucart
 */
class UserDatabase {
    
    private Connection conn = null;
    
    public UserDatabase(){
        this.conn = Database.getInstance().getConnection();
    }

    public boolean authenticate(String username, String password) {
        try {
            PreparedStatement s = conn.prepareStatement("SELECT username FROM user WHERE username = ? AND password = SHA1(?)");
            s.setString(1, username);
            s.setString(2, password);
            s.executeQuery();
            
            ResultSet rs = s.getResultSet();
            if( rs.next() ){
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
}
