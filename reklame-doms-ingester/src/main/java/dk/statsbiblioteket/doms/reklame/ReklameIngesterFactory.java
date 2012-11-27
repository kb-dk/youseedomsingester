package dk.statsbiblioteket.doms.reklame;

import dk.statsbiblioteket.doms.common.DefaultIngestContext;
import dk.statsbiblioteket.doms.common.Ingester;
import dk.statsbiblioteket.doms.common.IngesterFactory;

/** Create the object instances for doms ingester. */
public class ReklameIngesterFactory extends IngesterFactory {
    private Ingester ingester;
    private DefaultIngestContext context;

    /**
     * Initialise factory with given configuration.
     *
     * @param context The configuration.
     */
    public ReklameIngesterFactory(DefaultIngestContext context) {
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
            ingester = new ReklameDomsIngester(config, getWebservice(context));
        }
        return ingester;
    }

}
