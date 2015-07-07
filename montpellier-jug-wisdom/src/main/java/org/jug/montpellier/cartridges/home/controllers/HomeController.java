/*
 * #%L
 * Wisdom-Framework
 * %%
 * Copyright (C) 2013 - 2014 Wisdom Framework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.jug.montpellier.cartridges.home.controllers;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.felix.ipojo.annotations.Requires;
import org.jooq.DSLContext;
import org.jug.montpellier.core.api.JugSupport;
import org.jug.montpellier.core.controller.JugController;
import org.jug.montpellier.models.Event;
import org.montpellierjug.store.jooq.tables.interfaces.IEvent;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Path;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

@Controller
@Path("/")
public class HomeController extends JugController {

    @View("home")
    Template template;

    @Requires
    DSLContext dslContext;

    public HomeController(@Requires JugSupport jugSupport) {
        super(jugSupport);
    }

    @Route(method = HttpMethod.GET, uri = "")
    public Result welcome() {
        Timestamp today = new Timestamp(Calendar.getInstance().getTime().getTime());
        List<IEvent> eventsPojo = dslContext
                .select().from(org.montpellierjug.store.jooq.tables.Event.EVENT)
                .where(org.montpellierjug.store.jooq.tables.Event.EVENT.OPEN.equal(false))
                .and(org.montpellierjug.store.jooq.tables.Event.EVENT.DATE.lessThan(today))
                .orderBy(org.montpellierjug.store.jooq.tables.Event.EVENT.DATE.desc())
                .limit(3)
                .fetchInto(org.montpellierjug.store.jooq.tables.pojos.Event.class);
        List<Event> events = Event.build(eventsPojo);
        return template(template).withParam("events", events).render();
    }

}
