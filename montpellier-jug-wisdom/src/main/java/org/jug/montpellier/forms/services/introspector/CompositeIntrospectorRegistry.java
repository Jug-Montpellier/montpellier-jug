package org.jug.montpellier.forms.services.introspector;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.forms.apis.Introspector;
import org.jug.montpellier.forms.apis.IntrospectorRegistry;
import org.jug.montpellier.forms.models.PropertyValue;
import org.wisdom.api.Controller;

import java.util.List;

/**
 * Created by Eric Taix on 28/03/15.
 */
@Component
@Provides(specifications = IntrospectorRegistry.class)
@Instantiate
public class CompositeIntrospectorRegistry implements IntrospectorRegistry {

    @Requires(specification = Introspector.class)
    List<Introspector> introspectors;

    @Override
    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws NoSuchFieldException {
        return introspectors.stream().map((introspector -> {
            try {
                return introspector.getPropertyValue(object, propertyName, controller);
            } catch (NoSuchFieldException e) {
                return null;
            }
        })).reduce(null, (currentPropertyValue, propertyValue) -> currentPropertyValue != null ? currentPropertyValue : propertyValue);
    }
}
