package org.jug.montpellier.cartridges.partners;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.cartridges.partners.controllers.PartnersController;
import org.jug.montpellier.core.api.Cartridge;
import org.wisdom.api.router.Router;

/**
 * Created by manland on 04/06/15.
 */
@Component
@Provides(specifications = Cartridge.class)
@Instantiate
public class PartnersCartrige implements Cartridge {

    @Requires
    Router router;

    @Override
    public String label() {
        return "PartnerS";
    }

    @Override
    public String routeUri() {
        return router.getReverseRouteFor(PartnersController.class, "all");
    }

    @Override
    public int position() {
        return 2;
    }
}
