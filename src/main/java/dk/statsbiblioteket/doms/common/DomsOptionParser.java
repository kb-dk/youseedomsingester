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
    protected static final Option USERNAME_OPTION 
        = new Option("user", true, "The username for the DOMS server");
    protected static final Option PASSWORD_OPTION 
        = new Option("pass", true, "The password for the DOMS server");

    protected static final Option TEMPLATE_OPTION
        = new Option("template", true, "The pid of template object");
    protected static final Option WSDL_OPTION
        = new Option("wsdl", true, "The wsdl address of the doms server");

    protected static final Option PROGRAM_PID_OPTION
        = new Option("programpid", true, "The program pid to link to this object");


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
     * Method to verify that options are 'sane'. 
     * Default implementation checks that if username is supplied, password should also be supplied. 
     */
    protected void checkOptions() {
        if(getContext().getUsername() != null) {
            if(getContext().getPassword() == null) {
                throw new RuntimeException("Username was supplied, but no password. Can't continue.");
            }
        }
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
        parseUsername(cmd);
        parsePassword(cmd);
        parseTemplate(cmd);
        parseWSDL(cmd);


        parseSpecifics(cmd);
        checkOptions();
        
        log.debug("Read parameters for '{}'. Context: '{}'",
                getContext().getFilename(), getContext());
        return getContext();
    }


    private void parseWSDL(CommandLine cmd) {
        String username = cmd.getOptionValue(WSDL_OPTION.getOpt());
        if (username != null) {
            getContext().setWSDLlocation(username);
        }
    }

    private void parseTemplate(CommandLine cmd) {
        String username = cmd.getOptionValue(TEMPLATE_OPTION.getOpt());
        if (username != null) {
            getContext().setTemplatePid(username);
        }
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
    
    protected void parseUsername(CommandLine cmd) {
        String username = cmd.getOptionValue(USERNAME_OPTION.getOpt());
        if (username != null) {
            getContext().setUsername(username);
        }
    }
    
    protected void parsePassword(CommandLine cmd){
        String password = cmd.getOptionValue(PASSWORD_OPTION.getOpt());
        if (password != null) {
            getContext().setPassword(password);
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
