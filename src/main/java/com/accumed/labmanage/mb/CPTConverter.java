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

@FacesConverter("cptConverter")
public class CPTConverter implements Converter, Serializable {

    private PPO_Service service;

    @Override
    public com.accumed.pposervice.ws.FindCptResponse.Return getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            for (com.accumed.pposervice.ws.FindCptResponse.Return p : getCPts(value)) {
                if (p.getCode().equals(value)) {
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof com.accumed.pposervice.ws.FindCptResponse.Return) {
            com.accumed.pposervice.ws.FindCptResponse.Return xcode = (com.accumed.pposervice.ws.FindCptResponse.Return) value;
            return xcode.getCode();
        }
        return (String) value;
    }

    private List<com.accumed.pposervice.ws.FindCptResponse.Return> getCPts(String query) {
        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            java.lang.String code = query;
            java.lang.String desc = "";
            // TODO process result here
            java.util.List<com.accumed.pposervice.ws.FindCptResponse.Return> result = port.findCpt(code, desc);
            System.out.println("Result = " + result);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(CPTConverter.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        return null;
    }

    private PPO_Service getPPPService() {
        String wsdlURL = "";
        try {
            wsdlURL = (String) (new InitialContext().lookup("java:comp/env/com.accumed.labManage.PPO.WSDL.URL"));
        } catch (NumberFormatException | NamingException ex) {
            Logger.getLogger(CPTConverter.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        if (service == null) {
            try {
                service = new PPO_Service(new java.net.URL(wsdlURL));
            } catch (MalformedURLException ex) {
                Logger.getLogger(CPTConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return service;
    }

}
