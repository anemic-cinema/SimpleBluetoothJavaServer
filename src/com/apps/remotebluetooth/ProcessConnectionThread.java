package com.apps.remotebluetooth;
import com.apps.remotebluetooth.ClientConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable{

	private StreamConnection mConnection;
	private OutputStream outStream = null; 
	
	// Constant that indicate command from devices
	private static final int EXIT = -1;
	private static final int PLAY  = 1; 
	private static final int PAUSE = 2;
	private static final int NEXT  = 3;
	private static final int PREV  = 4;
	
	private static String cmd = "";
	
	public ProcessConnectionThread(StreamConnection connection)
	{
		mConnection = connection;
	}
	
	@Override
	public void run() {
		try {
			// prepare to receive data
			InputStream inputStream = mConnection.openInputStream();
			System.out.println("Waiting for commands...");
			
	        while (true) {
	        	int command = inputStream.read();
	        	if (command == EXIT){	
	        		outStream.close();
	        		System.out.println("Finish process");
	        		break;
	        	}
	        	processCommand(command);
	        	// Send response to Bluetooth client
	        	sendResponse();
        	}
        } catch (Exception e) {
    		e.printStackTrace();
    	}
	}
	
	/**
	 * Send Response to the client
	 * @param outStream
	 * @return 
	 * @throws IOException 
	 */
	public void sendResponse(){
		try{
			//send response to spp client
			if(outStream == null){
				outStream = mConnection.openOutputStream();	
			}
			PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
			System.out.println("Response String from SPP Server");
			pWriter.print("Response String from SPP Server\r\n");
			pWriter.flush();
			pWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;	
		} 
	}
	
	/**
	 * Process the command from client
	 * @param command the command code
	 */
	public static void processCommand(int command) { //private void processCommand(int command) {
		try {
			switch (command) {
	    	case PLAY:
	            System.out.println("----------------------------------------");
	            System.out.println("CMD: +PlayOrPause+");
	            System.out.println("----------------------------------------\n");
	    		cmd = "PlayOrPause";
	    		break;
	    	case PAUSE:
	            System.out.println("----------------------------------------");
	            System.out.println("CMD: +Pause+");
	            System.out.println("----------------------------------------\n");
	    		cmd = "PlayOrPause";
	    		break;
	    	case NEXT:
	            System.out.println("----------------------------------------");
	            System.out.println("CMD: +Next+");
	            System.out.println("----------------------------------------\n");
    			cmd = "StartNext";
	    		break;
	    	case PREV:
	            System.out.println("----------------------------------------");
	            System.out.println("CMD: +Prev+");
	            System.out.println("----------------------------------------\n");
	    		cmd = "StartPrevious";
	    		break;
			}
			ClientConnection request = new ClientConnection();
			request.getRequest(cmd);
		} catch (Exception e) {	
			e.printStackTrace();		
		}
	}
}