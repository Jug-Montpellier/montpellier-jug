package org.jug.montpellier.forms.services;

import com.google.common.collect.Maps;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.forms.annotations.ListView;
import org.jug.montpellier.forms.apis.IntrospectorRegistry;
import org.jug.montpellier.forms.models.ListViewCell;
import org.jug.montpellier.forms.models.ListViewColumn;
import org.jug.montpellier.forms.models.ListViewRow;
import org.jug.montpellier.forms.models.PropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.Controller;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.Renderable;
import org.wisdom.api.templates.Template;

import java.io.IOException;
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
    private static final String DEFAULT_PROPERTY_ID = "id";
    @Requires
    IntrospectorRegistry introspectorRegistry;

    @View("editors/listview")
    Template template;

    @Override
    public <T extends Object> Renderable getRenderable(Controller controller, List<T> objects, Class<T> objectClass) throws Exception {
        return getRenderable(controller, objects, objectClass, Maps.newHashMap());
    }

    @Override
    public <T> Renderable getRenderable(Controller controller, List<T> objects, Class<T> objectClass, Map<String, Object> additionnalParameters) throws Exception {
        final List<ListViewColumn> columns = introspectorRegistry.getColumns(objectClass);
        List<ListViewRow> listViewRows = objects.stream().map((final T object) -> {
            ListViewRow row = new ListViewRow();
            try {
                // Build id property when the user click
                String id = introspectorRegistry.getIdProperty(objectClass);
                if (id == null) {
                    id = DEFAULT_PROPERTY_ID;
                }
                Field idField = object.getClass().getDeclaredField(id);
                idField.setAccessible(true);
                row.id = idField.get(object);
                // Build cells data
                row.cells = columns.stream().map((ListViewColumn column) -> {
                    ListViewCell cell = new ListViewCell();
                    try {
                        PropertyValue propertyValue = introspectorRegistry.getPropertyValue(object, column.getField(), controller);
                        cell.content = propertyValue.view;
                    } catch (NoSuchFieldException e) {
                        cell.content = "error";
                    }
                    return cell;
                }).collect(Collectors.toList());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                LOG.error("Error while getting id() for object: "+object, e);
            } catch (IOException e) {
                LOG.error("Error while getting id() for object: "+object, e);
            }
            return row;
        }).collect(Collectors.toList());

        Map<String, Object> parameters = Maps.newHashMap(additionnalParameters);
        parameters.put("title", annotation.title());
        parameters.put("hasData", !listViewRows.isEmpty());
        parameters.put("rows", listViewRows);
        parameters.put("labels", Arrays.asList(columns.stream().map(column -> column.getLabel()).collect(Collectors.toList())));
        return template.render(controller, parameters);
    }
}
