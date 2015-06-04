package org.jug.montpellier.models;

import org.jug.montpellier.forms.annotations.ListView;
import org.jug.montpellier.forms.annotations.Property;
import org.jug.montpellier.forms.services.editors.extended.BigStringEditorService;
import org.jug.montpellier.forms.services.editors.extended.DateEditorService;
import org.jug.montpellier.forms.services.editors.extended.WebSiteUrlEditorService;
import org.jug.montpellier.forms.services.editors.specific.MultiSpeakerEditorService;
import org.jug.montpellier.forms.services.editors.specific.MultiTalkEditorService;
import org.montpellierjug.store.jooq.tables.interfaces.IEvent;
import org.montpellierjug.store.jooq.tables.interfaces.ISpeaker;
import org.montpellierjug.store.jooq.tables.interfaces.ITalk;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by manland on 26/04/15.
 */
@ListView(title = "Events", labels = {"Title", "Date"}, columns = {"title", "date"})
public class Event implements IEvent {

    @Property(visible = false)
    private Long id;
    @Property(displayLabel = "Title")
    private String title;
    @Property(displayLabel = "Date", description = "The day of the event", editorService = DateEditorService.class)
    private Timestamp date;
    @Property(displayLabel = "Capacity", description = "The number max of participants")
    private Integer capacity;
    @Property(displayLabel = "Description", editorService = BigStringEditorService.class)
    private String description;
    @Property(displayLabel = "Location")
    private String location;
    @Property(displayLabel = "Map")
    private String map;
    @Property(visible = false)
    private Boolean open;
    @Property(displayLabel = "Registration URL", description = "The url where participant can be registered (eventbrite)", editorService = WebSiteUrlEditorService.class)
    private String registrationurl;
    @Property(displayLabel = "Report", description = "Tell if the event was a success or not")
    private String report;

//    @Property(displayLabel = "Speakers", editorService = MultiSpeakerEditorService.class)
    @Property(visible = false)
    private List<ISpeaker> speakers;
//    @Property(displayLabel = "Talks", editorService = MultiTalkEditorService.class)
    @Property(visible = false)
    private List<ITalk> talks;

    @Override
    public IEvent setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public IEvent setCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    @Override
    public Integer getCapacity() {
        return this.capacity;
    }

    @Override
    public IEvent setDate(Timestamp date) {
        this.date = date;
        return this;
    }

    @Override
    public Timestamp getDate() {
        return this.date;
    }

    @Override
    public IEvent setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public IEvent setLocation(String location) {
        this.location = location;
        return this;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public IEvent setMap(String map) {
        this.map = map;
        return this;
    }

    @Override
    public String getMap() {
        return this.map;
    }

    @Override
    public IEvent setOpen(Boolean open) {
        this.open = open;
        return this;
    }

    @Override
    public Boolean getOpen() {
        return this.open;
    }

    @Override
    public IEvent setRegistrationurl(String registrationurl) {
        this.registrationurl = registrationurl;
        return this;
    }

    @Override
    public String getRegistrationurl() {
        return this.registrationurl;
    }

    @Override
    public IEvent setReport(String report) {
        this.report = report;
        return this;
    }

    @Override
    public String getReport() {
        return this.report;
    }

    @Override
    public IEvent setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    //TODO : delete event partner
    @Override
    public IEvent setPartnerId(Long value) {
        return this;
    }

    //TODO : delete event partner
    @Override
    public Long getPartnerId() {
        return null;
    }

    @Override
    public void from(IEvent from) {
        setCapacity(from.getCapacity());
        setDate(from.getDate());
        setDescription(from.getDescription());
        setId(from.getId());
        setLocation(from.getLocation());
        setMap(from.getMap());
        setOpen(from.getOpen());
        setRegistrationurl(from.getRegistrationurl());
        setTitle(from.getTitle());
        setReport(from.getReport());
    }

    @Override
    public <E extends IEvent> E into(E into) {
        into.from(this);
        return into;
    }

    /**
     * #########################
     * ADDED TO POJO
     * #########################
     */

    public IEvent setSpeakers(List<ISpeaker> speakers) {
        this.speakers = speakers;
        return this;
    }

    public List<ISpeaker> getSpeakers() {
        return this.speakers;
    }

    public IEvent setTalks(List<ITalk> talks) {
        this.talks = talks;
        return this;
    }

    public List<ITalk> getTalks() {
        return this.talks;
    }

    /**
     * #########################
     * UTILITIES
     * #########################
     */

    public static Event build(IEvent eventPojo) {
        Event event = new Event();
        event.from(eventPojo);
        return event;
    }

    public static <T extends IEvent> List<Event> build(Stream<T> from) {
        return from.map((IEvent elem) -> build(elem)).collect(Collectors.toList());
    }

    public static <T extends IEvent> List<Event> build(List<T> from) {
        return build(from.stream());
    }

    public static Event build(IEvent ievent, List<ISpeaker> speakers) {
        Event event = Event.build(ievent);
        event.setSpeakers(speakers);
        return event;
    }

    public static Event build(IEvent ievent, List<ISpeaker> speakers, List<Talk> talks) {
        Event event = Event.build(ievent);
        event.setSpeakers(speakers);
        List<ITalk> italks = talks.stream().map((Talk talk) -> {
            return talk;
        }).collect(Collectors.toList());
        event.setTalks(italks);
        return event;
    }

    /**
     * #########################
     * FOR THE VIEW
     * #########################
     */

    public String getDisplayableDate() {
        return DateFormat.getDateInstance(DateFormat.LONG, Locale.FRANCE).format(getDate());
    }

}
