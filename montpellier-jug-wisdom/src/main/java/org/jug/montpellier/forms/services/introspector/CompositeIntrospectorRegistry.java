package org.jug.montpellier.forms.services.introspector;

import org.apache.felix.ipojo.annotations.*;
import org.jug.montpellier.core.api.Cartridge;
import org.jug.montpellier.forms.apis.Introspector;
import org.jug.montpellier.forms.apis.IntrospectorRegistry;
import org.jug.montpellier.forms.models.ListViewColumn;
import org.jug.montpellier.forms.models.PropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric Taix on 28/03/15.
 */
@Component
@Provides(specifications = IntrospectorRegistry.class)
@Instantiate
public class CompositeIntrospectorRegistry implements IntrospectorRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(CompositeIntrospectorRegistry.class);

    private List<Introspector> introspectors = new ArrayList<>();

    @Override
    public List<PropertyValue> getPropertyValues(Object object, Controller controller) {
        return introspectors.stream().map((introspector -> {
            try {
                return introspector.getPropertyValues(object, controller);
            } catch (Exception e) {
                return null;
            }
        })).reduce(null, (currentPropertyValue, propertyValue) -> {
            return currentPropertyValue != null && !currentPropertyValue.isEmpty() ? currentPropertyValue : propertyValue;
        });
    }

    @Override
    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws NoSuchFieldException {
        return introspectors.stream().map((introspector -> {
            try {
                return introspector.getPropertyValue(object, propertyName, controller);
            } catch (Exception e) {
                return null;
            }
        })).reduce(null, (currentPropertyValue, propertyValue) -> currentPropertyValue != null ? currentPropertyValue : propertyValue);
    }

    @Override
    public String getTitle(Object object) {
        return "title";
    }

    @Override
    public List<ListViewColumn> getColumns(Class<?> objectClass) {
        return introspectors.stream().map((introspector -> {
            try {
                return introspector.getColumns(objectClass);
            } catch (Exception e) {
                return null;
            }
        })).reduce(null, (currentPropertyValue, propertyValue) -> currentPropertyValue != null ? currentPropertyValue : propertyValue);
    }

    @Override
    public String getIdProperty(Class<?> objectClass) throws IOException {
        return introspectors.stream().map((introspector -> {
            try {
                return introspector.getIdProperty(objectClass);
            } catch (Exception e) {
                return null;
            }
        })).reduce(null, (currentPropertyValue, propertyValue) -> currentPropertyValue != null ? currentPropertyValue : propertyValue);
    }

    @Bind(specification = Introspector.class, aggregate = true)
    public void bindIntrospector(Introspector introspector) {
        LOG.info("Adding introspector " + introspector);
        if (introspector != null) {
            introspectors.add(introspector);
        }
    }

    @Unbind(specification = Introspector.class, aggregate = true)
    public void unbindIntrospector(Introspector introspector) {
        LOG.info("Removing introspector " + introspector);
        if (introspector != null) {
            introspectors.remove(introspector);
        }
    }
}
