package dk.statsbiblioteket.doms.yousee;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.statsbiblioteket.util.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/** Parse options */
public class OptionParser {
    private static final Option FILENAME_OPTION
            = new Option("filename", true, "The sb filename of the media file");
    private static final Option URL_OPTION
            = new Option("url", true, "The permanent url to the file");
    private static final Option FFPROBE_LOCATION_OPTION
            = new Option("ffprobe", true, "The file containing the ffprobe profile in xml");
    private static final Option CHECKSUM_OPTION
            = new Option("checksum", true, "The checksum of the file");
    private static final Option CROSSCHECK_LOCATION_OPTION
            = new Option("crosscheck", true, "The file containing the crosscheck profile in xml");
    private static final Option METADATA_LOCATION_OPTION
            = new Option("metadata", true, "The file containing the bibliographic metadata in xml");
    private static final Option CONFIG_OPTION
            = new Option("cinfig", true, "A property file with configuration");

    private static Options options;
    static {
        OptionParser.options = new Options();
        OptionParser.options.addOption(OptionParser.FILENAME_OPTION);
        OptionParser.options.addOption(OptionParser.URL_OPTION);
        OptionParser.options.addOption(OptionParser.FFPROBE_LOCATION_OPTION);
        OptionParser.options.addOption(OptionParser.CHECKSUM_OPTION);
        OptionParser.options.addOption(OptionParser.CROSSCHECK_LOCATION_OPTION);
        OptionParser.options.addOption(OptionParser.METADATA_LOCATION_OPTION);
        for (Object option : OptionParser.options.getOptions()) {
            if (option instanceof Option) {
                Option option1 = (Option) option;
                option1.setRequired(true);
            }
        }
        OptionParser.options.addOption(OptionParser.CONFIG_OPTION);
    }

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Parse options. Options can be seen by running with no options.
     * On parse errors, will print error message on System.err, and return null.
     * Options referring files will load the contents of those files.
     *
     * @param args Options.
     * @return The context parsed from options, or null on errors parsing or loading content.
     */
    public IngestContext parseOptions(String[] args) {
        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            parseError(e.toString());
            return null;
        }

        String filename = cmd.getOptionValue(FILENAME_OPTION.getOpt());
        if (filename == null) {
            parseError(FILENAME_OPTION.toString());
            return null;
        }
        IngestContext context = new IngestContext(filename);

        String url = cmd.getOptionValue(URL_OPTION.getOpt());
        if (url == null) {
            parseError(URL_OPTION.toString());
            return null;
        }
        context.setRemoteURL(url);

        String checksum = cmd.getOptionValue(CHECKSUM_OPTION.getOpt());
        if (checksum == null) {
            parseError(CHECKSUM_OPTION.toString());
            return null;
        }
        context.setChecksum(checksum);

        String ffprobeLocation = cmd.getOptionValue(FFPROBE_LOCATION_OPTION.getOpt());
        if (ffprobeLocation == null) {
            parseError(FFPROBE_LOCATION_OPTION.toString());
            return null;
        }
        try {
            String ffprobeContents = Files.loadString(new File(ffprobeLocation));
            context.setCrosscheckContents(ffprobeContents);
        } catch (IOException e) {
            parseError(e.toString());
            return null;
        }

        String crosscheckLocation = cmd.getOptionValue(CROSSCHECK_LOCATION_OPTION.getOpt());
        if (crosscheckLocation == null) {
            parseError(CROSSCHECK_LOCATION_OPTION.toString());
            return null;
        }
        try {
            String crosscheckContents = Files.loadString(new File(crosscheckLocation));
            context.setCrosscheckContents(crosscheckContents);
        } catch (IOException e) {
            parseError(e.toString());
            return null;
        }

        String metadataLocation = cmd.getOptionValue(METADATA_LOCATION_OPTION.getOpt());
        if (metadataLocation == null) {
            parseError(METADATA_LOCATION_OPTION.toString());
            return null;
        }
        try {
            String metadataContents = Files.loadString(new File(metadataLocation));
            context.setYouseeMetadataContents(metadataContents);
        } catch (IOException e) {
            parseError(e.toString());
            return null;
        }

        String configFile = cmd.getOptionValue(CONFIG_OPTION.getOpt());
        if (configFile != null) {
            try {
                Properties config = new Properties(System.getProperties());
                config.load(new FileInputStream(configFile));
                context.setConfig(config);
            } catch (IOException e) {
                parseError(e.toString());
                return null;
            }
        }

        log.debug("Read parameters for '{}'. Context: '{}'", context.getFilename(), context);
        return context;
    }

    private void printUsage() {
        final HelpFormatter usageFormatter = new HelpFormatter();
        usageFormatter.printHelp("youseeDomsIngester", options, true);
    }

    private void parseError(String message) {
        System.err.println("Error parsing arguments");
        System.err.println(message);
        log.error("Error parsing arguments: '{}'", message);
        printUsage();
    }
}
