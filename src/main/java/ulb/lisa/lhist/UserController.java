/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.lhist;

/**
 *
 * @author Adrien Foucart
 */
public class UserController {
    
    private static User current_user = null;
    private static UserDatabase db = new UserDatabase();
    
    public static boolean authenticate(String username, String password){
        if(db.authenticate(username, password)) {
            current_user = new User(username);
            return true;
        }
        
        return false;        
    }
    
}
