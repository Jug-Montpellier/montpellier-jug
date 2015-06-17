package org.jug.montpellier.cartridges.events.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Path;
import org.wisdom.api.annotations.PathParameter;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.scheduler.Async;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by cheleb on 17/06/15.
 */
@Controller
@Path("/eventbrite")
public class EventBriteService extends DefaultController{

    private String token=System.getProperty("token");

    @Route(uri="/events", method = HttpMethod.GET)
    @Async
    public Result events() throws IOException {
       return ramaining("/v3/events/search/", "&organizer.id=1464915124");
    }

    @Route(uri="/attendees/{id}", method = HttpMethod.GET)
    @Async
    public Result attendee(@PathParameter("id") String id) throws IOException {
        return ramaining("/v3/events/"+id+"/attendees/", "");
    }

    public Result ramaining(String url, String extra) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet("https://www.eventbriteapi.com"+url+"?token="+token+extra);

            // Create a custom response handler
            ResponseHandler<Result> responseHandler = new ResponseHandler<Result>() {

                @Override
                public Result handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? ok(EntityUtils.toString(entity)) : noContent();
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            return httpclient.execute(httpget, responseHandler);
        } finally {
            httpclient.close();
        }



    }
///v3/
}
