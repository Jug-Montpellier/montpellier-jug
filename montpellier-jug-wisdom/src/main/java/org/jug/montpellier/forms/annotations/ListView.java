package org.jug.montpellier.forms.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Eric Taix on 07/03/2015.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListView {

    /**
     * The field which contains the id. The field value will be used to generate the URI when the user will click on a row.
     * The {@link Object#toString()} will be called on the field to generate the URI
     * @return
     */
    String id() default "id";

    /**
     * The label list which will be displayed into the column headers
     * @return
     */
    String[] labels();

    /**
     * The field used to display content in each cell
     * @return
     */
    String[] columns();

    String title();

}
