package dk.statsbiblioteket.doms.yousee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command line interface for doms ingest in YouSee workflow.
 */
public class YouseeIngesterCLI {
    /** Log for this class. */
    private static final Logger log = LoggerFactory.getLogger(YouseeIngesterCLI.class);

    /**
     * Parse options and ingest into doms.
     *
     * @param args Options. Run with no parameters to get usage.
     */
    public static void main( String[] args ) {
        IngestContext context = IngesterFactory.getOptionParser().parseOptions(args);
        if (context == null) {
            System.exit(1);
            return;
        }

        String uuid;
        try {
            uuid = IngesterFactory.getIngester(context.getConfig()).ingest(context);
        } catch (Exception e) {
            System.err.println("Unable to ingest '" + context.getFilename() + "' into doms: " + e);
            log.error("Unable to ingest '{}' into doms. Context: {}", new Object[]{context.getFilename(), context, e});
            System.exit(2);
            return;
        }

        System.out.println("{" + "\"domsPid\":\"" + uuid + "\"}");
        System.exit(0);
    }
}
