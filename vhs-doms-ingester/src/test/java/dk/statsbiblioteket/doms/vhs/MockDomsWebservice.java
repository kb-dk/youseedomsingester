package dk.statsbiblioteket.doms.vhs;

import dk.statsbiblioteket.doms.central.*;

import javax.jws.WebParam;
import java.lang.String;
import java.util.List;

/**
 */
public class MockDomsWebservice implements CentralWebservice {
    @Override
    public String newObject(
            @WebParam(name = "pid", targetNamespace = "") String s,
            @WebParam(name = "oldID", targetNamespace = "") List<String> strings,
            @WebParam(name = "comment", targetNamespace = "") String s1)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        return "mockedUpPid";
    }

    @Override
    public ObjectProfile getObjectProfile(
            @WebParam(name = "pid", targetNamespace = "") String s)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        return null;
    }

    @Override
    public void setObjectLabel(
            @WebParam(name = "pid", targetNamespace = "") String s,
            @WebParam(name = "name", targetNamespace = "") String s1,
            @WebParam(name = "comment", targetNamespace = "") String s2)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {

    }

    @Override
    public void deleteObject(
            @WebParam(name = "pids", targetNamespace = "") List<String> strings,
            @WebParam(name = "comment", targetNamespace = "") String s)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {

    }

    @Override
    public void markPublishedObject(
            @WebParam(name = "pids", targetNamespace = "") List<String> strings,
            @WebParam(name = "comment", targetNamespace = "") String s)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        // Sure. Done!
    }

    @Override
    public void markInProgressObject(
            @WebParam(name = "pids", targetNamespace = "") List<String> strings,
            @WebParam(name = "comment", targetNamespace = "") String s)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        // Sure. Done!
    }

    @Override
    public void modifyDatastream(
            @WebParam(name = "pid", targetNamespace = "") String s,
            @WebParam(name = "datastream", targetNamespace = "") String s1,
            @WebParam(name = "contents", targetNamespace = "") String s2,
            @WebParam(name = "comment", targetNamespace = "") String s3)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        // Sure. Done!
    }

    @Override
    public String getDatastreamContents(
            @WebParam(name = "pid", targetNamespace = "") String s,
            @WebParam(name = "datastream", targetNamespace = "") String s1)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        return "<mockedUpDatastreamContents/>";
    }

    @Override
    public void addFileFromPermanentURL(
            @WebParam(name = "pid", targetNamespace = "") String s,
            @WebParam(name = "filename", targetNamespace = "") String s1,
            @WebParam(name = "md5sum", targetNamespace = "") String s2,
            @WebParam(name = "permanentURL", targetNamespace = "") String s3,
            @WebParam(name = "formatURI", targetNamespace = "") String s4,
            @WebParam(name = "comment", targetNamespace = "") String s5)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        // Sure. Done!
    }

    @Override
    public String getFileObjectWithURL(
            @WebParam(name = "URL", targetNamespace = "") String s)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        return "mockedUpPid";
    }

    @Override
    public void addRelation(
            @WebParam(name = "pid", targetNamespace = "") String s,
            @WebParam(name = "relation", targetNamespace = "") Relation relation,
            @WebParam(name = "comment", targetNamespace = "") String s1)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {

    }

    @Override
    public List<Relation> getRelations(
            @WebParam(name = "pid", targetNamespace = "") String s)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        return null;
    }

    @Override
    public List<Relation> getNamedRelations(
            @WebParam(name = "pid", targetNamespace = "") String s,
            @WebParam(name = "predicate", targetNamespace = "") String s1)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        return null;
    }

    @Override
    public List<Relation> getInverseRelations(
            @WebParam(name = "pid", targetNamespace = "") String s)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        return null;
    }

    @Override
    public List<Relation> getInverseRelationsWithPredicate(
            @WebParam(name = "pid", targetNamespace = "") String s,
            @WebParam(name = "predicate", targetNamespace = "") String s1)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        return null;
    }

    @Override
    public void deleteRelation(
            @WebParam(name = "pid", targetNamespace = "") String s,
            @WebParam(name = "relation", targetNamespace = "") Relation relation,
            @WebParam(name = "comment", targetNamespace = "") String s1)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {

    }

    @Override
    public ViewBundle getViewBundle(
            @WebParam(name = "pid", targetNamespace = "") String s,
            @WebParam(name = "ViewAngle", targetNamespace = "") String s1)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        return null;
    }

    @Override
    public ViewBundle getViewBundleFromSpecificTime(
            @WebParam(name = "pid", targetNamespace = "")
            String pid,
            @WebParam(name = "ViewAngle", targetNamespace = "")
            String viewAngle,
            @WebParam(name = "asOfTime", targetNamespace = "")
            long asOfTime) throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<RecordDescription> getIDsModified(
            @WebParam(name = "since", targetNamespace = "") long l,
            @WebParam(name = "collectionPid", targetNamespace = "") String s,
            @WebParam(name = "viewAngle", targetNamespace = "") String s1,
            @WebParam(name = "state", targetNamespace = "") String s2,
            @WebParam(name = "offset", targetNamespace = "") Integer integer,
            @WebParam(name = "limit", targetNamespace = "") Integer integer1)
            throws InvalidCredentialsException, MethodFailedException {
        return null;
    }

    @Override
    public long getLatestModified(
            @WebParam(name = "collectionPid", targetNamespace = "") String s,
            @WebParam(name = "viewAngle", targetNamespace = "") String s1,
            @WebParam(name = "state", targetNamespace = "") String s2)
            throws InvalidCredentialsException, MethodFailedException {
        return 0;
    }

    @Override
    public List<String> findObjectFromDCIdentifier(
            @WebParam(name = "string", targetNamespace = "") String s)
            throws InvalidCredentialsException, MethodFailedException {
        return null;
    }

    @Override
    public SearchResultList findObjects(
            @WebParam(name = "query", targetNamespace = "") String s,
            @WebParam(name = "offset", targetNamespace = "") int i,
            @WebParam(name = "pageSize", targetNamespace = "") int i1)
            throws InvalidCredentialsException, MethodFailedException {
        return null;
    }

    @Override
    public void lockForWriting()
            throws InvalidCredentialsException, MethodFailedException {

    }

    @Override
    public void unlockForWriting()
            throws InvalidCredentialsException, MethodFailedException {

    }

    @Override
    public User createTempAdminUser(
            @WebParam(name = "username", targetNamespace = "") String s,
            @WebParam(name = "roles", targetNamespace = "") List<String> strings)
            throws InvalidCredentialsException, MethodFailedException {
        return null;
    }

    @Override
    public List<String> getObjectsInCollection(
            @WebParam(name = "collectionPid", targetNamespace = "") String s,
            @WebParam(name = "contentModelPid", targetNamespace = "") String s1)
            throws InvalidCredentialsException, InvalidResourceException,
            MethodFailedException {
        return null;
    }

    @Override
    public List<Method> getMethods(
            @WebParam(name = "pid", targetNamespace = "")
            String pid) throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String invokeMethod(
            @WebParam(name = "cmpid", targetNamespace = "")
            String cmpid,
            @WebParam(name = "methodName", targetNamespace = "")
            String methodName,
            @WebParam(name = "parameters", targetNamespace = "")
            List<Pair> parameters) throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Link> getObjectLinks(
            @WebParam(name = "pid", targetNamespace = "")
            String pid,
            @WebParam(name = "asOfTime", targetNamespace = "")
            long asOfTime) throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
