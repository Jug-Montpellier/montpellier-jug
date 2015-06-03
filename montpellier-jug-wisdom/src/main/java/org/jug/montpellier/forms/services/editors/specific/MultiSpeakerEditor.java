package org.jug.montpellier.forms.services.editors.specific;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.PropertyValue;
import org.jug.montpellier.forms.services.editors.base.BaseEditor;
import org.jug.montpellier.models.Speaker;
import org.montpellierjug.store.jooq.tables.daos.SpeakerDao;
import org.montpellierjug.store.jooq.tables.interfaces.ISpeaker;
import org.wisdom.api.Controller;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by fteychene on 02/06/2015.
 */
public class MultiSpeakerEditor extends BaseEditor implements Editor {

    Template editorTemplate;
    SpeakerDao speakerDao;

    List<Long> value;

    public MultiSpeakerEditor(Template editorTemplate, Template viewTemplate, EditorService factory, SpeakerDao speakerDao) {
        super(factory, viewTemplate);
        this.editorTemplate = editorTemplate;
        this.speakerDao = speakerDao;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getAsText() {
        if (value != null) {
            return value.toString();
        } else {
            return "";
        }
    }

    @Override
    public void setValue(Object value) {
        this.value = (List<Long>) value;
    }

    @Override
    public Renderable getEditor(Controller controller, PropertyValue property) {
        Map<String, Object> parameters = new HashMap<>();

        List<ISpeaker> speakers = speakerDao.findAll().stream().sorted((s1, s2) -> s1.getFullname().compareTo(s2.getFullname()))
                .collect(Collectors.toList());

        property.value = ((List<Long>)property.value).stream().map(m -> {
            MultiSpeakerItem item = new MultiSpeakerItem();
            item.setId(m);
            speakers.stream().filter(speaker -> speaker.getId().equals(m)).limit(1).findFirst().ifPresent(speaker -> item.setFullName(speaker.getFullname()));
            return item;
        }).collect(Collectors.toList());

        parameters.put("property", property);
        parameters.put("speakers", speakers.stream().filter(speaker -> !value.contains(speaker.getId())).collect(Collectors.toList()));
        return editorTemplate.render(controller, parameters);
    }


    static class MultiSpeakerItem {
        private Long id;
        private String fullName;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
    }
}
