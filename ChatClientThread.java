import java.net.*;
import java.io.*;

// WHen a client is created, it gets its own thread
// anything that leaves ChatClient goes through here and aything that leavs server and goes to chatclient must also go tthrough here
public class ChatClientThread extends Thread
{  private Socket           socket   = null;
   private ChatClient       client   = null;
   private DataInputStream  streamIn = null;
   

   public ChatClientThread(ChatClient _client, Socket _socket)
   {  client   = _client;
      socket   = _socket;
      open(); // goes to open() and gets client input and comes back then goes to ChatClient.start()
      start();
   }
   public void open()
   {  try
      {
		  streamIn  = new DataInputStream(socket.getInputStream());
      //streamIn2 = new DataInputStream(socket.getInputStream());
      }
      catch(IOException ioe)
      {
		 System.out.println("Error getting input stream: " + ioe);
         client.stop();
      }
   }
   public void close()
   {  try
      {  if (streamIn != null) streamIn.close();
      }
      catch(IOException ioe)
      {  System.out.println("Error closing input stream: " + ioe);
      }
   }

   public void run() // when running - waiting for .bye to be entered - if so it runs .bye from the ChatClient.java
   {
	   while (true && client!= null){
		  try {

			  client.handle(streamIn.readUTF());
          }
          catch(IOException ioe)
          {
			  client = null;
			  System.out.println("Listening error: " + ioe.getMessage());

         }
      }
   }
}
