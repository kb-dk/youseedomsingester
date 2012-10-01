package dk.statsbiblioteket.doms.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private final String allowedFormatName;
    private final String formaturi;
    private final boolean appendCodecsToFormatUri;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public FFProbeParser(String allowedFormatName, String formaturi, boolean appendCodecsToFormatUri) {
        this.allowedFormatName = allowedFormatName;
        this.formaturi = formaturi;
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
        if (format_name.equals(allowedFormatName)){
            format_uri = formaturi;
        } else {
            throw new RuntimeException("Invalid ffprobe file, bad format name. Format was: '" + 
                    format_name + "', required: '" + allowedFormatName +"'");
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

        log.debug("allowedformat: '" + formaturi + "', actual format: '" + format_uri +"'");

        return format_uri;
    }
}
