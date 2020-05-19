/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accumed.labmanage.mb;

import java.io.Serializable;

/**
 *
 * @author smutlak
 */
public class ReceiverTotalVsLab implements Serializable {

    //existsed in com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return
    protected Integer year;
    protected Integer month;
    protected String receiverid;
    protected String receivername;
    protected String senderid;
    protected Double total;
    protected Double totalLab;
    protected Integer claimsCount;

    public ReceiverTotalVsLab() {
    }

    public ReceiverTotalVsLab(com.accumed.pposervice.ws.GetAccuntTotalsVSLabsResponse.Return orig) {
        this.year = orig.getYear();
        this.month = orig.getMonth();
        this.receiverid = orig.getReceiverid();
        this.receivername = orig.getReceivername();
        this.senderid = orig.getSenderid();
        this.total = orig.getTotal();
        this.totalLab = orig.getTotalLab();
        this.claimsCount = orig.getClaimsCount();
    }

    public ReceiverTotalVsLab(Integer totalClaimCount, Double totalTotal, Double totalTotalLab) {
                
        this.year = null;
        this.month = null;
        this.receiverid = null;
        this.receivername = "Total";
        this.senderid = null;
        
        this.total = totalTotal;
        this.totalLab = totalTotalLab;
        this.claimsCount = totalClaimCount;
        
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    public String getReceivername() {
        return receivername;
    }

    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotalLab() {
        return totalLab;
    }

    public void setTotalLab(Double totalLab) {
        this.totalLab = totalLab;
    }

    public Integer getClaimsCount() {
        return claimsCount;
    }

    public void setClaimsCount(Integer claimsCount) {
        this.claimsCount = claimsCount;
    }

}
