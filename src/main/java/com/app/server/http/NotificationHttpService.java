package com.app.server.http;

import com.app.server.http.utils.APPResponse;
import com.app.server.services.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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

