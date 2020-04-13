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
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;


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
        values.add(300);
        values.add(50);
        values.add(100);
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        bgColors.add("rgb(255, 205, 86)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("Red");
        labels.add("Blue");
        labels.add("Yellow");
        data.setLabels(labels);

        pieModel.setExtender("removeLegend");
        pieModel.setData(data);
    }

    public PieChartModel getPieModel() {
        pieModel.setExtender("removeLegend");
        return pieModel;
    }
}
