package bin;
import java.util.ArrayList;
import java.io.Serializable;

public class Packet implements Serializable
{
	String topic;
	String message;
	byte[] filedata;
	int filelength;
	String sender;
	String receiver;
	String filename;
	
	public Packet()
	{
		topic = null;
		message = null;
		sender = null;
		receiver = null;
		filelength = 0;
		filedata = new byte[0];
		filename = null;
	}
	
	public Packet(int length)
	{
		topic = "file";
		message = null;
		sender = null;
		receiver = null;
		filelength = length;
		filedata = new byte[length];
		filename = null;
	}
}
