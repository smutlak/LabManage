/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accumed.labmanage.mb;

import com.accumed.pposervice.ws.PPO_Service;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author smutlak
 */
@ManagedBean
@SessionScoped
public class Main implements Serializable {

//    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_9081/PPOService/PPO.wsdl")
    private PPO_Service service;

    private String signupEmail;
    private String signupPass;
    private String signRegualtor;
    private String signupFacilityLicense;
    private String signupRegUsr;
    private String signupRegPass;
    private Boolean PPOConnectionTest;
    private String status;
    private Long accountid;

    //for login
    private String message;
    private String username;
    private String password;
    private String facilityLicense;

    /**
     * Creates a new instance of Main
     */
    public Main() {

    }

    private PPO_Service getPPPService() {
        String wsdlURL = "";
        try {
            wsdlURL = (String) (new InitialContext().lookup("java:comp/env/com.accumed.labManage.PPO.WSDL.URL"));
        } catch (NumberFormatException | NamingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        if (service == null) {
            try {
                service = new PPO_Service(new java.net.URL(wsdlURL));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return service;
    }

    public String getSignupEmail() {
        return signupEmail;
    }

    public void setSignupEmail(String signupEmail) {
        this.signupEmail = signupEmail;
    }

    public String getSignupPass() {
        return signupPass;
    }

    public void setSignupPass(String signupPass) {
        this.signupPass = signupPass;
    }

    public String getSignRegualtor() {
        return signRegualtor;
    }

    public void setSignRegualtor(String signRegualtor) {
        this.signRegualtor = signRegualtor;
    }

    public String getSignupFacilityLicense() {
        return signupFacilityLicense;
    }

    public void setSignupFacilityLicense(String signupFacilityLicense) {
        this.signupFacilityLicense = signupFacilityLicense;
    }

    public String getSignupRegUsr() {
        return signupRegUsr;
    }

    public void setSignupRegUsr(String signupRegUsr) {
        this.signupRegUsr = signupRegUsr;
    }

    public String getSignupRegPass() {
        return signupRegPass;
    }

    public void setSignupRegPass(String signupRegPass) {
        this.signupRegPass = signupRegPass;
    }

    public String signup() {
        status = "";
        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            Long result = port.signUp(this.signupEmail, this.signupPass,
                    this.signRegualtor, this.signupFacilityLicense, this.signupRegUsr,
                    this.signupRegPass);
            System.out.println("Result = " + result);

            if (result > 0) {
                this.setAccountid(result);
                this.setUsername(signupEmail);
                this.setPassword(signupPass);
                this.setFacilityLicense(signupFacilityLicense);

                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()
                        + "/faces/initAccount.xhtml");
                return "";
            } else if (result == -1) {
                status = "Error while signing up!!";
            } else if (result == -2) {
                status = "User already exist!!";
            }
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Exception caught", ex);
        }
        return "";
    }

    public void testRegConnection() {

        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            java.lang.String result = port.testRegConnection(
                    this.signupFacilityLicense, this.signupRegUsr,
                    this.signupRegPass);
            System.out.println("Result = " + result);
            if (result != null && result.isEmpty()) {
                this.status = "Regulator connection test completed successfully.";
                this.PPOConnectionTest = true;
            } else {
                this.status = result;
                this.PPOConnectionTest = false;
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Exception caught", ex);
            this.status = ex.getMessage();
            this.PPOConnectionTest = false;
        }
        /*
        java.util.Date currDate = new java.util.Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(currDate);
        cal.add(java.util.Calendar.DAY_OF_YEAR, -95);
        java.util.Date fromDate = cal.getTime();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        
        javax.xml.ws.Holder<Integer> searchTransactionsResult = new javax.xml.ws.Holder<Integer>();
        javax.xml.ws.Holder<java.lang.String> foundTransactions = new javax.xml.ws.Holder<java.lang.String>();
        javax.xml.ws.Holder<java.lang.String> errorMessage = new javax.xml.ws.Holder<java.lang.String>();
        
        try { // Call Web Service Operation
            https.www_shafafiya_org.v2.WebservicesSoap port = service.getWebservicesSoap();
            // TODO initialize WS operation arguments here
            java.lang.String login = this.signupRegUsr;
            java.lang.String pwd = this.signupRegPass;
            int direction = 1;
            java.lang.String callerLicense = this.signupFacilityLicense;
            java.lang.String ePartner = null;
            int transactionID = 2;
            int transactionStatus = 2;
            java.lang.String transactionFileName = null;
            java.lang.String transactionFromDate = formatter.format(fromDate);
            java.lang.String transactionToDate = formatter.format(currDate);
            int minRecordCount = -1;
            int maxRecordCount = -1;
            
            port.searchTransactions(login, pwd, direction, callerLicense, ePartner, transactionID, transactionStatus, transactionFileName, transactionFromDate, transactionToDate, minRecordCount, maxRecordCount, searchTransactionsResult, foundTransactions, errorMessage);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Exception caught", ex);
            ex.printStackTrace();            
            this.status = ex.getMessage();
            this.PPOConnectionTest = false;
            return;
        }
        
        if(errorMessage.value != null && !errorMessage.value.isEmpty()){
            this.status = errorMessage.value;
            this.PPOConnectionTest = false;
            return;
        }
        
        if(searchTransactionsResult != null && searchTransactionsResult.value!=0){
            this.status = "searchTransactionsResult returned="+searchTransactionsResult.value;
            this.PPOConnectionTest = false;
            return;
        }

        this.status = "Regulator connection test completed successfully.";
        this.PPOConnectionTest = true;*/
    }

    public Boolean getPPOConnectionTest() {
        return PPOConnectionTest;
    }

    public void setPPOConnectionTest(Boolean PPOConnectionTest) {
        this.PPOConnectionTest = PPOConnectionTest;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getAccountid() {
        return accountid;
    }

    public void setAccountid(Long accountid) {
        this.accountid = accountid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String goToSignup() {
        return "signup.xhtml?faces-redirect=true";
    }

    public String login() {

        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            Long result = port.login(this.username, this.password);
            System.out.println("Result = " + result);

            if (result > 0) {
                this.setAccountid(result);
                FacesContext context = FacesContext.getCurrentInstance();
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()
                        + "/faces/initAccount.xhtml");
                return "";
            }
        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Exception caught", ex);
        }
        return "";
    }

    public String logout() {

        this.accountid = null;
        this.username = "";
        this.password = "";
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();
        return "/faces/login.xhtml?faces-redirect=true";
    }

    public String getFacilityLicense() {
        if (facilityLicense == null && this.accountid > 0) {

            try {
                com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
                java.lang.Long accountId = Long.valueOf(this.accountid);
                String result = port.getAccountFacilityLicense(accountId);
                System.out.println("Result = " + result);
                if(result != null){
                    facilityLicense = result;
                }
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Exception caught", ex);
            }

        }
        return facilityLicense==null?"Unknown":facilityLicense;
    }

    public void setFacilityLicense(String facilityLicense) {
        this.facilityLicense = facilityLicense;
    }

}
