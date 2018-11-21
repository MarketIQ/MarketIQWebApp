package com.app.server.http;
import com.app.server.http.utils.APPResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.app.server.services.SessionService;
import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("sessions")
public class SessionHttpService {


        private SessionService service;
        private ObjectWriter ow;


        public SessionHttpService() {
            service = SessionService.getInstance();
            ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        }

        @OPTIONS
        @PermitAll
        public Response optionsById() {

            return Response.ok().build();
        }

        @POST
        @Consumes({ MediaType.APPLICATION_JSON})
        @Produces({ MediaType.APPLICATION_JSON})
        public APPResponse create(Object request) {

            return new APPResponse(service.create(request));
        }

    }
