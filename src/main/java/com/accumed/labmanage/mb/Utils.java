/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accumed.labmanage.mb;

import com.accumed.pposervice.ws.PPO_Service;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author smutlak
 */
public class Utils {

    private PPO_Service service;

    private PPO_Service getPPPService() {
        String wsdlURL = "";
        try {
            wsdlURL = (String) (new InitialContext().lookup("java:comp/env/com.accumed.labManage.PPO.WSDL.URL"));
        } catch (NumberFormatException | NamingException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        if (service == null) {
            try {
                service = new PPO_Service(new java.net.URL(wsdlURL));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return service;
    }

    protected void validate() {
        try {
            com.accumed.pposervice.ws.ScrubScrubbingRequest request = new com.accumed.pposervice.ws.ScrubScrubbingRequest();
            java.lang.String user = "ACCUMED";
            java.lang.String psw = "@CCUMED";
            java.lang.Boolean debug = Boolean.FALSE;

            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            com.accumed.pposervice.ws.ScrubResponseReturn result = port.validateClaim(request, user, psw, debug);
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
