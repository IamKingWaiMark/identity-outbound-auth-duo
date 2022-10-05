package org.wso2.carbon.identity.authenticator.duo;

import com.duosecurity.exception.DuoException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authentication.framework.AbstractApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.FederatedApplicationAuthenticator;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.exception.AuthenticationFailedException;
import org.wso2.carbon.identity.application.common.model.Property;
import org.wso2.carbon.identity.core.util.IdentityUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * @author King Wai Mark @ NYU
 */
public class DuoUniversalPrompt extends AbstractApplicationAuthenticator implements FederatedApplicationAuthenticator {

    private static final long serialVersionUID = 4438354156955223655L;
    private static final Log log = LogFactory.getLog(DuoUniversalPrompt.class);
    private DuoUniversalPromptClient duoUniversalPromptClient;

    @Override
    public boolean canHandle(HttpServletRequest request) {

        return request.getParameter(DuoAuthenticatorConstants.SIG_RESPONSE) != null;
    }

    @Override
    protected void initiateAuthenticationRequest(HttpServletRequest request, HttpServletResponse response,
                                                 AuthenticationContext context)
            throws AuthenticationFailedException {
        Map<String, String> authenticatorProperties = context.getAuthenticatorProperties();

        context.setProperty(DuoAuthenticatorConstants.AUTHENTICATION, DuoAuthenticatorConstants.AUTHENTICATOR_NAME);

        String clientId = authenticatorProperties.get
                (DuoAuthenticatorConstants.INTEGRATION_KEY);
        String clientSecret = authenticatorProperties.get
                (DuoAuthenticatorConstants.SECRET_KEY);
        String apiHost = authenticatorProperties.get
                (DuoAuthenticatorConstants.HOST);
        String enrollmentPage = null;
        enrollmentPage = DuoAuthenticatorConstants.DUO_PAGE;
        String duoUrl = IdentityUtil.getServerURL(enrollmentPage, false, false);
        String redirectUri = duoUrl;
        String username = String.valueOf(context.getProperty(DuoAuthenticatorConstants.DUO_USERNAME));
        duoUniversalPromptClient = new DuoUniversalPromptClient(
                clientId, clientSecret, apiHost, redirectUri
        );

        try {
            response.sendRedirect("https://www.youtube.com/watch?v=AIwMdPKnWqY&ab_channel=" + username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Get the configuration properties of UI.
     */
    @Override
    public List<Property> getConfigurationProperties() {

        List<Property> configProperties = new ArrayList<>();

        Property duoHost = new Property();
        duoHost.setDisplayName("Host");
        duoHost.setName(DuoAuthenticatorConstants.HOST);
        duoHost.setDescription("Enter host name of Duo Account");
        duoHost.setRequired(true);
        duoHost.setDisplayOrder(1);
        configProperties.add(duoHost);

        Property integrationKey = new Property();
        integrationKey.setDisplayName("Integration Key");
        integrationKey.setName(DuoAuthenticatorConstants.INTEGRATION_KEY);
        integrationKey.setDescription("Enter Integration Key");
        integrationKey.setRequired(true);
        integrationKey.setDisplayOrder(2);
        configProperties.add(integrationKey);

        Property adminIntegrationKey = new Property();
        adminIntegrationKey.setDisplayName("Admin Integration Key");
        adminIntegrationKey.setName(DuoAuthenticatorConstants.ADMIN_IKEY);
        adminIntegrationKey.setDescription("Enter Admin Integration Key");
        adminIntegrationKey.setRequired(false);
        adminIntegrationKey.setDisplayOrder(3);
        configProperties.add(adminIntegrationKey);

        Property secretKey = new Property();
        secretKey.setDisplayName("Secret Key");
        secretKey.setName(DuoAuthenticatorConstants.SECRET_KEY);
        secretKey.setDescription("Enter Secret Key");
        secretKey.setRequired(true);
        secretKey.setConfidential(true);
        secretKey.setDisplayOrder(4);
        configProperties.add(secretKey);

        Property adminSecretKey = new Property();
        adminSecretKey.setName(DuoAuthenticatorConstants.ADMIN_SKEY);
        adminSecretKey.setDisplayName("Admin Secret Key");
        adminSecretKey.setRequired(false);
        adminSecretKey.setDescription("Enter Admin Secret Key");
        adminSecretKey.setConfidential(true);
        adminSecretKey.setDisplayOrder(5);
        configProperties.add(adminSecretKey);


        return configProperties;
    }

    @Override
    protected void processAuthenticationResponse(HttpServletRequest request, HttpServletResponse response,
                                                 AuthenticationContext context) throws AuthenticationFailedException {


    }

    @Override
    public String getContextIdentifier(HttpServletRequest request) {

        return request.getParameter(DuoAuthenticatorConstants.SESSION_DATA_KEY);
    }

    @Override
    public String getName() {

        return DuoUniversalPromptConstants.AUTHENTICATOR_NAME;
    }

    @Override
    public String getFriendlyName() {

        return DuoUniversalPromptConstants.AUTHENTICATOR_FRIENDLY_NAME;
    }

    @Override
    protected boolean retryAuthenticationEnabled() {

        return true;
    }
}
