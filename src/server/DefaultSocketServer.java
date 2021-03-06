package server;

import java.net.*;
import java.io.*;

import adapter.BuildAuto;

public class DefaultSocketServer extends Thread implements SocketServerInterface, SocketServerConstants {

	private BufferedReader reader;
	private BufferedWriter writer;
	private ServerSocket sock;
	private Socket sockClient;
	private String strHost;
	private int iPort;
	private boolean listening;
	private BuildAuto buildAutoInterface;

	public DefaultSocketServer(String strHost, int iPort) {
		setPort(iPort);
		setHost(strHost);
		buildAutoInterface = new BuildAuto(); 
		buildAutoInterface.init(); 
	}

	public void run() {
		listen();
	}

	public boolean listen() {

		try {
			sock = new ServerSocket(iPort);
		} catch (IOException socketError) {
			if (DEBUG)
				System.err.println("Unable to connect to " + strHost + " on port " + iPort);
			return false;
		}

		listening = true;
		System.err.println("Listening to " + strHost + " on port " + iPort);

		
		while (listening) {
			try {
				sockClient = sock.accept();
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
			SocketClientHandler d = new SocketClientHandler(sockClient, this, buildAutoInterface);
			d.start();
		}
		return true;
	}

	public void close() {
		listening = false;
		try {
			writer.close();
			reader.close();
			sock.close();
			sockClient.close();
		} catch (IOException e) {
			if (DEBUG)
				System.err.println("Error closing socket to " + strHost);
		}
	}

	public void setHost(String strHost) {
		this.strHost = strHost;
	}

	public void setPort(int iPort) {
		this.iPort = iPort;
	}

	public static void main(String arg[]) {
		String strLocalHost = "";
		try {
			strLocalHost = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			System.err.println("Unable to find local host");
		}
		DefaultSocketServer d = new DefaultSocketServer(strLocalHost, iDAYTIME_PORT);
		d.start();
	}

}