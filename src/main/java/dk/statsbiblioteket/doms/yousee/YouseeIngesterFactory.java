package dk.statsbiblioteket.doms.yousee;

import dk.statsbiblioteket.doms.common.Ingester;
import dk.statsbiblioteket.doms.common.IngesterFactory;

/** Create the object instances for doms ingester. */
public class YouseeIngesterFactory extends IngesterFactory {
    private Ingester ingester;
    private YouseeIngestContext context;

    /**
     * Initialise factory with given configuration.
     *
     * @param config The configuration.
     */
    public YouseeIngesterFactory(YouseeIngestContext context) {
        super(context.getConfig());
        this.context = context;
    }

    /**
     * Get ingester singleton.
     *
     * @return A doms ingester.
     */
    public synchronized Ingester getIngester() {
        if (ingester == null) {
            ingester = new YouseeDomsIngester(config, getWebservice(context));
        }
        return ingester;
    }

}
