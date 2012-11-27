package dk.statsbiblioteket.doms.vhs;

import dk.statsbiblioteket.doms.common.Ingester;
import dk.statsbiblioteket.doms.common.IngesterFactory;

/** Create the object instances for doms ingester. */
public class VHSIngesterFactory extends IngesterFactory {
    private Ingester ingester;
    private VHSIngestContext context;

    /**
     * Initialise factory with given configuration.
     *
     * @param context The configuration.
     */
    public VHSIngesterFactory(VHSIngestContext context) {
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
            ingester = new VHSDomsIngester(config, getWebservice(context));
        }
        return ingester;
    }

}
