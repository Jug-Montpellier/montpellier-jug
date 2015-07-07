package org.jug.montpellier.forms.services.editors.specific;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.PropertyValue;
import org.jug.montpellier.forms.services.editors.base.BaseEditor;
import org.montpellierjug.store.jooq.tables.daos.SpeakerDao;
import org.wisdom.api.Controller;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

/**
 * Created by Eric Taix on 21/03/2015.
 */
public class SpeakerChooserEditor  extends BaseEditor implements Editor {

    private final Template editorTemplate;
    private Long speakerId;
    private SpeakerDao speakerDao;

    public SpeakerChooserEditor(Template editorTemplate, Template viewTemplate, EditorService factory, SpeakerDao speakerDao) {
        super(factory, viewTemplate);
        this.editorTemplate = editorTemplate;
        this.speakerDao = speakerDao;
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
        List<SpeakerItem> items = speakerDao.findAll().stream()
            .map((speaker) -> {
                SpeakerItem item = new SpeakerItem();
                item.fullname = speaker.getFullname();
                item.id = speaker.getId();
                item.selected = speaker.getId().equals(speakerId);
                return item;
            })
            .sorted((s1, s2) -> s1.fullname.compareTo(s2.fullname))
            .collect(Collectors.toList());
        parameters.put("speakers", items);
        return editorTemplate.render(controller, parameters);
    }

    public class SpeakerItem {
        public String fullname;
        public Long id;
        public boolean selected;
    }
}
