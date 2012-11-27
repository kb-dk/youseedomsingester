package dk.statsbiblioteket.doms.reklame;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.central.InvalidCredentialsException;
import dk.statsbiblioteket.doms.central.InvalidResourceException;
import dk.statsbiblioteket.doms.central.MethodFailedException;
import dk.statsbiblioteket.doms.central.ObjectProfile;
import dk.statsbiblioteket.doms.central.RecordDescription;
import dk.statsbiblioteket.doms.central.Relation;
import dk.statsbiblioteket.doms.central.SearchResult;
import dk.statsbiblioteket.doms.central.User;
import dk.statsbiblioteket.doms.central.ViewBundle;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Mockup central web service.
 */
public class MockupCentralWebservice implements CentralWebservice {
    // Calls of the named methods and their parameters and return values.
    public List<Object[]> newObject = new ArrayList<Object[]>();
    public List<Object[]> getFileObjectWithURL = new ArrayList<Object[]>();
    public List<Object[]> addFileFromPermanentURL = new ArrayList<Object[]>();
    public List<Object[]> findObjectFromDCIdentifier = new ArrayList<Object[]>();
    public List<Object[]> markInProgressObject = new ArrayList<Object[]>();
    public List<Object[]> getDatastreamContents = new ArrayList<Object[]>();
    public List<Object[]> modifyDatastream = new ArrayList<Object[]>();
    public List<Object[]> addRelation = new ArrayList<Object[]>();
    public List<Object[]> markPublishedObject = new ArrayList<Object[]>();
    public List<Object[]> setObjectLabel = new ArrayList<Object[]>();
    // Calls of other methods
    public int other;

    private void otherMethodCalled() {
        other++;
    }

