package dk.statsbiblioteket.doms.yousee;

import java.util.Properties;

/**
 * Create the doms ingester instance.
 */
public class IngesterFactory {
    private static Ingester ingester;

    public static Ingester getIngester() {
        if (ingester == null) {
            // TODO: Consider where to get properties
            Properties config = System.getProperties();
            ingester = new DomsIngester(config);
        }
        return ingester;
    }
}
