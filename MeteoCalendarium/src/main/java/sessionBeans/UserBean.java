/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import managerBeans.UserManager;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author home
 */
@Named
@RequestScoped
public class UserBean {

    
    @EJB
    UserManager um;
    
    public UserBean() {
    }
    
    public String getName() {
        return um.getLoggedUser().getEmail();
    }
}
