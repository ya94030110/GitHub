package bin;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;
import bin.Packet;

public class Client {
	private Socket s;
	private ObjectInputStream in; 
	private ObjectOutputStream out;
	private boolean flag = true;
	private BufferedReader br = null;
	private static InetAddress serverIP = null;
	private static int serverPort = 0;
	
	public static void main(String[] args) {
		try{
			serverIP = InetAddress.getByName(args[0]);
			serverPort = Integer.parseInt(args[1]);
		}
		catch(UnknownHostException e)
		{
			
		}
		new Client().stratUp();
	}

	private void stratUp() {
		try {
			s = new Socket(serverIP, serverPort);
			//System.out.println("socket ok");
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			br = new BufferedReader(new InputStreamReader(System.in));
			String name = Login();
			Packet pack = new Packet();
			pack.topic = "online";
			pack.sender = name;
			out.writeObject(pack);
			new Thread(new ClientThread()).start();
			String str = null;
			while (flag && (str=br.readLine())!=null) {
				if (!flag) break;
				handleInput(str);
			}
		} 
		catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		Packet pack = new Packet();
		pack.topic = "quit";
		try{
			out.writeObject(pack);
		}
		catch(IOException e)
		{
			
		}
	}
	
	private String Login()
	{
		int login_flag = 0;
		String name = null;
		while(login_flag == 0)
		{
			try{
				System.out.print("請輸入帳號(或輸入new註冊新帳號):");
				name = br.readLine();
				if(name.equals("new"))
				{
					name = Register();
					login_flag = 1;
					continue;
				}
				Packet accpack = new Packet();
				accpack.topic = "account";
				accpack.message = name;
				out.writeObject(accpack);
				accpack = (Packet)in.readObject();
				if((accpack.message).equals("no account"))
				{
					System.out.println("無此帳號，請重試一次!");
					continue;
				}
				System.out.print("請輸入密碼:");
				Packet passpack = new Packet();
				passpack.topic = "password";
				passpack.message = br.readLine();
				out.writeObject(passpack);
				accpack = (Packet)in.readObject();
				if((accpack.message).equals("password error"))
				{
					System.out.println("密碼錯誤，請重試一次!");
					continue;
				}
				if((accpack.message).equals("online"))
				{
					System.out.println("此帳號已在線上，請重試一次!");
					continue;
				}
				login_flag = 1;
			}
			catch(ClassNotFoundException e)
			{
				
			}
			catch(IOException e)
			{
				
			}
			
		}
		return name;
		
	}
	
	private String Register()
	{
		int register_flag = 0;
		String name = null;
		
		try {
			while(register_flag ==0) {
				System.out.print("請輸入新帳號:");
				name = br.readLine();
				System.out.print("請輸入新密碼:");
				String password = br.readLine();
				Packet regpack = new Packet();
				regpack.topic = "register";
				regpack.message = name + " " + password;
				out.writeObject(regpack);
				Packet pack = (Packet)in.readObject();
				if((pack.message).equals("existed"))
				{
					System.out.println("此帳號已被使用，請重試一次!");
					continue;
				}
				register_flag = 1;
			}
			
			
		}
		catch(IOException e)
		{
			
		}
		catch(ClassNotFoundException e)
		{
			
		}
		return name;
	}
	
	private String getFilename(String path)
	{
		int n1 = path.lastIndexOf('/');
		int n2 = path.lastIndexOf('\\');
		int n = n1 > n2? n1 : n2;
		return path.substring(n + 1, path.length());
	}
	
	private void handleInput(String input)
	{
		String[] splited = input.split(" ");
		int length = splited.length;
		if(splited[0].equals("quit"))
		{
			Packet pack = new Packet();
			pack.topic = "quit";
			try{
				out.writeObject(pack);
			}
			catch(IOException e)
			{
				
			}
		}
		else if(splited[0].equals("message"))
		{
			String msg = null;
			String option = null;
			option = splited[1];
			if(option.equals("-u"))
			{
				msg = splited[3];
				for(int i = 4; i < length; i++) msg = msg + " " + splited[i];
				Packet pack = new Packet();
				pack.topic = "message";
				pack.message = msg;
				pack.receiver = splited[2];
				try{
					out.writeObject(pack);
				}
				catch(IOException e)
				{
					
				}
			}
			else if(option.equals("-b"))
			{
				msg = splited[2];
				for(int i = 3; i < length; i++) msg = msg + " " + splited[i];
				Packet pack = new Packet();
				pack.topic = "message";
				pack.message = msg;
				pack.receiver = "all";
				try{
					out.writeObject(pack);
				}
				catch(IOException e)
				{
					
				}
			}
			else System.out.println("使用方法：message [-u 接收者 or -b] [message]");
			
		}
		
		else if(splited[0].equals("send"))
		{
			String msg = null;
			String option = null;
			option = splited[1];
			if(option.equals("-u"))
			{
				String filepath = splited[3];
				FileInputStream fi;
				try {
					fi = new FileInputStream(filepath);
				}
				catch(FileNotFoundException e)
				{
					System.out.println("檔案不存在!");
					return;
				}
				try{
					Packet pack = new Packet(fi.available());
					fi.read(pack.filedata);
					fi.close();
					pack.receiver = splited[2];
					pack.filename = getFilename(filepath);
					final Packet pack2 = pack;
					new Thread(new Runnable(){ 
						public void run(){
							try {
								out.writeObject(pack2);
							}
							catch(IOException e)
							{
								
							}
						}
					}).start();
					
				}
				catch(IOException e)
				{
					
				}
			}
			else if(option.equals("-b"))
			{
				String filepath = splited[2];
				FileInputStream fi;
				try {
					fi = new FileInputStream(filepath);
				}
				catch(FileNotFoundException e)
				{
					System.out.println("檔案不存在!");
					return;
				}
				
				try{
					Packet pack = new Packet(fi.available());
					fi.read(pack.filedata);
					pack.receiver = "all";
					pack.filename = getFilename(filepath);
					final Packet pack2 = pack;
					new Thread(new Runnable(){ 
						public void run(){
							try {
								out.writeObject(pack2);
							}
							catch(IOException e)
							{
								
							}
						}
					}).start();
				}
				catch(IOException e)
				{
					
				}
			}
			else System.out.println("使用方法：file [-u 接收者 or -b] [filepath]");
			
		}
		
		else if(splited[0].equals("help"))
		{
			System.out.println("message [-u 接收者 ] [message]: 傳送訊息message給接收者");
			System.out.println("message [-b] [message]: 傳送訊息message給所有使用者");
			System.out.println("file [-u 接收者] [filepath] :傳送在filepath的檔案給接收者");
			System.out.println("file [-b] [filepath] :傳送在filepath的檔案給所有在線上的使用者");
			System.out.println("quit :關閉client");
		}
		else
		{
			System.out.println("未知的指令，請輸入help尋找可用指令");
		}
	}
	
	private void receive() {
		try {
			Packet pack = (Packet) in.readObject();
			if ((pack.topic).equals("disconnect"))
			{
				flag = false;
				System.out.println("按下Enter繼續...");
				try {
					if (s != null) s.close();
					if (in != null) in.close();
					if (out != null) out.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if((pack.topic).equals("message"))
			{
				System.out.println(pack.message);
			}
			if((pack.topic).equals("file"))
			{
				FileOutputStream fo = new FileOutputStream("file/" + pack.filename, false);
				fo.write(pack.filedata);
				fo.close();
				System.out.println(pack.message);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			
		}
	}
	
	private class ClientThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				if (!flag) break;
				receive();
			}
		}
		
	}

}