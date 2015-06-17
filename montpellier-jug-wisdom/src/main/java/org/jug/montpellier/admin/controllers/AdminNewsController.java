package org.jug.montpellier.admin.controllers;

import com.google.common.collect.Maps;
import org.apache.felix.ipojo.annotations.Requires;
import org.jooq.DSLContext;
import org.jooq.SelectOrderByStep;
import org.jooq.SelectWhereStep;
import org.jug.montpellier.core.api.JugSupport;
import org.jug.montpellier.core.controller.JugController;
import org.jug.montpellier.forms.apis.ListView;
import org.jug.montpellier.forms.apis.PropertySheet;
import org.jug.montpellier.models.News;
import org.montpellierjug.store.jooq.Tables;
import org.montpellierjug.store.jooq.tables.daos.NewsDao;
import org.wisdom.api.annotations.*;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.security.Authenticated;
import org.wisdom.api.templates.Template;
import org.wisdom.oauth2.OAuth2WisdomAuthenticator;
import org.wisdom.oauth2.controller.Role;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created by fteychene on 04/06/2015.
 */
@Controller
@Path("/admin/news")
@Authenticated(OAuth2WisdomAuthenticator.NAME)
public class AdminNewsController extends JugController {

    @View("admin")
    Template template;

    @Requires
    PropertySheet propertySheet;

    @Requires
    ListView listView;

    @Requires
    NewsDao newsDao;

    @Requires
    DSLContext dslContext;

    public AdminNewsController(@Requires JugSupport jugSupport) {
        super(jugSupport);
    }

    @Role("admin")
    @Route(method = HttpMethod.GET, uri = "/")
    public Result home(@QueryParameter("search") String search) throws Exception {
        SelectOrderByStep selectStep = dslContext.selectFrom(Tables.NEWS);
        if (search != null && !search.isEmpty()) {
            selectStep = ((SelectWhereStep)selectStep).where(org.montpellierjug.store.jooq.tables.News.NEWS.TITLE.likeIgnoreCase("%" + search + "%"));
        }
        List<News> news = selectStep.orderBy(org.montpellierjug.store.jooq.tables.News.NEWS.DATE.desc()).fetchInto(News.class);
        return template(template).withListview(listView.getRenderable(this, news, News.class)).render();
    }

    @Role("admin")
    @Route(method = HttpMethod.GET, uri = "/{id}")
    public Result get(@Parameter("id") Long id) throws Exception {
        News editedNews = News.build(newsDao.findById(id));
        return template(template).withPropertySheet(propertySheet.getRenderable(this, editedNews)).render();
    }

    @Role("admin")
    @Route(method = HttpMethod.POST, uri = "/{id}")
    public Result saveTalk(@Parameter("id") Long id, @Body News news) throws InvocationTargetException, ClassNotFoundException, IntrospectionException, IllegalAccessException {
        newsDao.update(news.into(new org.montpellierjug.store.jooq.tables.pojos.News()));
        return redirect(".");
    }

    @Role("admin")
    @Route(method = HttpMethod.GET, uri = "/new/")
    public Result createTalk() throws Exception {
        Map<String, Object> additionalParameters = Maps.newHashMap();
        additionalParameters.put("cancelRedirect", "..");
        return template(template).withPropertySheet(propertySheet.getRenderable(this, new News(), additionalParameters)).render();
    }

    @Role("admin")
    @Route(method = HttpMethod.POST, uri = "/new/")
    public Result saveNewTalk(@Body News news) {
        newsDao.insert(news.into(new org.montpellierjug.store.jooq.tables.pojos.News()));
        return redirect("..");
    }
}
