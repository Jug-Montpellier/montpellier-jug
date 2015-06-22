package org.jug.montpellier.cartridges.events.services;

/**
 * Created by chelebithil on 22/06/15.
 */
public class Profile {
    private String email;
    private String first_name;
    private String last_name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return getEmail();
    }
}
