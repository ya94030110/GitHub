import java.io.*;
import java.util.concurrent.TimeUnit;
public class EnterContent
{
	public EnterContent()
	{
		
	}
	public void work(DataOutputStream output, String Content)
	{
		try
		{
				output.writeUTF(Content + "\r");
				output.flush();
		        TimeUnit.SECONDS.sleep(1);
		}
		catch(Exception e)
		{
			
		}
	}
}