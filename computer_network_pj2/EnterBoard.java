import java.io.*;
import java.util.concurrent.TimeUnit;
public class EnterBoard
{
	public EnterBoard()
	{
		
	}
	public void work(DataOutputStream output, String BoardName)
	{
		try
		{
			String s = "";
			output.writeUTF("s");
	        output.flush();
	        TimeUnit.SECONDS.sleep(1);
	        output.writeUTF(BoardName + "\r");
	        output.flush();
	        TimeUnit.SECONDS.sleep(1);
	        s+=(char)3;
	        output.writeUTF(s);
	        output.flush();
	        s="";
	        TimeUnit.SECONDS.sleep(1);
		}
		catch(Exception e)
		{
			
		}
	}
}