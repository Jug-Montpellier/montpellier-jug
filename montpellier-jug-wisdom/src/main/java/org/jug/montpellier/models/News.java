package org.jug.montpellier.models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jug.montpellier.forms.annotations.ListView;
import org.jug.montpellier.forms.annotations.Property;
import org.jug.montpellier.forms.services.editors.extended.BigStringEditorService;
import org.jug.montpellier.forms.services.editors.extended.DateEditorService;
import org.montpellierjug.store.jooq.tables.interfaces.INews;

@ListView(title = "News", labels = {"Title", "Date"}, columns = {"title", "date"})
public class News implements INews {

    @Property(visible = false)
    public Long id;
    @Property(displayLabel = "Title")
    public String title;
    @Property(visible = false, editorService = DateEditorService.class)
    public Timestamp date;
    @Property(displayLabel = "Content", editorService = BigStringEditorService.class)
    public String content;
    @Property(displayLabel = "Comments")
    public boolean comments;

    public News() {
        this.date = new Timestamp(new Date().getTime());
    }

    public News(String title, String content) {
        this();
        this.title = title;
        this.content = content;
    }

    @Override
    public void from(INews from) {
        setId(from.getId());
        setDate(from.getDate());
        setTitle(from.getTitle());
        setContent(from.getContent());
        setComments(from.getComments());
    }

    public static News build(INews from) {
        News news = new News();
        news.from(from);
        return news;
    }

    public static <T extends INews> List<News> build(Stream<T> from) {
        return from.map((INews elem) -> build(elem)).collect(Collectors.toList());
    }

    public static <T extends INews> List<News> build(List<T> from) {
        return build(from.stream());
    }

    @Override
    public <E extends INews> E into(E into) {
        into.from(this);
        return into;
    }

    public String getTitle() {
        return title;
    }

    public INews setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getDate() {
        return date;
    }

    @Override
    public INews setDate(Timestamp value) {
        date = value;
        return this;
    }

    @Override
    public INews setId(Long value) {
        id = value;
        return this;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public INews setComments(Boolean value) {
        comments = value;
        return this;
    }

    @Override
    public Boolean getComments() {
        return comments;
    }

    public INews setContent(String content) {
        this.content = content;
        return this;
    }

    public String toString() {
        return "News :: " + this.title + " => " + this.content + "\n\t the: " + date;
    }
}
