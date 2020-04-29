/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accumed.labmanage.mb;

/**
 *
 * @author smutlak
 */
import com.accumed.pposervice.ws.PPO_Service;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@FacesConverter("insurerConverter")
public class InsurerConverter implements Converter, Serializable {

    private PPO_Service service;

    @Override
    public com.accumed.pposervice.ws.FindInsurerResponse.Return getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            for (com.accumed.pposervice.ws.FindInsurerResponse.Return p : getInsurers(value)) {
                if (p.getAuth().equals(value)) {
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof com.accumed.pposervice.ws.FindInsurerResponse.Return) {
            com.accumed.pposervice.ws.FindInsurerResponse.Return xcode = (com.accumed.pposervice.ws.FindInsurerResponse.Return) value;
            return xcode.getAuth();
        }
        return (String) value;
    }

    private List<com.accumed.pposervice.ws.FindInsurerResponse.Return> getInsurers(String query) {
        try { // Call Web Service Operation
            
            FacesContext context = FacesContext.getCurrentInstance();
            Dashboard dashboardBean = context.getApplication().evaluateExpressionGet(context, "#{dashboard}", Dashboard.class);
            if(dashboardBean != null){
                return dashboardBean.getPartitialInsurersList();
            }
//            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
//            java.lang.String auth = query;
//            java.lang.String name = query;
//            // TODO process result here
//            java.util.List<com.accumed.pposervice.ws.FindInsurerResponse.Return> result = port.findInsurer(auth, name);
//            System.out.println("Result = " + result);
//            return result;
        } catch (Exception ex) {
            Logger.getLogger(InsurerConverter.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        return null;
    }

    private PPO_Service getPPPService() {
        String wsdlURL = "";
        try {
            wsdlURL = (String) (new InitialContext().lookup("java:comp/env/com.accumed.labManage.PPO.WSDL.URL"));
        } catch (NumberFormatException | NamingException ex) {
            Logger.getLogger(InsurerConverter.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        if (service == null) {
            try {
                service = new PPO_Service(new java.net.URL(wsdlURL));
            } catch (MalformedURLException ex) {
                Logger.getLogger(InsurerConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return service;
    }

}
