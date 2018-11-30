package com.app.server.http;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.exceptions.APPUnauthorizedException;
import com.app.server.http.utils.APPCrypt;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.BusinessCompany;
import com.app.server.models.Product;
import com.app.server.models.PaymentDetails;
import com.app.server.models.Wishlist;
import com.app.server.services.BusinessCompanyService;
import com.app.server.services.ProductService;
import com.app.server.services.WishlistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.jvnet.hk2.annotations.Optional;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("business")
public class BusinessCompanyHttpService {
    private BusinessCompanyService service;
    private ProductService productService;
    private WishlistService wishlistService;
    private ObjectWriter ow;

    public BusinessCompanyHttpService() {
        productService = ProductService.getInstance();
        service = BusinessCompanyService.getInstance();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    @OPTIONS
    @PermitAll
    public Response optionsById() {
        return Response.ok().build();
    }
//    @GET
//    @Produces({ MediaType.APPLICATION_JSON})
//    public APPResponse getAll() {
//        return new APPResponse(service.getAll());
//    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getSome(@Optional @QueryParam("category") String category) {

        if (category != null) {
            return new APPResponse(service.getSome(category));
        }
        return new APPResponse(service.getAll());
    }

    @GET
    @Path("{emailAddress}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("emailAddress") String emailAddress) {
        try {
            BusinessCompany d = service.getOne(emailAddress);
            if (d == null)
                throw new APPNotFoundException(56, "BusinessCompany not found");
            return new APPResponse(d);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new APPNotFoundException(56, "BusinessCompany not found");
        } catch (Exception e) {
            e.printStackTrace();
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
    public APPResponse update(@PathParam("emailAddress") String emailAddress, Object request) {
        return new APPResponse(service.update(emailAddress, request));
    }

    @DELETE
    @Path("{emailAddress}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete(@PathParam("emailAddress") String emailAddress) {
        return new APPResponse(service.delete(emailAddress));
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete() {
        return new APPResponse(service.deleteAll());
    }

    @GET
    @Path("{emailAddress}/products/")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getAllProducts(@Context HttpHeaders headers, @PathParam("emailAddress") String emailAddress) throws Exception {
        return new APPResponse(productService.getAllProductsInBusiness(headers, emailAddress));
    }

    @GET
    @Path("{emailAddress}/products/{pid}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getOneProduct(@PathParam("emailAddress") String emailAddress, @PathParam("pid") String productId) {
        try {
            Product d = productService.getOne(emailAddress, productId);
            if (d == null)
                throw new APPNotFoundException(56, "Product not found");
            return new APPResponse(d);
        } catch (IllegalArgumentException e) {
            throw new APPNotFoundException(56, "Product not found");
        } catch (Exception e) {
            throw new APPInternalServerException(0, "Something happened. Come back later.");
        }
    }

    @POST
    @Path("{emailAddress}/products/")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse createProduct(@PathParam("emailAddress") String emailAddress, Object request) {
        return new APPResponse(productService.create(emailAddress, request));
    }

    @PATCH
    @Path("{emailAddress}/products/{pid}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse updateProduct(@PathParam("emailAddress") String emailAddress, @PathParam("pid") String pid, Object request) {
        return new APPResponse(productService.update(emailAddress, pid, request));
    }

    @DELETE
    @Path("{emailAddress}/products/{pid}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse deleteProduct(@PathParam("emailAddress") String emailAddress, @PathParam("pid") String pid) {
        return new APPResponse(productService.delete(emailAddress, pid));
    }

    @DELETE
    @Path("{emailAddress}/products/")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse deleteAll() {
        return new APPResponse(productService.deleteAll());
    }

    //getting one subresource
    //getOneWishList
    @GET
    @Path("{emailAddress}/wishlist/{sid}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getOneWishList(@PathParam("emailAddress") String emailAddress, @PathParam("sid") String sid) {
        try {
            Wishlist d = wishlistService.getOneWishList(emailAddress);
            if (d == null)
                throw new APPNotFoundException(56, "User not found");
            return new APPResponse(d);
        } catch (IllegalArgumentException e) {
            throw new APPNotFoundException(56, "User not found");
        } catch (Exception e) {
            throw new APPInternalServerException(0, e.getLocalizedMessage());
        }

    }

    //getting subresources
    @GET
    @Path("{emailAddress}/wishlist")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getWishLists(@Context HttpHeaders headers, @PathParam("emailAddress") String emailAddress) throws Exception {

        if (!(checkAuthentication(headers, emailAddress))) {
            throw new APPUnauthorizedException(401, "Token Not found");
        }

        try {
            List<Wishlist> wishlistBusinessCompanies = wishlistService.getWishList(emailAddress);
            if (wishlistBusinessCompanies == null || wishlistBusinessCompanies.isEmpty())
                throw new APPNotFoundException(56, "WishlistBusinessCompany is empty for the user");
            return new APPResponse(wishlistBusinessCompanies);
        } catch (IllegalArgumentException e) {
            throw new APPNotFoundException(56, "WishlistBusinessCompany not found");
        } catch (Exception e) {
            throw new APPInternalServerException(0, e.getMessage());
        }

    }

    //Creating subresources
    @POST
    @Path("{emailAddress}/wishlists")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse createWishList(@PathParam("emailAddress") String emailAddress, Object request) {
        return new APPResponse(wishlistService.createWishList(emailAddress, request));
    }

    @PATCH
    @Path("{emailAddress}/wishlists/{sid}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse updatewishlist(@PathParam("sid") String id, Object request) {

        return new APPResponse(wishlistService.updateWishlist(id, request));

    }

    //delete subresource
    @DELETE
    @Path("{emailAddress}/wishlists/{sid}")
    @Produces({MediaType.APPLICATION_JSON})
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
    boolean checkAuthentication(HttpHeaders headers, String emailAddress) {
        List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaders == null)
            throw new APPUnauthorizedException(70, "No Authorization Headers");
        String token = authHeaders.get(0);
        String clearToken = null;
        try {
            clearToken = APPCrypt.decrypt(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (emailAddress.compareTo(clearToken) != 0) {
            throw new APPUnauthorizedException(71, "Invalid token. Please try getting a new token");
        }
        return true;
    }


}

