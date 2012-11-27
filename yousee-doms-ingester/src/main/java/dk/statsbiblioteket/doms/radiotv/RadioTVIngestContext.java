package dk.statsbiblioteket.doms.radiotv;

import dk.statsbiblioteket.doms.common.DefaultIngestContext;

/**
 * Tuple of input data for ingest.
 */
public class RadioTVIngestContext extends DefaultIngestContext {
    private String crosscheckContents;

    public String getCrosscheckContents() {
        return crosscheckContents;
    }

    public void setCrosscheckContents(String crosscheckContents) {
        this.crosscheckContents = crosscheckContents;
    }
}
