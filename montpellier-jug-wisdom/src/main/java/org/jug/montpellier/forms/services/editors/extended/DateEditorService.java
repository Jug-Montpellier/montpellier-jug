package org.jug.montpellier.forms.services.editors.extended;

import java.util.Date;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.services.editors.base.TimestampAsDateEditor;
import org.wisdom.api.annotations.View;
import org.wisdom.api.templates.Template;

/**
 * Created by Eric Taix on 08/03/2015.
 */
@Component
@Provides(specifications = EditorService.class)
@Instantiate
public class DateEditorService implements EditorService {

    @View("editors/extended/date")
    Template editorTemplate;
    @View("views/base/stringAsText")
    Template viewTemplate;

    @Override
    public Class<? extends Object> getEditedType() {
        return Date.class;
    }

    @Override
    public Editor createFormEditor(Object model) {
        TimestampAsDateEditor editor = new TimestampAsDateEditor(editorTemplate,viewTemplate, this);
        editor.setValue(model);
        return editor;
    }

}
