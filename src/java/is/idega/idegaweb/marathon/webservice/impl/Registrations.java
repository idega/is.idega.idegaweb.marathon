/**
 * Registrations.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.impl;

public class Registrations  implements java.io.Serializable {
    private is.idega.idegaweb.marathon.webservice.impl.Registration[] registrations;

    public Registrations() {
    }

    public Registrations(
           is.idega.idegaweb.marathon.webservice.impl.Registration[] registrations) {
           this.registrations = registrations;
    }


    /**
     * Gets the registrations value for this Registrations.
     * 
     * @return registrations
     */
    public is.idega.idegaweb.marathon.webservice.impl.Registration[] getRegistrations() {
        return registrations;
    }


    /**
     * Sets the registrations value for this Registrations.
     * 
     * @param registrations
     */
    public void setRegistrations(is.idega.idegaweb.marathon.webservice.impl.Registration[] registrations) {
        this.registrations = registrations;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Registrations)) return false;
        Registrations other = (Registrations) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.registrations==null && other.getRegistrations()==null) || 
             (this.registrations!=null &&
              java.util.Arrays.equals(this.registrations, other.getRegistrations())));
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
        if (getRegistrations() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRegistrations());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRegistrations(), i);
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
        new org.apache.axis.description.TypeDesc(Registrations.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:MarathonRegistrationService", "Registrations"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registrations");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registrations"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:MarathonRegistrationService", "Registration"));
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
