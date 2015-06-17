package org.jug.montpellier.forms.apis;

import org.jug.montpellier.forms.models.Property;

/**
 * Manages FormEditor and property type relationship. FormEditor are registered by OSGi injection
 * Created by Eric Taix on 08/03/2015.
 */
public interface EditorRegistry {

    /**
     * Try to create a {@link org.jug.montpellier.forms.apis.Editor} instance according
     * to the object type. May return null if no suitable Editor could be found
     */
    public Editor createEditor(Object field, Class<?> fieldClass, Property fieldProperty) throws ClassNotFoundException;
}
