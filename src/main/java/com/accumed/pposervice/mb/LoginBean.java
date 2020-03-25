/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accumed.pposervice.mb;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tamer
 */
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        message = "";
    }
    // private NeoUser user = new NeoUser();
    private String message;
    private String username;
    private String password;
    private Object loggedInUser;
    private int type = 1;

    public String getNameOfLoggedInUser() {
//        if (loggedInUser != null) {
//            if (loggedInUser instanceof NeoUser) {
//                return ((NeoUser) loggedInUser).getUserName();
//            }
//            if (loggedInUser instanceof AccumedFacilityUser) {
//                return ((AccumedFacilityUser) loggedInUser).getAccumedUser();
//            }
//
//        }
        return null;
    }

    public Integer getIDOfLoggedInUser() {
//        if (loggedInUser != null) {
//            if (loggedInUser instanceof NeoUser) {
//                return ((NeoUser) loggedInUser).getUserId();
//            }
//            if (loggedInUser instanceof AccumedFacilityUser) {
//                return ((AccumedFacilityUser) loggedInUser).getId();
//            }
//        }
        return null;
    }

    public String getLastDeployedDate() {
        InputStream is = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/META-INF/MANIFEST.MF");

        try {
            if (is != null) {
                Manifest manifest = new Manifest(is);
                String buildTime = manifest.getMainAttributes().getValue("Build-Time");
                if (buildTime == null) {
                    return "";
                }
                return buildTime;
            } else {
                Enumeration<URL> resources = getClass().getClassLoader()
                        .getResources("META-INF/MANIFEST.MF");
                while (resources.hasMoreElements()) {
                    Manifest manifest = new Manifest(resources.nextElement().openStream());
                    String buildTime = manifest.getMainAttributes().getValue("Build-Time");
                    if (buildTime == null) {
                        return "";
                    }
                    return buildTime;

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
//            Utils.getInstance().reportErrorMessage(ex);
        }
        return "";

    }

    public String signup() {
        return "signup.xhtml?faces-redirect=true";
    }
    
    public String login() {

//        NeoUser user = NeoUserDAO.getInstance().isAuthenticated(username, password);
//        if (user != null) {
//            loggedInUser = user;
//            // AllocSecurityManager.getInstance().setLoggedInUser(user);
//            message = "";
//            // HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//            return "restricted/ValidateACFFile/index.xhtml?faces-redirect=true";
//
//        }
//        AccumedFacilityUser facilityUser = FacilityUserDAO.getInstance().isAuthenticated(username, password);
//        if (facilityUser != null) {
//            loggedInUser = facilityUser;
//            // AllocSecurityManager.getInstance().setLoggedInUser(user);
//            message = "";
//            // HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//            return "restricted/ValidateACFFile/index.xhtml?faces-redirect=true";
//        }
//
//        loggedInUser = null;
//        message = "Wrong Username / Password";
        return "/Login.xhtml";

    }

//    public String login2() {
//        return "/index.xhtml?faces-redirect=true";
//    }
    public boolean isAthenticated() {
        return loggedInUser != null;
    }

    public boolean isCoordinator() {
//        return isAthenticated() && loggedInUser instanceof NeoUser;// && ((NeoUser) loggedInUser).isCoordinator();
        return true;
    }

    public Integer getCurrentUserID() {
//        if (loggedInUser instanceof NeoUser) {
//            return ((NeoUser) loggedInUser).getUserId();
//        }
//        if (loggedInUser instanceof AccumedFacilityUser) {
//            return ((AccumedFacilityUser) loggedInUser).getId();
//        }
        return null;
    }

    public boolean isClientUser() {
        //return isAthenticated() && loggedInUser instanceof AccumedFacilityUser;
        return true;
    }

    public boolean isNormalBillingUser() {
//        return isAthenticated() && loggedInUser instanceof NeoUser;// && !((NeoUser) loggedInUser).isCoordinator();
        return true;

    }

    public String getHeaderFileName() {
        if (isCoordinator()) {
            return "header";
        }
        if (isNormalBillingUser()) {
            return "header-BUser";
        }
        if (isClientUser()) {
            return "header-client";
        }
        return null;
    }

    public String getHomePageName() {
        if (isCoordinator()) {
            return "coordinator_home";
        }
        if (isClientUser()) {
            return "client_home";
        }
        return "home";
        // return "issues";
    }

    public String logout() {

        loggedInUser = null;
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return "/Login.xhtml?faces-redirect=true";
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the loggedInUser
     */
    public Object getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * @param loggedInUser the loggedInUser to set
     */
    public void setLoggedInUser(/*NeoUser*/ Object loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }
}
