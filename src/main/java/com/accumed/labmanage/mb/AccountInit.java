package com.accumed.labmanage.mb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.accumed.pposervice.ws.PPO_Service;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.el.ELException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author smutlak
 */
@ManagedBean
@RequestScoped
public class AccountInit {

    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_9081/PPOService/PPO.wsdl")
    private PPO_Service service;

    /**
     * Creates a new instance of AccountInit
     */
    public AccountInit() {
    }

    private PPO_Service getPPPService() {
        String wsdlURL = "";
        try {
            wsdlURL = (String) (new InitialContext().lookup("java:comp/env/com.accumed.labManage.PPO.WSDL.URL"));
        } catch (NumberFormatException | NamingException ex) {
            Logger.getLogger(AccountInit.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        if (service == null) {
            try {
                service = new PPO_Service(new java.net.URL(wsdlURL));
            } catch (MalformedURLException ex) {
                Logger.getLogger(AccountInit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return service;
    }

    public void getTransctions() {

        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            // TODO initialize WS operation arguments here
            FacesContext context = FacesContext.getCurrentInstance();
            Main mainBean = context.getApplication().evaluateExpressionGet(context, "#{main}", Main.class);
            java.lang.Long accountId = mainBean.getAccountid();
             
            // TODO process result here
            java.util.List<com.accumed.pposervice.ws.GetFacilityMonthTransactionResponse.Return> result = port.getFacilityMonthTransaction(accountId);
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            Logger.getLogger(AccountInit.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }

    }

}
