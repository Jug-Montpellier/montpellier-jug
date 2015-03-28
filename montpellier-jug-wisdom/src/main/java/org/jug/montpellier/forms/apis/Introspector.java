package org.jug.montpellier.forms.apis;

import org.jug.montpellier.forms.models.PropertyValue;
import org.wisdom.api.Controller;

/**
 * Created by Eric Taix on 28/03/15.
 */
public interface Introspector {

    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws NoSuchFieldException;

}
