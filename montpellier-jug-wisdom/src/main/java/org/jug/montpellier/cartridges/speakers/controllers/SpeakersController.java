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
package org.jug.montpellier.cartridges.speakers.controllers;

import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.cartridges.speakers.services.SpeakersService;
import org.jug.montpellier.core.api.JugSupport;
import org.jug.montpellier.core.controller.JugController;
import org.wisdom.api.annotations.*;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.templates.Template;

@Controller
@Path("/speakers")
public class SpeakersController extends JugController {

    @View("speakers")
    Template template;
    @Requires
    SpeakersService speakersService;

    public SpeakersController(@Requires JugSupport jugSupport) {
        super(jugSupport);
    }

    @Route(method = HttpMethod.GET, uri = "/")
    public Result speakers() {
        return speakersService.renderSpeakers(template(template));
    }

    @Route(method = HttpMethod.GET, uri = "/{id}")
    public Result speaker(@Parameter("id") Long id) {
        return speakersService.renderSpeakers(template(template), id);
    }

}
