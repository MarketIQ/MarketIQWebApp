package com.app.server.services;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.models.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONObject;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Instant;

public class TransactionService {
    private static TransactionService self;
    private ObjectWriter ow;
    private static TransactionService thirdPartyService;

    private TransactionService() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }
    public static TransactionService getInstance(){
        if (self == null)
            self = new TransactionService();
        return self;
    }
//
//    @POST
//    @Produces({ MediaType.APPLICATION_JSON})
//    public Transaction makePayment(Object request) {
//        try{
//            JSONObject json = null;
//            json = new JSONObject(ow.writeValueAsString(request));
//
//            Transaction transaction = convertJsonToThirdPartyPayment(json);
//
//            transaction.setPaymentStatus("SUCCESSFUL");
//            transaction.setPaymentTime(String.valueOf(Instant.now().getEpochSecond()));
//            return transaction;
//        } catch(JsonProcessingException e) {
//            System.out.println("Failed to create a document");
//            throw new APPInternalServerException(33, "Payment Failed");
//        } catch(Exception e){
//            System.out.println("Failed to create a document");
//            throw new APPInternalServerException(33, "Payment Failed");
//        }
//
//    }
//
//    private Transaction convertJsonToThirdPartyPayment(JSONObject json){
//        Transaction transaction = new Transaction(
//                    json.getString("userReferenceId"), json.getString("paymentType"),
//                    json.getString("paymentAmount"),
//                    json.getString("paymentCurrency"),
//                    json.getString("requestTime"),
//                    json.getString("paymentStatus"));
//            return transaction;
//    }
}
