package dk.statsbiblioteket.doms.yousee;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.central.CentralWebserviceService;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/** Create the object instances for doms ingester. */
public class IngesterFactory {
    private static Ingester ingester;
    private static final String FEDORA_USERNAME_PROPERTY = "dk.statsbiblioteket.doms.yousee.fedorausername";
    private static final String FEDORA_PASSWORD_PROPERTY = "dk.statsbiblioteket.doms.yousee.fedorapassword";
    private static final String FEDORA_WEBSERVICE_URL_PROPERTY = "dk.statsbiblioteket.doms.yousee.fedorawebserviceurl";

    /**
     * Get ingester singleton.
     *
     * @param config Configuration to use.
     * @return A doms ingester.
     */
    public static Ingester getIngester(Properties config) {
        if (ingester == null) {
            if (config == null) {
                config = new Properties(System.getProperties());
            }
            ingester = new DomsIngester(config, getWebservice(config));
        }
        return ingester;
    }

    private static CentralWebservice getWebservice(Properties config) {
        try {
            CentralWebservice webservice = new CentralWebserviceService(
                    new URL(config.getProperty(FEDORA_WEBSERVICE_URL_PROPERTY,
                                               "http://localhost:7880/centralWebservice-service/central/?wsdl")),
                    new QName("http://central.doms.statsbiblioteket.dk/", "CentralWebserviceService"))
                    .getCentralWebservicePort();
            Map<String, Object> domsAPILogin = ((BindingProvider) webservice).getRequestContext();
            domsAPILogin.put(BindingProvider.USERNAME_PROPERTY,
                             config.getProperty(FEDORA_USERNAME_PROPERTY, "fedoraAdmin"));
            domsAPILogin.put(BindingProvider.PASSWORD_PROPERTY,
                             config.getProperty(FEDORA_PASSWORD_PROPERTY, "fedoraAdminPass"));
            return webservice;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static OptionParser getOptionParser() {
        return new OptionParser();
    }
}
