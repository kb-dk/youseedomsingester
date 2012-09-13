package dk.statsbiblioteket.doms.vhs;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.statsbiblioteket.doms.common.DomsOptionParser;
import dk.statsbiblioteket.doms.common.IngestContext;

/** Parse options */
public class VHSOptionParser extends DomsOptionParser {
    private VHSIngestContext context;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public VHSOptionParser() {
        options = new Options();
        options.addOption(DomsOptionParser.FILENAME_OPTION);
        options.addOption(DomsOptionParser.URL_OPTION);
        options.addOption(DomsOptionParser.FFPROBE_LOCATION_OPTION);
        options.addOption(VHSOptionParser.METADATA_LOCATION_OPTION);
        for (Object option : options.getOptions()) {
            if (option instanceof Option) {
                ((Option) option).setRequired(true);
            }
        }
        options.addOption(DomsOptionParser.CONFIG_OPTION);
    }

    @Override
    protected synchronized IngestContext getContext() {
        if(context == null) {
            context = new VHSIngestContext();
        }
        return context; 
    }

    @Override
    protected String getHelpText() {
        return "vhsDomsIngester";
    }
}
