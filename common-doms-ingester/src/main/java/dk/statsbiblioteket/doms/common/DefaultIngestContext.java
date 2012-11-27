package dk.statsbiblioteket.doms.common;

import java.util.Properties;

/**
 * Tuple of input data for ingest.
 */
public class DefaultIngestContext implements IngestContext {
    private String remoteURL;
    private String ffprobeContents;
    // Checksum is assumed to be part of received metadata.
    private String reklameMetadataContents;
    private String filename;
    private Properties config;
    private String username;
    private String password;
    private String templatePid;
    private String WSDLlocation;
    private String ffprobeErrorLog;


    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reklame.IngestContextIface#getRemoteURL()
     */
    @Override
    public String getRemoteURL() {
        return remoteURL;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reklame.IngestContextIface#setRemoteURL(java.lang.String)
     */
    @Override
    public void setRemoteURL(String remoteURL) {
        this.remoteURL = remoteURL;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reaklme.IngestContextIface#getFfprobeContents()
     */
    @Override
    public String getFfprobeContents() {
        return ffprobeContents;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reklame.IngestContextIface#setFfprobeContents(java.lang.String)
     */
    @Override
    public void setFfprobeContents(String ffprobeContents) {
        this.ffprobeContents = ffprobeContents;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reklame.IngestContextIface#getMetadataContents()
     */
    @Override
    public String getMetadataContents() {
        return reklameMetadataContents;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reklame.IngestContextIface#setMetadataContents(java.lang.String)
     */
    @Override
    public void setMetadataContents(String metadataContents) {
        this.reklameMetadataContents = metadataContents;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reklame.IngestContextIface#getFilename()
     */
    @Override
    public String getFilename() {
        return filename;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reklame.IngestContextIface#setFilename(java.lang.String)
     */
    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reklame.IngestContextIface#getConfig()
     */
    @Override
    public Properties getConfig() {
        return config;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reklame.IngestContextIface#setConfig(java.util.Properties)
     */
    @Override
    public void setConfig(Properties config) {
        this.config = config;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.reklame.IngestContextIface#toString()
     */
    @Override
    public String toString() {
        return "IngestContext{"
                + "remoteURL='" + remoteURL + '\''
                + ", ffprobeContents='" + ffprobeContents + '\''
                + ", reklameMetadataContents='" + reklameMetadataContents + '\''
                + ", filename='" + filename + '\''
                + ", configFile='" + config + '\''
                + '}';
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getTemplatePid() {
        return templatePid;
    }

    @Override
    public void setTemplatePid(String templatePid) {
        this.templatePid = templatePid;
    }

    @Override
    public String getWSDLlocation() {
        return WSDLlocation;
    }

    @Override
    public void setWSDLlocation(String WSDLlocation) {
        this.WSDLlocation = WSDLlocation;
    }

    @Override
    public void setFfprobeErrorContents(String ffprobeErrorLog) {
        this.ffprobeErrorLog = ffprobeErrorLog;
    }

    @Override
    public String getFfprobeErrorContents() {
        return ffprobeErrorLog;
    }
}
