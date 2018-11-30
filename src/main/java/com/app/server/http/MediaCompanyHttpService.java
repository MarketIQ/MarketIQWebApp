package com.app.server.http;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.exceptions.APPUnauthorizedException;
import com.app.server.http.utils.APPCrypt;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.MediaCompany;
import com.app.server.models.Wishlist;
import com.app.server.services.MediaCompanyService;
import com.app.server.services.WishlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("mediacompany")

public class MediaCompanyHttpService {
    private MediaCompanyService service;
    private ObjectWriter ow;
    private WishlistService wishlistService;


    public MediaCompanyHttpService() {
        service = MediaCompanyService.getInstance();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        wishlistService = new WishlistService();

    }


    @OPTIONS
    @PermitAll
    public Response optionsById() {
        return Response.ok().build();
    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getAll(@QueryParam("category") String category,
                              @QueryParam("subCategory") String subCategory) {

        return new APPResponse(service.getAll(category, subCategory));
    }

    @GET
    @Path("{emailAddress}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("emailAddress") String emailAddress) {
        try {
            MediaCompany d = service.getOne(emailAddress);
            if (d == null)
                throw new APPNotFoundException(56, "Wishlist List not found");
            return new APPResponse(d);
        }
        catch (IllegalArgumentException e) {
            throw new APPNotFoundException(56, "Wishlist List not found");
        }
        catch (Exception e) {
            throw new APPInternalServerException(0, "Something happened. Come back later.");
        }

    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse create(Object request) throws JsonProcessingException {
        return new APPResponse(service.create(request));
    }

    @PATCH
    @Path("{emailAddress}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse update(@PathParam("emailAddress") String emailAddress, MediaCompany request) {

        if (request.getName() == null || request.getEmailAddress() == null ||
        request.getPhoneNumber() == null){
            return new APPResponse(400);
        }

        return new APPResponse(service.update(emailAddress, request));

    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete() {

        return new APPResponse(service.deleteAll());
    }

    @DELETE
    @Path("{emailAddress}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete(@PathParam("emailAddress") String emailAddress) {

        return new APPResponse(service.delete(emailAddress));

    }
    //getting one subresource
    //getOneWishList
    @GET
    @Path("{emailAddress}/getwishlist/{sid}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getOneWishList(@PathParam("emailAddress") String emailAddress, @PathParam("sid") String sid) {
        try {
            Wishlist d = wishlistService.getOneWishList(emailAddress);
            if (d == null)
                throw new APPNotFoundException(56,"User not found");
            return new APPResponse(d);
        }
        catch(IllegalArgumentException e){
            throw new APPNotFoundException(56,"User not found");
        }
        catch (Exception e) {
            throw new APPInternalServerException(0,e.getLocalizedMessage());
        }

    }
    //getting subresources
    @GET
    @Path("{emailAddress}/wishlist")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getWishLists(@PathParam("emailAddress") String emailAddress) {
        try {
            List<Wishlist> wishLists = wishlistService.getWishList(emailAddress);
            if (wishLists == null || wishLists.isEmpty())
                throw new APPNotFoundException(56,"WishlistBusinessCompany is empty for the user");
            return new APPResponse(wishLists);
        }
        catch(IllegalArgumentException e){
            throw new APPNotFoundException(56,"WishlistBusinessCompany not found");
        }
        catch (Exception e) {
            throw new APPInternalServerException(0,e.getMessage());
        }

    }

    //Creating subresources
    @POST
    @Path("{emailAddress}/wishlist")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse createWishList(@PathParam("emailAddress") String emailAddress, Object request) {
        return new APPResponse(wishlistService.createWishList(emailAddress,request));
    }

    @PATCH
    @Path("{emailAddress}/updateWishlist/{sid}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse updatewishlist(@PathParam("sid") String emailAddress, Object request){

        return new APPResponse(wishlistService.updateWishlist(emailAddress,request));

    }

    //delete subresource
    @DELETE
    @Path("{emailAddress}/deletewishlist/{sid}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse deletewishlist(@PathParam("sid") String sid) {

        return new APPResponse(wishlistService.deletewishlist(sid));
    }

//    @POST
//    @Path("{emailAddress}/paymentdetails")
//    @Consumes({ MediaType.APPLICATION_JSON})
//    @Produces({ MediaType.APPLICATION_JSON})
//    public APPResponse addPaymentDetails(@Context HttpHeaders headers, @PathParam("emailAddress") String emailAddress, Object request) throws Exception {
//        //check authentication
//        if (!(checkAuthentication(headers, emailAddress))){
//            throw new APPUnauthorizedException(401,"Token Not found");
//        }
//        //add payment deatils
//        try {
//            PaymentDetails paymentDetails = service.addPaymentDetails(request);
//            if (paymentDetails == null)
//                throw new APPNotFoundException(56,"PaymentDetails is empty for the user");
//            return new APPResponse(paymentDetails);
//        }
//        catch(IllegalArgumentException e){
//            throw new APPNotFoundException(56,"PaymentDetails not found");
//        }
//        catch (Exception e) {
//            throw new APPInternalServerException(0,e.getMessage());
//        }
//
//    }

    //Authorization check
    boolean checkAuthentication(HttpHeaders headers, String id) throws Exception {
        List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null)
            throw new APPUnauthorizedException(70, "No Authorization Headers");
        String token = authHeaders.get(0);
        String clearToken = APPCrypt.decrypt(token);
        if (id.compareTo(clearToken) != 0) {
            throw new APPUnauthorizedException(71, "Invalid token. Please try getting a new token");
        }
        return true;
    }

//    @POST
//    @Path("{id}/subscription")
//    @Consumes({ MediaType.APPLICATION_JSON})
//    @Produces({ MediaType.APPLICATION_JSON})
//    public APPResponse register_subscription(@Context HttpHeaders headers, Object request) {
//        try {
//            PaymentDetails paymentDetails = service.register_subscription(headers,request);
//            if (paymentDetails == null)
//                throw new APPNotFoundException(56,"WishList is empty for the user");
//            return new APPResponse(paymentDetails);
//        }
//        catch(IllegalArgumentException e){
//            throw new APPNotFoundException(56,"WishList not found");
//        }
//        catch (Exception e) {
//            throw new APPInternalServerException(0,e.getMessage());
//        }
//
//    }

}

