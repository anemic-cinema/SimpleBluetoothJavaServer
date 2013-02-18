package com.apps.remotebluetooth;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;


public class ClientConnection {

	static final String addr = "http://localhost:8888/kevo/";
	private static boolean flag = false;
	
	/* Constructor */
	public ClientConnection() {
	}

	private String randomize(){
		double get_random = (double) Math.random() * 1.1000000;
		String random = Double.toString(get_random);	
		return random;
	}

    public void getRequest(String args) throws Exception {
    	SchemeRegistry schemeRegistry = new SchemeRegistry();
    	schemeRegistry.register(
    	         new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
    	schemeRegistry.register(
    	         new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

    	PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
    	// Increase max total connection to 200
    	cm.setMaxTotal(200);
    	// Increase default max connection per route to 20
    	cm.setDefaultMaxPerRoute(20);
    	// Increase max connections for localhost:80 to 50
    	HttpHost localhost = new HttpHost("locahost", 80);
    	cm.setMaxPerRoute(new HttpRoute(localhost), 50);
    	
    	
        HttpClient httpclient = new DefaultHttpClient();
        try {
        	String getRandom = randomize();
        	String parameters;

        	if(args != null && !args.isEmpty()){
        		parameters = "cmd=" + args + "&random=" + getRandom;
        	//parameters = "random=" + getRandom;
        	} else {
        		parameters = "random=" + getRandom;
        	}
       	
        	/*
        	 * TODO Implementing authentication system
        	 */
//            httpclient.getCredentialsProvider().setCredentials(
//                    new AuthScope("http://localhost:8888/kevo/index.html"),
//                    new UsernamePasswordCredentials("display.none", "bosconero"));
            
            HttpGet httpget = new HttpGet(ClientConnection.addr + "?" + parameters);

            // Execute HTTP request
            System.out.println("----------------------------------------");
            System.out.println("SERVER REQUEST: " + httpget.getURI());
            System.out.println("----------------------------------------\n");
            
            HttpResponse response = httpclient.execute(httpget);

            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            System.out.println("----------------------------------------\n");

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            

            // If the response does not enclose an entity, there is no need
            // to bother about connection release
            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    instream.read();
                    // do something useful with the response
                    System.out.println("----------------------------------------");
                    System.out.println("SERVER RESPONSE: " 	+ ContentType.getOrDefault(entity) + "\n"
                    										+ EntityUtils.toString(entity));
                    System.out.println("----------------------------------------\n");
              
                    //instream.read()
                } catch (IOException ex) {
                    // In case of an IOException the connection will be released
                    // back to the connection manager automatically
                    throw ex;
                } catch (RuntimeException ex) {
                    // In case of an unexpected exception you may want to abort
                    // the HTTP request in order to shut down the underlying
                    // connection immediately.
                    httpget.abort();
                    throw ex;
                } finally {
                    // Closing the input stream will trigger connection release
                    try { instream.close(); } catch (Exception ignore) {}
                }
            }

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
            /*
             * TODO Improve this part
             */
            if(!flag){
            	// set flag to true
            	flag = true;
	        	  /*
	             * TODO Improve this part
	             */
            	// sleep the thread with timeout because foobar macro is slow to update
            	Thread.sleep(200);
            	getRequest("");
            } else {
            	flag = false;
            }
        }
    }

}
