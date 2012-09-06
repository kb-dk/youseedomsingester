package dk.statsbiblioteket.doms.vhs;

import java.util.Arrays;
import java.util.Properties;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.common.DomsIngester;
import dk.statsbiblioteket.doms.common.FFProbeParser;
import dk.statsbiblioteket.doms.common.IngestContext;

/** Ingester for Doms. */
public class VHSDomsIngester extends DomsIngester {
    public VHSDomsIngester(Properties config, CentralWebservice webservice) {
    	super(config, webservice);
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
    	VHSIngestContext vhsContext;
    	if(!(context instanceof VHSIngestContext)) {
    		throw new IllegalStateException("IngestContext context is not of type VHSIngestContext");
    	} else {
    		vhsContext = (VHSIngestContext) context;
    	}
        // Template object to clone to get new objects, get from properties file
        String template = config.getProperty(TEMPLATE_PROPERTY,
                "doms:Template_RadioTVFile"); // 2nd arg is default value

        String allowedFormatName = config.getProperty(ALLOWED_FORMAT_NAME_PROPERTY, "mpeg");
        String validFormatUri = config.getProperty(FORMAT_URI_PROPERTY, "info:pronom/x-fmt/386");
        
        // Get FFProbe output from context
        String FFProbeOutput = vhsContext.getFfprobeContents();

        try {
            String formatUri = (new FFProbeParser(allowedFormatName, validFormatUri))
            		.getFormatURIFromFFProbeOutput(FFProbeOutput);

            // Via DOMS Central, get PID of DOMS file-object which corresponds
            // to the file with the given URL (URL from context).
            String message = "Processed by '" + getClass().getName() + "'";
            String PIDOfObjectWithURL;

            PIDOfObjectWithURL = centralWebservice.getFileObjectWithURL(
            		vhsContext.getRemoteURL());
            if (PIDOfObjectWithURL == null) {
                // If not found, clone template (config)
                PIDOfObjectWithURL = centralWebservice.newObject(template, null,
                        message);
                centralWebservice.addFileFromPermanentURL(PIDOfObjectWithURL,
                		vhsContext.getFilename(), null, vhsContext.getRemoteURL(),
                        formatUri, message);
            }

            // Mark object as in progress
            centralWebservice.markInProgressObject(Arrays.asList(
                    PIDOfObjectWithURL), message);

            // Update elements of object from context
            setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                    "FFPROBE", vhsContext.getFfprobeContents(), message);
            // Checksum is assumed to be part of received metadata.
            setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                    "VHS_METADATA", vhsContext.getMetadataContents(),
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

}
