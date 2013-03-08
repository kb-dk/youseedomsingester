package dk.statsbiblioteket.doms.radiotv;

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
import dk.statsbiblioteket.util.Files;

import javax.xml.bind.JAXBException;

/** Parse options */
public class RadioTVOptionParser extends DomsOptionParser {
    private RadioTVIngestContext context;
    private static final Option CROSSCHECK_LOCATION_OPTION
    = new Option("crosscheck", true, "The file containing the crosscheck xml-profile");

    private final Logger log = LoggerFactory.getLogger(getClass());

    public RadioTVOptionParser() throws JAXBException {
        super();
        options = new Options();
        options.addOption(DomsOptionParser.FILENAME_OPTION);
        options.addOption(DomsOptionParser.URL_OPTION);
        options.addOption(DomsOptionParser.FFPROBE_LOCATION_OPTION);
        options.addOption(DomsOptionParser.FFPROBE_ERROR_LOG_LOCATION_OPTION);
        options.addOption(DomsOptionParser.METADATA_LOCATION_OPTION);
        for (Object option : options.getOptions()) {
            if (option instanceof Option) {
                ((Option) option).setRequired(true);
            }
        }

        options.addOption(DomsOptionParser.USERNAME_OPTION);
        options.addOption(DomsOptionParser.PASSWORD_OPTION);
        options.addOption(RadioTVOptionParser.CROSSCHECK_LOCATION_OPTION);
        options.addOption(DomsOptionParser.CONFIG_OPTION);
        options.addOption(DomsOptionParser.PROGRAM_PID_OPTION);
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
            RadioTVIngestContext context = (RadioTVIngestContext) getContext();
            context.setCrosscheckContents(crosscheckContents);
        } catch (IOException e) {
            parseError(e.toString());
            throw new OptionParseException(e.toString());
        }
        
        String programPid = cmd.getOptionValue(PROGRAM_PID_OPTION.getOpt());
        if (programPid != null) {
            ((RadioTVIngestContext) getContext()).setProgramPid(programPid);
        }
    }


    @Override
    protected synchronized IngestContext getContext() {
        if(context == null) {
            context = new RadioTVIngestContext();
        }
        return context;
    }

    @Override
    protected String getHelpText() {
        return "youseeDomsIngester";
    }
}
