/**
 * IContestantService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client;

public interface IContestantService extends java.rmi.Remote {
    public void registerContestant(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantRequest request) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault;
    public void updateContestant(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateContestantRequest request) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault;
    public void registerTeam(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamRequest request) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault;
    public void updateTeam(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateTeamRequest request) throws java.rmi.RemoteException, is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceFault;
}
