package org.jug.montpellier.forms.services.editors.specific;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.montpellierjug.store.jooq.tables.daos.SpeakerDao;
import org.wisdom.api.annotations.View;
import org.wisdom.api.templates.Template;

/**
 * Created by Shinzul on 03/06/2015.
 */
@Component
@Provides(specifications = EditorService.class)
@Instantiate
public class MultiSpeakerEditorService implements EditorService {

    @View("editors/specific/multispeakerchooser")
    Template editorTemplate;
    @View("views/specific/multispeakerchooser")
    Template viewTemplate;
    @Requires
    SpeakerDao speakerDao;

    @Override
    public Class<? extends Object> getEditedType() {
        return null;
    }

    @Override
    public Editor createFormEditor(Object model) {
        MultiSpeakerEditor editor = new MultiSpeakerEditor(editorTemplate, viewTemplate, this, speakerDao);
        editor.setValue(model);
        return editor;
    }
}
