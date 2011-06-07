/**
 * ISBService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client;

public interface ISBService_PortType extends java.rmi.Remote {
    public is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client.Session authenticateUser(is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client.Login in0) throws java.rmi.RemoteException;
    public boolean registerRunner(is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client.RunnerInfo in1) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client.SessionTimedOutException;
}
