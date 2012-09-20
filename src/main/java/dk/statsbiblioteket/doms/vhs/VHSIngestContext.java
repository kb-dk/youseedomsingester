package dk.statsbiblioteket.doms.vhs;

import java.util.Properties;

import dk.statsbiblioteket.doms.common.IngestContext;

/**
 * Tuple of input data for ingest.
 */
public class VHSIngestContext implements IngestContext {
    private String remoteURL;
    private String ffprobeContents;
    // Checksum is assumed to be part of received metadata.
    private String VHSMetadataContents;
    private String filename;
    private Properties config;
    private String username;
    private String password;
    private String templatePid;
    private String WSDLlocation;

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#getRemoteURL()
     */
    @Override
    public String getRemoteURL() {
        return remoteURL;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#setRemoteURL(java.lang.String)
     */
    @Override
    public void setRemoteURL(String remoteURL) {
        this.remoteURL = remoteURL;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#getFfprobeContents()
     */
    @Override
    public String getFfprobeContents() {
        return ffprobeContents;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#setFfprobeContents(java.lang.String)
     */
    @Override
    public void setFfprobeContents(String ffprobeContents) {
        this.ffprobeContents = ffprobeContents;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#getVHSMetadataContents()
     */
    @Override
    public String getMetadataContents() {
        return VHSMetadataContents;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#setVHSMetadataContents(java.lang.String)
     */
    @Override
    public void setMetadataContents(String metadataContents) {
        this.VHSMetadataContents = metadataContents;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#getFilename()
     */
    @Override
    public String getFilename() {
        return filename;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#setFilename(java.lang.String)
     */
    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#getConfig()
     */
    @Override
    public Properties getConfig() {
        return config;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#setConfig(java.util.Properties)
     */
    @Override
    public void setConfig(Properties config) {
        this.config = config;
    }

    /* (non-Javadoc)
     * @see dk.statsbiblioteket.doms.vhs.IngestContextIface#toString()
     */
    @Override
    public String toString() {
        return "IngestContext{"
                + "remoteURL='" + remoteURL + '\''
                + ", ffprobeContents='" + ffprobeContents + '\''
                + ", VHSMetadataContents='" + VHSMetadataContents + '\''
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

    public String getTemplatePid() {
        return templatePid;
    }

    public void setTemplatePid(String templatePid) {
        this.templatePid = templatePid;
    }

    public String getWSDLlocation() {
        return WSDLlocation;
    }

    public void setWSDLlocation(String WSDLlocation) {
        this.WSDLlocation = WSDLlocation;
    }
}
