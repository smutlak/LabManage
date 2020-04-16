/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accumed.labmanage.mb;

import com.accumed.pposervice.ws.ScrubResponseReturnOutcome;
import java.io.Serializable;

/**
 *
 * @author smutlak
 */
public class RulesOutcome implements Serializable {

    private String categories;
    private Integer id;
    private String longMsg;
    private String ruleID;
    private String ruleName;
    private String severity;
    private String shortMsg;
    private String targetObject;

    public RulesOutcome() {
    }

//    public RulesOutcome(String targetObject, ScrubResponseReturnOutcome obj) {
//        this.categories = obj.getCategories();
//        this.id = obj.getId();
//        this.longMsg = obj.getLongMsg();
//        this.ruleID = obj.getRuleID();
//        this.ruleName = obj.getRuleName();
//        this.severity = obj.getSeverity();
//        this.shortMsg = obj.getShortMsg();
//        this.targetObject = targetObject;
//    }

    public RulesOutcome(String targetObject, Integer id, String ruleID,
            String ruleName, String severity, String shortMsg, String longMsg,
            String categories) {
        this.categories = categories;
        this.id = id;
        this.longMsg = longMsg;
        this.ruleID = ruleID;
        this.ruleName = ruleName;
        this.severity = severity;
        this.shortMsg = shortMsg;
        this.targetObject = targetObject;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLongMsg() {
        return longMsg;
    }

    public void setLongMsg(String longMsg) {
        this.longMsg = longMsg;
    }

    public String getRuleID() {
        return ruleID;
    }

    public void setRuleID(String ruleID) {
        this.ruleID = ruleID;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getShortMsg() {
        return shortMsg;
    }

    public void setShortMsg(String shortMsg) {
        this.shortMsg = shortMsg;
    }

    public String getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

}
