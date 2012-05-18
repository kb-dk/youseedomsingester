package dk.statsbiblioteket.doms.yousee;

import dk.statsbiblioteket.util.Files;
import org.apache.commons.cli.*;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class YouseeIngesterCLI {

    private static Options options;

    private static final Option FILENAME_OPTION = new Option("filename", true,
                                                             "The sb filename of the media file");
    private static final Option URL_OPTION = new Option("url", true,
                                                        "The permanent url to the file");
    private static final Option FFPROBE_LOCATION_OPTION = new Option("ffprobe", true,
                                                                     "The file containing the ffprobe profile in xml");
    private static final Option CHECKSUM_OPTION = new Option("checksum", true,
                                                             "The checksum of the file");
    private static final Option CROSSCHECK_LOCATION_OPTION = new Option("crosscheck", true,
                                                                        "The file containing the crosscheck profile in xml");
    private static final Option METADATA_LOCATION_OPTION = new Option("metadata", true,
                                                                      "The file containing the bibliographic metadata in xml");


    static{
        options = new Options();
        options.addOption(FILENAME_OPTION);
        options.addOption(URL_OPTION);
        options.addOption(FFPROBE_LOCATION_OPTION);
        options.addOption(CHECKSUM_OPTION);
        options.addOption(CROSSCHECK_LOCATION_OPTION);
        options.addOption(METADATA_LOCATION_OPTION);
        for (Object option : options.getOptions()) {
            if (option instanceof Option) {
                Option option1 = (Option) option;
                option1.setRequired(true);
            }
        }


    }

    public static void printUsage(){
        final HelpFormatter usageFormatter = new HelpFormatter();
        usageFormatter.printHelp("youseeDomsIngester",options,true);
    }


    public static void main( String[] args ) {

        CommandLineParser parser = new PosixParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException e) {
            parseError(e.toString());
            return;
        }

        String filename = cmd.getOptionValue(FILENAME_OPTION.getOpt());
        if (filename == null){
            parseError(FILENAME_OPTION.toString());
            return;
        }
        IngestContext context = new IngestContext(filename);

        String url = cmd.getOptionValue(URL_OPTION.getOpt());
        if (url == null){
            parseError(URL_OPTION.toString());
            return;
        }
        context.setRemoteURL(url);

        String checksum = cmd.getOptionValue(CHECKSUM_OPTION.getOpt());
        if (checksum == null){
            parseError(CHECKSUM_OPTION.toString());
            return;
        }
        context.setChecksum(checksum);


        String ffprobeLocation = cmd.getOptionValue(FFPROBE_LOCATION_OPTION.getOpt());
        if (ffprobeLocation == null){
            parseError(FFPROBE_LOCATION_OPTION.toString());
            return;
        }
        try {
            String ffprobeContents = Files.loadString(new File(ffprobeLocation));
            context.setCrosscheckContents(ffprobeContents);
        } catch (IOException e) {
            parseError(e.toString());
            return;
        }

        String crosscheckLocation = cmd.getOptionValue(CROSSCHECK_LOCATION_OPTION.getOpt());
        if (crosscheckLocation == null){
            parseError(CROSSCHECK_LOCATION_OPTION.toString());
            return;
        }
        try {
            String crosscheckContents = Files.loadString(new File(crosscheckLocation));
            context.setCrosscheckContents(crosscheckContents);
        } catch (IOException e) {
            parseError(e.toString());
            return;
        }

        String metadataLocation = cmd.getOptionValue(METADATA_LOCATION_OPTION.getOpt());
        if (metadataLocation == null){
            parseError(METADATA_LOCATION_OPTION.toString());
            return;
        }
        try {
            String metadataContents = Files.loadString(new File(metadataLocation));
            context.setYouseeMetadataContents(metadataContents);
        } catch (IOException e) {
            parseError(e.toString());
            return;
        }

        String uuid = "uuid:" + UUID.randomUUID().toString();
        System.out.println(uuid);
        exit(0);
    }

    private static void parseError(String message){
        System.err.println("Error parsing arguments");
        System.err.println(message);
        printUsage();
        exit(1);
    }


    private static void exit(int code){

        System.exit(code);
        return;
    }

}
