package org.jug.montpellier.cartridges.events.controller;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.aries.util.io.IOUtils;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jug.montpellier.cartridges.events.services.EventBriteService;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Path;
import org.wisdom.api.annotations.PathParameter;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.scheduler.Async;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.http.Status;

/**
 * Created by cheleb on 17/06/15.
 */
@Controller
@Path("/eventbrite")
public class EventBriteController extends DefaultController {

    private String token;

    @Requires
    private EventBriteService eventBriteService;

    private static String EVENT_BRITE_API = "https://www.eventbriteapi.com";

    {
        token = System.getProperty("token");
        if (token == null)
            logger().warn("No token provided, eventbrite api won't be available");
    }

    @Route(uri = "/events", method = HttpMethod.GET)
    @Async
    public Result events() throws IOException {
        return ok(eventBriteService.events());
        //return eventBriteCall(RequestBuilder.withToken(token).events());
    }

    @Route(uri = "/attendees/{id}", method = HttpMethod.GET)
    @Async
    public Result attendee(@PathParameter("id") String id) throws IOException {
        return ok(eventBriteService.attendees(id));
    }


    /**
     * EventBrite call to API in a new thread.
     *
     * @param rb
     * @return
     * @throws IOException
     */
    public Result eventBriteCall(WithToken rb) throws IOException {

        if (token == null)
            return status(Status.SERVICE_UNAVAILABLE);

        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream inputStream = new PipedInputStream(outputStream);

        new Thread() {
            @Override
            public void run() {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                try {
                    HttpGet httpget = new HttpGet(rb.build());

                    // Create a custom response handler
                    ResponseHandler<Result> responseHandler = response -> toResult(response, outputStream);

                    httpclient.execute(httpget, responseHandler);
                } catch (ClientProtocolException e) {
                    logger().error(rb.build(), e);
                } catch (IOException e) {
                    logger().error(rb.build(), e);
                } finally {
                    try {
                        httpclient.close();
                    } catch (IOException e) {
                        logger().error(rb.build(), e);
                    }
                }

            }
        }.start();

        return ok(inputStream).json();


    }

    private Result toResult(HttpResponse response, PipedOutputStream outputStream) throws IOException {
        Result result = new Result(response.getStatusLine().getStatusCode());
        // Copy headers
        for (Header h : response.getAllHeaders()) {
            result.with(h.getName(), h.getValue());
        }

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            IOUtils.copyAndDoNotCloseInputStream(entity.getContent(), outputStream);
            outputStream.close();

        }

        return result;
    }


    static class RequestBuilder implements WithToken {

        private final String token;

        private String uri;

        public static WithToken withToken(String token) {
            return new RequestBuilder(token);
        }

        private RequestBuilder(String token) {
            this.token = token;
        }

        private Map<String, String> params = new LinkedHashMap<>();

        public WithToken events() {
            this.uri = "/v3/events/search/";
            return withParam("organizer.id", "1464915124");
        }

        @Override
        public WithToken attendees(String eventId) {
            this.uri = "/v3/events/" + eventId + "/attendees/";
            return this;
        }

        public WithToken withParam(String name, String value) {
            params.put(name, value);
            return this;
        }

        /**
         * Build request for EventBrite API.
         *
         * @return
         */
        public String build() {
            if (params.isEmpty())
                return EVENT_BRITE_API + uri + "?token=" + token;

            StringBuilder stringBuilder = new StringBuilder(EVENT_BRITE_API)
                    .append(uri)
                    .append("?token=").append(token);

            for (Map.Entry<String, String> param : params.entrySet()) {
                stringBuilder.append('&')
                        .append(param.getKey()).append('=').append(param.getValue());
            }
            return stringBuilder.toString();
        }
    }

    public interface WithToken {

        WithToken events();

        WithToken attendees(String eventId);

        WithToken withParam(String name, String value);

        String build();
    }
}
