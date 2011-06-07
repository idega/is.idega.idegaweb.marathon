/**
 * UpdateTeamRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client;

public class UpdateTeamRequest  extends is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.UpdateRequest  implements java.io.Serializable {
    private is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] newMembers;

    private is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] removedMembers;

    private java.lang.String teamID;

    private is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] updatedMembers;

    public UpdateTeamRequest() {
    }

    public UpdateTeamRequest(
           java.lang.String distance,
           is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.Login login,
           java.lang.String charityID,
           java.lang.String email,
           java.lang.String password,
           is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] newMembers,
           is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] removedMembers,
           java.lang.String teamID,
           is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] updatedMembers) {
        super(
            distance,
            login,
            charityID,
            email,
            password);
        this.newMembers = newMembers;
        this.removedMembers = removedMembers;
        this.teamID = teamID;
        this.updatedMembers = updatedMembers;
    }


    /**
     * Gets the newMembers value for this UpdateTeamRequest.
     * 
     * @return newMembers
     */
    public is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] getNewMembers() {
        return newMembers;
    }


    /**
     * Sets the newMembers value for this UpdateTeamRequest.
     * 
     * @param newMembers
     */
    public void setNewMembers(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] newMembers) {
        this.newMembers = newMembers;
    }


    /**
     * Gets the removedMembers value for this UpdateTeamRequest.
     * 
     * @return removedMembers
     */
    public is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] getRemovedMembers() {
        return removedMembers;
    }


    /**
     * Sets the removedMembers value for this UpdateTeamRequest.
     * 
     * @param removedMembers
     */
    public void setRemovedMembers(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] removedMembers) {
        this.removedMembers = removedMembers;
    }


    /**
     * Gets the teamID value for this UpdateTeamRequest.
     * 
     * @return teamID
     */
    public java.lang.String getTeamID() {
        return teamID;
    }


    /**
     * Sets the teamID value for this UpdateTeamRequest.
     * 
     * @param teamID
     */
    public void setTeamID(java.lang.String teamID) {
        this.teamID = teamID;
    }


    /**
     * Gets the updatedMembers value for this UpdateTeamRequest.
     * 
     * @return updatedMembers
     */
    public is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] getUpdatedMembers() {
        return updatedMembers;
    }


    /**
     * Sets the updatedMembers value for this UpdateTeamRequest.
     * 
     * @param updatedMembers
     */
    public void setUpdatedMembers(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.TeamMember[] updatedMembers) {
        this.updatedMembers = updatedMembers;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UpdateTeamRequest)) return false;
        UpdateTeamRequest other = (UpdateTeamRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.newMembers==null && other.getNewMembers()==null) || 
             (this.newMembers!=null &&
              java.util.Arrays.equals(this.newMembers, other.getNewMembers()))) &&
            ((this.removedMembers==null && other.getRemovedMembers()==null) || 
             (this.removedMembers!=null &&
              java.util.Arrays.equals(this.removedMembers, other.getRemovedMembers()))) &&
            ((this.teamID==null && other.getTeamID()==null) || 
             (this.teamID!=null &&
              this.teamID.equals(other.getTeamID()))) &&
            ((this.updatedMembers==null && other.getUpdatedMembers()==null) || 
             (this.updatedMembers!=null &&
              java.util.Arrays.equals(this.updatedMembers, other.getUpdatedMembers())));
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
        if (getNewMembers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNewMembers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNewMembers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRemovedMembers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRemovedMembers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRemovedMembers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTeamID() != null) {
            _hashCode += getTeamID().hashCode();
        }
        if (getUpdatedMembers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUpdatedMembers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUpdatedMembers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UpdateTeamRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "UpdateTeamRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newMembers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "NewMembers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamMember"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamMember"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("removedMembers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "RemovedMembers"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updatedMembers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "UpdatedMembers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamMember"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamMember"));
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
