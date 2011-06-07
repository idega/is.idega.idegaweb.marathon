/**
 * TeamMember.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client;

public class TeamMember  implements java.io.Serializable {
    private java.lang.String personID;

    private java.lang.Boolean showOnWeb;

    public TeamMember() {
    }

    public TeamMember(
           java.lang.String personID,
           java.lang.Boolean showOnWeb) {
           this.personID = personID;
           this.showOnWeb = showOnWeb;
    }


    /**
     * Gets the personID value for this TeamMember.
     * 
     * @return personID
     */
    public java.lang.String getPersonID() {
        return personID;
    }


    /**
     * Sets the personID value for this TeamMember.
     * 
     * @param personID
     */
    public void setPersonID(java.lang.String personID) {
        this.personID = personID;
    }


    /**
     * Gets the showOnWeb value for this TeamMember.
     * 
     * @return showOnWeb
     */
    public java.lang.Boolean getShowOnWeb() {
        return showOnWeb;
    }


    /**
     * Sets the showOnWeb value for this TeamMember.
     * 
     * @param showOnWeb
     */
    public void setShowOnWeb(java.lang.Boolean showOnWeb) {
        this.showOnWeb = showOnWeb;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TeamMember)) return false;
        TeamMember other = (TeamMember) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.personID==null && other.getPersonID()==null) || 
             (this.personID!=null &&
              this.personID.equals(other.getPersonID()))) &&
            ((this.showOnWeb==null && other.getShowOnWeb()==null) || 
             (this.showOnWeb!=null &&
              this.showOnWeb.equals(other.getShowOnWeb())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getPersonID() != null) {
            _hashCode += getPersonID().hashCode();
        }
        if (getShowOnWeb() != null) {
            _hashCode += getShowOnWeb().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TeamMember.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "TeamMember"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "PersonID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("showOnWeb");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "ShowOnWeb"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
