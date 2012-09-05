package dk.statsbiblioteket.doms.yousee;

import dk.statsbiblioteket.doms.common.Ingester;
import dk.statsbiblioteket.doms.common.IngesterFactory;

import java.util.Properties;

/** Create the object instances for doms ingester. */
public class YouseeIngesterFactory extends IngesterFactory {
    private Ingester ingester;

    /**
     * Initialise factory with given configuration.
     *
     * @param config The configuration.
     */
    public YouseeIngesterFactory(Properties config) {
    	super(config);
    }

    /**
     * Get ingester singleton.
     *
     * @return A doms ingester.
     */
    public synchronized Ingester getIngester() {
        if (ingester == null) {
            ingester = new YouseeDomsIngester(config, getWebservice());
        }
        return ingester;
    }

}
