/**
 * ServiceException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client;

public class ServiceException  implements java.io.Serializable {
    private java.lang.String exceptionTypeName;

    private java.lang.String exceptionTypeNamespace;

    private java.lang.String helpLink;

    private is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceException innerException;

    private java.lang.String message;

    private java.lang.String source;

    private java.lang.String stackTrace;

    public ServiceException() {
    }

    public ServiceException(
           java.lang.String exceptionTypeName,
           java.lang.String exceptionTypeNamespace,
           java.lang.String helpLink,
           is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceException innerException,
           java.lang.String message,
           java.lang.String source,
           java.lang.String stackTrace) {
           this.exceptionTypeName = exceptionTypeName;
           this.exceptionTypeNamespace = exceptionTypeNamespace;
           this.helpLink = helpLink;
           this.innerException = innerException;
           this.message = message;
           this.source = source;
           this.stackTrace = stackTrace;
    }


    /**
     * Gets the exceptionTypeName value for this ServiceException.
     * 
     * @return exceptionTypeName
     */
    public java.lang.String getExceptionTypeName() {
        return exceptionTypeName;
    }


    /**
     * Sets the exceptionTypeName value for this ServiceException.
     * 
     * @param exceptionTypeName
     */
    public void setExceptionTypeName(java.lang.String exceptionTypeName) {
        this.exceptionTypeName = exceptionTypeName;
    }


    /**
     * Gets the exceptionTypeNamespace value for this ServiceException.
     * 
     * @return exceptionTypeNamespace
     */
    public java.lang.String getExceptionTypeNamespace() {
        return exceptionTypeNamespace;
    }


    /**
     * Sets the exceptionTypeNamespace value for this ServiceException.
     * 
     * @param exceptionTypeNamespace
     */
    public void setExceptionTypeNamespace(java.lang.String exceptionTypeNamespace) {
        this.exceptionTypeNamespace = exceptionTypeNamespace;
    }


    /**
     * Gets the helpLink value for this ServiceException.
     * 
     * @return helpLink
     */
    public java.lang.String getHelpLink() {
        return helpLink;
    }


    /**
     * Sets the helpLink value for this ServiceException.
     * 
     * @param helpLink
     */
    public void setHelpLink(java.lang.String helpLink) {
        this.helpLink = helpLink;
    }


    /**
     * Gets the innerException value for this ServiceException.
     * 
     * @return innerException
     */
    public is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceException getInnerException() {
        return innerException;
    }


    /**
     * Sets the innerException value for this ServiceException.
     * 
     * @param innerException
     */
    public void setInnerException(is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ServiceException innerException) {
        this.innerException = innerException;
    }


    /**
     * Gets the message value for this ServiceException.
     * 
     * @return message
     */
    public java.lang.String getMessage() {
        return message;
    }


    /**
     * Sets the message value for this ServiceException.
     * 
     * @param message
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }


    /**
     * Gets the source value for this ServiceException.
     * 
     * @return source
     */
    public java.lang.String getSource() {
        return source;
    }


    /**
     * Sets the source value for this ServiceException.
     * 
     * @param source
     */
    public void setSource(java.lang.String source) {
        this.source = source;
    }


    /**
     * Gets the stackTrace value for this ServiceException.
     * 
     * @return stackTrace
     */
    public java.lang.String getStackTrace() {
        return stackTrace;
    }


    /**
     * Sets the stackTrace value for this ServiceException.
     * 
     * @param stackTrace
     */
    public void setStackTrace(java.lang.String stackTrace) {
        this.stackTrace = stackTrace;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiceException)) return false;
        ServiceException other = (ServiceException) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.exceptionTypeName==null && other.getExceptionTypeName()==null) || 
             (this.exceptionTypeName!=null &&
              this.exceptionTypeName.equals(other.getExceptionTypeName()))) &&
            ((this.exceptionTypeNamespace==null && other.getExceptionTypeNamespace()==null) || 
             (this.exceptionTypeNamespace!=null &&
              this.exceptionTypeNamespace.equals(other.getExceptionTypeNamespace()))) &&
            ((this.helpLink==null && other.getHelpLink()==null) || 
             (this.helpLink!=null &&
              this.helpLink.equals(other.getHelpLink()))) &&
            ((this.innerException==null && other.getInnerException()==null) || 
             (this.innerException!=null &&
              this.innerException.equals(other.getInnerException()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.source==null && other.getSource()==null) || 
             (this.source!=null &&
              this.source.equals(other.getSource()))) &&
            ((this.stackTrace==null && other.getStackTrace()==null) || 
             (this.stackTrace!=null &&
              this.stackTrace.equals(other.getStackTrace())));
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
        if (getExceptionTypeName() != null) {
            _hashCode += getExceptionTypeName().hashCode();
        }
        if (getExceptionTypeNamespace() != null) {
            _hashCode += getExceptionTypeNamespace().hashCode();
        }
        if (getHelpLink() != null) {
            _hashCode += getHelpLink().hashCode();
        }
        if (getInnerException() != null) {
            _hashCode += getInnerException().hashCode();
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getSource() != null) {
            _hashCode += getSource().hashCode();
        }
        if (getStackTrace() != null) {
            _hashCode += getStackTrace().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiceException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exceptionTypeName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ExceptionTypeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exceptionTypeNamespace");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ExceptionTypeNamespace"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("helpLink");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "HelpLink"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("innerException");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "InnerException"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "ServiceException"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "Message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "Source"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stackTrace");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tmsoftware.is/Service/Faults/", "StackTrace"));
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
