package org.jug.montpellier.models;

import java.sql.Timestamp;

import org.jug.montpellier.forms.annotations.ListView;
import org.jug.montpellier.forms.annotations.Property;
import org.jug.montpellier.forms.services.editors.extended.BigStringEditorService;
import org.jug.montpellier.forms.services.editors.extended.DateEditorService;
import org.jug.montpellier.forms.services.editors.extended.ImageUrlEditorService;
import org.jug.montpellier.forms.services.editors.extended.WebSiteUrlEditorService;
import org.montpellierjug.store.jooq.tables.interfaces.IYearpartner;

@ListView(title = "Partners", labels = {"", "Name", "StartDate", "StopDate"}, columns = {"logourl", "name","startdate", "stopdate"})
public class Yearpartner implements IYearpartner {

    @Property(visible = false)
    private Long id;
    @Property(displayLabel = "Partner's name")
    private String name;
    @Property(displayLabel = "Logo", description="You can also copy/paste a base64 image", editorService = ImageUrlEditorService.class)
    private String logourl;
    @Property(displayLabel = "Web Site", editorService = WebSiteUrlEditorService.class)
    private String url;
    @Property(displayLabel = "Description", editorService = BigStringEditorService.class)
    private String description;
    @Property(displayLabel = "Start date", editorService = DateEditorService.class)
    private Timestamp startdate;
    @Property(displayLabel = "Stop date", editorService = DateEditorService.class)
    private Timestamp stopdate;


    public static Yearpartner build(IYearpartner from) {
        Yearpartner yearpartner = new Yearpartner();
        yearpartner.from(from);
        return yearpartner;
    }

    @Override
    public IYearpartner setId(Long value) {
        this.id = value;
        return this;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public IYearpartner setDescription(String value) {
        this.description = value;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public IYearpartner setLogourl(String value) {
        this.logourl = value;
        return this;
    }

    @Override
    public String getLogourl() {
        return logourl;
    }

    @Override
    public IYearpartner setName(String value) {
        this.name = value;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IYearpartner setStartdate(Timestamp value) {
        this.startdate = value;
        return this;
    }

    @Override
    public Timestamp getStartdate() {
        return startdate;
    }

    @Override
    public IYearpartner setStopdate(Timestamp value) {
        this.stopdate = value;
        return this;
    }

    @Override
    public Timestamp getStopdate() {
        return stopdate;
    }

    @Override
    public IYearpartner setUrl(String value) {
        this.url = value;
        return this;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void from(IYearpartner from) {
        setId(from.getId());
        setDescription(from.getDescription());
        setLogourl(from.getLogourl());
        setName(from.getName());
        setStartdate(from.getStartdate());
        setStopdate(from.getStopdate());
        setUrl(from.getUrl());
    }

    @Override
    public <E extends IYearpartner> E into(E into) {
        into.from(this);
        return into;
    }
}
