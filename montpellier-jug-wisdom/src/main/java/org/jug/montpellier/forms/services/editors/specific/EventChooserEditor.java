package org.jug.montpellier.forms.services.editors.specific;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.PropertyValue;
import org.jug.montpellier.forms.services.editors.base.BaseEditor;
import org.montpellierjug.store.jooq.tables.daos.EventDao;
import org.montpellierjug.store.jooq.tables.interfaces.IEvent;
import org.wisdom.api.Controller;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by fteychene on 04/06/2015
 */
public class EventChooserEditor extends BaseEditor implements Editor {

    private final Template editorTemplate;
    private Long speakerId;
    private EventDao eventDao;

    public EventChooserEditor(Template editorTemplate, Template viewTemplate, EditorService factory, EventDao eventDao) {
        super(factory, viewTemplate);
        this.editorTemplate = editorTemplate;
        this.eventDao = eventDao;
    }

    @Override
    public Object getValue() {
        return speakerId;
    }

    @Override
    public void setValue(Object value) {
        speakerId = (Long) value;
    }

    @Override
    public String getAsText() {
        return speakerId != null ? speakerId.toString() : null;
    }

    @Override
    public Renderable getEditor(Controller controller, PropertyValue property) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("property", property);
        List<EventItem> items = eventDao.findAll().stream()
                .sorted((s1, s2) -> s2.getDate().compareTo(s1.getDate()))
                .map((event) -> {
                    EventItem item = new EventItem();
                    item.title = event.getTitle();
                    item.id = event.getId();
                    item.selected = event.getId().equals(speakerId);
                    return item;
                })
                .collect(Collectors.toList());

        parameters.put("speakers", items);
        return editorTemplate.render(controller, parameters);
    }

    public class EventItem {
        public String title;
        public Long id;
        public boolean selected;
    }
}
