/**
 * Registration.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.impl;

public class Registration  implements java.io.Serializable {
    private java.lang.String charityId;

    private int distance;

    private java.lang.String personalId;

    public Registration() {
    }

    public Registration(
           java.lang.String charityId,
           int distance,
           java.lang.String personalId) {
           this.charityId = charityId;
           this.distance = distance;
           this.personalId = personalId;
    }


    /**
     * Gets the charityId value for this Registration.
     * 
     * @return charityId
     */
    public java.lang.String getCharityId() {
        return charityId;
    }


    /**
     * Sets the charityId value for this Registration.
     * 
     * @param charityId
     */
    public void setCharityId(java.lang.String charityId) {
        this.charityId = charityId;
    }


    /**
     * Gets the distance value for this Registration.
     * 
     * @return distance
     */
    public int getDistance() {
        return distance;
    }


    /**
     * Sets the distance value for this Registration.
     * 
     * @param distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }


    /**
     * Gets the personalId value for this Registration.
     * 
     * @return personalId
     */
    public java.lang.String getPersonalId() {
        return personalId;
    }


    /**
     * Sets the personalId value for this Registration.
     * 
     * @param personalId
     */
    public void setPersonalId(java.lang.String personalId) {
        this.personalId = personalId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Registration)) return false;
        Registration other = (Registration) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.charityId==null && other.getCharityId()==null) || 
             (this.charityId!=null &&
              this.charityId.equals(other.getCharityId()))) &&
            this.distance == other.getDistance() &&
            ((this.personalId==null && other.getPersonalId()==null) || 
             (this.personalId!=null &&
              this.personalId.equals(other.getPersonalId())));
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
        if (getCharityId() != null) {
            _hashCode += getCharityId().hashCode();
        }
        _hashCode += getDistance();
        if (getPersonalId() != null) {
            _hashCode += getPersonalId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Registration.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:MarathonRegistrationService", "Registration"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("charityId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "charityId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "distance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "personalId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
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
