package org.jug.montpellier.admin.services;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.admin.controllers.AdminSpeakerController;
import org.jug.montpellier.core.api.Cartridge;
import org.wisdom.api.router.Router;

@Component
@Provides(specifications = Cartridge.class)
@Instantiate
public class AdminCartridge implements Cartridge {

    @Requires
    private Router router;

    @Override
    public String label() {
        return "Administration";
    }

    @Override
    public String routeUri() {
        return router.getReverseRouteFor(AdminSpeakerController.class, "all");
    }

    @Override
    public int position() {
        return 1000;
    }
}
