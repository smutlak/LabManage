package com.accumed.labmanage.mb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.accumed.pposervice.ws.GetFacilityMonthTransactionResponse;
import com.accumed.pposervice.ws.PPO_Service;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author smutlak
 */
@ManagedBean
@ViewScoped
public class AccountInit implements Serializable {

    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_9081/PPOService/PPO.wsdl")
    private PPO_Service service;
    java.util.List<com.accumed.pposervice.ws.GetFacilityMonthTransactionResponse.Return> trans = null;
    private String progressStyle;
    private Integer progress;
    private Integer delayCounter = 0;
    private static final Integer DELAY_COUNT = 4;

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

    public List<GetFacilityMonthTransactionResponse.Return> getTrans() {
        if (trans == null) {
            try { // Call Web Service Operation
                com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
                // TODO initialize WS operation arguments here
                FacesContext context = FacesContext.getCurrentInstance();
                Main mainBean = context.getApplication().evaluateExpressionGet(context, "#{main}", Main.class);
                java.lang.Long accountId = mainBean.getAccountid();

                // TODO process result here
                trans = port.getFacilityMonthTransaction(accountId);
                System.out.println("Result = " + trans);
            } catch (Exception ex) {
                Logger.getLogger(AccountInit.class.getName()).log(Level.SEVERE,
                        "exception caught", ex);
            }
        }
        return trans;
    }

    public void setTrans(List<GetFacilityMonthTransactionResponse.Return> trans) {
        this.trans = trans;
    }

//    public void processTran() {
//        if(trans != null){
//            for(com.accumed.pposervice.ws.GetFacilityMonthTransactionResponse.Return t: trans){
//                if(!t.isPersist()){
//                    
//                }
//            }
//        }
//    }
    public String getTransactionsStatus() {
        java.lang.String result = "";
        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            // TODO initialize WS operation arguments here
            FacesContext context = FacesContext.getCurrentInstance();
            Main mainBean = context.getApplication().evaluateExpressionGet(context, "#{main}", Main.class);
            java.lang.Long accountId = mainBean.getAccountid();

            // TODO process result here
            result = port.getAccountTransactionStatus(accountId);
            if (result != null && !result.equalsIgnoreCase("Completed") && result.length() > 0) {
                String[] splitted = result.split("/");
                if (splitted.length > 0) {
                    int done = Integer.parseInt(splitted[0]);
                    int from = Integer.parseInt(splitted[1]);
                    double perc = done * 100 / from;
                    int iPerc = (int) perc;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AccountInit.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        return result;
    }

    public String getProgressStyle() {
        return "c100 p" + (getProgress() == null ? 0 : getProgress()) + " green";
    }

    public void setProgressStyle(String progressStyle) {
        this.progressStyle = progressStyle;
    }

    public String getProgressPercentage() {

        return (getProgress() == null ? 0 : getProgress()) + "%";
    }

    public Integer calcProgress() {
        java.lang.String result = "";
        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            // TODO initialize WS operation arguments here
            FacesContext context = FacesContext.getCurrentInstance();
            Main mainBean = context.getApplication().evaluateExpressionGet(context, "#{main}", Main.class);
            java.lang.Long accountId = mainBean.getAccountid();

            // TODO process result here
            result = port.getAccountTransactionStatus(accountId);
            if (result != null && !result.equalsIgnoreCase("Completed")
                    && result.length() > 0
                    && !result.equalsIgnoreCase("No Transactions")) {
                String[] splitted = result.split("/");
                if (splitted.length > 0) {
                    int done = Integer.parseInt(splitted[0]);
                    int from = Integer.parseInt(splitted[1]);
                    int iPerc = (int) Math.ceil(done * 100 / from);
                    progress = iPerc;
                }
            }
            if (result.equalsIgnoreCase("No Transactions")) {
                if (delayCounter <= 0) {
                    delayCounter++;
                }
                if (delayCounter >= DELAY_COUNT) {
                    delayCounter = 0;
                    context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()
                            + "/faces/dashboard.xhtml");
               }
                return 0;
            }
            if (result.equalsIgnoreCase("Completed")) {
                context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath()
                            + "/faces/dashboard.xhtml");
                return 100;
            }
        } catch (Exception ex) {
            Logger.getLogger(AccountInit.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }

        return 0;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

}
