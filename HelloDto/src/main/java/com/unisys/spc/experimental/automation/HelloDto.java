package com.unisys.spc.experimental.automation;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

//@XmlRootElement(name = "hellodto")
//@XmlAccessorType(XmlAccessType.FIELD)

// TODO. 19.7 Maps of JAXB objects. Your parameter or method return type must be a generic with a String as key and JAXB object's type

@XmlRootElement // RESTEasy should automatically select JAXBXmlRootElementProvider  (uncertain whether XmlRootEntity annotation is necessary. See Chapter 19 on JAXB
public class HelloDto implements Serializable {

	@XmlElement
	private String greeting;

	@XmlElement
	private String recipient;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public HelloDto()
	{
		greeting="";
		recipient="";
	}
	public HelloDto(String greet, String recip)
	{
		greeting = greet;
		recipient = recipient;
	}
	
//	@XmlElementWrapper
//	@XmlAnyElement(lax=true)
	public String getGreeting() {
		return greeting;
	}
	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}
//	@XmlElementWrapper
//	@XmlAnyElement(lax=true)
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
}
