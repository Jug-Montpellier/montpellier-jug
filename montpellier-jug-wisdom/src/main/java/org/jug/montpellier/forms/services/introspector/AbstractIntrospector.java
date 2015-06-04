package org.jug.montpellier.forms.services.introspector;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorRegistry;
import org.jug.montpellier.forms.models.PropertyValue;
import org.wisdom.api.Controller;

import java.lang.reflect.Field;

/**
 * Created by Eric Taix on 04/06/15.
 */
public abstract class AbstractIntrospector {

    public abstract EditorRegistry getEditorRegistry();

    protected PropertyValue buildPropertyValue(Object object, Field field, org.jug.montpellier.forms.models.Property property, Controller controller) throws IllegalAccessException, ClassNotFoundException {
        Editor editor = getEditorRegistry().createEditor(field.get(object), field.getType(), property);
        if (editor != null) {
            PropertyValue propertyValue = new PropertyValue();
            propertyValue.name = field.getName();
            propertyValue.displayname = property != null && property.getLabel() != null && !property.getLabel().isEmpty() ? property.getLabel() : field.getName();
            propertyValue.description = property != null && property.getDescription() != null && !property.getDescription().isEmpty() ? property.getDescription() : "";
            propertyValue.value = editor.getValue();
            propertyValue.valueAsText = editor.getAsText();
            propertyValue.editorName = editor.service().getClass().getSimpleName().toLowerCase();
            propertyValue.visible = property != null ? property.isVisible() : propertyValue.visible;
            propertyValue.editor = editor.getEditor(controller, propertyValue).content();
            propertyValue.view = editor.getView(controller, propertyValue).content();
            return propertyValue;
        }
        return null;
    }
}
