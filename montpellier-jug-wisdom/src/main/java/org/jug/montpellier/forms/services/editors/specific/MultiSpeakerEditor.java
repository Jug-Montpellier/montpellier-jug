package org.jug.montpellier.forms.services.editors.specific;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.PropertyValue;
import org.jug.montpellier.forms.services.editors.base.BaseEditor;
import org.montpellierjug.store.jooq.tables.daos.SpeakerDao;
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
        value = (List<Long>) value;
    }

    @Override
    public Renderable getEditor(Controller controller, PropertyValue property) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("property", property);
        parameters.put("speakers",
                speakerDao.findAll().stream().sorted((s1, s2) -> s1.getFullname().compareTo(s2.getFullname()))
                        .collect(Collectors.toList()));
        return editorTemplate.render(controller, parameters);
    }
}
