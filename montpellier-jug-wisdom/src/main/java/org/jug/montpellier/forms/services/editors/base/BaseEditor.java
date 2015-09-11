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
 * Created by Eric Taix on 13/03/2015.
 */
public abstract class BaseEditor implements Editor {

    private final EditorService factory;
    private Template viewTemplate;

    public BaseEditor(EditorService factory, Template viewTemplate) {
        this.factory = factory;
        this.viewTemplate = viewTemplate;
    }
    @Override
    public EditorService service() {
        return factory;
    }

    @Override
    public Renderable getView(Controller controller, PropertyValue property) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("property", property);
        return viewTemplate.render(controller, parameters);
    }
}
