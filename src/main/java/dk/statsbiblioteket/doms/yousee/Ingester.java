package dk.statsbiblioteket.doms.yousee;

/**
 * Ingest objects with given context.
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
