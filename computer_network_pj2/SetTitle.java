import java.io.*;
import java.util.concurrent.TimeUnit;
public class SetTitle
{
	public SetTitle()
	{
		
	}
	public void work(DataOutputStream output, String TitleName)
	{
		try
		{
			String s = "";
			s+=(char)16;
	        output.writeUTF(s);
	        output.flush();
	        TimeUnit.SECONDS.sleep(1);
	        output.writeUTF("\r");
	        output.flush();
	        TimeUnit.SECONDS.sleep(1);
	        output.writeUTF(TitleName + "\r");
	        output.flush();
	        TimeUnit.SECONDS.sleep(1);
		}
		catch(Exception e)
		{
			
		}
	}
}