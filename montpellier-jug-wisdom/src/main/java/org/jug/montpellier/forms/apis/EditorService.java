package org.jug.montpellier.forms.apis;

/**
 * Created by Eric Taix on 08/03/2015.
 */
public interface EditorService {

    /**
     * Returns the class which can be edited by the @{link Editor}.<br/>
     * If an {@link org.jug.montpellier.forms.apis.Editor} overrides another one, it should returns null to avoid being used in common cases
     * @return
     */
    public Class<? extends Object> getEditedType();

    /**
     * Creates the FormEditor instance for a specific model instance
     * @param model
     * @return
     */
    public Editor createFormEditor(Object model);

}
