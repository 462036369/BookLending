package bookLendingClient.model;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client {
	//客户端套接字
	private Socket socket;
	private static Client instance = new Client();
	private DataOutputStream dos;
	private DataInputStream dis; 
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	//构造方法私有化
	private Client() {
		try {
			this.socket = new Socket("localhost",8080);
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	//取得本类唯一对象
	public static Client getInstance() {
		return instance;
	}
	/**
	 * 向服务器发送数据
	 * @param msg 要发送的字符串
	 */
	public void send(String msg) {
		//通过io发送数据
		try {
			dos = new DataOutputStream(this.socket.getOutputStream());
			dos.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void send(Object obj) {
		try {
			oos = new ObjectOutputStream(this.socket.getOutputStream());
			oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public Object receiveObject() {
		try {
			this.ois = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
			return ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 从服务器接收数据
	 * @return 接受数据的字符串表示
	 */
	public String receive() {
		//同样io
		try {
			dis = new DataInputStream(this.socket.getInputStream());
			String msg = dis.readUTF();
			//dis.close();
			return msg;
		} catch (IOException e) {
			//异常处理
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 关闭套接字
	 */
	public void close() {
		try {
			
			this.socket.close();
			if(this.dos != null)
				this.dos.close();
			if(this.dis != null)
				this.dis.close();
			if(this.oos != null)
				this.oos.close();
			if(this.ois != null)
				this.ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
