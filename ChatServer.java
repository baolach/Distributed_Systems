import java.net.*;
import java.io.*;

public class ChatServer implements Runnable
{

   // Array of clients
   private ChatServerThread clients[] = new ChatServerThread[50];
   private ServerSocket server = null;
   private Thread       thread = null;
   private int clientCount = 0;
   public String[] items = { "clock", "laptop", "vase", "car", "holiday"};
   public int[] price = { 100, 500, 300, 15000, 1000};
   public String[] temp;// = { "100", "500", "20" };

   public ChatServer(int port)
   {
	  try {

		 System.out.println("... Binding to port " + port + ", please wait  ...");
         server = new ServerSocket(port);
         System.out.println("Sucessfully connected to Server on: " + server.getInetAddress()); // getInetAddress returns the IP address of the remote client
         start(); // starts ChatServer.start() to check if thread is null // if not it goes to run
      }
      catch(IOException ioe)
      {
		  System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());

      }
   }


   public void run()
   {
	  while (thread != null)
      {
		 try{

			System.out.println("... Waiting for bidders to join ...");
      addThread(server.accept());

			int pause = (int)(Math.random()*3000);
			Thread.sleep(pause);

    }
    catch(IOException ioe)
    {
      System.out.println("Server accept error: " + ioe);
			stop();
    }
    catch (InterruptedException e){
		 	  System.out.println(e);
		 }
   } // end while
 } // end run


 public void start() // if thread not null, go to run()
   {
  if (thread == null) {
    thread = new Thread(this);
         thread.start();
      }
   }

   public void stop(){
	   thread = null;

   }

   private int findClient(int ID)
   {
	   for (int i = 0; i < clientCount; i++)
         if (clients[i].getID() == ID)
            return i;
      return -1;
   }


//////////////////// This isnt being accessed
   public synchronized void broadcast(int ID, String input)
   {

     System.out.println("User inputted: " + input);
	   if (input.equals(".bye")){
		     clients[findClient(ID)].send(".bye");
         remove(ID);
      }
      else{
        System.out.println("TEST: User message will broadcast");
         for (int i = 0; i < clientCount; i++)
         {
           System.out.println("TEST: inside for loop");
			        if(clients[i].getID() != ID)
              {
              /////////////////// how can I change this to the name rather than the ID? to make it better
            	clients[i].send(ID + ": " + input); // Client ID: + client message goes to all other clients
              }
		      }
       }
       notifyAll();
   }
   public synchronized void remove(int ID)
   {
	  int pos = findClient(ID);
      if (pos >= 0){
		 ChatServerThread toTerminate = clients[pos];
         System.out.println("Removing client thread " + ID + " at " + pos);

         if (pos < clientCount-1)
            for (int i = pos+1; i < clientCount; i++)
               clients[i-1] = clients[i];
         clientCount--;

         try{
			 toTerminate.close();
	     }
         catch(IOException ioe)
         {
			 System.out.println("Error closing thread: " + ioe);
		 }
		 toTerminate = null;
		 System.out.println("Client " + pos + " removed");
		 notifyAll();
      }
   }

   private void addThread(Socket socket)
   {
	  if (clientCount < clients.length){

		 System.out.println("Bidder accepted: " + socket);
         clients[clientCount] = new ChatServerThread(this, socket);
         try{
			clients[clientCount].open();
            clients[clientCount].start();
            clientCount++;
         }
         catch(IOException ioe){
			 System.out.println("Error opening thread: " + ioe);
		  }
	  }
      else
         System.out.println("Client refused: maximum " + clients.length + " reached.");
   }


   public static void main(String args[]) {
	   ChatServer server = null;
      if (args.length != 1)
         System.out.println("Usage: java ChatServer port");
      else
         server = new ChatServer(Integer.parseInt(args[0]));
   }

}
