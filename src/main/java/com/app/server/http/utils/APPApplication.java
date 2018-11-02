package com.app.server.http.utils;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Collections;

//@ApplicationPath("/")

@Path("")

public class APPApplication extends ResourceConfig {

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n");
    }
    public APPApplication() {
//        MeteringService.getInstance();
          packages("com.app.server.http");

        System.out.println("Application Instantiated");
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    public Object getAll() {
        Version ver = new Version("0.2.37", "2018-08-10");
        return ver;
    }

    public class Version {
        String version, date;
        public Version(String version,String date) {
            this.version = version;
            this.date = date;
        }

    }



    @Provider
    public class OptionsAcceptHeader implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext requestContext,
                           ContainerResponseContext responseContext) throws IOException {

            if ("OPTIONS".equals(requestContext.getMethod())) {
                if (responseContext.getHeaderString("Accept-Patch")==null) {
                    responseContext.getHeaders().put(
                            "Accept-Patch", Collections.<Object>singletonList("application/json-patch+json"));
                }
            }
        }
    }
}