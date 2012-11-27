package dk.statsbiblioteket.doms.radiotv;

import dk.statsbiblioteket.doms.common.Ingester;
import dk.statsbiblioteket.doms.common.IngesterFactory;

/** Create the object instances for doms ingester. */
public class RadioTVIngesterFactory extends IngesterFactory {
    private Ingester ingester;
    private RadioTVIngestContext context;

    /**
     * Initialise factory with given configuration.
     *
     * @param context The configuration.
     */
    public RadioTVIngesterFactory(RadioTVIngestContext context) {
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
            ingester = new RadioTVDomsIngester(config, getWebservice(context));
        }
        return ingester;
    }

}
