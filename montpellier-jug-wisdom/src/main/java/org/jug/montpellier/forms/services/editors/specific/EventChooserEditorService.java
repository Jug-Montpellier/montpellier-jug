package org.jug.montpellier.forms.services.editors.specific;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.models.Event;
import org.montpellierjug.store.jooq.tables.daos.EventDao;
import org.wisdom.api.annotations.View;
import org.wisdom.api.templates.Template;

/**
 * Created by fteychene on 04/06/2015
 */
@Component
@Provides(specifications = EditorService.class)
@Instantiate
public class EventChooserEditorService implements EditorService {

    @View("editors/specific/eventchooser")
    Template editorTemplate;
    @View("views/base/string")
    Template viewTemplate;

    @Requires
    private EventDao eventDao;

    @Override
    public Class<? extends Object> getEditedType() {
        return Event.class;
    }

    @Override
    public Editor createFormEditor(Object model) {
        EventChooserEditor editor = new EventChooserEditor(editorTemplate, viewTemplate, this, eventDao);
        editor.setValue(model);
        return editor;
    }
}
