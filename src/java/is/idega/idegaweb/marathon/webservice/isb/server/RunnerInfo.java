/**
 * RunnerInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.marathon.webservice.isb.server;

public class RunnerInfo  implements java.io.Serializable {
    private java.lang.String distance;

    private java.lang.String email;

    private java.lang.String leg;

    private java.lang.String mobile;

    private is.idega.idegaweb.marathon.webservice.isb.server.RelayPartnerInfo[] partners;

    private java.lang.String personalID;

    private java.lang.String phone;

    private java.lang.String registeredBy;

    private is.idega.idegaweb.marathon.webservice.isb.server.Session session;

    private java.lang.String shirtSize;

    private java.lang.String shirtSizeGender;

    public RunnerInfo() {
    }

    public RunnerInfo(
           java.lang.String distance,
           java.lang.String email,
           java.lang.String leg,
           java.lang.String mobile,
           is.idega.idegaweb.marathon.webservice.isb.server.RelayPartnerInfo[] partners,
           java.lang.String personalID,
           java.lang.String phone,
           java.lang.String registeredBy,
           is.idega.idegaweb.marathon.webservice.isb.server.Session session,
           java.lang.String shirtSize,
           java.lang.String shirtSizeGender) {
           this.distance = distance;
           this.email = email;
           this.leg = leg;
           this.mobile = mobile;
           this.partners = partners;
           this.personalID = personalID;
           this.phone = phone;
           this.registeredBy = registeredBy;
           this.session = session;
           this.shirtSize = shirtSize;
           this.shirtSizeGender = shirtSizeGender;
    }


    /**
     * Gets the distance value for this RunnerInfo.
     * 
     * @return distance
     */
    public java.lang.String getDistance() {
        return distance;
    }


    /**
     * Sets the distance value for this RunnerInfo.
     * 
     * @param distance
     */
    public void setDistance(java.lang.String distance) {
        this.distance = distance;
    }


    /**
     * Gets the email value for this RunnerInfo.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this RunnerInfo.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the leg value for this RunnerInfo.
     * 
     * @return leg
     */
    public java.lang.String getLeg() {
        return leg;
    }


    /**
     * Sets the leg value for this RunnerInfo.
     * 
     * @param leg
     */
    public void setLeg(java.lang.String leg) {
        this.leg = leg;
    }


    /**
     * Gets the mobile value for this RunnerInfo.
     * 
     * @return mobile
     */
    public java.lang.String getMobile() {
        return mobile;
    }


    /**
     * Sets the mobile value for this RunnerInfo.
     * 
     * @param mobile
     */
    public void setMobile(java.lang.String mobile) {
        this.mobile = mobile;
    }


    /**
     * Gets the partners value for this RunnerInfo.
     * 
     * @return partners
     */
    public is.idega.idegaweb.marathon.webservice.isb.server.RelayPartnerInfo[] getPartners() {
        return partners;
    }


    /**
     * Sets the partners value for this RunnerInfo.
     * 
     * @param partners
     */
    public void setPartners(is.idega.idegaweb.marathon.webservice.isb.server.RelayPartnerInfo[] partners) {
        this.partners = partners;
    }


    /**
     * Gets the personalID value for this RunnerInfo.
     * 
     * @return personalID
     */
    public java.lang.String getPersonalID() {
        return personalID;
    }


    /**
     * Sets the personalID value for this RunnerInfo.
     * 
     * @param personalID
     */
    public void setPersonalID(java.lang.String personalID) {
        this.personalID = personalID;
    }


    /**
     * Gets the phone value for this RunnerInfo.
     * 
     * @return phone
     */
    public java.lang.String getPhone() {
        return phone;
    }


    /**
     * Sets the phone value for this RunnerInfo.
     * 
     * @param phone
     */
    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }


    /**
     * Gets the registeredBy value for this RunnerInfo.
     * 
     * @return registeredBy
     */
    public java.lang.String getRegisteredBy() {
        return registeredBy;
    }


    /**
     * Sets the registeredBy value for this RunnerInfo.
     * 
     * @param registeredBy
     */
    public void setRegisteredBy(java.lang.String registeredBy) {
        this.registeredBy = registeredBy;
    }


    /**
     * Gets the session value for this RunnerInfo.
     * 
     * @return session
     */
    public is.idega.idegaweb.marathon.webservice.isb.server.Session getSession() {
        return session;
    }


    /**
     * Sets the session value for this RunnerInfo.
     * 
     * @param session
     */
    public void setSession(is.idega.idegaweb.marathon.webservice.isb.server.Session session) {
        this.session = session;
    }


    /**
     * Gets the shirtSize value for this RunnerInfo.
     * 
     * @return shirtSize
     */
    public java.lang.String getShirtSize() {
        return shirtSize;
    }


    /**
     * Sets the shirtSize value for this RunnerInfo.
     * 
     * @param shirtSize
     */
    public void setShirtSize(java.lang.String shirtSize) {
        this.shirtSize = shirtSize;
    }


    /**
     * Gets the shirtSizeGender value for this RunnerInfo.
     * 
     * @return shirtSizeGender
     */
    public java.lang.String getShirtSizeGender() {
        return shirtSizeGender;
    }


    /**
     * Sets the shirtSizeGender value for this RunnerInfo.
     * 
     * @param shirtSizeGender
     */
    public void setShirtSizeGender(java.lang.String shirtSizeGender) {
        this.shirtSizeGender = shirtSizeGender;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RunnerInfo)) return false;
        RunnerInfo other = (RunnerInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.distance==null && other.getDistance()==null) || 
             (this.distance!=null &&
              this.distance.equals(other.getDistance()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.leg==null && other.getLeg()==null) || 
             (this.leg!=null &&
              this.leg.equals(other.getLeg()))) &&
            ((this.mobile==null && other.getMobile()==null) || 
             (this.mobile!=null &&
              this.mobile.equals(other.getMobile()))) &&
            ((this.partners==null && other.getPartners()==null) || 
             (this.partners!=null &&
              java.util.Arrays.equals(this.partners, other.getPartners()))) &&
            ((this.personalID==null && other.getPersonalID()==null) || 
             (this.personalID!=null &&
              this.personalID.equals(other.getPersonalID()))) &&
            ((this.phone==null && other.getPhone()==null) || 
             (this.phone!=null &&
              this.phone.equals(other.getPhone()))) &&
            ((this.registeredBy==null && other.getRegisteredBy()==null) || 
             (this.registeredBy!=null &&
              this.registeredBy.equals(other.getRegisteredBy()))) &&
            ((this.session==null && other.getSession()==null) || 
             (this.session!=null &&
              this.session.equals(other.getSession()))) &&
            ((this.shirtSize==null && other.getShirtSize()==null) || 
             (this.shirtSize!=null &&
              this.shirtSize.equals(other.getShirtSize()))) &&
            ((this.shirtSizeGender==null && other.getShirtSizeGender()==null) || 
             (this.shirtSizeGender!=null &&
              this.shirtSizeGender.equals(other.getShirtSizeGender())));
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
        if (getDistance() != null) {
            _hashCode += getDistance().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getLeg() != null) {
            _hashCode += getLeg().hashCode();
        }
        if (getMobile() != null) {
            _hashCode += getMobile().hashCode();
        }
        if (getPartners() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPartners());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPartners(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPersonalID() != null) {
            _hashCode += getPersonalID().hashCode();
        }
        if (getPhone() != null) {
            _hashCode += getPhone().hashCode();
        }
        if (getRegisteredBy() != null) {
            _hashCode += getRegisteredBy().hashCode();
        }
        if (getSession() != null) {
            _hashCode += getSession().hashCode();
        }
        if (getShirtSize() != null) {
            _hashCode += getShirtSize().hashCode();
        }
        if (getShirtSizeGender() != null) {
            _hashCode += getShirtSizeGender().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RunnerInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://illuminati.is", "RunnerInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "distance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("mobile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "mobile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partners");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "partners"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://illuminati.is", "RelayPartnerInfo"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://illuminati.is", "item"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "personalID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "phone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registeredBy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "registeredBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("session");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "session"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://illuminati.is", "Session"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shirtSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "shirtSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shirtSizeGender");
        elemField.setXmlName(new javax.xml.namespace.QName("http://illuminati.is", "shirtSizeGender"));
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
