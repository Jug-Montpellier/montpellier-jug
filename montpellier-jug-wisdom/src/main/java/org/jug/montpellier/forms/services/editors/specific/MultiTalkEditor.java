package org.jug.montpellier.forms.services.editors.specific;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.PropertyValue;
import org.jug.montpellier.forms.services.editors.base.BaseEditor;
import org.montpellierjug.store.jooq.tables.daos.TalkDao;
import org.montpellierjug.store.jooq.tables.interfaces.ISpeaker;
import org.montpellierjug.store.jooq.tables.interfaces.ITalk;
import org.wisdom.api.Controller;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by fteychen on 04/06/2015.
 */
public class MultiTalkEditor extends BaseEditor implements Editor {

    Template editorTemplate;
    private List<ITalk> value;
    private TalkDao talkDao;

    public MultiTalkEditor(Template editorTemplate, Template viewTemplate, EditorService factory, TalkDao talkDao) {
        super(factory, viewTemplate);
        this.editorTemplate = editorTemplate;
        this.talkDao = talkDao;
    }

    @Override
    public Object getValue() {
        return this.value;
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
        this.value = (List<ITalk>) value;
    }

    @Override
    public Renderable getEditor(Controller controller, PropertyValue property) {
        Map<String, Object> parameters = new HashMap<>();

        List<ITalk> talks = talkDao.findAll().stream().sorted((s1, s2) -> s2.getEventId().compareTo(s1.getEventId()))
                .collect(Collectors.toList());

        parameters.put("property", property);
        parameters.put("talks", talks);
        return editorTemplate.render(controller, parameters);
    }
}
