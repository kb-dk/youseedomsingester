package dk.statsbiblioteket.doms.vhs;

import dk.statsbiblioteket.doms.common.DefaultIngestContext;

/**
 * Tuple of input data for ingest.
 */
public class VHSIngestContext extends DefaultIngestContext {
    private String programPid;

    public String getProgramPid() {
        return programPid;
    }

    public void setProgramPid(String programPid) {
        this.programPid = programPid;
    }
}
