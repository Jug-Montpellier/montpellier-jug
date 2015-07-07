package org.jug.montpellier.admin.controllers;

import java.io.IOException;

import org.apache.felix.ipojo.annotations.Requires;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.oltu.oauth2.common.OAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Path;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.bodies.NoHttpBody;
import org.wisdom.api.cache.Cache;
import org.wisdom.api.configuration.ApplicationConfiguration;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.security.Authenticated;
import org.wisdom.oauth2.OAuth2WisdomAuthenticator;
import org.wisdom.oauth2.controller.Role;

/**
 * Created by christophem on 17/06/15.
 */
@Controller
@Path("/admin/logout")
@Authenticated(OAuth2WisdomAuthenticator.NAME)
public class AdminLogoutController extends DefaultController {

    static final Logger LOGGER = LoggerFactory.getLogger(AdminLogoutController.class);

    @Requires
    ApplicationConfiguration configuration;

    @Requires
    private Cache cache;


    @Role("admin")
    @Route(method = HttpMethod.GET, uri = "/")
    public Result revokeToken() {
        final String token = retrieveToken();
        cache.remove(token);
        try {
            String clientId = configuration.get("oauth2.clientId");
            String clientSecret = configuration.get("oauth2.clientSecret");
            String revokeLocation = configuration.get("oauth2.revokeLocation");
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                    new UsernamePasswordCredentials(clientId, clientSecret));
            CloseableHttpClient client = HttpClients.custom()
                    .setDefaultCredentialsProvider(credsProvider).build();
            // Create AuthCache instance
            AuthCache authCache = new BasicAuthCache();
            // Generate BASIC scheme object and add it to the local
            // auth cache
            BasicScheme basicAuth = new BasicScheme();
            HttpHost target = new HttpHost("accounts.google.com", 443, "https");
            authCache.put(target, basicAuth);
            // Add AuthCache to the execution context
            HttpClientContext localContext = HttpClientContext.create();
            localContext.setAuthCache(authCache);

            StringBuilder sb = new StringBuilder();
            sb.append(revokeLocation).append("?").append("token=").append(token);
            HttpGet httpGet = new HttpGet(sb.toString());

            CloseableHttpResponse response = client.execute(httpGet, localContext);
            try {
                if (response.getStatusLine().getStatusCode()==200){
                    return redirect("/");
                } else {
                    return status(response.getStatusLine().getStatusCode()).render(NoHttpBody.INSTANCE);
                }

            } finally {
                response.close();
            }
        } catch (IOException e) {
           LOGGER.error("impossible to invalidate token",e);
        }
        return status(500).render(NoHttpBody.INSTANCE);
    }

    private String retrieveToken() {
        String token = session(OAuth.OAUTH_ACCESS_TOKEN);
        if (token == null)
            token = request().getHeader(OAuth.OAUTH_ACCESS_TOKEN);
        if (token == null)
            token = request().parameter(OAuth.OAUTH_ACCESS_TOKEN);

        return token;
    }

}