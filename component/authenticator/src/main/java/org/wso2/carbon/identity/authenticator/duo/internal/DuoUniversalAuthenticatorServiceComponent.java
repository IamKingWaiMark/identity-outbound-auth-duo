package org.wso2.carbon.identity.authenticator.duo.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.application.authentication.framework.ApplicationAuthenticator;
import org.wso2.carbon.identity.authenticator.duo.DuoUniversalPrompt;
import org.wso2.carbon.user.core.service.RealmService;

import java.util.Hashtable;

/**
 * @author King Wai Mark @NYU
 */
public class DuoUniversalAuthenticatorServiceComponent {
    private static final Log log = LogFactory.getLog(DuoUniversalAuthenticatorServiceComponent.class);

    protected void activate(ComponentContext ctxt) {
        try {
            DuoUniversalPrompt authenticator = new DuoUniversalPrompt();
            Hashtable<String, String> props = new Hashtable<>();
            ctxt.getBundleContext().registerService(ApplicationAuthenticator.class.getName(),
                    authenticator, props);
            if (log.isDebugEnabled()) {
                log.debug("UniversalDuoAuthenticator bundle is activated");
            }
        } catch (Exception e) {
            log.fatal("Error while activating the DUO authenticator ", e);
        }



    }

    protected void deactivate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.info("DuoAuthenticator bundle is deactivated");
        }
    }

    protected void setRealmService(RealmService realmService) {
        log.debug("Setting the Realm Service");
        DuoServiceHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {
        log.debug("UnSetting the Realm Service");
        DuoServiceHolder.getInstance().setRealmService(null);
    }
}
