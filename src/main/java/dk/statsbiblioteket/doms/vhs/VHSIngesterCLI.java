package dk.statsbiblioteket.doms.vhs;

import dk.statsbiblioteket.doms.vhs.IngestContext;
import dk.statsbiblioteket.doms.vhs.IngesterFactory;
import dk.statsbiblioteket.doms.vhs.OptionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command line interface for doms ingest in VHS workflow.
 */
public class VHSIngesterCLI {
    /** Log for this class. */
    private static final Logger log
            = LoggerFactory.getLogger(VHSIngesterCLI.class);

    /**
     * Parse options and ingest into doms.
     *
     * @param args Options. Run with no parameters to get usage.
     */
    public static void main(String[] args) {
        IngestContext context = new OptionParser().parseOptions(args);
        if (context == null) {
            System.exit(1);
            return;
        }

        String uuid;
        try {
            uuid = new IngesterFactory(context.getConfig()).getIngester().ingest(context);
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
