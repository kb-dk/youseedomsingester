package dk.statsbiblioteket.doms.common;

import java.util.Properties;

public interface IngestContext {

    public abstract String getRemoteURL();

    public abstract void setRemoteURL(String remoteURL);

    public abstract String getFfprobeContents();

    public abstract void setFfprobeContents(String ffprobeContents);

    public abstract String getMetadataContents();

    public abstract void setMetadataContents(String metadataContents);

    public abstract String getFilename();

    public abstract void setFilename(String filename);

    public abstract Properties getConfig();

    public abstract void setConfig(Properties config);

    public abstract String getUsername();
    
    public abstract void setUsername(String username);

    public abstract String getPassword();
    
    public abstract void setPassword(String password);
    
    public abstract String toString();

}