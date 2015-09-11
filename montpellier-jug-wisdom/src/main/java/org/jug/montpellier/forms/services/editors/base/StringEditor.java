package org.jug.montpellier.forms.services.editors.base;

import java.util.HashMap;
import java.util.Map;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.PropertyValue;
import org.wisdom.api.Controller;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

/**
 * Created by Eric Taix on 11/03/2015.
 */
public class StringEditor extends BaseEditor implements Editor {

    private final Template editorTemplate;
    private String value;

    public StringEditor(Template editorTemplate, Template viewemplate, EditorService factory) {
        super(factory, viewemplate);
        this.editorTemplate = editorTemplate;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String)value;
    }

    @Override
    public Object getValue() {
        return getAsText();
    }

    @Override
    public String getAsText() {
        return value;
    }

    @Override
    public Renderable getEditor(Controller controller, PropertyValue property) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("property", property);
        return editorTemplate.render(controller, parameters);
    }
}