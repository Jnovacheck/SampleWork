package guiEmailSender;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Open an SMTP connection to a mailserver and send one mail.
 *
<<<<<<< HEAD
 * @author Jeremy Novak, Starter code created by Jussi Kangasharju (see README for more details)
 * @version October 2016
=======
 * @author Jeremy Novak
 * starter can be found at http://www.csc.villanova.edu/~schragge/CSC8560/mailagentprogram.html
>>>>>>> 713d30db9d1e3bdf27589f21f4c1b26a98661184
 */
public class SMTPConnection {
	/* The socket to the server */
	private Socket connection;

	/* Streams for reading and writing the socket */
	private BufferedReader fromServer;
	private DataOutputStream toServer;

	private static final int SMTP_PORT = 25;
	private static final String NL = "\r\n";

	// List of all the reply codes that could occur while sending messaged
	private static final String OPEN = "220";
	private static final String HELO = "250";
	private static final String MAILFROM = "250";
	private static final String RCPTTO = "250";
	private static final String DATA = "354";
	private static final String MESSAGE = "250";
	private static final String QUIT = "221";

	/* Are we connected? Used in close() to determine what to do. */
	private boolean isConnected = false;

	/*
	 * Create an SMTPConnection object. Create the socket and the associated
	 * streams. Initialize SMTP connection.
	 */
	public SMTPConnection(Envelope envelope) throws IOException {
		connection = new Socket(envelope.DestAddr, SMTP_PORT);
		fromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		toServer = new DataOutputStream(connection.getOutputStream());

		/*
		 * Read a line from server and check that the reply code is 220. If not,
		 * throw an IOException.
		 */
		String response = fromServer.readLine();
		if (!response.startsWith(OPEN)) {
			connection.close();
			throw new IOException("220: reply not received from server.");

		}

		/*
		 * SMTP handshake. We need the name of the local machine. Send the
		 * appropriate SMTP handshake command.
		 */
		String localhost = "Jeremy";
		sendCommand("HELO " + localhost + NL, HELO);

		isConnected = true;
	}// STMPConnection(Envelope)

	/**
	 * Send the message. Write the correct SMTP-commands in the correct order.
	 * No checking for errors, just throw them to the caller
	 * 
	 * @param envelope
	 *            the details of the message, as well as it's header details
	 * @throws IOException Throws an exception if the reply form the server is not the expected reply code
	 */
	public void send(Envelope envelope) throws IOException {
		sendCommand("MAIL FROM: <" + envelope.Sender + ">" + NL, MAILFROM);
		sendCommand("RCPT TO: <" + envelope.Recipient + ">" + NL, RCPTTO);
		sendCommand("DATA" + NL, DATA);
		sendCommand(envelope.Message + NL + "." + NL, MESSAGE);
		return;
	}

	/**
	 * Close the connection. First, terminate on SMTP level, then close the
	 * socket.
	 * 
	 */
	public void close() {
		isConnected = false;
		try {
			sendCommand("QUIT" + NL, QUIT);
			connection.close();
		} catch (IOException e) {
			System.out.println("Unable to close connection: " + e);
			isConnected = true;
		}
	}// close()

	/**
	 * Send an SMTP command to the server. Check that the reply code is what is
	 * is supposed to be according to RFC 821.
	 * 
	 * @param command
	 *            Command is the information that needs to be sent to the Socket
	 * @param rc
	 *            A sting that represents the expected Reply Code from the
	 *            Socket
	 * @throws IOException Throws an exception if the reply form the server is not the expected reply code
	 */
	private void sendCommand(String command, String rc) throws IOException {
		/* Write command to server and read reply from server. */
		toServer.write(command.getBytes("US-ASCII"));
		String response = fromServer.readLine();

		/*
		 * Check that the server's reply code is the same as the parameter rc.
		 * If not, throw an IOException.
		 */
		if (!response.startsWith(rc)) {
			System.out.println(rc + " something when wrong with a command: " + command);
			System.out.println(response);
			close();
			throw new IOException("ERROR: " + rc + " has occured.");
		}
	}// sendCommand(String, String)

	/**
	 * Destructor. Closes the connection if something bad happens.
	 * 
	 * 
	 */
	protected void finalize() throws Throwable {
		if (isConnected) {
			close();
		}
		super.finalize();
	}// finalize()
}// STMPConection Class

