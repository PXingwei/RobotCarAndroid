package com.myService;

import java.io.IOException;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import com.mywificar.MyVideo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service{
	
	
	private int msg;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onStart(){
		
		
//		try {
//			socket = new Socket();
//			//socket = new Socket(InetAddress.getByName(CtrlIp),Integer.parseInt(CtrlPort));
//			SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(MyVideo.CtrlIp), Integer.parseInt(MyVideo.CtrlPort));
//			socket.connect(socketAddress, 1000);
//			Toast.makeText(MyService.this,"网络连接成功！",Toast.LENGTH_SHORT).show();
//			Log.e("Ping", "1..."); 
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			Toast.makeText(MyService.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
//			Log.e("Ping", "2..."); 
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			Toast.makeText(MyService.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
//			Log.e("Ping", "3...");
//			 
//			e.printStackTrace();
//	       
//		}
	}

}
