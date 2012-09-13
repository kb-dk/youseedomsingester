package dk.statsbiblioteket.doms.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

/** Parse options */
public abstract class DomsOptionParser {
    protected static final Option FILENAME_OPTION
        = new Option("filename", true, "The sb filename of the media file");
    protected static final Option URL_OPTION
        = new Option("url", true, "The permanent url to the file");
    protected static final Option FFPROBE_LOCATION_OPTION
        = new Option("ffprobe", true, "The file containing the ffprobe profile in xml");
    protected static final Option METADATA_LOCATION_OPTION
        = new Option("metadata", true, "The file containing the bibliographic metadata in xml");
    protected static final Option CONFIG_OPTION
        = new Option("config", true, "A property file with configuration");

    protected static Options options;

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Get the context fitting for the option parser 
     */
    protected abstract IngestContext getContext();

    protected abstract String getHelpText();

    protected void parseSpecifics(CommandLine cmd) throws OptionParseException {
        return;
    }

    /**
     * Parse options. Options can be seen by running with no options.
     * On parse errors, will print error message on System.err, and return null.
     * Options referring files will load the contents of those files.
     *
     * @param args Options.
     * @return The context parsed from options, or null on errors parsing or
     * loading content.
     * @throws OptionParseException 
     */
    public IngestContext parseOptions(String[] args) throws OptionParseException {
        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            parseError(e.toString());
            throw new OptionParseException(e.getMessage(), e);
        }

        parseFilename(cmd);
        parseURL(cmd);
        parseFFprobe(cmd);
        parseMetadata(cmd);
        parseConfig(cmd);
        parseSpecifics(cmd);

        log.debug("Read parameters for '{}'. Context: '{}'",
                getContext().getFilename(), getContext());
        return getContext();
    }

    protected void parseFilename(CommandLine cmd) throws OptionParseException {
        String filename = cmd.getOptionValue(FILENAME_OPTION.getOpt());
        if (filename == null) {
            parseError(FILENAME_OPTION.toString());
            throw new OptionParseException(FILENAME_OPTION.toString());
        } else {
            getContext().setFilename(filename);
        }
    }

    protected void parseURL(CommandLine cmd) throws OptionParseException {
        String url = cmd.getOptionValue(URL_OPTION.getOpt());
        if (url == null) {
            parseError(URL_OPTION.toString());
            throw new OptionParseException(URL_OPTION.toString());
        } else {
            getContext().setRemoteURL(url);
        }
    }

    protected void parseFFprobe(CommandLine cmd) throws OptionParseException {
        String ffprobeLocation = cmd.getOptionValue(FFPROBE_LOCATION_OPTION.getOpt());
        if (ffprobeLocation == null) {
            parseError(FFPROBE_LOCATION_OPTION.toString());
            throw new OptionParseException(FFPROBE_LOCATION_OPTION.toString());
        }
        try {
            String ffprobeContents = Files.loadString(new File(ffprobeLocation));
            getContext().setFfprobeContents(ffprobeContents);
        } catch (IOException e) {
            parseError(e.toString());
        }
    }

    protected void parseMetadata(CommandLine cmd) throws OptionParseException {
        String metadataLocation = cmd.getOptionValue(METADATA_LOCATION_OPTION.getOpt());
        if (metadataLocation == null) {
            parseError(METADATA_LOCATION_OPTION.toString());
            throw new OptionParseException(METADATA_LOCATION_OPTION.toString());
        }
        try {
            String metadataContents = Files.loadString(new File(metadataLocation));
            getContext().setMetadataContents(metadataContents);
        } catch (IOException e) {
            parseError(e.toString());
        }
    }

    protected void parseConfig(CommandLine cmd) throws OptionParseException {
        String configFile = cmd.getOptionValue(CONFIG_OPTION.getOpt());
        if (configFile != null) {
            try {
                Properties config = new Properties(System.getProperties());
                config.load(new FileInputStream(configFile));
                getContext().setConfig(config);
            } catch (IOException e) {
                parseError(e.toString());
                throw new OptionParseException(e.toString());
            }
        }
    }

    protected void printUsage() {
        final HelpFormatter usageFormatter = new HelpFormatter();
        usageFormatter.printHelp(getHelpText(), options, true);
    }

    protected void parseError(String message) {
        System.err.println("Error parsing arguments");
        System.err.println(message);
        log.error("Error parsing arguments: '{}'", message);
        printUsage();
    }
}
