package org.jug.montpellier.cartridges.speakers.services;

import java.util.List;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.core.controller.JugController;
import org.montpellierjug.store.jooq.tables.daos.SpeakerDao;
import org.montpellierjug.store.jooq.tables.daos.TalkDao;
import org.montpellierjug.store.jooq.tables.interfaces.ISpeaker;
import org.montpellierjug.store.jooq.tables.pojos.Talk;
import org.wisdom.api.http.Result;


@Component
@Provides(specifications = SpeakersService.class)
@Instantiate
public class SpeakersServiceImpl implements SpeakersService {

    @Requires
    TalkDao talkDao;
    @Requires
    SpeakerDao speakerDao;


    @Override
    public Result renderSpeakers(JugController.Templatable templatable) {
        return templatable
                .withParam("speakers", speakerDao.findAll())
                .withParam("showAllSpeakers", true)
                .render();
    }

    @Override
    public Result renderSpeakers(JugController.Templatable templatable, Long id) {
        ISpeaker speaker = speakerDao.findById(id);
        List<Talk> speakersTalks = talkDao.fetchBySpeakerId(speaker.getId());

        return templatable
                .withParam("speaker", speaker)
                .withParam("speakersTalks", speakersTalks)
                .withParam("speakersTalksLength", speakersTalks.size() > 0)
                .render();
    }
}
