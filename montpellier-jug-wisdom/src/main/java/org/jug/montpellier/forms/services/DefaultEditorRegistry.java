package org.jug.montpellier.forms.services;

import org.apache.felix.ipojo.annotations.*;
import org.jug.montpellier.forms.apis.Editor;
import org.jug.montpellier.forms.apis.EditorRegistry;
import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.models.Property;
import org.jug.montpellier.forms.services.editors.base.NotImplementedEditorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eric Taix on 08/03/2015.
 */
@Component
@Provides(specifications = EditorRegistry.class)
@Instantiate
public class DefaultEditorRegistry implements EditorRegistry {

    private static Logger logger = LoggerFactory.getLogger(DefaultEditorRegistry.class);

    private Map<Class, EditorService> formEditorByEditedType = new HashMap<>();
    private Map<Class, EditorService> formEditorByClass = new HashMap<>();

    @Override
    public Editor createEditor(Object field, Class<?> fieldClass, Property property) throws ClassNotFoundException {
        EditorService factory = formEditorByEditedType.get(fieldClass);
        if (property != null && property.getEditor() != null && !property.getEditor().equals(NotImplementedEditorService.class)) {
            factory = formEditorByClass.get(property.getEditor());
        }
        if (factory != null) {
            return factory.createFormEditor(field);
        }
        return null;
    }

    @Bind(specification = EditorService.class, aggregate = true)
    public synchronized void bindFormEditorFactory(EditorService factory) {
        logger.info("Adding EditorFactory " + factory);
        if (factory != null) {
            if (factory.getEditedType() != null) {
                formEditorByEditedType.put(factory.getEditedType(), factory);
            }
            formEditorByClass.put(factory.getClass(), factory);
        }
    }

    @Unbind(specification = EditorService.class, aggregate = true)
    public synchronized void unbindFormEditorFactory(EditorService factory) {
        logger.info("Removing EditorFactory " + factory);
        if (factory != null) {
            formEditorByEditedType.remove(factory.getEditedType());
            formEditorByClass.remove(factory.getClass());
        }
    }

    @Invalidate
    public void stop() {
        formEditorByEditedType.clear();
        formEditorByClass.clear();
    }
}
