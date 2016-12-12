import java.io.*;
import java.util.concurrent.TimeUnit;
public class Finish
{
	public Finish()
	{
		
	}
	public void work(DataOutputStream output)
	{
		try
		{
			String s = "";
			s+=(char)24;
	        output.writeUTF(s);
	        output.flush();
	        s="";
	        TimeUnit.SECONDS.sleep(1);
	        output.writeUTF("S\r");
	        output.flush();
	        TimeUnit.SECONDS.sleep(1);
	        output.writeUTF("\r");
	        output.flush();
	        TimeUnit.SECONDS.sleep(1);
			s+=(char)3;
	        output.writeUTF(s);
	        output.flush();
	        s="";
		}
		catch(Exception e)
		{
			
		}
	}
}