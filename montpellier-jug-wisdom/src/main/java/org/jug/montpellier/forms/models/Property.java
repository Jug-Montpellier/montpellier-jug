package org.jug.montpellier.forms.models;

import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.services.editors.base.NotImplementedEditorService;

/**
 * Created by Eric Taix on 04/06/15.
 */
public class Property {

    private boolean visible;
    private String label;
    private String description;
    private Class<? extends EditorService> editor;

    public static Property from(org.jug.montpellier.forms.annotations.Property propertyAnnotation) {
        return new Property().setVisible(propertyAnnotation.visible())
                    .setLabel(propertyAnnotation.displayLabel())
                    .setDescription(propertyAnnotation.description())
                    .setEditor(propertyAnnotation.editorService());
    }

    public boolean isVisible() {
        return visible;
    }

    public Property setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public Property setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Property setDescription(String description) {
        this.description = description;
        return this;
    }

    public Class<? extends EditorService> getEditor() {
        return editor;
    }

    public Property setEditor(Class<? extends EditorService> editor) {
        this.editor = editor;
        return this;
    }
}
