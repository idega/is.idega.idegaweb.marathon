/**
 * MarathonWS2Soap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.glitnirws;

public interface MarathonWS2Soap_PortType extends java.rmi.Remote {

    /**
     * Athugar hvort kennitala sé skráð í virkum viðskiptum við Glitnir
     * Banka
     */
    public boolean erIVidskiptumVidGlitni(java.lang.String kennitala) throws java.rmi.RemoteException;

    /**
     * Athugar hvort kennitolur séu skráðar í virkum viðskiptum hjá
     * Glitnir Banka
     */
    public java.lang.String[] erIVidskiptumVidGlitni2(java.lang.String[] kennitolur) throws java.rmi.RemoteException;
}
