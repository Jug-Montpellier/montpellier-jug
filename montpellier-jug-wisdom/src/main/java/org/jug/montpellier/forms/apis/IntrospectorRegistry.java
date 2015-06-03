package org.jug.montpellier.forms.apis;

import org.jug.montpellier.forms.models.ListViewColumn;
import org.jug.montpellier.forms.models.PropertyValue;
import org.wisdom.api.Controller;

import java.io.IOException;
import java.util.List;

/**
 * Created by Eric Taix on 28/03/15.
 */
public interface IntrospectorRegistry {

    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws NoSuchFieldException;

    public List<ListViewColumn> getColumns(Class<?> objectClass);

    public String getIdProperty(Class<?> objectClass) throws IOException;
}
