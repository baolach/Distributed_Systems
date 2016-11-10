import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;


// console was renamed to newbid

// todo - clients are not communicating wiht each other anymore
// maybe the variables have to be stroed in ChatClientThread????


public class ChatClient implements Runnable
{  private Socket socket              = null;
   private Thread thread              = null;
   private BufferedReader  newbid   = null;
   private DataOutputStream streamOut = null;
   private ChatClientThread client    = null;
   private String chatName;
   public String[] items = { "clock", "laptop", "vase", "car", "holiday"};
   public int[] price = { 100, 500, 300, 15000, 1000};
   public String[] temp;// = { "100", "500", "20" };
   public long startTime;
   public long elapsedTime;
   public boolean sold ;




   public ChatClient(String serverName, int serverPort)
   {
	  System.out.println("... Establishing connection. Please wait ...");


    try{

      System.out.println("Enter name");
      // takes input from the keyboard and attaches to the client batfile
      Scanner input = new Scanner(System.in);
      this.chatName = input.nextLine();
		  socket = new Socket(serverName, serverPort);
         System.out.println("... Connected. You can now bid in the auction ...");// + socket);
         start();

    } catch(UnknownHostException uhe){
    		      System.out.println("Host unknown: " + uhe.getMessage());
    	  }
          catch(IOException ioe){
    		  System.out.println("Unexpected exception: " + ioe.getMessage());
    	  }



  } // end public chatClient


   public void run() // when a client enters something this is shown to all other clients
   {
	   while (thread != null){
		 try {
            for(int i=0; i<items.length ; i++)
            {
              sold = false;
              System.out.println("... The item on auction is: " + items[i]);
              System.out.println("... The bidding starts at: " + price[i]);
              //System.out.printf("sold is t/f?" + sold);

              //System.out.println("... The timer has begun! You have 5 seconds to bid");




              while(sold == false) // this occurs for each item - goes until connection is closed and item is sold // 5000ms is 5 seconds
              {

                //System.out.printf("sold is t/f?" + sold);

                System.out.printf("bid: ");
                String message = newbid.readLine(); // user input each time
                int temp = Integer.parseInt(message); // temp used to convert to integer
System.out.println("test" + temp);

                if(temp>price[i])
                {
                     price[i] = temp;
                     System.out.println("new price:" +price[i]);
                     streamOut.writeUTF(message); //"new price is:" + price[i]
                     streamOut.flush();

                      // startTime =System.currentTimeMillis();
                      // elapsedTime=0L;
                      //
                      // elapsedTime = (new Date()).getTime() - startTime;


                      // if the timer has run out && there has been a user input
                      // if((price[i] != 100))// && (elapsedtime>5000))
                      // {
                      //   System.out.println( items[i] + " has been sold for " + price[i]);
                      //   sold = true;
                      //   break; // breaks out and moves onto next item
                      // }


                }
                else{
                  System.out.println("bid must be higher than previous bid");
                }


              //  System.out.println("Test to see what happens when leaves for loop");



              } // end innerfor loop

            } // outter for



       } // end try
         catch(IOException ioe)
         {  System.out.println("Sending error: " + ioe.getMessage());
            stop();
         }
      }
   }

   public void handle(String msg) // this isn't working for some reason
   {  if (msg.equals(".bye"))
      {  System.out.println("... You have left the auction. Press ENTER to close connection ...");
         stop();
      }
      else
         System.out.println(msg);
   }



// gets thread from a client- sends to chatClientThread and it comes back
   public void start() throws IOException
   {
	  newbid = new BufferedReader(new InputStreamReader(System.in));

      streamOut = new DataOutputStream(socket.getOutputStream());
      if (thread == null)
      {  client = new ChatClientThread(this, socket);
         thread = new Thread(this);
         thread.start();
      }
   }

   public void stop()
   {
      try
      {  if (newbid   != null)  newbid.close();
         if (streamOut != null)  streamOut.close();
         if (socket    != null)  socket.close();
      }
      catch(IOException ioe)
      {
		  System.out.println("Error closing ...");

      }
      client.close();
      thread = null;
   }


   public static void main(String args[])
   {  ChatClient client = null;
      if (args.length != 2)
         System.out.println("Error. Input must be in the following notation: java ChatClient host port name");
      else
         client = new ChatClient(args[0], Integer.parseInt(args[1]));
   }
} // end class


/*
This was in the ChatCLient method at the top
Instantiate the BufferedReader, using the file named 'myfile'.
    fromFile = new BufferedReader(new FileReader("myfile"));

    // Read a single line from the file.
    String lineOfText = fromFile.readLine();

    // Loop through all the lines of text until we reach the end of the file.
    // At the end of the file, readLine() will simply return null.
    for(int i = 1; lineOfText != null; i++) {
      System.out.println(lineOfText); // System.out.println("LINE " + i + ": " + lineOfText);

      lineOfText = fromFile.readLine();
    }

  } catch (IOException ioe) {
    System.out.println("Problem with IO: " + ioe.getMessage());
  }
}

public static void main(String args[]) {
  new Reader();
}

*/
///////////////////
