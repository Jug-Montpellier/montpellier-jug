package org.jug.montpellier.forms.models;

/**
 * Created by Eric Taix on 03/06/15.
 */
public class ListViewColumn {
    private String field;
    private String label;

    public String getField() {
        return field;
    }

    public ListViewColumn setField(String field) {
        this.field = field;
        return this;
    }

    public String getLabel() {
        return label;
    }

    public ListViewColumn setLabel(String label) {
        this.label = label;
        return this;
    }
}
