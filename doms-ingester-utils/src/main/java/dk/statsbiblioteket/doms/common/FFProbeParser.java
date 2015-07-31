package dk.statsbiblioteket.doms.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class to parse FFprobe output xml files. 
 */
public class FFProbeParser {

    private final Map<String, String> allowedFormats;
    //private final String formaturi;
    private final boolean appendCodecsToFormatUri;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public FFProbeParser(Map<String, String> allowedFormats, boolean appendCodecsToFormatUri) {
        this.allowedFormats = allowedFormats;
        //this.formaturi = formaturi;
        this.appendCodecsToFormatUri = appendCodecsToFormatUri;
    }

    public String getFormatURIFromFFProbeOutput(String FFProbeOutput)
            throws XPathExpressionException, ParserConfigurationException,
            IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(
                FFProbeOutput.getBytes()));
        XPathFactory xPathfactory = XPathFactory.newInstance();

        XPath xpath;
        XPathExpression expr;

        // Get format name
        String format_name;
        xpath = xPathfactory.newXPath();
        expr = xpath.compile("ffprobe/format/@format_name");
        format_name = expr.evaluate(doc);
        if (format_name.trim().isEmpty()){
            throw new RuntimeException("Invalid ffprobe file, no format_name");
        }

        // Get codec name
        List<String> codecs = new ArrayList<String>();
        xpath = xPathfactory.newXPath();
        expr = xpath.compile("ffprobe/streams/stream/@codec_name");
        NodeList codecsNodeList
                = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        Set<String> codecSet = new HashSet<String>();
        // Convert nodelist to a set of strings, to remove duplicates
        for(int i = 0; i < codecsNodeList.getLength(); i++) {
            codecSet.add(codecsNodeList.item(i).getNodeValue());
        }
        // Convert set to a list of strings
        codecs = new ArrayList<String>(codecSet);
        // Sort it
        Collections.sort(codecs);

        String format_uri;
        if (allowedFormats.containsKey(format_name)){
            format_uri = allowedFormats.get(format_name);
        } else {
            throw new RuntimeException("Invalid ffprobe file, bad format name. Format was: '" +
                    format_name + "', allowed: '" + allowedFormats +"'");
        }

        if(appendCodecsToFormatUri) {
            if (codecs.size() > 0){
                format_uri= format_uri + ";codecs=\"";
                for (int i = 0; i < codecs.size(); i++){
                    String codec = codecs.get(i);
                    format_uri = format_uri + codec;
                    if (i+1 != codecs.size()){
                        format_uri = format_uri + ",";
                    }
                }
                format_uri = format_uri + "\"";
            }
        }

        log.debug("allowedformats: '" + allowedFormats + "', actual format: '" + format_uri +"'");

        return format_uri;
    }
}
