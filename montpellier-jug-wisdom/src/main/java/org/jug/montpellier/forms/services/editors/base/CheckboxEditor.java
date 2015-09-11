package org.jug.montpellier.forms.services.editors.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.PropertyValue;
import org.wisdom.api.Controller;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

/**
 * Created by fteychene on 02/06/2015.
 */
public class CheckboxEditor extends BaseEditor implements Editor {

    private final Template editorTemplate;
    private List<? extends Object> value;

    public CheckboxEditor(Template editorTemplate, EditorService service) {
        super(service, editorTemplate);
        this.editorTemplate = editorTemplate;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (List<? extends Object>) value;
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
