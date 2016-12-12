import java.io.*;
import java.util.concurrent.TimeUnit;
import java.net.*;
public class project2
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
            BufferedReader br = new BufferedReader(new FileReader("P_input.txt"));
            Login login = new Login();
            EnterBoard enterboard = new EnterBoard();
            SetTitle settitle = new SetTitle();
            EnterContent entercontent = new EnterContent();
            Finish finish = new Finish();
            int OpenTag = 0, get_acc = 0, a = 0;
            String CloseTag = "", Tag = "", account = "", password, in, tmp;
            try
            {
            	//socket = new Socket( host, port );
                //input = new DataInputStream( socket.getInputStream() );
                //output = new DataOutputStream( socket.getOutputStream() );
    	        //input.close();
    	        //output.close();
    	        //socket.close();
            	while((in = br.readLine()) !=null)
            	{
            		int m = in.length();
            		int i;
            		tmp = "";
            		for (i = 0; i < m; i++)
            		{
            			if(OpenTag == 0)
            			{
            				if(in.charAt(i) != '<') continue;
            				i++;
            				Tag = "";
            				CloseTag = "";
            				CloseTag += "</";
            				for( ; in.charAt(i) != '>'; i++)
            				{
            					//System.out.println("Tag += " + in.substring(i, i+1));
            					Tag += in.charAt(i);
            					CloseTag += in.charAt(i);
            				}
            				CloseTag += ">";
            				OpenTag = 1;
            				//System.out.println("Tag: " + Tag);
            				if(Tag.equals("EXIT"))
            				{
            					input.close();
            	    	        output.close();
            	    	        socket.close();
            	    	        
            	    	        OpenTag = 0;
            				}
            				continue;
            			}
            			else
            			{
            				if(in.charAt(i) == '<' && in.charAt(i+1) == '/')
            				{
            					int n = CloseTag.length();
            					if(CloseTag.equals(in.substring(i, i+n)))
            					{
            						//if(!Tag.equals("CONTENT")) System.out.println("content: " + tmp);
            						OpenTag = 0;
            						if(Tag.equals("BOARD")) enterboard.work(output, tmp);
            						if(Tag.equals("P")) settitle.work(output, tmp);
            						if(Tag.equals("CONTENT"))
            						{
            							entercontent.work(output, tmp);
            							finish.work(output);
            						}
            						if(Tag.equals("ID"))
            						{
            							account = tmp;
            							get_acc = 1;
            						}
            						if(Tag.equals("PASS"))
            						{
            							socket = new Socket( host, port );
            			                input = new DataInputStream( socket.getInputStream() );
            			                output = new DataOutputStream( socket.getOutputStream() );
            			                
            			                password = tmp;
            			                login.work(output, account, password);
            						}
            						tmp = "";
            						i += n;
            					}
            				}
            				else
            				{
            					
            					tmp += in.charAt(i);
            					
            				}
            			}
            		}
            		if(OpenTag == 1)
            		{
            			//System.out.println("content: " + tmp);
            			entercontent.work(output, tmp);
            		}
            	}
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