package com.accumed.labmanage.mb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.accumed.pposervice.ws.FindCptResponse;
import com.accumed.pposervice.ws.FindIcdResponse;
import com.accumed.pposervice.ws.FindInsurerResponse;
import com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse;
import com.accumed.pposervice.ws.GetFacilityMonthTransactionResponse;
import com.accumed.pposervice.ws.PPO_Service;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
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
    private List<com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return> curMonthlabs;
    private PieChartModel pieModel;
    private PieChartModel pieModel2;

    //Claim Validation
    private com.accumed.pposervice.ws.FindCptResponse.Return cptCode;
    private List<com.accumed.pposervice.ws.FindCptResponse.Return> selectedCpts;
    private com.accumed.pposervice.ws.FindIcdResponse.Return icdCode;
    private List<com.accumed.pposervice.ws.FindIcdResponse.Return> selectedIcds;
    private String gender = "1";
    private Date DOB;
    private com.accumed.pposervice.ws.FindInsurerResponse.Return insurer;

    //validation
    List<RulesOutcome> rulesAlerts;

    //last options in list.
    java.util.List<com.accumed.pposervice.ws.FindIcdResponse.Return> partitialIcdList;
    java.util.List<com.accumed.pposervice.ws.FindCptResponse.Return> partitialCptList;
    java.util.List<com.accumed.pposervice.ws.FindInsurerResponse.Return> partitialInsurersList;

    com.accumed.pposervice.ws.FindIcdResponse.Return selectedIcd;
    com.accumed.pposervice.ws.FindCptResponse.Return selectedCpt;

    private Integer selectedMonth;
    private Integer selectedYear;

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

    public List<GetAccuntTotalsVSLabsResponse.Return> getCurMonthlabs() {
        if (curMonthlabs != null && !curMonthlabs.isEmpty()) {
            if (curMonthlabs.get(0).getYear().equals(getSelectedYear()) && curMonthlabs.get(0).getMonth().equals(getSelectedMonth())) {
                return curMonthlabs;
            }
        }

        curMonthlabs = new ArrayList();
        getLabs();
        if (labs != null && !labs.isEmpty()) {
            labs.stream().filter((lab) -> (lab.getYear().equals(getSelectedYear()) && lab.getMonth().equals(getSelectedMonth()))).forEachOrdered((lab) -> {
                curMonthlabs.add(lab);
            });
        }
        return curMonthlabs;
    }

    public void setCurMonthlabs(List<GetAccuntTotalsVSLabsResponse.Return> curMonthlabs) {
        this.curMonthlabs = curMonthlabs;
    }

    private List<com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return> getLabs() {

        if (labs != null && !labs.isEmpty()) {
            return labs;
        }
        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();

            FacesContext context = FacesContext.getCurrentInstance();
            Main mainBean = context.getApplication().evaluateExpressionGet(context, "#{main}", Main.class);
            java.lang.Long accountId = mainBean.getAccountid();
            // TODO process result here
            java.util.List<com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return> result = port.getAccuntTotalsVSLabs(accountId, false);
            labs = result;
            System.out.println("Result = " + result);
        } catch (Exception ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }

        if (labs != null && !labs.isEmpty()) {
            this.setSelectedYear(labs.get(labs.size() - 1).getYear());
            this.setSelectedMonth(labs.get(labs.size() - 1).getMonth());
        }
        return labs;
    }

    public void setLabs(List<com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return> labs) {
        this.labs = labs;
    }

    @PostConstruct
    public void init() {
        {
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
            labels.add("Total Lab. amount");
            labels.add("Total claimed amount");
            //labels.add("Yellow");
            data.setLabels(labels);

            PieChartOptions opt = new PieChartOptions();
            Legend legend = new Legend();
            legend.setDisplay(false);
            opt.setLegend(legend);

            org.primefaces.model.charts.optionconfig.title.Title title = new org.primefaces.model.charts.optionconfig.title.Title();
            title.setDisplay(true);
            title.setText("Total Lab orders (AED)");
            title.setPosition("bottom");
            opt.setTitle(title);

            pieModel.setOptions(opt);
            pieModel.setData(data);
        }

        {
            pieModel2 = new PieChartModel();
            ChartData data = new ChartData();

            PieChartDataSet dataSet = new PieChartDataSet();
            List<Number> values = new ArrayList<>();
            String Percent = calcPercentage(getTotal(), getLabTotal());
            double labsPercentage = Double.parseDouble(Percent.substring(0, Percent.indexOf("%")));
            double otherPercentage  =  100-labsPercentage;
            values.add(labsPercentage);
            values.add(otherPercentage);
            //values.add(100);
            dataSet.setData(values);

            List<String> bgColors = new ArrayList<>();
            bgColors.add("rgb(255, 99, 132)");
            bgColors.add("rgb(54, 162, 235)");
            //bgColors.add("rgb(255, 205, 86)");
            dataSet.setBackgroundColor(bgColors);

            data.addChartDataSet(dataSet);
            List<String> labels = new ArrayList<>();
            labels.add("Total Lab. amount");
            labels.add("Total claimed amount");
            //labels.add("Yellow");
            data.setLabels(labels);

            PieChartOptions opt = new PieChartOptions();
            Legend legend = new Legend();
            legend.setDisplay(false);
            opt.setLegend(legend);

            org.primefaces.model.charts.optionconfig.title.Title title = new org.primefaces.model.charts.optionconfig.title.Title();
            title.setDisplay(true);
            title.setText("Total Lab orders (%)");
            title.setPosition("bottom");
            opt.setTitle(title);

            pieModel2.setOptions(opt);
            pieModel2.setData(data);
        }
    }

    public PieChartModel getPieModel() {
        //pieModel.setExtender("removeLegend");
        return pieModel;
    }

    public List<com.accumed.pposervice.ws.FindCptResponse.Return> complete(String query) {

        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
//            java.lang.String code = query;
//            java.lang.String desc = "";
            // TODO process result here
            //java.util.List<com.accumed.pposervice.ws.FindCptResponse.Return> result = port.findCpt(code, desc);
            partitialCptList = port.findCpt(query, query);
            System.out.println("Result = " + partitialCptList + "For =" + query);
            return partitialCptList;
        } catch (Exception ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        return null;
    }

    public List<com.accumed.pposervice.ws.FindIcdResponse.Return> completeICD(String query) {

        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            //java.util.List<com.accumed.pposervice.ws.FindIcdResponse.Return> result = port.findIcd(query, query);
            partitialIcdList = port.findIcd(query, query);
            System.out.println("Result = " + partitialIcdList);
            return partitialIcdList;
        } catch (Exception ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE,
                    "exception caught", ex);
        }
        return null;
    }

    public List<com.accumed.pposervice.ws.FindInsurerResponse.Return> completeInsurer(String query) {

        try { // Call Web Service Operation
            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();
            //java.util.List<com.accumed.pposervice.ws.FindInsurerResponse.Return> result = port.findInsurer(query, query);
            partitialInsurersList = port.findInsurer(query, query);
            System.out.println("Result = " + partitialInsurersList + " For = " + query);
            return partitialInsurersList;

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
        String ret = "";
        getLabs();
        if (labs != null && !labs.isEmpty()) {
            ret = new java.text.DateFormatSymbols().getMonths()[getSelectedMonth() - 1];
            ret += " - " + getSelectedYear();
        }
        return ret;
    }

    private int getTotal() {
        List<com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return> curLabs = getCurMonthlabs();
        double ret = 0;
        if (curLabs != null && !curLabs.isEmpty()) {
            for (com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return lab : curLabs) {
                ret += lab.getTotal();
            }
        }
        return (int) ret;
    }

    private int getLabTotal() {
        List<com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return> curLabs = getCurMonthlabs();
        double ret = 0;
        if (curLabs != null && !curLabs.isEmpty()) {
            for (com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return lab : curLabs) {
                ret += lab.getTotalLab();
            }
        }
        return (int) ret;
    }

    public String calcPercentage(double total, double totalLab) {
        double d = (totalLab / total) * 10000;
        int i = (int) d;
        d = new Double(i) / new Double(100);
        return d + "%";
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public FindInsurerResponse.Return getInsurer() {
        return insurer;
    }

    public void setInsurer(FindInsurerResponse.Return insurer) {
        this.insurer = insurer;
    }

    public void resetClaimValidation() {
        this.cptCode = null;
        this.icdCode = null;
        this.DOB = null;
        this.insurer = null;
        this.selectedCpts = new ArrayList();
        this.selectedIcds = new ArrayList();
    }

    public void onTabChanged() {

    }

    public void validate() {
        FacesContext context = FacesContext.getCurrentInstance();
        Main mainBean = context.getApplication().evaluateExpressionGet(context, "#{main}", Main.class);

        rulesAlerts = (new Utils()).validate(mainBean.getUsername(), mainBean.getFacilityLicense(),
                insurer == null ? "Unknown" : insurer.getAuth(),
                gender, DOB, selectedCpts, selectedIcds);
    }

    public List<RulesOutcome> getRulesAlerts() {
        return rulesAlerts;
    }

    public void setRulesAlerts(List<RulesOutcome> rulesAlerts) {
        this.rulesAlerts = rulesAlerts;
    }

    public List<FindIcdResponse.Return> getPartitialIcdList() {
        return partitialIcdList;
    }

    public void setPartitialIcdList(List<FindIcdResponse.Return> partitialIcdList) {
        this.partitialIcdList = partitialIcdList;
    }

    public List<FindCptResponse.Return> getPartitialCptList() {
        return partitialCptList;
    }

    public void setPartitialCptList(List<FindCptResponse.Return> partitialCptList) {
        this.partitialCptList = partitialCptList;
    }

    public List<FindInsurerResponse.Return> getPartitialInsurersList() {
        return partitialInsurersList;
    }

    public void setPartitialInsurersList(List<FindInsurerResponse.Return> partitialInsurersList) {
        this.partitialInsurersList = partitialInsurersList;
    }

    public FindIcdResponse.Return getSelectedIcd() {
        return selectedIcd;
    }

    public void setSelectedIcd(FindIcdResponse.Return selectedIcd) {
        this.selectedIcd = selectedIcd;
    }

    public FindCptResponse.Return getSelectedCpt() {
        return selectedCpt;
    }

    public void setSelectedCpt(FindCptResponse.Return selectedCpt) {
        this.selectedCpt = selectedCpt;
    }

    public String getAlertImage(RulesOutcome alert) {
        if (alert.getSeverity().equalsIgnoreCase("BLOCKER")) {
            return "/resources/images/Blocker.png";
        } else if (alert.getSeverity().equalsIgnoreCase("CRITICAL")) {
            return "/resources/images/Blocker.png";
        } else if (alert.getSeverity().equalsIgnoreCase("SEVERE")) {
            return "/resources/images/Severe.png";
        } else if (alert.getSeverity().equalsIgnoreCase("WARNING")) {
            return "/resources/images/Warning.png";
        } else if (alert.getSeverity().equalsIgnoreCase("INFO")) {
            return "/resources/images/Info.png";
        }
        return "/resources/images/Blocker.png";
    }

    public Integer getAlertID(RulesOutcome alert) {
        if (alert.getRuleName().startsWith("J")) {
            return Integer.parseInt(alert.getRuleName().substring(1, alert.getRuleName().indexOf('_')));
        } else if (alert.getRuleName().startsWith("HAAD")) {
            return Integer.parseInt(alert.getRuleName().substring(4, alert.getRuleName().indexOf('_')));
        } else {
            return 0;
        }
    }

    public String getAlertDescription(RulesOutcome alert) {
        if (alert.getLongMsg() != null) {
            if (alert.getLongMsg().equalsIgnoreCase("E")) {
                return "";
            } else {
                return alert.getLongMsg();
            }
        }
        return "";
    }

    public Integer getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(Integer selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public Integer getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(Integer selectedYear) {
        this.selectedYear = selectedYear;
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    public void setPieModel2(PieChartModel pieModel2) {
        this.pieModel2 = pieModel2;
    }

    
    
}
