package org.jug.montpellier.cartridges.events.services;

/**
 * Created by chelebithil on 23/06/15.
 */
public class Event {

    private static class Name {
        String text;
        String html;

        public String getHtml() {
            return html;
        }

        public String getText() {
            return text;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public void setText(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "Name{" +
                    "text='" + text + '\'' +
                    ", html='" + html + '\'' +
                    '}';
        }
    }

    private String id;

    private Name name;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", name=" + name +
                '}';
    }
}
