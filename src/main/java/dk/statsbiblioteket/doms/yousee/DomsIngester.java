package dk.statsbiblioteket.doms.yousee;

import org.w3c.dom.Document;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.central.InvalidCredentialsException;
import dk.statsbiblioteket.doms.central.InvalidResourceException;
import dk.statsbiblioteket.doms.central.MethodFailedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Arrays;
import java.util.Properties;

/** Ingester for Doms. */
public class DomsIngester implements Ingester {
    private static final String TEMPLATE_PROPERTY = "dk.statsbiblioteket.doms.yousee.template";
    //TODO: Constant? From FFPROBE? Input?
    private static final String FORMAT_URI_PROPERTY = "dk.statsbiblioteket.doms.yousee.formaturi";

    private final DocumentBuilder db;
    private final CentralWebservice webservice;
    private final Properties config;

    public DomsIngester(Properties config, CentralWebservice webservice) {
        this.config = config;
        this.db = getDocumentBuilder();
        this.webservice = webservice;
    }

    private DocumentBuilder getDocumentBuilder() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilderFactory.setCoalescing(true);
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
            documentBuilderFactory.setIgnoringComments(true);
            return documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String ingest(IngestContext context) {
        String template = config.getProperty(TEMPLATE_PROPERTY, "doms:Template_RadioTVFile");
        String formatUri = config
                .getProperty(FORMAT_URI_PROPERTY, "info:mime/video/MP2T;codecs=\"aac_latm,dvbsub,h264\"");

        try {
            // Look up object with the given URL
            String message = "Processed by '" + getClass().getName() + "'";
            String objectWithURL;
            try {
                objectWithURL = webservice.getFileObjectWithURL(context.getRemoteURL());
            } catch (InvalidResourceException e) {
                // If not found, clone template (config)
                objectWithURL = webservice.newObject(template, null, message);
                webservice.addFileFromPermanentURL(objectWithURL, context.getFilename(), null, context.getRemoteURL(),
                                                   formatUri, message);
            }

            webservice.markInProgressObject(Arrays.asList(objectWithURL), message);

            // Update elements of object from context
            setDatastreamContents(webservice, objectWithURL, "FFPROBE", context.getFfprobeContents(), message);
            setDatastreamContents(webservice, objectWithURL, "CROSSCHECK", context.getCrosscheckContents(), message);
            //TODO: Name of datastream?
            setDatastreamContents(webservice, objectWithURL, "METADATA", context.getChecksum(), message);
            //TODO: Checksum (In METADATA? Own datastream?)
            //TODO: Update content models with format uris, CROSSCHECK, METADATA
            //TODO: Update templates

            webservice.markPublishedObject(Arrays.asList(objectWithURL), message);
            return objectWithURL;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
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
