package dk.statsbiblioteket.doms.common;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.central.InvalidCredentialsException;
import dk.statsbiblioteket.doms.central.InvalidResourceException;
import dk.statsbiblioteket.doms.central.MethodFailedException;
import dk.statsbiblioteket.doms.common.IngestContext;
import dk.statsbiblioteket.doms.common.Ingester;

import jdk.nashorn.internal.parser.JSONParser;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Ingester for Doms.
 */
public abstract class DomsIngester implements Ingester {
    protected static final String TEMPLATE_PROPERTY = "dk.statsbiblioteket.doms.common.template";
    protected static final String ALLOWED_FORMATS_PROPERTY = "dk.statsbiblioteket.doms.common.allowedformats";
    //protected static final String FORMAT_URI_PROPERTY = "dk.statsbiblioteket.doms.common.formaturi";

    protected final DocumentBuilder db;
    protected final CentralWebservice centralWebservice;
    protected final Properties config;

    public DomsIngester(Properties config, CentralWebservice webservice) {
        this.config = config;
        this.db = getDocumentBuilder();
        this.centralWebservice = webservice;
    }

    protected DocumentBuilder getDocumentBuilder() {
        try {
            DocumentBuilderFactory documentBuilderFactory
                    = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilderFactory.setCoalescing(true);
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
            documentBuilderFactory.setIgnoringComments(true);
            return documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Ingest data from context into DOMS via template given by config.
     *
     * @param context Command-line input parameters containing metadata to be
     *                ingested in DOMS.
     * @return The PID of the resulting DOMS file-object, now containing the
     *         metadata from context.
     */
    @Override
    public abstract String ingest(IngestContext context);


    protected void setDatastreamContents(CentralWebservice webservice,
                                         String objectWithURL,
                                         String datastreamName,
                                         String datastreamContents,
                                         String message)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        //Remote any broken xml stuff
        datastreamContents = datastreamContents.replaceAll("<\\?xml.*\\?>", "");
        String datastreamContentsOrig = "";
        boolean update = false;
        try {
            datastreamContentsOrig
                    = webservice.getDatastreamContents(objectWithURL,
                    datastreamName);
            update = !xmlEquals(datastreamContentsOrig, datastreamContents);
        } catch (InvalidResourceException e) {
            update = true;
        }
        if (update) {
            webservice.modifyDatastream(objectWithURL, datastreamName,
                    datastreamContents, message);
        }
    }

    public boolean xmlEquals(String ffprobeOrig, String ffprobeContents) {
        try {
            Document doc1 = db.parse(new ByteArrayInputStream(
                    ffprobeOrig.getBytes()));
            doc1.normalizeDocument();

            Document doc2 = db.parse(new ByteArrayInputStream(
                    ffprobeContents.getBytes()));
            doc2.normalizeDocument();
            return doc1.isEqualNode(doc2);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Read the ALLOWED_FORMATS_PROPERTY from config, and transform into a Map.
     * @return a Map of allowed format names mapping to format URIs
     */
    protected Map<String,String> getAllowedFormatsProperty() {
        //TODO JSONParser?! (in JSON the ; should be :)
        Map<String, String> allowedFormats= new LinkedHashMap<String, String>();
        String allowedFormatsString = config.getProperty(ALLOWED_FORMATS_PROPERTY, "\"mpeg\":\"info:pronom/x-fmt/386\"");
        String[] allowedFormatsStringArray = allowedFormatsString.split(",");
        for (String format:allowedFormatsStringArray) {
            String[] formatNameUri = format.split(";");
            if (formatNameUri.length==2) {
                allowedFormats.put(formatNameUri[0], formatNameUri[1]);
            } else {
                throw new RuntimeException("Invalid allowedformats property in config file: '" +
                        allowedFormatsString +"'");
            }
        }
        return allowedFormats;
    }

}
