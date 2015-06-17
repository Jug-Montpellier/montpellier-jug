package org.jug.montpellier.cartridges.speakers.services;

import org.jug.montpellier.core.controller.JugController;
import org.wisdom.api.http.Result;

public interface SpeakersService {

    public Result renderSpeakers(JugController.Templatable templatable);

    public Result renderSpeakers(JugController.Templatable templatable, Long id);

}
