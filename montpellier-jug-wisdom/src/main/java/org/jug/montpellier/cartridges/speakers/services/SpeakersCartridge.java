package org.jug.montpellier.cartridges.speakers.services;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.cartridges.speakers.controllers.SpeakersController;
import org.jug.montpellier.core.api.Cartridge;
import org.wisdom.api.router.Router;

@Component
@Provides(specifications = Cartridge.class)
@Instantiate
public class SpeakersCartridge implements Cartridge {

    @Requires
    private Router router;

    @Override
    public String label() {
        return "Speakers";
    }

    @Override
    public String routeUri() {
        return router.getReverseRouteFor(SpeakersController.class, "speakers");
    }

    @Override
    public int position() {
        return 6;
    }
}
