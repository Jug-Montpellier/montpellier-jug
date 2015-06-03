package org.jug.montpellier.forms.services.introspector;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.jug.montpellier.forms.apis.Introspector;
import org.jug.montpellier.forms.models.ListViewColumn;
import org.jug.montpellier.forms.models.PropertyValue;
import org.wisdom.api.Controller;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Eric Taix on 01/04/15.
 */
@Component
@Provides(specifications = Introspector.class)
@Instantiate
public class YamlIntrospector implements Introspector {

    private Map<String, Object> yamlCache = new HashMap<>();

    @Override
    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws Exception {

        Object yamlObject = getYamlForObject(object.getClass());

        return null;
    }

    @Override
    public List<ListViewColumn> getColumns(Class<?> objectClass) throws IOException {
        Object yamlObject = getYamlForObject(objectClass);
        return null;
    }

    @Override
    public String getIdProperty(Class<?> objectClass) throws IOException {
        Object yamlObject = getYamlForObject(objectClass);
        return null;
    }

    private Object getYamlForObject(Class<?> objectClassname) throws IOException {
        String resourceName = "/" + objectClassname.getName().replaceAll("\\.", "/") + ".yaml";
        Object yamlObject = yamlCache.get(resourceName);
        if (yamlObject == null) {
            try (InputStream inputStream = getClass().getResourceAsStream(resourceName)) {
                Yaml yaml = new Yaml();
                yamlObject = yaml.load(inputStream);
                yamlCache.put(resourceName, yamlObject);
                System.out.println(yamlObject);
                //       Arrays.asList(properties.propertyNames()).stream().filter(propName -> {
                //           return propName.startsWith("property." + propertyName);
                //       });
            }
        }
        return yamlObject;
    }
}
