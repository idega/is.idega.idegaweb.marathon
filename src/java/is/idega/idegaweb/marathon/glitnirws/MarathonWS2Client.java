/**
 * $Id: MarathonWS2Client.java,v 1.1 2007/07/31 19:05:53 tryggvil Exp $
 * Created in 2007 by tryggvil
 *
 * Copyright (C) 2000-2007 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.glitnirws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Stub;
import org.apache.axis.deployment.wsdd.WSDDDocument;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.xml.sax.SAXException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWMainApplicationSettings;


/**
 * <p>
 * Utility class to call the Glitnir MarathonWS2 service
 * </p>
 *  Last modified: $Date: 2007/07/31 19:05:53 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:tryggvil@idega.com">tryggvil</a>
 * @version $Revision: 1.1 $
 */
public class MarathonWS2Client implements CallbackHandler {

	//String serviceUrl = "http://localhost:443/adgerdirv1/Marathon/MarathonWS2.asmx";

	private static final String PROP_GLITNIR_MARATHONWS_PASSWORD = "GLITNIR_MARATHONWS_PASSWORD";
	private static final String PROP_GLITNIR_MARATHONWS_USERNAME = "GLITNIR_MARATHONWS_USERNAME";
	private static final String PROP_GLITNIR_MARATHONWS_URL = "GLITNIR_MARATHONWS_URL";
	//Glitnir testserver
	private String serviceUrl = "https://ws-test.isb.is/adgerdirv1/Marathon/MarathonWS2.asmx";
	private String userName="idegatest";
	private String password="iwccJi432s";
	
	//Glitnir production server
	//private String serviceUrl = "https://ws.isb.is/adgerdirv1/Marathon/MarathonWS2.asmx";
	//private String userName="idegaws";
	//private String password="3ZK2VhJU";
	
	private String axisClientDeployConfigURI = "resource://is/idega/idegaweb/marathon/glitnirws/deploy_client.wsdd";
	
	public static void main(String[] args) throws Exception{
		MarathonWS2Client client = new MarathonWS2Client();
		client.executeTest(args);
	}
	
	public MarathonWS2Client(){
		
	}
	
	public MarathonWS2Client(IWMainApplication iwma){
		IWMainApplicationSettings settings = iwma.getSettings();
		String propServiceUrl = settings.getProperty(PROP_GLITNIR_MARATHONWS_URL);
		String propUserName = settings.getProperty(PROP_GLITNIR_MARATHONWS_USERNAME);
		String propPassword = settings.getProperty(PROP_GLITNIR_MARATHONWS_PASSWORD);
		
		if(propServiceUrl!=null){
			setServiceUrl(propServiceUrl);
		}
		if(propUserName!=null){
			setUserName(propUserName);
		}
		if(propPassword!=null){
			setPassword(propPassword);
		}
	}
	
	/**
	 * <p>
	 * TODO tryggvil describe method main
	 * </p>
	 * @param args
	 * @throws Exception 
	 */
	public void executeTest(String[] args) throws Exception {
		
		//String kennitala="1011783159";
		String kennitala = "0101560199";
		boolean erIVidskiptum = erIVidskiptumVidGlitni(kennitala);
		
		if(erIVidskiptum){
			System.out.println("Kennitala: "+kennitala+" er i vidskiptum");
		}
		else{
			System.out.println("Kennitala: "+kennitala+" er ekki i vidskiptum");
		}
	}
	
	
	public boolean erIVidskiptumVidGlitni(String kennitala) throws Exception{
		MarathonWS2Soap_PortType service = getService();
		return service.erIVidskiptumVidGlitni(kennitala);
	}
	
	public String[] erIVidskiptumVidGlitni2(String[] kennitolur) throws Exception{
		MarathonWS2Soap_PortType service = getService();
		return service.erIVidskiptumVidGlitni2(kennitolur);
	}
	
	
	private MarathonWS2Soap_PortType getService() throws FileNotFoundException,
	ServiceException, Exception {
		
		EngineConfiguration config = getAxisEngine();
		// String monitorServiceUrl =
		// "http://localhost/SvcFasteignaskra_0201/Fasteignaskra.asmx";
		
		MarathonWS2Locator locator = new MarathonWS2Locator(config);
		locator.setEndpointAddress("MarathonWS2Soap", getServiceUrl());
		MarathonWS2Soap_PortType port = locator.getMarathonWS2Soap();
		
		Stub stub = (Stub) port;
		
		stub._setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		stub._setProperty(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
		stub._setProperty(WSHandlerConstants.USER, getUserName());
		//stub._setProperty(WSHandlerConstants.PW_CALLBACK_CLASS, this.getClass().getName());
		stub._setProperty(WSHandlerConstants.PW_CALLBACK_REF, this);
		
		return port;
		
	}


	private String getServiceUrl() {
		return serviceUrl;
	}


	protected EngineConfiguration getAxisEngine() throws URISyntaxException,
	IOException, ParserConfigurationException, SAXException {


		InputStream in = getAxisClientDeployConfigAsStream();
		
		File wsddFile = getTempFile("client-config.wsdd");
		// wsddFile.delete();
		// wsddFile.createNewFile();
		OutputStream out = new FileOutputStream(wsddFile);
		
		byte[] buf = new byte[1024];
		int read = in.read(buf);
		
		while (read > 0) {
			out.write(buf, 0, read);
			read = in.read(buf);
		}
		in.close();
		out.close();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		WSDDDocument wsdddoc = new WSDDDocument(db.parse(wsddFile));
		
		//WSDDDocument wsdddoc = new WSDDDocument(db.parse(in));
		
		// sas = new SimpleAxisServer();
		// sas.setServerSocket(new ServerSocket(serverport));
		// sas.setMyConfig(wsdddoc.getDeployment());
		
		EngineConfiguration config = wsdddoc.getDeployment();
		// new FileProvider(getAxisClientDeployConfigAsStream());
		return config;
	}
	
	private InputStream getAxisClientDeployConfigAsStream()
	throws FileNotFoundException, URISyntaxException {
		String resourcePrefix = "resource://";
		String uri = getAxisClientDeployConfigURI();
		if (uri.startsWith(resourcePrefix)) {
			String path = uri.substring(resourcePrefix.length(), uri.length());
			return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			//return getClass().getResourceAsStream(path);
		} else {
			return new FileInputStream(new File(new java.net.URI(
					getAxisClientDeployConfigURI())));
		}
		//
	}
	
	private File getTempFile(String string) throws IOException {
		return File.createTempFile("marathonws2client", string);
	}

	public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
		String userId = getUserName();
		String passwd = getPassword();
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof WSPasswordCallback) {
				WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
				if (pc.getIdentifer().equals(userId)) {
					pc.setPassword(passwd);
				}
			}
			else {
				throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
			}
		}
	}

	private String getPassword() {
		return password;
	}


	
	public String getAxisClientDeployConfigURI() {
		return axisClientDeployConfigURI;
	}


	
	public void setAxisClientDeployConfigURI(String axisClientDeployConfigURI) {
		this.axisClientDeployConfigURI = axisClientDeployConfigURI;
	}


	
	public String getUserName() {
		return userName;
	}


	
	public void setUserName(String userName) {
		this.userName = userName;
	}


	
	public void setPassword(String password) {
		this.password = password;
	}

	
	protected void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
}
