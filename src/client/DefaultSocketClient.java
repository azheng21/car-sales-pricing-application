package client;

import java.net.*;
import java.util.Iterator;
import java.util.Map;
import java.net.URLEncoder;
import java.net.URLDecoder;

import adapter.BuildAuto;
import exception.AutoException;
import model.Automobile;

import java.io.*;

public class DefaultSocketClient extends Thread implements SocketClientInterface, SocketClientConstants {
	private BufferedReader stdIn_;
	private Socket socketClient;
	private InputStream socketClientInputStream;
	private OutputStream socketClientOutputStream;
	private BufferedReader reader;
	private BufferedWriter writer;
	private util.StreamIO streamIOUtil;
	private util.FileIO fileIOUtil;
	private String strHost;
	private int iPort;
	private CarModelOptionsIO carOptionsMenu;


	public DefaultSocketClient(String strHost, int iPort) {
		setPort(iPort);
		setHost(strHost);
		fileIOUtil = new util.FileIO();
		streamIOUtil = new util.StreamIO();
	}

	public void run() {
		try {
			openConnection();
			handleSession();
			closeSession();
		} catch (AutoException e) {
			if (DEBUG) {
				System.out.println("Host: " + strHost);
				System.out.println("Port: " + iPort);
				System.out.println(e.getMessage());
			}
		}
	}

	public void setStandardIn(BufferedReader stdIn) {
		stdIn_ = stdIn;
	}

	public void openConnection() throws AutoException {
		try {
			socketClient = new Socket(strHost, iPort);
		} catch (IOException socketError) {
			throw new exception.AutoException(1003);
		}
		try {
			socketClientInputStream = socketClient.getInputStream();
			socketClientOutputStream = socketClient.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(socketClientInputStream));
			writer = new BufferedWriter(new OutputStreamWriter(socketClientOutputStream));
		} catch (Exception e) {
			throw new exception.AutoException(1004);
		}
	}

	public void initCarOptionsMenu() throws AutoException {
		try {
			carOptionsMenu = new CarModelOptionsIO(this, stdIn_);
			carOptionsMenu.openConnection(socketClientInputStream, socketClientOutputStream);
		} catch (IOException e) {
			throw new exception.AutoException(1001);
		} catch (Exception e) {
			throw new exception.AutoException(1002);
		}
	}

	public void handleSession() {
		try {
			initCarOptionsMenu();
		} catch (AutoException e) {
			if (DEBUG)
				System.out.println(e.getMessage());
		}

		String strInput = "";
		String fromServer = "";
		if (DEBUG)
			System.out.println("Handling session with " + strHost + ":" + iPort);
		try {
			carOptionsMenu.displayMenu();
			while ((strInput = stdIn_.readLine()) != null) {
				if (carOptionsMenu.getMenuOption(strInput)) {
					fromServer = receiveInput();
					handleInput(fromServer);
				}
				carOptionsMenu.displayMenu();
			}
		} catch (IOException e) {
			if (DEBUG)
				System.out.println("client unexpectedly closed");
		} catch (AutoException e) {
			if (DEBUG)
				System.out.println(e.getMessage());
		}
	}

	public void sendOutput(String strOutput) {
		try {
			strOutput = URLEncoder.encode(strOutput, "ASCII");
			writer.write(strOutput, 0, strOutput.length());
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			if (DEBUG)
				System.out.println("Error writing to " + strHost);
		}
	}

	public String receiveInput() throws exception.AutoException {
		String strInput = null;
		try {
			strInput = reader.readLine();
		} catch (IOException e) {
			throw new exception.AutoException(1006);
		}
		if (strInput != null) {
			try {
				strInput = URLDecoder.decode(strInput, "ASCII");
			} catch (UnsupportedEncodingException e) {
				throw new exception.AutoException(1007);
			}
		}
		return strInput;
	}

	public Iterator<Map.Entry<String, String>> getAutomobileDirectoryIterator() throws exception.AutoException {
		String strOutput = "get automobile directory";
		String fromServer;
		Iterator<Map.Entry<String, String>> mapIterator = null;
		try {
			sendOutput(strOutput);
			fromServer = receiveInput();
			if (fromServer.equals("failed")) {
				throw new exception.AutoException(1000);
			} else {
				model.AutomobileTable.Directory automobileDirectory = fileIOUtil
					.directoryDeserializeFromStream(socketClientInputStream);
				mapIterator = automobileDirectory.map.entrySet().iterator();
			}
		} catch (AutoException e) {
			throw new exception.AutoException(1000);
		}
		return mapIterator;
	}

	public model.Automobile getAutomobile(String automobileKey) throws exception.AutoException {
		String strOutput = "begin customization";
		String fromServer;
		model.Automobile automobileObject = null;
		try {
			sendOutput(strOutput);
			sendOutput(automobileKey);
			fromServer = receiveInput();
			if (fromServer.equals("failed")) {
				throw new exception.AutoException(1005);
			} else {
				automobileObject = fileIOUtil.deserializeFromStream(socketClientInputStream);
			}
		} catch (AutoException e) {
			throw new exception.AutoException(1005);
		}
		return automobileObject;
	}

	public void handleInput(String strInput) {
		strInput = strInput.replace("\\n", "\n");
		System.out.println(strInput);
	}

	public void closeSession() {
		try {
			writer = null;
			reader = null;
			socketClient.close();
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
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		DefaultSocketClient socketClient = new DefaultSocketClient(strLocalHost, iDAYTIME_PORT);
		socketClient.setStandardIn(stdIn);
		socketClient.start();
	}

}