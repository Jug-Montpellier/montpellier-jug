package org.jug.montpellier.forms.apis;

import org.jug.montpellier.forms.models.ListViewColumn;
import org.jug.montpellier.forms.models.PropertyValue;
import org.wisdom.api.Controller;

import java.io.IOException;
import java.util.List;

/**
 * Created by Eric Taix on 28/03/15.
 */
public interface Introspector {

    public List<PropertyValue> getPropertyValues(Object object, Controller controller) throws Exception;

    public PropertyValue getPropertyValue(Object object, String propertyName, Controller controller) throws Exception;

    public List<ListViewColumn> getColumns(Class<?> objectClass) throws IOException;

    public String getIdProperty(Class<?> objectClass) throws IOException;

    public String getListTitle(Class<?> objectClass) throws IOException;
}
