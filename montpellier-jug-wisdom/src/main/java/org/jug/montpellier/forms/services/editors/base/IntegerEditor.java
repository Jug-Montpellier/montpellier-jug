package org.jug.montpellier.forms.services.editors.base;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.PropertyValue;
import org.wisdom.api.Controller;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric Taix on 11/03/2015.
 */
public class IntegerEditor extends BaseEditor implements Editor {

    private final Template editorTemplate;
    private Integer value;

    public IntegerEditor(Template editorTemplate, Template viewTemplate, EditorService service) {
        super(service, viewTemplate);
        this.editorTemplate = editorTemplate;
    }

    @Override
    public void setValue(Object value) {
        this.value = (Integer) value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getAsText() {
        return value.toString();
    }

    @Override
    public Renderable getEditor(Controller controller, PropertyValue property) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("property", property);
        return editorTemplate.render(controller, parameters);
    }
}