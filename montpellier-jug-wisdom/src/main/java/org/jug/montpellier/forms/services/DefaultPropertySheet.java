package org.jug.montpellier.forms.services;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.forms.annotations.Property;
import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorRegistry;
import org.jug.montpellier.forms.apis.PropertySheet;
import org.jug.montpellier.forms.models.PropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.Controller;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Eric Taix on 07/03/2015.
 */
@Component
@Provides(specifications = PropertySheet.class)
@Instantiate
public class DefaultPropertySheet implements PropertySheet {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultPropertySheet.class);

    @Requires
    EditorRegistry editorRegistry;
    @View("editors/propertysheet")
    Template template;

    public DefaultPropertySheet() {
    }

    @Override
    public Renderable getRenderable(Controller controller, Object object) throws IntrospectionException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
       return getRenderable(controller, object, Maps.newHashMap());
    }

    @Override
    public Renderable getRenderable(Controller controller, Object object, Map<String, Object> additionalParameters) throws IntrospectionException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Preconditions.checkNotNull(object, "PropertySheet getRenderable need a value as parameter");
        List<PropertyValue> defs = Arrays.asList(object.getClass().getDeclaredFields()).stream().map((Field field) -> {
            field.setAccessible(true);
            try {
                Property property = field.getAnnotation(Property.class);
                Editor editor = editorRegistry.createEditor(field.get(object), field.getType(), property);
                if (editor != null && property.visible()) {
                    PropertyValue propertyValue = new PropertyValue();
                    propertyValue.name = field.getName();
                    propertyValue.displayname = property != null && property.displayLabel() != null && !property.displayLabel().isEmpty() ? property.displayLabel() : field.getName();
                    propertyValue.description = property != null && property.description() != null && !property.description().isEmpty() ? property.description() : "";
                    propertyValue.value = editor.getValue();
                    propertyValue.valueAsText = editor.getAsText();
                    propertyValue.editorName = editor.service().getClass().getSimpleName().toLowerCase();
                    propertyValue.visible = property != null ? property.visible() : propertyValue.visible;
                    propertyValue.editor = editor.getEditor(controller, propertyValue).content();
                    return propertyValue;
                }
            } catch (Exception ex) {
                LOG.warn("Unable to retrieve Editor for field " + field + " due to an error", ex);
            }
            return null;

        }).collect(Collectors.toList());

        Map<String, Object> parameters = Maps.newHashMap(additionalParameters);
        parameters.put("properties", defs);
        return template.render(controller, parameters);
    }
}

