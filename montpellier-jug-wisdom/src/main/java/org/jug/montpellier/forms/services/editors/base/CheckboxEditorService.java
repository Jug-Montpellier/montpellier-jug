package org.jug.montpellier.forms.services.editors.base;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.PropertyValue;
import org.wisdom.api.Controller;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

import java.util.List;

/**
 * Created by fteychene on 02/06/2015.
 */
@Component
@Provides(specifications = EditorService.class)
@Instantiate
public class CheckboxEditorService implements EditorService {

    @View("editors/base/checkbox")
    Template editorTemplate;

    @Override
    public Class<? extends Object> getEditedType() {
        return null;
    }

    @Override
    public Editor createFormEditor(Object model) {
        CheckboxEditor editor = new CheckboxEditor(editorTemplate, this);
        editor.setValue(model);
        return editor;
    }
}
