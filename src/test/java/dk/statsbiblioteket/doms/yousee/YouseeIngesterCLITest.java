package dk.statsbiblioteket.doms.yousee;

/**
 * Created by IntelliJ IDEA.
 * User: abr
 * Date: 5/18/12
 * Time: 10:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class YouseeIngesterCLITest {
    @org.junit.Test
    public void testMain() throws Exception {
        YouseeIngesterCLI.main(new String[]{
                "-filename","testfile.mux",
                "-checksum","DEADFEAT",
                "-ffprobe","src/test/resources/ffprobeSample.xml",
                "-metadata","src/test/resources/metadataSample.xml",
                "-crosscheck","src/test/resources/crosscheckSample.xml",
                "-url","http://localhost/testfile1.mux"
        });
    }
}
