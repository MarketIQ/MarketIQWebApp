//package com.app.server.services;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;
//
//public class TransactionssService {
//    private static TransactionssService self;
//    private ObjectWriter ow;
//    private static TransactionssService thirdPartyService;
//
//    private TransactionssService() {
//        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//    }
//    public static TransactionssService getInstance(){
//        if (self == null)
//            self = new TransactionssService();
//        return self;
//    }
////
////    @POST
////    @Produces({ MediaType.APPLICATION_JSON})
////    public Transactionss makePayment(Object request) {
////        try{
////            JSONObject json = null;
////            json = new JSONObject(ow.writeValueAsString(request));
////
////            Transactionss transaction = convertJsonToThirdPartyPayment(json);
////
////            transaction.setPaymentStatus("SUCCESSFUL");
////            transaction.setPaymentTime(String.valueOf(Instant.now().getEpochSecond()));
////            return transaction;
////        } catch(JsonProcessingException e) {
////            System.out.println("Failed to create a document");
////            throw new APPInternalServerException(33, "Payment Failed");
////        } catch(Exception e){
////            System.out.println("Failed to create a document");
////            throw new APPInternalServerException(33, "Payment Failed");
////        }
////
////    }
////
////    private Transactionss convertJsonToThirdPartyPayment(JSONObject json){
////        Transactionss transaction = new Transactionss(
////                    json.getString("userReferenceId"), json.getString("paymentType"),
////                    json.getString("paymentAmount"),
////                    json.getString("paymentCurrency"),
////                    json.getString("requestTime"),
////                    json.getString("paymentStatus"));
////            return transaction;
////    }
//}
