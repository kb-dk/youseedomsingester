package dk.statsbiblioteket.doms.radiotv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.statsbiblioteket.doms.common.OptionParseException;

/**
 * Command line interface for doms ingest in YouSee workflow.
 */
public class RadioTVIngesterCLI {
    /** Log for this class. */
    private static final Logger log = LoggerFactory.getLogger(RadioTVIngesterCLI.class);

    /**
     * Parse options and ingest into doms.
     *
     * @param args Options. Run with no parameters to get usage.
     */
    public static void main(String[] args) {
        RadioTVIngestContext context;
        try {
            context = (RadioTVIngestContext) new RadioTVOptionParser().parseOptions(args);
        } catch (OptionParseException e1) {
            System.exit(1);
            return;
        }

        String uuid;
        try {
            uuid = new RadioTVIngesterFactory(context).getIngester().ingest(context);
        } catch (Exception e) {
            System.err.println("Unable to ingest '" + context.getFilename()
                    + "' into doms: " + e);
            log.error("Unable to ingest '{}' into doms. Context: {}",
                    new Object[]{context.getFilename(), context, e});
            System.exit(2);
            return;
        }

        System.out.println("{\"domsPid\" : \"" + uuid + "\"}");
        System.exit(0);
    }
}
