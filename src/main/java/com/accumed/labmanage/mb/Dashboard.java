package com.accumed.labmanage.mb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.accumed.pposervice.ws.FindCptResponse;
import com.accumed.pposervice.ws.FindIcdResponse;
import com.accumed.pposervice.ws.FindInsurerResponse;
import com.accumed.pposervice.ws.GetFacilityMonthTransactionResponse;
import com.accumed.pposervice.ws.PPO_Service;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.optionconfig.legend.Legend;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.charts.pie.PieChartOptions;

/**
 *
 * @author smutlak
 */
@ManagedBean
@ViewScoped
public class Dashboard implements Serializable {

    //@WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_9081/PPOService/PPO.wsdl")
    private PPO_Service service;
    private List<com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return> labs;
    private PieChartModel pieModel;
    
    
    
    //Claim Validation
    private com.accumed.pposervice.ws.FindCptResponse.Return cptCode;
    private List<com.accumed.pposervice.ws.FindCptResponse.Return> selectedCpts;
    private com.accumed.pposervice.ws.FindIcdResponse.Return icdCode;
    private List<com.accumed.pposervice.ws.FindIcdResponse.Return> selectedIcds;
    private String gender = "1";
    private String DOB;
    private com.accumed.pposervice.ws.FindInsurerResponse.Return insurer;
    

    /**
     * Creates a new instance of AccountInit
     */
    public Dashboard() {
    }

    private PPO_Service getPPPService() {
        String wsdlURL = "";
        try {
            wsdlURL = (String) (new InitialContext().lookup("java:comp/env/com.accumed.labManage.PPO.WSDL.URL"));
        } catch (NumberFormatException | NamingException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        if (service == null) {
            try {
                service = new PPO_Service(new java.net.URL(wsdlURL));
            } catch (MalformedURLException ex) {
                Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return service;
    }

    public List<com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return> getLabs() {

        if (labs != null && !labs.isEmpty()) {
            return labs;
        }
        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();

            FacesContext context = FacesContext.getCurrentInstance();
            Main mainBean = context.getApplication().evaluateExpressionGet(context, "#{main}", Main.class);
            java.lang.Long accountId = mainBean.getAccountid();
            // TODO process result here
            java.util.List<com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return> result = port.getAccuntTotalsVSLabs(accountId);
            labs = result;
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        return labs;
    }

    public void setLabs(List<com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return> labs) {
        this.labs = labs;
    }

    @PostConstruct
    public void init() {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        values.add(getLabTotal());
        values.add(getTotal());
        //values.add(100);
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        //bgColors.add("rgb(255, 205, 86)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("Lab");
        labels.add("Total");
        //labels.add("Yellow");
        data.setLabels(labels);
        PieChartOptions opt = new PieChartOptions();
        Legend legend = new Legend();
        legend.setDisplay(false);
        opt.setLegend(legend);
        pieModel.setOptions(opt);
        pieModel.setData(data);
    }

    public PieChartModel getPieModel() {
        pieModel.setExtender("removeLegend");
        return pieModel;
    }

    public List<com.accumed.pposervice.ws.FindCptResponse.Return> complete(String query) {

        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            java.lang.String code = query;
            java.lang.String desc = "";
            // TODO process result here
            java.util.List<com.accumed.pposervice.ws.FindCptResponse.Return> result = port.findCpt(code, desc);
            System.out.println("Result = " + result);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        return null;
    }

    public List<com.accumed.pposervice.ws.FindIcdResponse.Return> completeICD(String query) {

        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            java.lang.String code = query;
            java.lang.String desc = "";
            // TODO process result here
            java.util.List<com.accumed.pposervice.ws.FindIcdResponse.Return> result = port.findIcd(code, desc);
            System.out.println("Result = " + result);
            return result;
        } catch (Exception ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        return null;
    }
    
    public List<com.accumed.pposervice.ws.FindInsurerResponse.Return> completeInsurer(String query) {

        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            java.lang.String auth = query;
            java.lang.String name = query;
            // TODO process result here
            java.util.List<com.accumed.pposervice.ws.FindInsurerResponse.Return> result = port.findInsurer(auth, name);
            System.out.println("Result = " + result);
            return result;
            
        } catch (Exception ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        return null;
    }

    public FindCptResponse.Return getCptCode() {
        return cptCode;
    }

    public void setCptCode(FindCptResponse.Return cptCode) {
        this.cptCode = cptCode;
    }

    public void addCPT() {
        if (this.selectedCpts == null) {
            this.selectedCpts = new ArrayList();
        }
        this.selectedCpts.add(cptCode);
        cptCode = null;
    }

    public void addICD() {
        if (this.selectedIcds == null) {
            this.selectedIcds = new ArrayList();
        }
        this.selectedIcds.add(icdCode);
        icdCode = null;
    }

    public List<FindCptResponse.Return> getSelectedCpts() {
        return selectedCpts;
    }

    public void setSelectedCpts(List<FindCptResponse.Return> selectedCpts) {
        this.selectedCpts = selectedCpts;
    }

    public FindIcdResponse.Return getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(FindIcdResponse.Return icdCode) {
        this.icdCode = icdCode;
    }

    public List<FindIcdResponse.Return> getSelectedIcds() {
        return selectedIcds;
    }

    public void setSelectedIcds(List<FindIcdResponse.Return> selectedIcds) {
        this.selectedIcds = selectedIcds;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        String sMonth = "";
        getLabs();
        if (labs != null && !labs.isEmpty()) {
            sMonth = new java.text.DateFormatSymbols().getMonths()[labs.get(0).getMonth() - 1];
        }
        return sMonth + " Details";
    }

    private int getTotal() {
        getLabs();
        double ret = 0;
        if (labs != null && !labs.isEmpty()) {
            for (com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return lab : labs) {
                ret += lab.getTotal();
            }
        }
        return (int)ret;
    }
    
    private int getLabTotal() {
        getLabs();
        double ret = 0;
        if (labs != null && !labs.isEmpty()) {
            for (com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return lab : labs) {
                ret += lab.getTotalLab();
            }
        }
        return (int)ret;
    }

    public String calcPercentage(double total, double totalLab) {
        double d = (totalLab / total) * 10000;
        int i = (int) d;
        d = new Double(i) / new Double(100);
        return d + "%";
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public FindInsurerResponse.Return getInsurer() {
        return insurer;
    }

    public void setInsurer(FindInsurerResponse.Return insurer) {
        this.insurer = insurer;
    }
    
    public void resetClaimValidation(){
        this.cptCode = null;
        this.icdCode = null;
        this.DOB = null;
        this.insurer = null;
        this.selectedCpts = new ArrayList();
        this.selectedIcds = new ArrayList();
    }
    
    
    public void onTabChanged(){
        
    }
    
    public void validate(){
        (new Utils()).validate();
    }
}
