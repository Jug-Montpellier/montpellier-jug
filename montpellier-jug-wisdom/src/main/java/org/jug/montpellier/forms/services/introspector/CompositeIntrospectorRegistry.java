package org.jug.montpellier.forms.services.introspector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.felix.ipojo.annotations.Bind;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Unbind;
import org.jug.montpellier.forms.apis.Introspector;
import org.jug.montpellier.forms.apis.IntrospectorRegistry;
import org.jug.montpellier.forms.models.ListViewColumn;
import org.jug.montpellier.forms.models.PropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.Controller;

/**
 * Created by Eric Taix on 28/03/15.
 */
@Component
@Provides(specifications = IntrospectorRegistry.class)
@Instantiate
public class CompositeIntrospectorRegistry implements IntrospectorRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(CompositeIntrospectorRegistry.class);

    private List<Introspector> introspectors = new ArrayList<>();

    private Optional<Introspector> getIntrospector(Class<?> objectClass) {
        return introspectors.stream().filter(introspec -> introspec.accept(objectClass)).findFirst();
    }

    @Override
    public List<PropertyValue> getPropertyValues(Object object, Controller controller) throws Exception {
        return getIntrospector(object.getClass()).get().getPropertyValues(object, controller);
    }

    @Override
    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws Exception {
        return getIntrospector(object.getClass()).get().getPropertyValue(object, propertyName, controller);
    }

    @Override
    public String getListTitle(Class<?> objectClass) throws IOException {
        return getIntrospector(objectClass).get().getListTitle(objectClass);
    }

    @Override
    public List<ListViewColumn> getColumns(Class<?> objectClass) throws IOException {
        return getIntrospector(objectClass).get().getColumns(objectClass);
    }

    @Override
    public String getIdProperty(Class<?> objectClass) throws IOException {
        return getIntrospector(objectClass).get().getIdProperty(objectClass);
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
