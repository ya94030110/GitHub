package bin;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import bin.Packet;
import bin.Message;

public class Server {
	private List<ServerThread> clients = null;
	private List<String> online = new ArrayList<String>();
	private Map<String, String> accpass=new HashMap<String, String>();
	private ArrayList<Message> msgHistory = new ArrayList<Message>();
	private Map<String, Integer> readmsg = new HashMap<String, Integer>();
	private Map<String, ServerThread> stmap = new HashMap<String, ServerThread>();
	
	public static void main(String[] args) {
		new Server().startUp();
	}
	
	private void startUp() {
		ServerSocket ss = null;
		Socket s = null;
		try {
			ss = new ServerSocket(5858);
			clients = new ArrayList<ServerThread>();
			
			FileReader fr = new FileReader("bin/acc-pass.txt");
			BufferedReader fbr = new BufferedReader(fr);
			String str;
			while((str = fbr.readLine()) != null)
			{
				String[] splited = str.split(" ");
				accpass.put(splited[0], splited[1]);
				//System.out.println(splited[0] + " " + splited[1]);
			}
			fbr.close();
			
			fr = new FileReader("bin/msg_history.txt");
			fbr = new BufferedReader(fr);
			while((str = fbr.readLine()) != null)
			{
				String[] splited = str.split(" ");
				Message msg = new Message();
				msg.receiver = splited[1];
				msg.message = splited[0];
				msgHistory.add(msg);
				//System.out.println(splited[0] + " " + splited[1]);
			}
			fbr.close();
			
			fr = new FileReader("bin/readmsg.txt");
			fbr = new BufferedReader(fr);
			while((str = fbr.readLine()) != null)
			{
				String[] splited = str.split(" ");
				readmsg.put(splited[0], Integer.parseInt(splited[1]));
				//System.out.println(splited[0] + " " + readmsg.get(splited[0]));
			}
			fbr.close();
			
			while (true) {
				s = ss.accept();
				ServerThread st = new ServerThread(s);
				new Thread(st).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ss != null) 
					ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void send_all(Packet pack) {
		try{
			for (ServerThread st : clients)
				st.out.writeObject(pack);
		}
		catch(IOException e)
		{
			
		}
	}
	
	private void send_to(ObjectOutputStream out, Packet pack, String receiver)
	{
		try {
			if(accpass.get(receiver) == null)
			{
				Packet pack2 = new Packet();
				pack2.topic = "message";
				pack2.message = "用戶" + receiver +  "不存在";
				out.writeObject(pack2);
			}
			else
			{
				ServerThread st = stmap.get(receiver);
				if(st != null) st.out.writeObject(pack);
				else
				{
					if((pack.topic).equals("file"))
					{
						Packet pack2 = new Packet();
						pack2.topic = "message";
						pack2.message = "用戶" + receiver +  "不在線上";
						out.writeObject(pack2);
					}
				}
			}
		}
		catch(IOException e)
		{
			
		}
		
	}
	
	
	private class ServerThread implements Runnable {
		private Socket s = null;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private String name;
		private boolean flag = true;
		
		public ServerThread(Socket socket) throws IOException {
			this.s = socket;
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		
		private void receive() throws IOException {
			Packet pack = null;
			String account_tmp = null;
			try{
				while ((pack = (Packet)in.readObject()) != null)
				{
					if((pack.topic).equals("message"))
					{
						//System.out.println("message");
						pack.message = name + ":" + pack.message;
						if((pack.receiver).equals("all")) send_all(pack);
						else send_to(out, pack, pack.receiver);
						for(String tmp : online)
						{
							int readnum = readmsg.remove(tmp);
							readmsg.put(tmp, readnum + 1);
						}
						Message msg = new Message();
						msg.message = pack.message;
						msg.receiver = pack.receiver;
						msgHistory.add(msg);
						FileWriter fw = new FileWriter("bin/msg_history.txt", true);
						BufferedWriter wbr = new BufferedWriter(fw);
						wbr.write(pack.message + " " + pack.receiver);
						wbr.newLine();
						wbr.close();
						
					}
					
					if((pack.topic).equals("file"))
					{
						//System.out.println("message");
						pack.message = name + "傳送了一個檔案" + pack.filename;
						final Packet pack2 = pack;
						if((pack.receiver).equals("all"))
						{
							new Thread(new Runnable(){ 
								public void run(){
									send_all(pack2);
								}
							}).start();
						}
						else
						{
							new Thread(new Runnable(){ 
								public void run(){
									send_to(out, pack2, pack2.receiver);
								}
							}).start();
						}
						
					}
					
					if ((pack.topic).equals("quit")) {
						//System.out.println("quit");
						Packet pack2 = new Packet();
						pack2.topic = "disconnect";
						stop(name);
						out.writeObject(pack2);
						break;
					}
					if((pack.topic).equals("online"))
					{
						//System.out.println("online");
						String str = pack.sender;
						System.out.println(str+"["+s.getInetAddress().getHostAddress()+":"+s.getPort()+"]"+"已上線");
						name = str;
						clients.add(this);
						online.add(name);
						stmap.put(name, this);
						pack = new Packet();
						pack.topic = "message";
						pack.message = name+"已上線";
						send_all(pack);
						//System.out.println(readmsg.get(name));
						try {
							pack = new Packet();
							pack.topic = "message";
							pack.message = "尚未看的訊息:";
							out.writeObject(pack);
						}
						catch(IOException e)
						{
								
						}
						for(int i = readmsg.get(name); i < msgHistory.size(); i++)
						{
							try {
								if(((msgHistory.get(i)).receiver).equals("all") || ((msgHistory.get(i)).receiver).equals(name))
								{
									pack = new Packet();
									pack.topic = "message";
									pack.message = (msgHistory.get(i)).message;
									out.writeObject(pack);
								}
							}
							catch(IOException e)
							{
								
							}
						}
						readmsg.remove(name);
						readmsg.put(name, msgHistory.size());
						writefile(readmsg);
					}
					if((pack.topic).equals("account"))
					{
						String account = pack.message;
						if(accpass.get(account) == null)
						{
							pack.message = "no account";
						}
						else account_tmp = account;
						out.writeObject(pack);
					}
					if((pack.topic).equals("password"))
					{
						String password = pack.message;
						if((accpass.get(account_tmp)).equals(password))
						{
							if(online.contains(account_tmp)) pack.message = "online";
						}
						else pack.message = "password error";
						out.writeObject(pack);
					}
					if((pack.topic).equals("register"))
					{
						String[] splited = (pack.message).split(" ");
						FileWriter fw = new FileWriter("bin/acc-pass.txt", true);
						if(accpass.get(splited[0]) != null || splited[0].equals("new") || splited[0].equals("all")) pack.message = "existed";
						else
						{
							accpass.put(splited[0], splited[1]);
							readmsg.put(splited[0], msgHistory.size());
							writefile(readmsg);
							BufferedWriter wbr = new BufferedWriter(fw);
							wbr.write(pack.message);
							wbr.newLine();
							wbr.close();
						}
						out.writeObject(pack);
					}
				
				}
				
			}
			catch(ClassNotFoundException e)
			{
				
			}
			catch(EOFException e)
			{
				Packet pack2 = new Packet();
				pack2.topic = "disconnect";
				stop(name);
				out.writeObject(pack2);
			}
		}
		
		private void writefile(Map<String, Integer> map)
		{
			try {
				FileWriter fw = new FileWriter("bin/readmsg.txt", false);
				BufferedWriter wbr = new BufferedWriter(fw);
				for (Map.Entry<String,Integer> entry:map.entrySet())
				{
					wbr.write(entry.getKey() + " " + entry.getValue());
					wbr.newLine();
				}
				wbr.close();
			}
			catch(IOException e)
			{
				
			}
		}
		
		private void stop(String name) {
			clients.remove(this);
			online.remove(name);
			stmap.remove(name);
			flag = false;
			if(name != null)
			{
				System.out.println(name+"["+s.getInetAddress().getHostAddress()+":"+s.getPort()+"]"+"已下線");
				Packet pack = new Packet();
				pack.topic = "message";
				pack.message = name+"已下線";
				send_all(pack);
			}
		}

		@Override
		public void run() {
			try {
				while (true) {
					if (!flag) break;
					receive();
				}
			} catch (SocketException e) {
				stop(name);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (s != null) 
						s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
}