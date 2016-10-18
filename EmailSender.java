import java.io.*;
import java.net.*;

public class EmailSender
{
   public static void main(String[] args) throws Exception
   {
      // Establish a TCP connection with the mail server.
	Socket socket = new Socket( "aspmx.l.google.com", 25);

      // Create a BufferedReader to read a line at a time.
      InputStream is = socket.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);

      // Read greeting from the server.
      String response = br.readLine();
      //System.out.println(response);
      if (!response.startsWith("220")) {
    	 socket.close();
         throw new Exception("220 reply not received from server.");
         
      }

      // Get a reference to the socket's output stream.
      OutputStream os = socket.getOutputStream();

      // Enter HELO command and get server response.
      String command = "HELO alice\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      //System.out.println(response);
      if (!response.startsWith("250")) {
    	 socket.close();
         throw new Exception("250 reply not received from server.");
      }

      // Enter MAIL FROM command.
      command = "Mail From: <LTabak@cornellcollege.edu>\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      //System.out.println(response);
      if (!response.startsWith("250")) {
    	 socket.close();
         throw new Exception("250 reply not received from server.");
      }

      // Enter RCPT TO command.
      command = "RCPT TO: <Jnovak17@cornellcollege.edu>\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      //System.out.println(response);
      if (!response.startsWith("250")) {
    	 socket.close();
         throw new Exception("250 reply not received from server.");
      }

      // Enter DATA command.
      command = "DATA\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      //System.out.println(response);
      if (!response.startsWith("354")) {
    	 socket.close();
         throw new Exception("354 DATA no-go.");
      }

      // Send message data, and end the message with a period.
      command = "SUBJECT: Just a reminder\r\n"
      			+ "FROM: Ltabak@cornellcollege.edu\r\n"
      			+ "TO: Jnovak17@cornellcollege.edu\r\n"
      			+ "\n"
      			+ "You are a great student!\r\n"
      			+ ".\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      //System.out.println(response);
      if (!response.startsWith("250")) {
    	  socket.close();
         throw new Exception("250 reply not received from server.");
      }

      // Enter QUIT command.
      command = "QUIT\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      //System.out.println(response);
      
      socket.close();
   }//Main
   
   
   
}//Program








