package com.apps.remotebluetooth;

import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class WaitThread implements Runnable{

	/** Constructor */
	public WaitThread() {
	}
	
	@Override
	public void run() {
		waitForConnection();
	}
	
	/** Waiting for connection from devices */
	private void waitForConnection() {
		// retrieve the local Bluetooth device object
		LocalDevice local = null;
		
		StreamConnectionNotifier notifier;
		StreamConnection connection = null;
		
		// setup the server to listen for connection
		try {
			local = LocalDevice.getLocalDevice();
			local.setDiscoverable(DiscoveryAgent.GIAC);			
			UUID uuid = new UUID("04c6093b00001000800000805f9b34fb", false);
            System.out.println("----------------------------------------");
			System.out.println("SERVER UID: " + uuid.toString());
            System.out.println("----------------------------------------\n");
            String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetoothServer";
            notifier = (StreamConnectionNotifier)Connector.open(url);
        } catch (BluetoothStateException e) {
            System.out.println("----------------------------------------");
            System.out.println("SERVER RESPONSE: Bluetooth is not turned on.");
            System.out.println("----------------------------------------\n");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// waiting for connection
		while(true) {
			try {
	            System.out.println("----------------------------------------");
	            System.out.println("SERVER RESPONSE: Waiting for connection...");
	            System.out.println("----------------------------------------\n");
	            /*TEST COMMAND*/
	            String cmd = "StartNext"; // just for test
	            ClientConnection request = new ClientConnection();// just for test
	    		request.getRequest(cmd);// just for test
	    		/*/TEST COMMAND*/
				connection = notifier.acceptAndOpen();
				Thread processThread = new Thread(new ProcessConnectionThread(connection));
				processThread.start(); 
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
