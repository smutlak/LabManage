/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.accumed.labmanage.mb;

import com.accumed.pposervice.ws.PPO_Service;
import com.accumed.pposervice.ws.ScrubResponseReturnClaimClaimType;
import com.accumed.pposervice.ws.ScrubScrubbingRequestClaim;
import com.accumed.pposervice.ws.ScrubScrubbingRequestClaimActivity;
import com.accumed.pposervice.ws.ScrubScrubbingRequestClaimClaimType;
import com.accumed.pposervice.ws.ScrubScrubbingRequestClaimContract;
import com.accumed.pposervice.ws.ScrubScrubbingRequestClaimDiagnosis;
import com.accumed.pposervice.ws.ScrubScrubbingRequestClaimEncounter;
import com.accumed.pposervice.ws.ScrubScrubbingRequestClaimPatient;
import com.accumed.pposervice.ws.ScrubScrubbingRequestClaimPatientPatientInsurance;
import com.accumed.pposervice.ws.ScrubScrubbingRequestHeader;
import com.accumed.pposervice.ws.ScrubScrubbingRequestHeaderExtendedValidationType;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

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

    private List<ScrubScrubbingRequestClaimClaimType> convert(List<ScrubResponseReturnClaimClaimType> src) {
        List<ScrubScrubbingRequestClaimClaimType> ret = new ArrayList();
        for (ScrubResponseReturnClaimClaimType src1 : src) {
            ScrubScrubbingRequestClaimClaimType dest = new ScrubScrubbingRequestClaimClaimType();
            dest.setType(src1.getType());
        }
        return ret;
    }

    protected com.accumed.pposervice.ws.ScrubResponseReturn validate(String userName,
            String senderID, String receiverID, String gender,
            Date DOB, List<com.accumed.pposervice.ws.FindCptResponse.Return> cpts,
            List<com.accumed.pposervice.ws.FindIcdResponse.Return> icds) {
        com.accumed.pposervice.ws.ScrubResponseReturn result = null;
        try {
            com.accumed.pposervice.ws.ScrubScrubbingRequest request = buildRequest(userName,
                    senderID, receiverID, "Analyzing", gender,
                    dateFormatter.format(DOB), cpts, icds);

            java.lang.String user = "ACCUMED";
            java.lang.String psw = "@CCUMED";
            java.lang.Boolean debug = Boolean.FALSE;

            com.accumed.pposervice.ws.PPO port = getPPPService().getPPOPort();

            //call first time
            result = port.validateClaim(request, user, psw, debug);

            request.getClaim().getClaimType().addAll(convert(result.getClaim().getClaimType()));

            request.getHeader().getExtendedValidationType().remove(0);
            ScrubScrubbingRequestHeaderExtendedValidationType extValidatioType = new ScrubScrubbingRequestHeaderExtendedValidationType();
            extValidatioType.setType("Submission");
            request.getHeader().getExtendedValidationType().add(extValidatioType);

            //call second time
            result = port.validateClaim(request, user, psw, debug);

            System.out.println("Result = " + result);
        } catch (Exception ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    protected com.accumed.pposervice.ws.ScrubScrubbingRequest buildRequest(String userName,
            String senderID, String receiverID, String extValidatioTypeStr, String gender,
            String DOB, List<com.accumed.pposervice.ws.FindCptResponse.Return> cpts,
            List<com.accumed.pposervice.ws.FindIcdResponse.Return> icds) {

        //Build Contract
        ScrubScrubbingRequestClaimContract contract = new ScrubScrubbingRequestClaimContract();
        contract.setNetwork("Undefined");
        contract.setPackageName("Undefined");
        contract.setPolicy("Undefined");
        contract.setSubNetworkName("Undefined");

        //Build PatientInsurance
//        ScrubScrubbingRequestClaimPatientPatientInsurance patientInsurance = new ScrubScrubbingRequestClaimPatientPatientInsurance();
//        patientInsurance.set
        //Build Patient
        ScrubScrubbingRequestClaimPatient patient = new ScrubScrubbingRequestClaimPatient();
        patient.setGENDERID(gender);
        patient.setDATEOFBIRTH(DOB);
        //there are more
        patient.setPatientInsurance(null);

        //Build header
        ScrubScrubbingRequestHeaderExtendedValidationType extValidatioType = new ScrubScrubbingRequestHeaderExtendedValidationType();
        extValidatioType.setType(extValidatioTypeStr);
        ScrubScrubbingRequestHeader header = new ScrubScrubbingRequestHeader();
        header.getExtendedValidationType().add(extValidatioType);
        header.setReceiverID(receiverID);
        header.setWorkflow(null);

        //Build Claim
        ScrubScrubbingRequestClaim claim = new ScrubScrubbingRequestClaim();
        claim.setDateSettlement(null);
        claim.setDenialCode(null);
        claim.setEmiratesIDNumber("784-1937-1472627-5");
        claim.setFileAttached(Boolean.FALSE);
        claim.setGross(0);
        claim.setID(0);
        claim.setIDPayer("0");
        claim.setIdCaller(0);
        claim.setImported(Boolean.TRUE);
        claim.setMemberID("UnKnown");
        claim.setNet(0);
        claim.setPatientShare(0);
        claim.setPayerID(receiverID);
        claim.setPaymentReference(null);
        claim.setPending(Boolean.FALSE);
        claim.setProviderID(senderID);
        claim.setProviderInvoiceAmount(0);
        claim.setStatus(null);

        //complex
        claim.setContract(contract);
        claim.setPatient(patient);
        claim.setRegulatorMemberInfo(null);
        claim.setResubmission(null);

        //Build encounter 
        ScrubScrubbingRequestClaimEncounter encounter = new ScrubScrubbingRequestClaimEncounter();
        encounter.setEnd(dateFormatter.format(new Date()));
        encounter.setEndType(1);
        encounter.setFacilityID(senderID);
        encounter.setIdCaller(0);
        encounter.setNewPatient(1);
        encounter.setOrderingClinician(DOB);
        encounter.setPatientID(DOB);
        encounter.setStart(dateFormatter.format(new Date()));
        encounter.setStartType(1);
        encounter.setTransferDestination(null);
        encounter.setTransferSource(null);
        encounter.setType(1);
        encounter.setAuthorisation(null);

        //comlpex 2
        claim.getEncounter().add(encounter);

        //Build activities
        for (com.accumed.pposervice.ws.FindCptResponse.Return cpt : cpts) {
            ScrubScrubbingRequestClaimActivity act = new ScrubScrubbingRequestClaimActivity();
            act.setCode(cpt.getCode());
            act.setQuantity(1);
            act.setID("0");
            act.setType(3);
            act.setStart(dateFormatter.format(new Date()));

            act.setClinician(DOB);
            act.setCopayment(0);
            act.setDeductible(0);
            act.setDenialCode(null);
            act.setGross(0);
            act.setIdCaller(0);
            act.setList(0);
            act.setManualPrices(Boolean.TRUE);
            act.setNet(0);
            act.setOrderingClinician(DOB);
            act.setPatientShare(0);
            act.setPaymentAmount(0);
            act.setPriorAuthorizationID(DOB);
            act.setProviderCode(DOB);
            act.setProviderNet(0);
            act.setProviderPatientShare(0);
            act.setProviderType(Integer.MIN_VALUE);
            act.setSysList(0);
            act.setSysNet(0);

            claim.getActivity().add(act);
        }

        //Build Diagnosis
        int diagCnt = 0;
        for (com.accumed.pposervice.ws.FindIcdResponse.Return icd : icds) {
            ScrubScrubbingRequestClaimDiagnosis diag = new ScrubScrubbingRequestClaimDiagnosis();
            diag.setCode(icd.getCode());
            diag.setIdCaller(0);
            diag.setProviderCode(null);
            diag.setProviderType(null);
            diag.setType(diagCnt == 0 ? "Principal" : "Secondary");
            claim.getDiagnosis().add(diag);
            diagCnt++;
        }

        com.accumed.pposervice.ws.ScrubScrubbingRequest req = new com.accumed.pposervice.ws.ScrubScrubbingRequest();

        //req main settings
        req.setExcludeDBRules(Boolean.FALSE);
        req.setCallingApp("LabManage");
        req.setCallingAppVersion("1.0");
        req.setUserID(0);
        req.setUserName(userName);
        req.setSender(senderID);
        InetAddress hostname = null;
        try {
            hostname = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        req.setCallingServer(hostname == null ? "UnKnown" : hostname.getHostName());
        req.setTop20(0);

        //req settings for complex objects
        req.setClaim(claim);
        req.setHeader(header);

        return req;
    }

    private static ScrubScrubbingRequestHeaderExtendedValidationType[] getExtendedValidationType(String[] validationType) {
        ScrubScrubbingRequestHeaderExtendedValidationType[] arr = new ScrubScrubbingRequestHeaderExtendedValidationType[validationType.length];
        int i = 0;
        for (String type : validationType) {
            ScrubScrubbingRequestHeaderExtendedValidationType tt = new ScrubScrubbingRequestHeaderExtendedValidationType();
            tt.setType(type);
            arr[i++] = tt;
        }
        return arr;
    }

}
