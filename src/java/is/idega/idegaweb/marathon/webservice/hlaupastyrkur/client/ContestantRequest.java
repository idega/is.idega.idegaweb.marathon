/**
 * ContestantRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client;

public class ContestantRequest  extends is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.RegisterRequest  implements java.io.Serializable {
    private java.lang.String personalID;

    private java.lang.Boolean showOnWeb;

    public ContestantRequest() {
    }

    public ContestantRequest(
           java.lang.String distance,
           is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.Login login,
           java.lang.String charityID,
           java.lang.String name,
           java.lang.String password,
           java.lang.String username,
           java.lang.String personalID,
           java.lang.Boolean showOnWeb) {
        super(
            distance,
            login,
            charityID,
            name,
            password,
            username);
        this.personalID = personalID;
        this.showOnWeb = showOnWeb;
    }


    /**
     * Gets the personalID value for this ContestantRequest.
     * 
     * @return personalID
     */
    public java.lang.String getPersonalID() {
        return personalID;
    }


    /**
     * Sets the personalID value for this ContestantRequest.
     * 
     * @param personalID
     */
    public void setPersonalID(java.lang.String personalID) {
        this.personalID = personalID;
    }


    /**
     * Gets the showOnWeb value for this ContestantRequest.
     * 
     * @return showOnWeb
     */
    public java.lang.Boolean getShowOnWeb() {
        return showOnWeb;
    }


    /**
     * Sets the showOnWeb value for this ContestantRequest.
     * 
     * @param showOnWeb
     */
    public void setShowOnWeb(java.lang.Boolean showOnWeb) {
        this.showOnWeb = showOnWeb;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ContestantRequest)) return false;
        ContestantRequest other = (ContestantRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.personalID==null && other.getPersonalID()==null) || 
             (this.personalID!=null &&
              this.personalID.equals(other.getPersonalID()))) &&
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
        int _hashCode = super.hashCode();
        if (getPersonalID() != null) {
            _hashCode += getPersonalID().hashCode();
        }
        if (getShowOnWeb() != null) {
            _hashCode += getShowOnWeb().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ContestantRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "ContestantRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/charities/services/", "PersonalID"));
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
