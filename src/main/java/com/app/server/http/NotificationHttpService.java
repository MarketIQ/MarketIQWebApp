package com.app.server.http;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.http.utils.PATCH;
import com.app.server.models.MediaCompany;
import com.app.server.models.WishlistMediaCompany;
import com.app.server.services.EmailService;
import com.app.server.services.MediaCompanyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sun.xml.internal.xsom.impl.EmptyImpl;
import org.json.JSONObject;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("notifications")

public class NotificationHttpService {
    private ObjectWriter ow;
    private EmailService service=new EmailService();

    public NotificationHttpService() {
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }



    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public APPResponse create(Object request) throws JsonProcessingException {

        JSONObject json = null;
        json = new JSONObject(ow.writeValueAsString(request));

        String to = json.getString("emailAddress");
        String message = json.getString("message");;
        service.sendEmail(to,message);
        return new APPResponse(200);
    }




}