    @Override
    public String newObject(@WebParam(name = "pid", targetNamespace = "") String pid,
                            @WebParam(name = "oldID", targetNamespace = "") List<String> oldID,
                            @WebParam(name = "comment", targetNamespace = "") String comment)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        String returnValue = "uuid:" + UUID.randomUUID().toString();
        newObject.add(new Object[]{pid, oldID, comment, returnValue});
        return returnValue;
    }

    @Override
    public ObjectProfile getObjectProfile(@WebParam(name = "pid", targetNamespace = "") String pid)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        otherMethodCalled();
        return null;
    }

    @Override
    public void setObjectLabel(@WebParam(name = "pid", targetNamespace = "") String pid,
                               @WebParam(name = "name", targetNamespace = "") String name,
                               @WebParam(name = "comment", targetNamespace = "") String comment)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        setObjectLabel.add(new Object[]{pid, name, comment});
    }

    @Override
    public void deleteObject(@WebParam(name = "pids", targetNamespace = "") List<String> pids,
                             @WebParam(name = "comment", targetNamespace = "") String comment)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        otherMethodCalled();
    }

    @Override
    public void markPublishedObject(@WebParam(name = "pids", targetNamespace = "") List<String> pids,
                                    @WebParam(name = "comment", targetNamespace = "") String comment)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        markPublishedObject.add(new Object[]{pids, comment});
    }

    @Override
    public void markInProgressObject(@WebParam(name = "pids", targetNamespace = "") List<String> pids,
                                     @WebParam(name = "comment", targetNamespace = "") String comment)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        markInProgressObject.add(new Object[]{pids, comment});
    }

    @Override
    public void modifyDatastream(@WebParam(name = "pid", targetNamespace = "") String pid,
                                 @WebParam(name = "datastream", targetNamespace = "") String datastream,
                                 @WebParam(name = "contents", targetNamespace = "") String contents,
                                 @WebParam(name = "comment", targetNamespace = "") String comment)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        modifyDatastream.add(new Object[]{pid, datastream, contents, comment});
    }

    @Override
    public String getDatastreamContents(@WebParam(name = "pid", targetNamespace = "") String pid,
                                        @WebParam(name = "datastream", targetNamespace = "") String datastream)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        String returnValue = "<placeholder/>";
        getDatastreamContents.add(new Object[]{pid, datastream, returnValue});
        return returnValue;
    }

    @Override
    public void addFileFromPermanentURL(@WebParam(name = "pid", targetNamespace = "") String pid,
                                        @WebParam(name = "filename", targetNamespace = "") String filename,
                                        @WebParam(name = "md5sum", targetNamespace = "") String md5Sum,
                                        @WebParam(name = "permanentURL", targetNamespace = "") String permanentURL,
                                        @WebParam(name = "formatURI", targetNamespace = "") String formatURI,
                                        @WebParam(name = "comment", targetNamespace = "") String comment)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        addFileFromPermanentURL.add(new Object[]{pid, filename, md5Sum, permanentURL, formatURI, comment});
    }

    @Override
    public String getFileObjectWithURL(@WebParam(name = "URL", targetNamespace = "") String url)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        String returnValue;
        if (url.equals("http://bitfinder.statsbiblioteket.dk/reklame/tv2rekl199901_0101.mpg")) {
            returnValue = UUID.randomUUID().toString();
        } else {
            returnValue = null;
        }
        getFileObjectWithURL.add(new Object[]{url, returnValue});
        return returnValue;
    }

    @Override
    public void addRelation(@WebParam(name = "pid", targetNamespace = "") String pid,
                            @WebParam(name = "relation", targetNamespace = "") Relation relation,
                            @WebParam(name = "comment", targetNamespace = "") String comment)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        addRelation.add(new Object[]{pid, relation, comment});
    }

    @Override
    public List<Relation> getRelations(@WebParam(name = "pid", targetNamespace = "") String pid)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        otherMethodCalled();
        return null;
    }

    @Override
    public List<Relation> getNamedRelations(@WebParam(name = "pid", targetNamespace = "") String pid,
                                            @WebParam(name = "predicate", targetNamespace = "") String predicate)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        otherMethodCalled();
        return null;
    }

    @Override
    public List<Relation> getInverseRelations(@WebParam(name = "pid", targetNamespace = "") String pid)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        otherMethodCalled();
        return null;
    }

    @Override
    public List<Relation> getInverseRelationsWithPredicate(@WebParam(name = "pid", targetNamespace = "") String pid,
                                                           @WebParam(name = "predicate", targetNamespace = "")
                                                           String predicate)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        otherMethodCalled();
        return null;
    }

    @Override
    public void deleteRelation(@WebParam(name = "pid", targetNamespace = "") String pid,
                               @WebParam(name = "relation", targetNamespace = "") Relation relation,
                               @WebParam(name = "comment", targetNamespace = "") String comment)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        otherMethodCalled();
    }

    @Override
    public ViewBundle getViewBundle(@WebParam(name = "pid", targetNamespace = "") String pid,
                                    @WebParam(name = "ViewAngle", targetNamespace = "") String viewAngle)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        otherMethodCalled();
        return null;
    }

    @Override
    public List<RecordDescription> getIDsModified(@WebParam(name = "since", targetNamespace = "") long since,
                                                  @WebParam(name = "collectionPid", targetNamespace = "")
                                                  String collectionPid,
                                                  @WebParam(name = "viewAngle", targetNamespace = "") String viewAngle,
                                                  @WebParam(name = "state", targetNamespace = "") String state,
                                                  @WebParam(name = "offset", targetNamespace = "") Integer offset,
                                                  @WebParam(name = "limit", targetNamespace = "") Integer limit)
            throws InvalidCredentialsException, MethodFailedException {
        otherMethodCalled();
        return null;
    }

    @Override
    public long getLatestModified(@WebParam(name = "collectionPid", targetNamespace = "") String collectionPid,
                                  @WebParam(name = "viewAngle", targetNamespace = "") String viewAngle,
                                  @WebParam(name = "state", targetNamespace = "") String state)
            throws InvalidCredentialsException, MethodFailedException {
        otherMethodCalled();
        return 0;
    }

    @Override
    public List<String> findObjectFromDCIdentifier(@WebParam(name = "string", targetNamespace = "") String string)
            throws InvalidCredentialsException, MethodFailedException {
        List<String> returnValue;
        if (string.equals("tv2rekl199901_0101")) {
            returnValue = Arrays.asList(UUID.randomUUID().toString());
        } else {
            returnValue = null;
        }
        findObjectFromDCIdentifier.add(new Object[]{string, returnValue});
        return returnValue;
    }

    @Override
    public List<SearchResult> findObjects(@WebParam(name = "query", targetNamespace = "") String query,
                                          @WebParam(name = "offset", targetNamespace = "") int offset,
                                          @WebParam(name = "pageSize", targetNamespace = "") int pageSize)
            throws InvalidCredentialsException, MethodFailedException {
        otherMethodCalled();
        return null;
    }

    @Override
    public void lockForWriting() throws InvalidCredentialsException, MethodFailedException {
        otherMethodCalled();
    }

    @Override
    public void unlockForWriting() throws InvalidCredentialsException, MethodFailedException {
        otherMethodCalled();
    }

    @Override
    public User createTempAdminUser(@WebParam(name = "username", targetNamespace = "") String username,
                                    @WebParam(name = "roles", targetNamespace = "") List<String> roles)
            throws InvalidCredentialsException, MethodFailedException {
        otherMethodCalled();
        return null;
    }

    @Override
    public List<String> getObjectsInCollection(
            @WebParam(name = "collectionPid", targetNamespace = "") String collectionPid,
            @WebParam(name = "contentModelPid", targetNamespace = "") String contentModelPid)
            throws InvalidCredentialsException, InvalidResourceException, MethodFailedException {
        otherMethodCalled();
        return null;
    }
}
