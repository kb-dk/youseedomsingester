package dk.statsbiblioteket.doms.reklame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: kfc
 * Date: 11/20/12
 * Time: 9:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReklameOptionParserTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testParseOptions() throws Exception {
        ReklameIngestContext context = (ReklameIngestContext) new ReklameOptionParser().parseOptions(new String[]{
                "-config", getClass().getClassLoader().getResource("reklame-ingester.properties").getFile(),
                "-filename", "tv2rekl199901_0100.mpg",
                "-url", "http://bitfinder.statsbiblioteket.dk/reklame/tv2rekl199901_0100.mpg",
                "-ffprobe", getClass().getClassLoader().getResource("tv2rekl199901_0100.mpg.stdout").getFile(),
                "-ffprobeErrorLog", getClass().getClassLoader().getResource("tv2rekl199901_0100.mpg.stderr").getFile(),
                "-reklamemetadata", getClass().getClassLoader().getResource("tv2rekl199901_0100.meta.xml").getFile(),
                "-pbcore", getClass().getClassLoader().getResource("tv2rekl199901_0100.xml").getFile(),
                "-wsdl", "http://localhost:7880/centralWebservice-service/central/?wsdl",
                "-user", "fedoraAdmin",
                "-pass", "fedoraAdminPass",
                "-template", "doms:Template_ReklameFile",
                "-reklametemplate", "doms:Template_Reklamefilm",
        });
    }
}
