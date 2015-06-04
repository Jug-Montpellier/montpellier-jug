package org.jug.montpellier.forms.services.introspector;

import org.apache.felix.ipojo.annotations.*;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.Property;

import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorRegistry;
import org.jug.montpellier.forms.apis.Introspector;
import org.jug.montpellier.forms.models.ListViewColumn;
import org.jug.montpellier.forms.models.PropertyValue;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Eric Taix on 01/04/15.
 */
@Component
@Provides(specifications = Introspector.class)
@Instantiate
public class YamlIntrospector extends AbstractIntrospector implements Introspector {

    private static final Logger LOG = LoggerFactory.getLogger(YamlIntrospector.class);

    private Map<String, YamlObject> yamlCache = new HashMap<>();

    @Requires
    EditorRegistry editorRegistry;
    @Context
    BundleContext context;

    public YamlIntrospector(BundleContext context) {
        this.context = context;
    }

    @Override
    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws Exception {
        YamlObject yamlObject = getYamlForObject(object.getClass());
        Field field = object.getClass().getDeclaredField(propertyName);
        field.setAccessible(true);
        try {
            HashMap<String, String> propertyDef = yamlObject.getField("properties." + propertyName, HashMap.class);
            Property property = new Property()
                                    .setVisible(Boolean.parseBoolean(propertyDef.get("visible")))
                                    .setLabel(propertyDef.get("label"))
                                    .setDescription(propertyDef.get("description"));
            if (propertyDef.get("editor") != null) {
                property.setEditor((Class<? extends EditorService>) Class.forName(propertyDef.get("editor")));
            }

            if (property != null) {
                return buildPropertyValue(object, field, property, controller);
            }
        } catch (Exception ex) {
            LOG.warn("Unable to retrieve Editor for field " + field + " due to an error", ex);
        }
        return null;
    }

    @Override
    public List<ListViewColumn> getColumns(Class<?> objectClass) throws IOException {
        YamlObject yamlObject = getYamlForObject(objectClass);
        return Arrays.asList(yamlObject.getField("listview.columns").split("\\|")).stream().map(column -> {
            String[] keyvalue = column.split(":");
            return new ListViewColumn().setField(keyvalue[0]).setLabel(keyvalue.length > 1 ? keyvalue[1] : "");
        }).collect(Collectors.toList());
    }

    @Override
    public String getIdProperty(Class<?> objectClass) throws IOException {
        YamlObject yamlObject = getYamlForObject(objectClass);
        return yamlObject.getField("listview.id");
    }

    private YamlObject getYamlForObject(Class<?> objectClassname) throws IOException {
        String resourceName = "introspector/" + objectClassname.getName() + ".yaml";
        YamlObject yamlObject = yamlCache.get(resourceName);
        if (yamlObject == null) {
            URL url = context.getBundle().getResource(resourceName);
            try (InputStream inputStream = url.openConnection().getInputStream()) {
                yamlObject = YamlObject.from(inputStream);
                yamlCache.put(resourceName, yamlObject);
            }
        }
        return yamlObject;
    }

    @Override
    public EditorRegistry getEditorRegistry() {
        return editorRegistry;
    }
}
