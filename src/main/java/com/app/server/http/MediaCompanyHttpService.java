package com.app.server.http;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.MediaCompany;
import com.app.server.models.WishlistMediaCompany;
import com.app.server.services.MediaCompanyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("mediacompany")

public class MediaCompanyHttpService {
    private MediaCompanyService service;
    private ObjectWriter ow;


    public MediaCompanyHttpService() {
        service = MediaCompanyService.getInstance();
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

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
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse getOne(@PathParam("id") String id) {
        try {
            MediaCompany d = service.getOne(id);
            if (d == null)
                throw new APPNotFoundException(56, "WishlistMediaCompany List not found");
            return new APPResponse(d);
        }
        catch (IllegalArgumentException e) {
            throw new APPNotFoundException(56, "WishlistMediaCompany List not found");
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
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse update(@PathParam("id") String id, MediaCompany request) {

        if (request.getName() == null || request.getEmailAddress() == null ||
        request.getPhoneNumber() == null){
            return new APPResponse(400);
        }

        return new APPResponse(service.update(id, request));

    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete() {

        return new APPResponse(service.deleteAll());
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse delete(@PathParam("id") String id) {

        return new APPResponse(service.delete(id));

    }
    //getting one subresource
    //getOneWishList
    @GET
    @Path("{id}/getwishlist/{sid}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getOneWishList(@PathParam("id") String id,@PathParam("sid") String sid) {
        try {
            WishlistMediaCompany d = service.getOneWishList(id,sid);
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
    @Path("{id}/wishlist")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse getWishLists(@PathParam("id") String id) {
        try {
            List<WishlistMediaCompany> wishLists = service.getWishList(id);
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
    @Path("{id}/wishlist")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse createWishList(@PathParam("id") String id,Object request) {
        return new APPResponse(service.createWishList(id,request));
    }

    @PATCH
    @Path("{id}/updateWishlist/{sid}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse updatewishlist(@PathParam("sid") String id, Object request){

        return new APPResponse(service.updatewishlist(id,request));

    }

    //delete subresource
    @DELETE
    @Path("{id}/deletewishlist/{sid}")
    @Produces({ MediaType.APPLICATION_JSON})
    public APPResponse deletewishlist(@PathParam("sid") String sid) {

        return new APPResponse(service.deletewishlist(sid));
    }

}

