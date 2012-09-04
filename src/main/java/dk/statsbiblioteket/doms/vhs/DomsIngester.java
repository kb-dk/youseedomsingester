package dk.statsbiblioteket.doms.vhs;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.central.InvalidCredentialsException;
import dk.statsbiblioteket.doms.central.InvalidResourceException;
import dk.statsbiblioteket.doms.central.MethodFailedException;
import dk.statsbiblioteket.doms.common.FFProbeParser;
import dk.statsbiblioteket.doms.vhs.Ingester;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Properties;

/** Ingester for Doms. */
public class DomsIngester implements Ingester {
    private static final String TEMPLATE_PROPERTY
            = "dk.statsbiblioteket.doms.vhs.template";


    private final DocumentBuilder db;
    private final CentralWebservice centralWebservice;
    private final Properties config;

    public DomsIngester(Properties config, CentralWebservice webservice) {
        this.config = config;
        this.db = getDocumentBuilder();
        this.centralWebservice = webservice;
    }

    private DocumentBuilder getDocumentBuilder() {
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
     * @param context Command-line input parameters containing metadata to be
     * ingested in DOMS.
     * @return The PID of the resulting DOMS file-object, now containing the
     * metadata from context.
     */
    @Override
    public String ingest(IngestContext context) {
        // Template object to clone to get new objects, get from properties file
        String template = config.getProperty(TEMPLATE_PROPERTY,
                "doms:Template_RadioTVFile"); // 2nd arg is default value

        // Get FFProbe output from context
        String FFProbeOutput = context.getFfprobeContents();

        try {
            String formatUri
                    = (new FFProbeParser()).getFormatURIFromFFProbeOutput(
                    FFProbeOutput);

            // Via DOMS Central, get PID of DOMS file-object which corresponds
            // to the file with the given URL (URL from context).
            String message = "Processed by '" + getClass().getName() + "'";
            String PIDOfObjectWithURL;

            PIDOfObjectWithURL = centralWebservice.getFileObjectWithURL(
                    context.getRemoteURL());
            if (PIDOfObjectWithURL == null) {
                // If not found, clone template (config)
                PIDOfObjectWithURL = centralWebservice.newObject(template, null,
                        message);
                centralWebservice.addFileFromPermanentURL(PIDOfObjectWithURL,
                        context.getFilename(), null, context.getRemoteURL(),
                        formatUri, message);
            }

            // Mark object as in progress
            centralWebservice.markInProgressObject(Arrays.asList(
                    PIDOfObjectWithURL), message);

            // Update elements of object from context
            setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                    "FFPROBE", context.getFfprobeContents(), message);
            // Checksum is assumed to be part of received metadata.
            setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                    "VHS_METADATA", context.getVHSMetadataContents(),
                    message);
            //TODO: Update content models with format uris, METADATA

            // Mark object as published
            centralWebservice.markPublishedObject(Arrays.asList(
                    PIDOfObjectWithURL), message);

            return PIDOfObjectWithURL;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void setDatastreamContents(CentralWebservice webservice,
                                       String objectWithURL,
                                       String datastreamName,
                                       String datastreamContents,
                                       String message)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
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

    protected boolean xmlEquals(String ffprobeOrig, String ffprobeContents) {
        try {
            Document doc1= db.parse(new ByteArrayInputStream(
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


}
