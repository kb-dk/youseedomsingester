package dk.statsbiblioteket.doms.vhs;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.common.IngestContext;
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
        String resourceDir = new File(Thread.currentThread().getContextClassLoader().getResource("ffprobeSample_ts.xml").toURI()).getParent();

        String[] args = {
                "-filename", "testfile.ts",
                "-ffprobe", resourceDir+ "/ffprobeSample_ts.xml",
                "-ffprobeErrorLog", resourceDir+ "/ffprobeSampleErrorLog_ts.xml",
                "-metadata", resourceDir+ "/metadataSample_ts.xml",
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

        String resourceDir = new File(Thread.currentThread().getContextClassLoader().getResource("ffprobeSample_ts.xml").toURI()).getParent();

        String[] args = {
                "-filename", "testfile.ts",
                "-ffprobe", resourceDir+ "/ffprobeSample_ts.xml",
                "-ffprobeErrorLog", resourceDir+ "/ffprobeSampleErrorLog_ts.xml",
                "-metadata", resourceDir+ "/metadataSample_ts.xml",
                //"-crosscheck", resourceDir+"/crosscheckSample.xml",
                "-url", "http://localhost/testfile1.ts",
                "-config", resourceDir+"/vhs-doms-ingester.properties"
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
        //No asserts, but also no exceptions thrown, so we assume success
    }

    /**
     * Test whether the getAllowedFormatsProperty() method of DomsIngester works as expected
     * @throws IOException In case xml files could not be loaded
     */
    @Test
    public void testgetAllowedFormatsProperty() throws IOException, URISyntaxException {
        //TODO (is tested indirectly in testIngest() above()

    }
}
