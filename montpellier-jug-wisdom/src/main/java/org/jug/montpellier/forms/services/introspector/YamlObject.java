package org.jug.montpellier.forms.services.introspector;

import java.io.InputStream;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

/**
 * Created by Eric Taix on 04/06/15.
 */
public class YamlObject {

    private HashMap yamlObject;

    private YamlObject(HashMap yamlObject) {
        this.yamlObject = yamlObject;
    }

    public static YamlObject from(InputStream inputStream) {
        Yaml yaml = new Yaml();
        return new YamlObject((HashMap) yaml.load(inputStream));
    }

    public static YamlObject from(String yamlString) {
        Yaml yaml = new Yaml();
        return new YamlObject((HashMap) yaml.load(yamlString));
    }

    public String getField(String fieldname) {
        return (String) getFieldInternal(fieldname, yamlObject);
    }

    public <T> T getField(String fieldname, Class<T> classname) {
        return (T) getFieldInternal(fieldname, yamlObject);
    }

    private Object getFieldInternal(String fieldname, HashMap fieldsMap) {
        String[] fields = fieldname.split("\\.", 2);
        Object object = fieldsMap.get(fields[0]);
        return fields.length == 1 ? object : getFieldInternal(fields[1], (HashMap) object);
    }
}
