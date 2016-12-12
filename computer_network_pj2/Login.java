import java.io.*;
import java.util.concurrent.TimeUnit;
public class Login
{
	public Login()
	{
		
	}
	public void work(DataOutputStream output, String ID, String Password)
	{
		try
		{
			output.writeUTF(ID + "\r");
			output.flush();
			TimeUnit.SECONDS.sleep(1);
			output.writeUTF(Password + "\r");
			output.flush();
			TimeUnit.SECONDS.sleep(1);
			String s = "";
			s+=(char)3;
	        output.writeUTF(s);
	        output.flush();
	        TimeUnit.SECONDS.sleep(1);
		}
		catch(Exception e)
		{
			
		}
	}
}