package dk.statsbiblioteket.doms.yousee;

import org.w3c.dom.Document;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.central.CentralWebserviceService;
import dk.statsbiblioteket.doms.central.InvalidCredentialsException;
import dk.statsbiblioteket.doms.central.InvalidResourceException;
import dk.statsbiblioteket.doms.central.MethodFailedException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/** Ingester for Doms. */
public class DomsIngester implements Ingester {
    // TODO: Configurable
    private static final String TEMPLATE = "doms:Template_RadioTVFile";
    private static final String FEDORA_USERNAME = "fedoraAdmin";
    private static final String FEDORA_PASSWORD = "fedoraAdminPass";
    private static final String FEDORA_WEBSERVICE_URL = "http://localhost:7880/centralWebservice-service/central/?wsdl";
    //TODO: Config? Constant? From FFPROBE?
    private static final String HTTP_PURL_ORG_NET_MEDIATYPES_APPLICATION_MPEG4_GENERIC
            = "http://purl.org/NET/mediatypes/application/mpeg4-generic";
    private final DocumentBuilderFactory documentBuilderFactory;
    private final DocumentBuilder db;

    public DomsIngester(Properties config) throws Exception {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setCoalescing(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        documentBuilderFactory.setIgnoringComments(true);
        db = documentBuilderFactory.newDocumentBuilder();
    }

    @Override
    public String ingest(IngestContext context) throws Exception {
        CentralWebservice webservice = new CentralWebserviceService(new URL(FEDORA_WEBSERVICE_URL), new QName(
                "http://central.doms.statsbiblioteket.dk/", "CentralWebserviceService")).getCentralWebservicePort();
        Map<String, Object> domsAPILogin = ((BindingProvider) webservice).getRequestContext();
        domsAPILogin.put(BindingProvider.USERNAME_PROPERTY, FEDORA_USERNAME);
        domsAPILogin.put(BindingProvider.PASSWORD_PROPERTY, FEDORA_PASSWORD);

        // Look up object with the given URL
        String message = "Processed by '" + getClass().getName() + "'";
        String objectWithURL;
        try {
            objectWithURL = webservice.getFileObjectWithURL(context.getRemoteURL());
        } catch (InvalidResourceException e) {
            // If not found, clone template (config)
            objectWithURL = webservice.newObject(TEMPLATE, null, message);
            webservice.addFileFromPermanentURL(objectWithURL, context.getFilename(), null, context.getRemoteURL(),
                                               HTTP_PURL_ORG_NET_MEDIATYPES_APPLICATION_MPEG4_GENERIC, message);
        }

        webservice.markInProgressObject(Arrays.asList(objectWithURL), message);

        // Update elements of object from context
        setDatastreamContents(webservice, objectWithURL, "FFPROBE", context.getFfprobeContents(), message);
        setDatastreamContents(webservice, objectWithURL, "CROSSCHECK", context.getCrosscheckContents(), message);
        //TODO: Should this be transformed
        //TODO: What datastream?
        //TODO: Checksum??
        setDatastreamContents(webservice, objectWithURL, "METADATA", context.getChecksum(), message);
        //TODO: Update templates

        webservice.markPublishedObject(Arrays.asList(objectWithURL), message);
        return objectWithURL;
    }

    private void setDatastreamContents(CentralWebservice webservice, String objectWithURL, String datastreamName,
                                       String datastreamContents, String message)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        String datastreamContentsOrig = webservice.getDatastreamContents(objectWithURL, datastreamName);
        if (!xmlEquals(datastreamContentsOrig, datastreamContents)) {
            webservice.modifyDatastream(objectWithURL, datastreamName, datastreamContents, message);
        }
    }

    private boolean xmlEquals(String ffprobeOrig, String ffprobeContents) {
        try {
            Document doc1 = db.parse(ffprobeOrig);
            doc1.normalizeDocument();

            Document doc2 = db.parse(ffprobeContents);
            doc2.normalizeDocument();
            return doc1.isEqualNode(doc2);
        } catch (Exception e) {
            return false;
        }
    }
}
