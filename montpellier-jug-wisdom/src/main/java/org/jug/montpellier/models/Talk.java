package org.jug.montpellier.models;

import org.jug.montpellier.forms.annotations.ListView;
import org.jug.montpellier.forms.annotations.Property;
import org.jug.montpellier.forms.services.editors.extended.BigStringEditorService;
import org.jug.montpellier.forms.services.editors.specific.EventChooserEditorService;
import org.jug.montpellier.forms.services.editors.specific.SpeakerChooserEditorService;
import org.montpellierjug.store.jooq.tables.interfaces.IEvent;
import org.montpellierjug.store.jooq.tables.interfaces.ISpeaker;
import org.montpellierjug.store.jooq.tables.interfaces.ITalk;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Eric Taix on 21/03/2015.
 */
@ListView(title = "Talks", labels = {"Date", "Titre"}, columns = {"datetime", "title"})
public class Talk implements ITalk {

    @Property(visible = false)
    private Long id;
    @Property(displayLabel = "Title")
    private String title;
    @Property(displayLabel = "Teaser", editorService = BigStringEditorService.class)
    private String teaser;
    @Property(displayLabel = "Start time")
    private String datetime;
    @Property(displayLabel = "Event", editorService = EventChooserEditorService.class)
    private Long eventid;
    @Property(displayLabel = "OrderInEvent?")
    private Integer orderinevent;
    @Property(displayLabel = "Speaker", editorService = SpeakerChooserEditorService.class)
    private Long speakerid;
    @Property(visible = false)
    private ISpeaker speaker;
    @Property(displayLabel = "Links")
    private String[] links;

    public static Talk build(ITalk from) {
        Talk talk = new Talk();
        talk.from(from);
        return talk;
    }

    public static <T extends ITalk> List<Talk> build(Stream<T> from) {
        return from.map((ITalk elem) -> build(elem)).collect(Collectors.toList());
    }

    public static <T extends ITalk> List<Talk> build(List<T> from) {
        return build(from.stream());
    }

    @Override
    public ITalk setId(Long value) {
        id = value;
        return this;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public ITalk setOrderinevent(Integer value) {
        orderinevent = value;
        return this;
    }

    @Override
    public Integer getOrderinevent() {
        return orderinevent;
    }

    @Override
    public ITalk setTeaser(String value) {
        teaser = value;
        return this;
    }

    @Override
    public String getTeaser() {
        return teaser;
    }

    @Override
    public ITalk setDatetime(String value) {
        datetime = value;
        return this;
    }

    @Override
    public String getDatetime() {
        return datetime;
    }

    @Override
    public ITalk setTitle(String value) {
        title = value;
        return this;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public ITalk setEventId(Long value) {
        eventid = value;
        return this;
    }

    @Override
    public Long getEventId() {
        return eventid;
    }

    @Override
    public ITalk setSpeakerId(Long value) {
        speakerid = value;
        return this;
    }

    @Override
    public Long getSpeakerId() {
        return speakerid;
    }

    @Override
    public ITalk setLinks(String[] value) {
        links = value;
        return this;
    }

    @Override
    public String[] getLinks() {
        return links;
    }

    @Override
    public void from(ITalk from) {
        setId(from.getId());
        setOrderinevent(from.getOrderinevent());
        setTeaser(from.getTeaser());
        setDatetime(from.getDatetime());
        setTitle(from.getTitle());
        setEventId(from.getEventId());
        setSpeakerId(from.getSpeakerId());
        setLinks(from.getLinks());
    }

    @Override
    public <E extends ITalk> E into(E into) {
        into.from(this);
        return into;
    }

    /**
     * #########################
     * ADDED TO POJO
     * #########################
     */

    public ITalk setSpeaker(ISpeaker speaker) {
        this.speaker = speaker;
        return this;
    }

    public ISpeaker getSpeaker() {
        return this.speaker;
    }
}
