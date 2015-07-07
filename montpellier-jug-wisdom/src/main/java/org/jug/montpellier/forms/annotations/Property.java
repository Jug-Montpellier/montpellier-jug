package org.jug.montpellier.forms.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jug.montpellier.forms.apis.EditorService;
import org.jug.montpellier.forms.services.editors.base.NotImplementedEditorService;

/**
 * Created by Eric Taix on 07/03/2015.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {

    /**
     * Defines if the property is rendered but visible or hidden. It's typically useful for primary key
     */
    boolean visible() default true;

    /**
     * Defines the label to display instead of the property's name
     */
    String displayLabel() default "";

    /**
     * Defines the description (if any) to display bellow the input
     */
    String description() default "";

    /**
     * This is defined as String because when I tried to use Class<? extends EditorService> and when bundles are reloaded
     * then the field annotations were missing. It's probably a OSGi classpath issue or something like that but I can't figure out
     * why! If you find a way to use Class instead of a String, please fix it ;-)
     * @return
     */
    Class<? extends EditorService> editorService() default NotImplementedEditorService.class;

}
