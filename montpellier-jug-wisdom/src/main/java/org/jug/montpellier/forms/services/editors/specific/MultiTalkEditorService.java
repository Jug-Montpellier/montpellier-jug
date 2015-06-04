package org.jug.montpellier.forms.services.editors.specific;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.montpellierjug.store.jooq.tables.daos.TalkDao;
import org.wisdom.api.annotations.View;
import org.wisdom.api.templates.Template;

/**
 * Created by fteychene on 04/06/2015.
 */
@Component
@Provides(specifications = EditorService.class)
@Instantiate
public class MultiTalkEditorService implements EditorService {

    @View("editors/specific/multitalkchooser")
    private Template editorTemplate;
    @View("views/specific/multitalkchooser")
    private Template viewTemplate;
    @Requires
    private TalkDao talkDao;

    @Override
    public Class<? extends Object> getEditedType() {
        return null;
    }

    @Override
    public Editor createFormEditor(Object model) {
        MultiTalkEditor editor = new MultiTalkEditor(editorTemplate, viewTemplate, this, talkDao);
        editor.setValue(model);
        return editor;
    }
}
