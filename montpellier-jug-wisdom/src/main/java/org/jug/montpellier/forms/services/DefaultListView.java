package org.jug.montpellier.forms.services;

import com.google.common.collect.Maps;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.forms.annotations.ListView;
import org.jug.montpellier.forms.annotations.Property;
import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorRegistry;
import org.jug.montpellier.forms.models.ListViewCell;
import org.jug.montpellier.forms.models.ListViewRow;
import org.jug.montpellier.forms.models.PropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.Controller;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Eric Taix on 24/03/15.
 */
@Component
@Provides(specifications = org.jug.montpellier.forms.apis.ListView.class)
@Instantiate
public class DefaultListView implements org.jug.montpellier.forms.apis.ListView {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultListView.class);

    @Requires
    EditorRegistry editorRegistry;
    @View("editors/listview")
    Template template;

    @Override
    public <T extends Object> Renderable getRenderable(Controller controller, List<T> objects, Class<T> objectClass) throws Exception {
        return getRenderable(controller, objects, objectClass, Maps.newHashMap());
    }

    @Override
    public <T> Renderable getRenderable(Controller controller, List<T> objects, Class<T> objectClass, Map<String, Object> additionnalParameters) throws Exception {
        ListView annotation = objectClass.getAnnotation(ListView.class);
        List<String> columns = Arrays.asList(annotation.columns());
        List<ListViewRow> listViewRows = objects.stream().map((final T object) -> {
            ListViewRow row = new ListViewRow();
            try {
                Field idField = object.getClass().getDeclaredField(annotation.id());
                idField.setAccessible(true);
                row.id = idField.get(object);
                row.cells = columns.stream().map((String column) -> {
                    ListViewCell cell = new ListViewCell();
                    try {
                        Field field = object.getClass().getDeclaredField(column);
                        field.setAccessible(true);
                        Property property = field.getAnnotation(Property.class);
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
                            cell.content = editor.getView(controller, propertyValue).content();
                        }
                    } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
                        cell.content = "error";
                    }
                    return cell;
                }).collect(Collectors.toList());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOG.error("Error while getting id() for object: "+object, e);
            }
            return row;
        }).collect(Collectors.toList());

        List<String> labels = Arrays.asList(annotation.labels());


        Map<String, Object> parameters = Maps.newHashMap(additionnalParameters);
        parameters.put("title", annotation.title());
        parameters.put("hasData", !listViewRows.isEmpty());
        parameters.put("rows", listViewRows);
        parameters.put("labels", labels);
        return template.render(controller, parameters);
    }
}
