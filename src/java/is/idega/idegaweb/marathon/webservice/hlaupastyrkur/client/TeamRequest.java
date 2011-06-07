/**
 * TeamRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client;

public class TeamRequest  extends is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.RegisterRequest  implements java.io.Serializable {
    private is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] members;

    private java.lang.String teamID;

    public TeamRequest() {
    }

    public TeamRequest(
           java.lang.String distance,
           is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.Login login,
           java.lang.String charityID,
           java.lang.String name,
           java.lang.String password,
           java.lang.String username,
           is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] members,
           java.lang.String teamID) {
        super(
            distance,
            login,
            charityID,
            name,
            password,
            username);
        this.members = members;
        this.teamID = teamID;
    }


    /**
     * Gets the members value for this TeamRequest.
     * 
     * @return members
     */
    public is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] getMembers() {
        return members;
    }


    /**
     * Sets the members value for this TeamRequest.
     * 
     * @param members
     */
    public void setMembers(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] members) {
        this.members = members;
    }


    /**
     * Gets the teamID value for this TeamRequest.
     * 
     * @return teamID
     */
    public java.lang.String getTeamID() {
        return teamID;
    }


    /**
     * Sets the teamID value for this TeamRequest.
     * 
     * @param teamID
     */
    public void setTeamID(java.lang.String teamID) {
        this.teamID = teamID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TeamRequest)) return false;
        TeamRequest other = (TeamRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.members==null && other.getMembers()==null) || 
             (this.members!=null &&
              java.util.Arrays.equals(this.members, other.getMembers()))) &&
            ((this.teamID==null && other.getTeamID()==null) || 
             (this.teamID!=null &&
              this.teamID.equals(other.getTeamID())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getMembers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMembers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMembers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTeamID() != null) {
            _hashCode += getTeamID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TeamRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("members");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "Members"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamMember"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamMember"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("teamID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
