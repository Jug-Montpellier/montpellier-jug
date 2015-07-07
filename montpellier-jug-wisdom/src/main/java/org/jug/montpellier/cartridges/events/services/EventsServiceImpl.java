package org.jug.montpellier.cartridges.events.services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jug.montpellier.core.controller.JugController;
import org.jug.montpellier.models.Event;
import org.montpellierjug.store.jooq.tables.daos.EventDao;
import org.montpellierjug.store.jooq.tables.daos.SpeakerDao;
import org.montpellierjug.store.jooq.tables.daos.TalkDao;
import org.montpellierjug.store.jooq.tables.interfaces.IEvent;
import org.montpellierjug.store.jooq.tables.interfaces.ISpeaker;
import org.montpellierjug.store.jooq.tables.interfaces.ITalk;
import org.wisdom.api.http.Result;

/**
 * Created by manland on 26/04/15.
 */
@Component
@Provides(specifications = EventsService.class)
@Instantiate
public class EventsServiceImpl implements EventsService {

    @Requires
    EventDao eventDao;
    @Requires
    TalkDao talkDao;
    @Requires
    SpeakerDao speakerDao;
    @Requires
    DSLContext dslContext;

    private List<Event> getAndBuild(Condition condition) {
        List<IEvent> eventsPojo = dslContext
                .select().from(org.montpellierjug.store.jooq.tables.Event.EVENT)
                .where(condition)
                .orderBy(org.montpellierjug.store.jooq.tables.Event.EVENT.DATE.desc())
                .fetchInto(org.montpellierjug.store.jooq.tables.pojos.Event.class);
        return Event.build(eventsPojo);
    }

    private Event fillCurrent(IEvent currentEvent) {
        List<org.jug.montpellier.models.Talk> talks = talkDao.fetchByEventId(currentEvent.getId()).stream().map((ITalk talk) -> {
            return org.jug.montpellier.models.Talk.build(talk);
        }).collect(Collectors.toList());
        List<ISpeaker> speakers = talks.stream().map((org.jug.montpellier.models.Talk talk) -> {
            ISpeaker speaker = speakerDao.fetchOneById(talk.getSpeakerId());
            talk.setSpeaker(speaker);
            return speaker;
        }).collect(Collectors.toList());
        return Event.build(currentEvent, speakers, talks);
    }

    @Override
    public Result renderEvents(JugController.Templatable templatable) {
        Timestamp today = new Timestamp(Calendar.getInstance().getTime().getTime());
        List<Event> upcomingEvents = getAndBuild(org.montpellierjug.store.jooq.tables.Event.EVENT.DATE.greaterOrEqual(today));
        List<Event> pastEvents = getAndBuild(org.montpellierjug.store.jooq.tables.Event.EVENT.DATE.lessThan(today));

        Event currentEvent = null;
        if(!upcomingEvents.isEmpty()) {
            currentEvent = upcomingEvents.get(0);
        } else {
            currentEvent = pastEvents.get(0);
        }

        if(currentEvent != null) {
            currentEvent = fillCurrent(currentEvent);
        }

        return templatable
                .withParam("upcomingEvents", upcomingEvents)
                .withParam("pastEvents", pastEvents)
                .withParam("currentEvent", currentEvent)
                .render();
    }

    @Override
    public Result renderEvents(JugController.Templatable templatable, Long id) {
        Timestamp today = new Timestamp(Calendar.getInstance().getTime().getTime());
        List<Event> upcomingEvents = getAndBuild(org.montpellierjug.store.jooq.tables.Event.EVENT.DATE.greaterOrEqual(today));
        List<Event> pastEvents = getAndBuild(org.montpellierjug.store.jooq.tables.Event.EVENT.DATE.lessThan(today));

        IEvent currentEvent = eventDao.findById(id);

        if(currentEvent != null) {
            currentEvent = fillCurrent(currentEvent);
        }

        return templatable
                .withParam("upcomingEvents", upcomingEvents)
                .withParam("pastEvents", pastEvents)
                .withParam("currentEvent", currentEvent)
                .render();
    }

}
