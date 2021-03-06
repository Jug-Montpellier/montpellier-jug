package org.jug.montpellier.forms.services.introspector;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Context;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.forms.apis.EditorRegistry;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.apis.Introspector;
import org.jug.montpellier.forms.models.ListViewColumn;
import org.jug.montpellier.forms.models.Property;
import org.jug.montpellier.forms.models.PropertyValue;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.Controller;

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
    public boolean accept(Class<?> objectClass) {
        try {
            return getYamlForObject(objectClass) != null;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public List<PropertyValue> getPropertyValues(Object object, Controller controller) throws Exception {
        YamlObject yamlObject = getYamlForObject(object.getClass());

        // WTF: Java8 does not support streams for Map.Entry
        List<PropertyValue> propertyValues = new ArrayList<>();
        HashMap<String, Object> properties = yamlObject.getField("properties", HashMap.class);
        for (Entry entry: properties.entrySet()) {
            String propertyName = (String) entry.getKey();
            try {
                propertyValues.add(getPropertyValue(object, propertyName, controller));
            }
            catch(NoSuchFieldException e) {
                LOG.warn("Field {} does not exist for class {}", propertyName, object.getClass());
            }
        }
        return propertyValues;
    }

    @Override
    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws Exception {
        YamlObject yamlObject = getYamlForObject(object.getClass());

        Field field = object.getClass().getDeclaredField(propertyName);
        field.setAccessible(true);

        HashMap<String, Object> propertyDef = yamlObject.getField("properties." + propertyName, HashMap.class);
        if (propertyDef != null) {
            Property property = new Property()
                    .setVisible((Boolean) propertyDef.getOrDefault("visible", true))
                    .setLabel((String) propertyDef.getOrDefault("label", ""))
                    .setDescription((String) propertyDef.getOrDefault("description", null));
            if (propertyDef.get("editor") != null) {
                property.setEditor((Class<? extends EditorService>) Class.forName((String) propertyDef.get("editor")));
            }
            return buildPropertyValue(object, field, property, controller);
        }
        throw new NoSuchFieldException();
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

    @Override
    public String getListTitle(Class<?> objectClass) throws IOException {
        YamlObject yamlObject = getYamlForObject(objectClass);
        return yamlObject.getField("listview.title");
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
