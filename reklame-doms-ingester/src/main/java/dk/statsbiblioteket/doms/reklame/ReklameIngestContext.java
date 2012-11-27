package dk.statsbiblioteket.doms.reklame;

import dk.statsbiblioteket.doms.common.DefaultIngestContext;

/**
 * Tuple of input data for ingest.
 */
public class ReklameIngestContext extends DefaultIngestContext {
    private String pbCoreContents;
    private String reklameTemplatePid;
    private String reklameMetadata;

    public String getPbCoreContents() {
        return pbCoreContents;
    }

    public void setPbCoreContents(String pbCoreContents) {
        this.pbCoreContents = pbCoreContents;
    }

    public String getReklameTemplatePid() {
        return reklameTemplatePid;
    }

    public void setReklameTemplatePid(String reklameTemplatePid) {
        this.reklameTemplatePid = reklameTemplatePid;
    }

    public String getReklameMetadata() {
        return reklameMetadata;
    }

    public void setReklameMetadata(String reklameMetadata) {
        this.reklameMetadata = reklameMetadata;
    }
}
