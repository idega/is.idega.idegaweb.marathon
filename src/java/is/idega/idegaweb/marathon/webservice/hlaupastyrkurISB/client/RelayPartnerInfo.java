/**
 * RelayPartnerInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client;

public class RelayPartnerInfo  implements java.io.Serializable {
    private java.lang.String email;

    private java.lang.String leg;

    private java.lang.String personalID;

    private java.lang.String shirtSize;

    public RelayPartnerInfo() {
    }

    public RelayPartnerInfo(
           java.lang.String email,
           java.lang.String leg,
           java.lang.String personalID,
           java.lang.String shirtSize) {
           this.email = email;
           this.leg = leg;
           this.personalID = personalID;
           this.shirtSize = shirtSize;
    }


    /**
     * Gets the email value for this RelayPartnerInfo.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this RelayPartnerInfo.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the leg value for this RelayPartnerInfo.
     * 
     * @return leg
     */
    public java.lang.String getLeg() {
        return leg;
    }


    /**
     * Sets the leg value for this RelayPartnerInfo.
     * 
     * @param leg
     */
    public void setLeg(java.lang.String leg) {
        this.leg = leg;
    }


    /**
     * Gets the personalID value for this RelayPartnerInfo.
     * 
     * @return personalID
     */
    public java.lang.String getPersonalID() {
        return personalID;
    }


    /**
     * Sets the personalID value for this RelayPartnerInfo.
     * 
     * @param personalID
     */
    public void setPersonalID(java.lang.String personalID) {
        this.personalID = personalID;
    }


    /**
     * Gets the shirtSize value for this RelayPartnerInfo.
     * 
     * @return shirtSize
     */
    public java.lang.String getShirtSize() {
        return shirtSize;
    }


    /**
     * Sets the shirtSize value for this RelayPartnerInfo.
     * 
     * @param shirtSize
     */
    public void setShirtSize(java.lang.String shirtSize) {
        this.shirtSize = shirtSize;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RelayPartnerInfo)) return false;
        RelayPartnerInfo other = (RelayPartnerInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.leg==null && other.getLeg()==null) || 
             (this.leg!=null &&
              this.leg.equals(other.getLeg()))) &&
            ((this.personalID==null && other.getPersonalID()==null) || 
             (this.personalID!=null &&
              this.personalID.equals(other.getPersonalID()))) &&
            ((this.shirtSize==null && other.getShirtSize()==null) || 
             (this.shirtSize!=null &&
              this.shirtSize.equals(other.getShirtSize())));
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
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getLeg() != null) {
            _hashCode += getLeg().hashCode();
        }
        if (getPersonalID() != null) {
            _hashCode += getPersonalID().hashCode();
        }
        if (getShirtSize() != null) {
            _hashCode += getShirtSize().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RelayPartnerInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://illuminati.is", "RelayPartnerInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("leg");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "leg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "personalID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shirtSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "shirtSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
