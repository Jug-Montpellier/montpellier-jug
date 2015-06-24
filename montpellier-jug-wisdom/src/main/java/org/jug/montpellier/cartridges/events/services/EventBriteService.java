package org.jug.montpellier.cartridges.events.services;

import com.google.common.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.http.*;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.protocol.HttpContext;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.cache.Cache;
import org.wisdom.api.configuration.Configuration;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by cheleb on 17/06/15.
 */
@Component
@Provides
@Instantiate
public class EventBriteService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EventBriteService.class);

    private String token;

    @Requires
    private Configuration configuration;

    @Requires
    private Cache cache;

    private ExecutorService executorService = Executors.newCachedThreadPool();


    private static String EVENT_BRITE_API = "https://www.eventbriteapi.com";

    {
        token = System.getProperty("token");
        if (token == null)
            LOGGER.warn("No token provided, eventbrite api won't be available");
    }


    public List<Profile> attendees(String eventId) throws IOException {
        List<Profile> cached = cache.get(attenteesCacheKey(eventId));
        if (cached != null) {
            LOGGER.debug("Found " + cached.size() + " attendee(s) in cache.");
            return cached;
        }
        WithToken withToken = RequestBuilder.withToken(token).attendees(eventId);

        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream inputStream = new PipedInputStream(outputStream);

        executorService.submit(() -> eventBriteRequest(withToken, new StreamConsumer(outputStream)));


        TypeToken<List<Profile>> typeToken = new TypeToken<List<Profile>>() {
        };
        JSONArray array = JsonPath.parse(inputStream).read("$.attendees[*].profile");

        List<Profile> list = new ArrayList<>();

        for (Object o: array){
            LinkedHashMap p = (LinkedHashMap) o;
            Profile profile = new Profile();
            profile.setLast_name((String) p.get("last_name"));
            profile.setFirst_name((String) p.get("first_name"));
            profile.setEmail((String) p.get("email"));
            list.add(profile);
        }

        cache.set(attenteesCacheKey(eventId), list, Duration.standardSeconds(configuration.getIntegerWithDefault("eventbrite.cache", 60)));


        LOGGER.debug("Put " + list.size() + " attendee in cache.");



        return list;
    }


    public List<Event> events() throws IOException {

        List<Event> cached = cache.get("events");

        if (cached != null) {
            LOGGER.debug("Found " + cached.size() + " events in cache.");
            return cached;
        }

        WithToken withToken = RequestBuilder.withToken(token).events();

        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream inputStream = new PipedInputStream(outputStream);

        executorService.submit(() -> eventBriteRequest(withToken, new StreamConsumer(outputStream)));


        TypeToken<List<Event>> typeToken = new TypeToken<List<Event>>() {
        };
        JSONArray read = JsonPath.parse(inputStream).read("$.events[*]");

        List<Event> res = new ArrayList<>();

        for (Object o : read) {
            LinkedHashMap json = (LinkedHashMap) o;
            Event event = new Event();
            event.setId((String) json.get("id"));

            LinkedHashMap name = (LinkedHashMap) json.get("name");
            if (name == null)
                event.setName("No name");
            else
                event.setName((String) name.get("text"));
            res.add(event);
        }

        cache.set("events", res, Duration.standardSeconds(configuration.getIntegerWithDefault("eventbrite.cache", 60)));

        LOGGER.debug("Put " + res.size() + " events in cache.");

        return res;
    }

    private String attenteesCacheKey(String eventId) {
        return "attentees-" + eventId;
    }

    public void eventBriteRequest(WithToken rb, AsyncCharConsumer<Boolean> responseConsumer) {

        String request = rb.build();

        try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
            httpclient.start();
            Future<Boolean> future = httpclient.execute(
                    HttpAsyncMethods.createGet(request),
                    responseConsumer, null);
            Boolean result = future.get();
            if (result != null && result.booleanValue()) {
                LOGGER.debug("Request successfully executed: " + request);
            } else {
                LOGGER.warn("Request failed: " + request);
            }
            LOGGER.debug("Shutting down");
        } catch (IOException | InterruptedException | ExecutionException e) {
            LOGGER.error(request, e);
        }

    }


    static class StreamConsumer extends AsyncCharConsumer<Boolean> {

        private final OutputStream outputStream;

        StreamConsumer(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        protected void onResponseReceived(final HttpResponse response) {
        }

        @Override
        protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
            while (buf.hasRemaining()) {
                outputStream.write(buf.get());
            }
        }

        @Override
        protected void releaseResources() {
            try {
                outputStream.close();
            } catch (IOException e) {
                LOGGER.warn(e.getLocalizedMessage(), e);
            }
        }

        @Override
        protected Boolean buildResult(final HttpContext context) {
            return Boolean.TRUE;
        }

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
