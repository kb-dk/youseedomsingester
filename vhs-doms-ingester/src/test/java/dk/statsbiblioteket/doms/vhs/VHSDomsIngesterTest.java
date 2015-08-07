package dk.statsbiblioteket.doms.vhs;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.common.IngestContext;
import dk.statsbiblioteket.util.Files;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

/**
 */
public class VHSDomsIngesterTest {
    /**
     * Test DOMSIngester against a mockup-doms-central webservice
     * @throws Exception
     */
    @Test
    public void testIngest() throws Exception {
        // Mock-up arguments
        String resourceDir = new File(Thread.currentThread().getContextClassLoader().getResource("ffprobeSample.xml").toURI()).getParent();

        String[] args = {
                "-filename", "testfile.ts",
                "-ffprobe", resourceDir+"/ffprobeSample.xml",
                "-ffprobeErrorLog", resourceDir+"/ffprobeSampleErrorLog.xml",
                "-metadata", resourceDir+"/metadataSample.xml",
                //"-crosscheck", resourceDir+"/crosscheckSample.xml",
                "-url", "http://localhost/testfile1.ts",
                "-config", resourceDir+"/vhs-doms-ingester.properties"
        };

        // Create an ingest context to test with
        IngestContext context = new VHSOptionParser().parseOptions(args);
        if (context == null) {
            assertTrue(false);
        }

        String uuid = "";
        try {
            Properties config = context.getConfig();

            CentralWebservice centralWebservice = new MockDomsWebservice();

            VHSDomsIngester ingester = new VHSDomsIngester(config, centralWebservice);

            uuid = ingester.ingest(context);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(uuid.equalsIgnoreCase("mockedUpPid"));
    }

    /**
     * Test DOMSIngester against the real DOMS central webservice
     * @throws Exception
     */
    @Test
    //@Ignore // new xsd's made this fail
    public void testIngestWithRealDOMS() throws Exception {

        String resourceDir = new File(Thread.currentThread().getContextClassLoader().getResource("crosscheckSample.xml").toURI()).getParent();

        String[] args = {
                "-filename", "testfile.mux",
                "-ffprobe", resourceDir+"/ffprobeSample.xml",
                "-ffprobeErrorLog", resourceDir+"/ffprobeSampleErrorLog.xml",
                "-metadata", resourceDir+"/metadataSample.xml",
                //"-crosscheck", resourceDir+"/crosscheckSample.xml",
                "-url", "http://localhost/testfile1.mux",
                "-config", resourceDir+"/radiotv-doms-ingester.properties"
        };


        // Create an ingest context to test with
        VHSIngestContext context = (VHSIngestContext) new VHSOptionParser().parseOptions(args);
        if (context == null) {
            //System.exit(1);
            //return;
            assertTrue(false);
        }

        String uuid = "";

        Properties config = context.getConfig();
        uuid = new VHSIngesterFactory(context).getIngester().ingest(context);
    }

    /**
     * Test whether the getAllowedFormatsProperty() method of DomsIngester works as expected
     * @throws IOException In case xml files could not be loaded
     */
    @Test
    public void testgetAllowedFormatsProperty() throws IOException, URISyntaxException {
        String ffprobeContents = Files.loadString(new File(Thread.currentThread().getContextClassLoader().getResource("ffprobeSample.xml").toURI()));
        String ffprobeContentsDifferent = Files.loadString(new File(Thread.currentThread().getContextClassLoader().getResource("ffprobeSample-different.xml").toURI()));

        boolean equals = new VHSDomsIngester(null, null).xmlEquals(ffprobeContents, ffprobeContentsDifferent);
        assertTrue(equals);

    }
}
