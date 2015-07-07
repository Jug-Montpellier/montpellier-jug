package org.jug.montpellier.admin;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.montpellierjug.store.jooq.tables.daos.SpeakerDao;
import org.montpellierjug.store.jooq.tables.pojos.Speaker;
import org.wisdom.api.http.Result;
import org.wisdom.api.interception.RequestContext;
import org.wisdom.oauth2.controller.AuthorityProvider;

/**
 * Created by chelebithil on 14/03/15.
 */
@Provides
@Component
@Instantiate
public class JUGAuthoritiesProvider implements AuthorityProvider {

    @Requires
    SpeakerDao speakerDao;

    @Override
    public Set<String> getAuthority(String email) {
        List<Speaker> speakers = speakerDao.fetchByEmail(email);
        if(speakers.isEmpty())
            return Collections.emptySet();
        Speaker speaker = speakers.get(0);
        if(speaker.getJugmember())
          return Collections.singleton("admin");
        return Collections.singleton("speaker");
    }

    @Override
    public Result handle(RequestContext requestContext, Set<String> set) {
        return null;
    }
}
