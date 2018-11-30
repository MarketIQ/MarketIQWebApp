package com.app.server.services;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.models.ThirdPartyPayment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Instant;


public class ThirdPartyPaymentService {

    private static ThirdPartyPaymentService self;
    private ObjectWriter ow;
    private static ThirdPartyPaymentService thirdPartyService;

    private ThirdPartyPaymentService() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }
    public static ThirdPartyPaymentService getInstance(){
        if (self == null)
            self = new ThirdPartyPaymentService();
        return self;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public ThirdPartyPayment makePayment(Object request) {
        try{
            JSONObject json = null;
            json = new JSONObject(ow.writeValueAsString(request));

            ThirdPartyPayment thirdPartyPayment = convertJsonToThirdPartyPayment(json);
            thirdPartyPayment.setPaymentStatus("SUCCESSFUL");
            thirdPartyPayment.setPaymentTime(String.valueOf(Instant.now().getEpochSecond()));
            return thirdPartyPayment;
        } catch(JsonProcessingException e) {
            System.out.println("Failed to create a document");
            throw new APPInternalServerException(33, "Payment Failed");
        } catch(Exception e){
            System.out.println("Failed to create a document");
            throw new APPInternalServerException(33, "Payment Failed");
        }
    }

    private ThirdPartyPayment convertJsonToThirdPartyPayment(JSONObject json){
        ThirdPartyPayment thirdPartyPayment = new ThirdPartyPayment(
                json.getString("userReferenceId"),
                json.getString("userName"),
                new Double(json.getString("paymentAmount")),
                json.getString("paymentStatus"),
                json.getString("paymentTime")
        );

        return thirdPartyPayment;
    }
}