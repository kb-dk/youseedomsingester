package dk.statsbiblioteket.doms.vhs;

import dk.statsbiblioteket.doms.common.DefaultIngestContext;

/**
 * Tuple of input data for ingest.
 */
public class VHSIngestContext extends DefaultIngestContext {
    private String crosscheckContents;
    private String programPid;

    public String getCrosscheckContents() {
        return crosscheckContents;
    }

    public void setCrosscheckContents(String crosscheckContents) {
        this.crosscheckContents = crosscheckContents;
    }

    public String getProgramPid() {
        return programPid;
    }

    public void setProgramPid(String programPid) {
        this.programPid = programPid;
    }
}
