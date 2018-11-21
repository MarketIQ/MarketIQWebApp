package com.app.server.http;

import com.app.server.http.utils.APPResponse;
import com.app.server.services.CompanyService;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/companies")
public class CompanyHttpService {
    private CompanyService companyService;

    //    private ObjectWriter ow;
    public CompanyHttpService() {
        companyService = CompanyService.getInstance();
//        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    }


    @POST
    @Path("/registration")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse register(Object request) {
        Object response = companyService.register(request);


        return new APPResponse(response);
    }

    @POST
    @Path("/authentication")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response createProduct(Object request) throws JsonProcessingException {
        Object response = companyService.login(request);

        if (response.equals("correct")) {
            return Response.ok().entity("{\"message\": \"user successfully logged in\"}").type("application/json").build();
        }

        return Response.serverError().type("application/json").build();
    }

}