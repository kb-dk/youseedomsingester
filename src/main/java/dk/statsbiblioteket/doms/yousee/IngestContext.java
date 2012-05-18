package dk.statsbiblioteket.doms.yousee;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 5/16/12
 * Time: 3:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class IngestContext {


    private String remoteURL;
    private String checksum;
    private String ffprobeContents;
    private String crosscheckContents;
    private String youseeMetadataContents;
    private String filename;

    public IngestContext(String filename) {
        this.filename = filename;
    }

    public String getRemoteURL() {
        return remoteURL;
    }

    public void setRemoteURL(String remoteURL) {
        this.remoteURL = remoteURL;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
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

    public String getYouseeMetadataContents() {
        return youseeMetadataContents;
    }

    public void setYouseeMetadataContents(String youseeMetadataContents) {
        this.youseeMetadataContents = youseeMetadataContents;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
