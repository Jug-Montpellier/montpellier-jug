package org.jug.montpellier.forms.services.editors.extended;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.services.editors.base.StringEditor;
import org.wisdom.api.annotations.View;
import org.wisdom.api.templates.Template;

/**
 * Created by Eric Taix on 08/03/2015.
 */
@Component
@Provides(specifications = EditorService.class)
@Instantiate
public class ImageUrlEditorService implements EditorService {

    @View("editors/extended/imageurl")
    Template editorTemplate;
    @View("views/extended/imageurl")
    Template viewTemplate;

    @Override
    public Class<? extends Object> getEditedType() {
        return null;
    }

    @Override
    public Editor createFormEditor(Object model) {
        StringEditor editor = new StringEditor(editorTemplate, viewTemplate, this);
        editor.setValue(model);
        return editor;
    }

}
