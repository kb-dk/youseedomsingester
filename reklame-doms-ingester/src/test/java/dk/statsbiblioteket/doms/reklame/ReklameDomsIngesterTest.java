package dk.statsbiblioteket.doms.reklame;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dk.statsbiblioteket.util.Files;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Test ingest.
 */
public class ReklameDomsIngesterTest {

    private Properties config;
    private ReklameIngestContext context100;
    private ReklameIngestContext context101;
    private ReklameIngestContext context102;

    @Before
    public void setUp() throws Exception {
        config = new Properties();

        //context for a new object
        context100 = new ReklameIngestContext();
        context100.setTemplatePid("doms:Template_ReklameFile");
        context100.setReklameTemplatePid("doms:Template_Reklamefilm");

        context100.setFilename("tv2rekl199901_0100.mpg");
        context100.setRemoteURL("http://bitfinder.statsbiblioteket.dk/reklame/tv2rekl199901_0100.mpg");

        context100.setPbCoreContents(
                Files.loadString(new File(getClass().getClassLoader().getResource("tv2rekl199901_0100.xml").getFile())));
        context100.setFfprobeContents(Files.loadString(
                new File(getClass().getClassLoader().getResource("tv2rekl199901_0100.mpg.stdout").getFile())));
        context100.setFfprobeErrorContents(Files.loadString(
                new File(getClass().getClassLoader().getResource("tv2rekl199901_0100.mpg.stderr").getFile())));
        context100.setReklameMetadata(Files.loadString(
                new File(getClass().getClassLoader().getResource("tv2rekl199901_0100.meta.xml").getFile())));

        context100.setWSDLlocation("http://localhost:7880/");
        context100.setUsername("fedoraAdmin");
        context100.setPassword("fedoraAdminPass");

        context100.setConfig(config);

        //context for an object that already exists
        context101 = new ReklameIngestContext();
        context101.setTemplatePid("doms:Template_ReklameFile");
        context101.setReklameTemplatePid("doms:Template_Reklamefilm");

        context101.setFilename("tv2rekl199901_0101.mpg");
        context101.setRemoteURL("http://bitfinder.statsbiblioteket.dk/reklame/tv2rekl199901_0101.mpg");

        context101.setPbCoreContents(
                Files.loadString(new File(getClass().getClassLoader().getResource("tv2rekl199901_0101.xml").getFile())));
        context101.setFfprobeContents(Files.loadString(
                new File(getClass().getClassLoader().getResource("tv2rekl199901_0101.mpg.stdout").getFile())));
        context101.setFfprobeErrorContents(Files.loadString(
                new File(getClass().getClassLoader().getResource("tv2rekl199901_0101.mpg.stderr").getFile())));
        context101.setReklameMetadata(Files.loadString(
                new File(getClass().getClassLoader().getResource("tv2rekl199901_0101.meta.xml").getFile())));

        context101.setWSDLlocation("http://localhost:7880/centralWebservice-service/central/?wsdl");
        context101.setUsername("fedoraAdmin");
        context101.setPassword("fedoraAdminPass");

        context101.setConfig(config);

        //context for an object that already exists
        context102 = new ReklameIngestContext();
        context102.setTemplatePid("doms:Template_ReklameFile");
        context102.setReklameTemplatePid("doms:Template_Reklamefilm");

        context102.setFilename("tv2rekl199901_0102.mpg");
        context102.setRemoteURL("http://bitfinder.statsbiblioteket.dk/reklame/tv2rekl199901_0102.mpg");

        context102.setFfprobeContents(Files.loadString(
                new File(getClass().getClassLoader().getResource("tv2rekl199901_0102.mpg.stdout").getFile())));
        context102.setFfprobeErrorContents(Files.loadString(
                new File(getClass().getClassLoader().getResource("tv2rekl199901_0102.mpg.stderr").getFile())));
        context102.setReklameMetadata(Files.loadString(
                new File(getClass().getClassLoader().getResource("tv2rekl199901_0102.meta.xml").getFile())));

        context102.setWSDLlocation("http://localhost:7880/centralWebservice-service/central/?wsdl");
        context102.setUsername("fedoraAdmin");
        context102.setPassword("fedoraAdminPass");

        context102.setConfig(config);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testIngest() throws Exception {
        MockupCentralWebservice webservice100 = new MockupCentralWebservice();
        new ReklameDomsIngester(config, webservice100).ingest(context100);

        Assert.assertEquals(1, webservice100.getFileObjectWithURL.size());
        Assert.assertEquals("http://bitfinder.statsbiblioteket.dk/reklame/tv2rekl199901_0100.mpg", webservice100.getFileObjectWithURL.get(0)[0]);
        Assert.assertEquals(null, webservice100.getFileObjectWithURL.get(0)[1]);
        Assert.assertEquals(1, webservice100.findObjectFromDCIdentifier.size());
        Assert.assertEquals("tv2rekl199901_0100", webservice100.findObjectFromDCIdentifier.get(0)[0]);
        Assert.assertEquals(null, webservice100.findObjectFromDCIdentifier.get(0)[1]);
        Assert.assertEquals("tv2rekl199901_0100", webservice100.findObjectFromDCIdentifier.get(0)[0]);

        Assert.assertEquals(2, webservice100.newObject.size());
        Assert.assertEquals("doms:Template_ReklameFile", webservice100.newObject.get(0)[0]);
        Assert.assertEquals(null, webservice100.newObject.get(0)[1]);
        Assert.assertEquals("doms:Template_Reklamefilm", webservice100.newObject.get(1)[0]);
        Assert.assertArrayEquals(new String[]{"tv2rekl199901_0100"},
                                 ((List<String>) (webservice100.newObject.get(1)[1])).toArray());
        Assert.assertEquals(1, webservice100.addFileFromPermanentURL.size());
        Assert.assertEquals(webservice100.newObject.get(0)[3], webservice100.addFileFromPermanentURL.get(0)[0]);
        Assert.assertEquals("tv2rekl199901_0100.mpg", webservice100.addFileFromPermanentURL.get(0)[1]);
        Assert.assertEquals("http://bitfinder.statsbiblioteket.dk/reklame/tv2rekl199901_0100.mpg",
                            webservice100.addFileFromPermanentURL.get(0)[3]);
        Assert.assertEquals(1, webservice100.setObjectLabel.size());
        Assert.assertEquals(webservice100.newObject.get(1)[3], webservice100.setObjectLabel.get(0)[0]);
        Assert.assertEquals("Fanta", webservice100.setObjectLabel.get(0)[1]);

        Assert.assertEquals(2, webservice100.markInProgressObject.size());
        Assert.assertEquals(webservice100.newObject.get(0)[3], ((List<String>) webservice100.markInProgressObject.get(0)[0]).get(0));
        Assert.assertEquals(webservice100.newObject.get(1)[3], ((List<String>) webservice100.markInProgressObject.get(1)[0]).get(0));
        Assert.assertEquals(4, webservice100.modifyDatastream.size());
        Assert.assertEquals(webservice100.newObject.get(0)[3], webservice100.modifyDatastream.get(0)[0]);
        Assert.assertEquals("FFPROBE", webservice100.modifyDatastream.get(0)[1]);
        Assert.assertEquals(webservice100.newObject.get(0)[3], webservice100.modifyDatastream.get(1)[0]);
        Assert.assertEquals("FFPROBE_ERROR_LOG", webservice100.modifyDatastream.get(1)[1]);
        Assert.assertEquals(webservice100.newObject.get(0)[3], webservice100.modifyDatastream.get(2)[0]);
        Assert.assertEquals("REKLAME_METADATA", webservice100.modifyDatastream.get(2)[1]);
        Assert.assertEquals(webservice100.newObject.get(1)[3], webservice100.modifyDatastream.get(3)[0]);
        Assert.assertEquals("PBCORE", webservice100.modifyDatastream.get(3)[1]);
        Assert.assertEquals(1, webservice100.addRelation.size());
        Assert.assertEquals(2, webservice100.markPublishedObject.size());
        Assert.assertEquals(webservice100.newObject.get(0)[3], ((List<String>) webservice100.markPublishedObject.get(0)[0]).get(0));
        Assert.assertEquals(webservice100.newObject.get(1)[3], ((List<String>) webservice100.markPublishedObject.get(1)[0]).get(0));

        Assert.assertEquals(0, webservice100.other);

        MockupCentralWebservice webservice101 = new MockupCentralWebservice();
        new ReklameDomsIngester(config, webservice101).ingest(context101);

        Assert.assertEquals(1, webservice101.getFileObjectWithURL.size());
        Assert.assertEquals(1, webservice101.findObjectFromDCIdentifier.size());
        // No new objects should be created, they are already there
        Assert.assertEquals(0, webservice101.newObject.size());
        Assert.assertEquals(0, webservice101.addFileFromPermanentURL.size());

        Assert.assertEquals(2, webservice101.markInProgressObject.size());
        Assert.assertEquals(4, webservice101.modifyDatastream.size());
        Assert.assertEquals(1, webservice101.addRelation.size());
        Assert.assertEquals(2, webservice101.markPublishedObject.size());

        Assert.assertEquals(0, webservice101.other);

        MockupCentralWebservice webservice102 = new MockupCentralWebservice();
        new ReklameDomsIngester(config, webservice102).ingest(context102);

        Assert.assertEquals(1, webservice102.getFileObjectWithURL.size());
        Assert.assertEquals(0, webservice102.findObjectFromDCIdentifier.size());
        Assert.assertEquals(1, webservice102.newObject.size());
        Assert.assertEquals(1, webservice102.addFileFromPermanentURL.size());

        Assert.assertEquals(1, webservice102.markInProgressObject.size());
        Assert.assertEquals(3, webservice102.modifyDatastream.size());
        Assert.assertEquals(0, webservice102.addRelation.size());
        Assert.assertEquals(1, webservice102.markPublishedObject.size());

        Assert.assertEquals(0, webservice102.other);
    }
}
