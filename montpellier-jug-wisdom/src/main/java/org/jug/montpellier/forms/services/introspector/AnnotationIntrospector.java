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

import java.io.IOException;
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
public class AnnotationIntrospector extends AbstractIntrospector implements Introspector {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationIntrospector.class);

    @Requires
    EditorRegistry editorRegistry;

    @Override
    public List<PropertyValue> getPropertyValues(Object object, Controller controller) throws NoSuchFieldException {
        List<PropertyValue> propertyValues = Arrays.asList(object.getClass().getDeclaredFields()).stream().map((Field field) -> {
            try {
                return getPropertyValue(object, field.getName(), controller);
            } catch (Exception e) {
                LOG.debug("Error while retreiving property value for field " + field.getName() + " of object " + object, e);
            }
            return null;
        }).filter(propertyValue -> propertyValue != null).collect(Collectors.toList());
        return propertyValues;
    }

    @Override
    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws Exception {
        Field field = object.getClass().getField(propertyName);
        field.setAccessible(true);
        Property property = field.getAnnotation(Property.class);
        if (property != null) {
            org.jug.montpellier.forms.models.Property prop = org.jug.montpellier.forms.models.Property.from(property);
            return buildPropertyValue(object, field, prop, controller);
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

    @Override
    public String getListTitle(Class<?> objectClass) throws IOException {
        ListView annotation = objectClass.getAnnotation(ListView.class);
        if (annotation == null) return null;
        return annotation.title();
    }

    @Override
    public EditorRegistry getEditorRegistry() {
        return editorRegistry;
    }
}
