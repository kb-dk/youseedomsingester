1.1
===
Removed marshalling/unmarshalling/schema-validation from the vhs-doms-ingester client code. The xml provided is now sent directly to DOMS as-is. It
is up to the DOMS server to validate the XML against the schema.
