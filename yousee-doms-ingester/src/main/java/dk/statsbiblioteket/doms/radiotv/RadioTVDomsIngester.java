package dk.statsbiblioteket.doms.radiotv;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.central.Relation;
import dk.statsbiblioteket.doms.common.DomsIngester;
import dk.statsbiblioteket.doms.common.FFProbeParser;
import dk.statsbiblioteket.doms.common.IngestContext;

/** Ingester for Doms. */
public class RadioTVDomsIngester extends DomsIngester {
    private static final String TEMPLATE_PROPERTY = "dk.statsbiblioteket.doms.common.template";


    public RadioTVDomsIngester(Properties config, CentralWebservice webservice) {
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
        RadioTVIngestContext radioTVContext;
        if(!(context instanceof RadioTVIngestContext)) {
            throw new IllegalStateException("IngestContext context is not of type RadioTVIngestContext");
        } else {
            radioTVContext = (RadioTVIngestContext) context;
        }
        checkConfig(radioTVContext);
        // Template object to clone to get new objects, get from properties file
        String template = config.getProperty(TEMPLATE_PROPERTY, "doms:Template_RadioTVFile"); 
        Map<String,String> allowedFormats = getAllowedFormatsProperty();
        //String validFormatUri = config.getProperty(FORMAT_URI_PROPERTY);
        
        // Get FFProbe output from context
        String FFProbeOutput = radioTVContext.getFfprobeContents();
        
        try {
            FFProbeParser ffProbeParser = new FFProbeParser(allowedFormats, appendCodecToFormatUri());
            String formatName = ffProbeParser.getFormatNameFromFFProbeOutput(FFProbeOutput);


            if(formatName.equals("mpeg") && (radioTVContext.getCrosscheckContents() != null)) {
                throw new RuntimeException("The format name is " + formatName +
                        ", and you provided crosscheckoutput, that cannot be.");
            }
            if(formatName.equals("mpegts") && (radioTVContext.getCrosscheckContents() == null)) {
                throw new RuntimeException("The format name is " + formatName +
                        ", and you didn't provide the required crosscheckoutput.");
            }

            String formatUri = ffProbeParser.getFormatURIFromFFProbeOutput(FFProbeOutput);

            // Via DOMS Central, get PID of DOMS file-object which corresponds
            // to the file with the given URL (URL from context).
            String message = "Processed by '" + getClass().getName() + "'";
            String PIDOfObjectWithURL;

            PIDOfObjectWithURL = centralWebservice.getFileObjectWithURL(
                    radioTVContext.getRemoteURL());
            if (PIDOfObjectWithURL == null) {
                // If not found, clone template (config)
                PIDOfObjectWithURL = centralWebservice.newObject(template, null,
                        message);
                centralWebservice.addFileFromPermanentURL(PIDOfObjectWithURL,
                        radioTVContext.getFilename(), null, radioTVContext.getRemoteURL(),
                        formatUri, message);
            }

            // Mark object as in progress
            centralWebservice.markInProgressObject(Arrays.asList(
                    PIDOfObjectWithURL), message);

            // Update elements of object from context
            setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                    "FFPROBE", radioTVContext.getFfprobeContents(), message);

            setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                    "FFPROBE_ERROR_LOG", radioTVContext.getFfprobeErrorContents(), message);

            // Set crosscheck datastream if it's provided. 
            if(radioTVContext.getCrosscheckContents() != null) {
                setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                        "CROSSCHECK", radioTVContext.getCrosscheckContents(), message);
            }
            // Checksum is assumed to be part of received metadata.
            setDatastreamContents(centralWebservice, PIDOfObjectWithURL,
                    "BROADCAST_METADATA", radioTVContext.getMetadataContents(),
                    message);
            //TODO: Update content models with format uris, CROSSCHECK, METADATA

            // Mark object as published
            centralWebservice.markPublishedObject(Arrays.asList(
                    PIDOfObjectWithURL), message);

            // Create the hasFile and hasExactFile relations in the Program Object, if any such was provided.
            if (radioTVContext.getProgramPid() != null){
                centralWebservice.markInProgressObject(Arrays.asList(radioTVContext.getProgramPid()), message);
                Relation rel = new Relation();
                rel.setLiteral(false);
                rel.setSubject(radioTVContext.getProgramPid());
                rel.setPredicate("http://doms.statsbiblioteket.dk/relations/default/0/1/#hasFile");
                rel.setObject(PIDOfObjectWithURL);
                centralWebservice.addRelation(radioTVContext.getProgramPid(), rel, message);
                Relation rel2 = new Relation();
                rel2.setLiteral(false);
                rel2.setSubject(radioTVContext.getProgramPid());
                rel2.setPredicate("http://doms.statsbiblioteket.dk/relations/default/0/1/#hasExactFile");
                rel2.setObject(PIDOfObjectWithURL);
                centralWebservice.addRelation(radioTVContext.getProgramPid(), rel2, message);
                centralWebservice.markPublishedObject(Arrays.asList(radioTVContext.getProgramPid()), message);
            }
            
            return PIDOfObjectWithURL;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
   
   
    private boolean appendCodecToFormatUri() {
        String allowedFormatName = config.getProperty(ALLOWED_FORMATS_PROPERTY);
        if(allowedFormatName.equals("mpeg")) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * Method to check if the config and context is sane 
     */
    private void checkConfig(RadioTVIngestContext context) {
        // TODO Consider moving into OptionParser class
        // TODO Make proper exceptions to toss
        String allowedFormatName = config.getProperty(ALLOWED_FORMATS_PROPERTY);
        if(allowedFormatName == null) {
            throw new RuntimeException("The config is missing '" + ALLOWED_FORMATS_PROPERTY + "'");
        }
    }
}
