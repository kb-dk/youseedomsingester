package dk.statsbiblioteket.doms.yousee;

/**
 * Created by IntelliJ IDEA.
 * User: kfc
 * Date: 5/23/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Ingester {
    /**
     * Ingest an object with the given context.
     *
     * @param ingestContext The ingest context.
     * @return The pid of the object that was ingested.
     */
    String ingest(IngestContext ingestContext);
}
