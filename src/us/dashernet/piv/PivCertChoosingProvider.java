package us.dashernet.piv;

import java.security.Provider;

class PivCertChoosingProvider extends Provider {
    public PivCertChoosingProvider() {
        super("pivcert", 1.0, "Custom provider for picking PIV Auth cert.");
        put("KeyManagerFactory.pivcert", "us.dashernet.piv.PivCertChoosingSpi");
    }
}
