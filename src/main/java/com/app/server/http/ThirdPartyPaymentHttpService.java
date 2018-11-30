package com.app.server.http;

import com.app.server.http.exceptions.APPInternalServerException;
import com.app.server.http.exceptions.APPNotFoundException;
import com.app.server.http.utils.APPResponse;
import com.app.server.models.ThirdPartyPayment;
import com.app.server.services.ThirdPartyPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

    @Path("/thirdpartypayment")
    public class ThirdPartyPaymentHttpService {

        private ThirdPartyPaymentService service;
        private ObjectWriter ow;

        public ThirdPartyPaymentHttpService() {
            service = ThirdPartyPaymentService.getInstance();
            ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        }

        @OPTIONS
        @PermitAll
        public Response optionsById() {
            return Response.ok().build();
        }

        @POST
        @Consumes({MediaType.APPLICATION_JSON})
        @Produces({MediaType.APPLICATION_JSON})
        public ThirdPartyPayment makePayment(Object request) {
            try {
                ThirdPartyPayment thirdPartyPayment = service.makePayment(request);
                if (thirdPartyPayment == null)
                    throw new APPNotFoundException(56,"Payment error");
                return thirdPartyPayment;
            }
            catch(IllegalArgumentException e){
                throw new APPNotFoundException(56,"Payment error");
            }
            catch (Exception e) {
                throw new APPInternalServerException(0,e.getMessage());
            }
        }
    }

