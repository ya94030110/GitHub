import java.io.*;
import java.net.*;
public class PTTPoster
{
    public static void main( String[] args ) throws IOException
    {
        String host = "140.112.172.11";
        int port = 23;
        Socket socket = null;
        try
        {
            DataInputStream input = null;
            DataOutputStream output = null;
            Login login = new Login();
            EnterBoard enterboard = new EnterBoard();
            SetTitle settitle = new SetTitle();
            EnterContent entercontent = new EnterContent();
            Finish finish = new Finish();
            try
            {
            	socket = new Socket( host, port );
                input = new DataInputStream( socket.getInputStream() );
                output = new DataOutputStream( socket.getOutputStream() );
                login.work(output, "ya94030110", "AA0857");
    	        enterboard.work(output, "Test");
    	        settitle.work(output, "Homework Test");
    	        entercontent.work(output, "YEEEEE");
    	        finish.work(output);
    	        input.close();
    	        output.close();
    	        socket.close();
    	        
    	        socket = new Socket( host, port );
                input = new DataInputStream( socket.getInputStream() );
                output = new DataOutputStream( socket.getOutputStream() );
    	        login.work(output, "p0208", "AA0857");
    	        enterboard.work(output, "Test");
    	        settitle.work(output, "Homework Test2");
    	        entercontent.work(output, "Big Platform");
    	        finish.work(output);
            }
            catch ( Exception e )
            {
            }
            finally 
            {
                if ( input != null )
                    input.close();
                if ( output != null )
                    output.close();
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        finally
        {
            if ( socket != null )
                socket.close();
        }
    }
}