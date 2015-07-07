package org.jug.montpellier.forms.services.editors.specific;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.PropertyValue;
import org.jug.montpellier.forms.services.editors.base.BaseEditor;
import org.wisdom.api.Controller;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

/**
 * Created by fteychene on 04/06/2015
 */
public class MultiStringEditor extends BaseEditor implements Editor {

    private final Template editorTemplate;
    private String[] values;

    public MultiStringEditor(Template editorTemplate, Template viewTemplate, EditorService factory) {
        super(factory, viewTemplate);
        this.editorTemplate = editorTemplate;
    }

    @Override
    public Object getValue() {
        return values;
    }

    @Override
    public void setValue(Object value) {
        values = (String[]) value;
    }

    @Override
    public String getAsText() {
        return Arrays.toString(values);
    }

    @Override
    public Renderable getEditor(Controller controller, PropertyValue property) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("property", property);
        return editorTemplate.render(controller, parameters);
    }
}
