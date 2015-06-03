package org.jug.montpellier.forms.services.introspector;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.forms.annotations.ListView;
import org.jug.montpellier.forms.annotations.Property;
import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorRegistry;
import org.jug.montpellier.forms.apis.Introspector;
import org.jug.montpellier.forms.models.ListViewColumn;
import org.jug.montpellier.forms.models.PropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.Controller;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Eric Taix on 28/03/15.
 */
@Component
@Provides(specifications = Introspector.class)
@Instantiate
public class AnnotationIntrospector implements Introspector {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationIntrospector.class);

    @Requires
    EditorRegistry editorRegistry;

    @Override
    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws NoSuchFieldException {
        Field field = object.getClass().getDeclaredField(propertyName);
        field.setAccessible(true);
        try {
            Property property = field.getAnnotation(Property.class);
            if (property != null) {
                Editor editor = editorRegistry.createEditor(field.get(object), field.getType(), property);
                if (editor != null) {
                    PropertyValue propertyValue = new PropertyValue();
                    propertyValue.name = field.getName();
                    propertyValue.displayname = property != null && property.displayLabel() != null && !property.displayLabel().isEmpty() ? property.displayLabel() : field.getName();
                    propertyValue.description = property != null && property.description() != null && !property.description().isEmpty() ? property.description() : "";
                    propertyValue.value = editor.getValue();
                    propertyValue.valueAsText = editor.getAsText();
                    propertyValue.editorName = editor.service().getClass().getSimpleName().toLowerCase();
                    propertyValue.visible = property != null ? property.visible() : propertyValue.visible;
                    propertyValue.editor = editor.getEditor(controller, propertyValue).content();
                    propertyValue.view = editor.getView(controller, propertyValue).content();
                    return propertyValue;
                }
            }
        } catch (Exception ex) {
            LOG.warn("Unable to retrieve Editor for field " + field + " due to an error", ex);
        }
        return null;
    }

    @Override
    public List<ListViewColumn> getColumns(Class<?> objectClass) {
        ListView annotation = objectClass.getAnnotation(ListView.class);
        if (annotation == null) return null;
        List<String> columns = Arrays.asList(annotation.columns());
        List<String> labels = Arrays.asList(annotation.labels());

        List<ListViewColumn> listViewColumns = columns.stream().map(column -> new ListViewColumn().setField(column)).collect(Collectors.toList());
        for (int i = 0; i < labels.size(); i++) {
            listViewColumns.get(i).setLabel(labels.get(i));
        }
        return listViewColumns;
    }

    @Override
    public String getIdProperty(Class<?> objectClass) {
        ListView annotation = objectClass.getAnnotation(ListView.class);
        return annotation != null ? annotation.id() : null;
    }
}
