package bin;
import java.io.Serializable;

public class Message implements Serializable
{
	String message;
	String receiver;
	
	public Message()
	{
		message = null;
		receiver = null;
	}
}
	