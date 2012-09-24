package dk.statsbiblioteket.doms.radiotv;

import java.util.Properties;

import dk.statsbiblioteket.doms.common.IngestContext;

/**
 * Tuple of input data for ingest.
 */
public class RadioTVIngestContext implements IngestContext {
    private String remoteURL;
    private String ffprobeContents;
    private String crosscheckContents;
    // Checksum is assumed to be part of received metadata.
    private String youseeMetadataContents;
    private String filename;
    private String username;
    private String password;
    private Properties config;

    public String getRemoteURL() {
        return remoteURL;
    }

    public void setRemoteURL(String remoteURL) {
        this.remoteURL = remoteURL;
    }

    public String getFfprobeContents() {
        return ffprobeContents;
    }

    public void setFfprobeContents(String ffprobeContents) {
        this.ffprobeContents = ffprobeContents;
    }

    public String getCrosscheckContents() {
        return crosscheckContents;
    }

    public void setCrosscheckContents(String crosscheckContents) {
        this.crosscheckContents = crosscheckContents;
    }

    public String getMetadataContents() {
        return youseeMetadataContents;
    }

    public void setMetadataContents(String metadataContents) {
        this.youseeMetadataContents = metadataContents;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Properties getConfig() {
        return config;
    }

    public void setConfig(Properties config) {
        this.config = config;
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
    public String toString() {
        return "IngestContext{"
                + "remoteURL='" + remoteURL + '\''
                + ", ffprobeContents='" + ffprobeContents + '\''
                + ", crosscheckContents='" + crosscheckContents + '\''
                + ", youseeMetadataContents='" + youseeMetadataContents + '\''
                + ", filename='" + filename + '\''
                + ", configFile='" + config + '\''
                + '}';
    }

    @Override
    public String getTemplatePid() {
        return null;
    }

    @Override
    public void setTemplatePid(String templatePid) {
        throw new IllegalAccessError("not implemented");
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getWSDLlocation() {
       return null;

    }

    @Override
    public void setWSDLlocation(String wsdlLocation) {
        throw new IllegalAccessError("not implemented");
    }
}
