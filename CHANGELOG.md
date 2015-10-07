2.1
====
* VHS DOMS Ingester now expects crosscheck input!

2.0
====
FFProbe parser includes codecs on format uri for all formats other than mpeg.
Introduce new config property map for allowed formats. Use <formatName>~<mimeType>,<formatName>~<mimetype> syntax. 
Included config files have been updated 

1.1
===
Removed marshalling/unmarshalling/schema-validation from the vhs-doms-ingester client code. The xml provided is now sent directly to DOMS as-is. It
is up to the DOMS server to validate the XML against the schema.
