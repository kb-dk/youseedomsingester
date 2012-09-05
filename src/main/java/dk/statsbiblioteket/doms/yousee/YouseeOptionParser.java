package dk.statsbiblioteket.doms.yousee;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.statsbiblioteket.doms.common.DomsOptionParser;
import dk.statsbiblioteket.doms.common.IngestContext;
import dk.statsbiblioteket.doms.common.OptionParseException;
import dk.statsbiblioteket.doms.vhs.VHSOptionParser;
import dk.statsbiblioteket.util.Files;

/** Parse options */
public class YouseeOptionParser extends DomsOptionParser {
	private YouseeIngestContext context;
    private static final Option CROSSCHECK_LOCATION_OPTION
            = new Option("crosscheck", true, "The file containing the crosscheck profile in xml");

    private final Logger log = LoggerFactory.getLogger(getClass());

    public YouseeOptionParser() {
    	options = new Options();
    	options.addOption(DomsOptionParser.FILENAME_OPTION);
        options.addOption(DomsOptionParser.URL_OPTION);
        options.addOption(DomsOptionParser.FFPROBE_LOCATION_OPTION);
        options.addOption(YouseeOptionParser.CROSSCHECK_LOCATION_OPTION);
        options.addOption(VHSOptionParser.METADATA_LOCATION_OPTION);
        for (Object option : options.getOptions()) {
            if (option instanceof Option) {
                Option option1 = (Option) option;
                option1.setRequired(true);
            }
        }
        options.addOption(DomsOptionParser.CONFIG_OPTION);
    }
    
    @Override
    protected void parseSpecifics(CommandLine cmd) throws OptionParseException {
    	String crosscheckLocation = cmd.getOptionValue(CROSSCHECK_LOCATION_OPTION.getOpt());
    	if (crosscheckLocation == null) {
    		parseError(CROSSCHECK_LOCATION_OPTION.toString());
    		throw new OptionParseException(CROSSCHECK_LOCATION_OPTION.toString());
    	}
    	try {
    		String crosscheckContents = Files.loadString(new File(crosscheckLocation));
    		YouseeIngestContext context = (YouseeIngestContext) getContext();
	    	context.setCrosscheckContents(crosscheckContents);
    	} catch (IOException e) {
    		parseError(e.toString());
    		throw new OptionParseException(e.toString());
    	}    
    }


	@Override
	protected synchronized IngestContext getContext() {
		if(context == null) {
			context = new YouseeIngestContext();
		}
		return context;
	}

	@Override
	protected String getHelpText() {
		return "youseeDomsIngester";
	}
}
