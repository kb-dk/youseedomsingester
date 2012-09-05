package dk.statsbiblioteket.doms.yousee;

import java.util.Arrays;
import java.util.Properties;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.common.DomsIngester;
import dk.statsbiblioteket.doms.common.FFProbeParser;
import dk.statsbiblioteket.doms.common.IngestContext;

/** Ingester for Doms. */
public class YouseeDomsIngester extends DomsIngester {
    private static final String TEMPLATE_PROPERTY = "dk.statsbiblioteket.doms.common.template";


    public YouseeDomsIngester(Properties config, CentralWebservice webservice) {
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
    	YouseeIngestContext youseeContext;
    	if(!(context instanceof YouseeIngestContext)) {
    		throw new IllegalStateException("IngestContext context is not of type YouseeIngestContext");
    	} else {
    		youseeContext = (YouseeIngestContext) context;
    	}
        // Template object to clone to get new objects, get from properties file
        String template = config.getProperty(TEMPLATE_PROPERTY, "doms:Template_RadioTVFile"); 
        
        // Get FFProbe output from context
        String FFProbeOutput = youseeContext.getFfprobeContents();
        //String formatUri = config.getProperty(
        //        "dk.statsbiblioteket.doms.yousee.formaturi",
        //        "info:mime/video/MP2T;codecs=\"aac_latm,dvbsub,h264\"");

        try {
            String formatUri
                    = (new FFProbeParser("mpegts", "info:mime/video/MP2T")).getFormatURIFromFFProbeOutput(
                    FFProbeOutput);

            // Via DOMS Central, get PID of DOMS file-object which corresponds
            // to the file with the given URL (URL from context).
            String message = "Processed by '" + getClass().getName() + "'";
            String PIDOfObjectWithURL;

            PIDOfObjectWithURL = centralWebservice.getFileObjectWithURL(
            		youseeContext.getRemoteURL());
            if (PIDOfObjectWithURL == null) {
                // If not found, clone template (config)
                PIDOfObjectWithURL = centralWebservice.newObject(template, null,
                        message);
                centralWebservice.addFileFromPermanentURL(PIDOfObjectWithURL,
                		youseeContext.getFilename(), null, youseeContext.getRemoteURL(),
                        formatUri, message);
            }

            // Mark object as in progress
            centralWebservice.markInProgressObject(Arrays.asList(
                    PIDOfObjectWithURL), message);

            // Update elements of object from context
            setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                    "FFPROBE", youseeContext.getFfprobeContents(), message);
            setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                    "CROSSCHECK", youseeContext.getCrosscheckContents(), message);
            // Checksum is assumed to be part of received metadata.
            setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                    "BROADCAST_METADATA", youseeContext.getMetadataContents(),
                    message);
            //TODO: Update content models with format uris, CROSSCHECK, METADATA

            // Mark object as published
            centralWebservice.markPublishedObject(Arrays.asList(
                    PIDOfObjectWithURL), message);

            return PIDOfObjectWithURL;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
