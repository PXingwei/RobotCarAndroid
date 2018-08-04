package com.mywificar;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;

import com.Constant.MyConstant;
import com.myService.MyService;
import com.myUtil.MyFileUtils;
import com.myUtil.MyFileUtils.NoSdcardException;
import com.myView.BallButton;
import com.myView.BallButton.BallChangeListener;
import com.myView.MySurfaceView;
import com.myView.RockerView;
import com.myView.RockerView.RockerChangeListener;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class MyVideo extends Activity
{
	
	URL videoUrl;
	private String urlstr;
	//HttpURLConnection conn;
	URL url = null;
	HttpURLConnection urlConn = null;
	Bitmap bmp2;
	private static final String TAG = "mHomeKeyEventReceiver"; 
	
	private RadioGroup radioGroup1 = null;
	private RadioButton radio10 = null;
	private RadioButton radio11 = null;
	//private RadioButton radio12 = null;
	private RadioButton radio13 = null;
	private RockerView rockerView;
	private BallButton ballbutton;
	
    private Button TakePhotos;
	private Button ViewPhotos;
	private Button Record;
    
	public static String CameraIp;
	public static String CtrlIp;
	public static String CtrlPort;
    public MySurfaceView r;
    public Socket socket;
    OutputStream socketWriter ;
    public static int flag = 2;
   
    public static float x0;
    public static float y0;
    public static float x1;
    public static float y1;
	public static int pointerCount; 
	private int param = 1;
	public int i = 0;
	public int j = 0;
	public int rec = 0;
	
	private boolean mode = true;
	public boolean isRunning = false;
	public static int index = 0;

	/*public int screenWidth;
	public int screenHeight;
	public int pixelformat;*/

	static final String IMAGE_TYPE = ".JPEG";

	long startTime;
	String tempFilePath;

	//boolean isSaveThreadRunning = false;
	double caustTime;
	
	public String savePath;
	
	public static final int MSG_SAVE_SUCCESS=100;
	public static final int MSG_STATE=101;
	
	static long getAvailableStore2;

	public Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_SAVE_SUCCESS:
				if(msg.obj!=null){
					savePath=(String) msg.obj;
//					pathTxtView.setVisibility(View.VISIBLE);
					Toast.makeText(MyVideo.this,"视频保存路径："+savePath,Toast.LENGTH_SHORT).show();	
				}
				break;
			case MSG_STATE:
				if(msg.obj!=null){
					Toast.makeText(MyVideo.this,"现在状态："+(String) msg.obj,Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
			}
		}
		
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.myvideo);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        } 
        
        findView();

        getMessage();
        InitSocket();
		//MySurfaceView类中的GetCameraIP方法，将CameraIp传递到urlstr中
		r.GetCameraIP(CameraIp);
		
		tempFilePath = "ScreenRecord/temp";
		try {
			new MyFileUtils().creatSDDir("ScreenRecord");
			new MyFileUtils().creatSDDir(tempFilePath);
		} catch (NoSdcardException e) {
			e.printStackTrace();
		}
		
		MyRockerChangeListener rc = new MyRockerChangeListener();
		rockerView.setRockerChangeListener(rc);
		
		MyBallChangeListener bc = new MyBallChangeListener();
		ballbutton.setBallChangeListener(bc);
        
		MyOnCheckedChangeListener cc = new MyOnCheckedChangeListener();
		radioGroup1.setOnCheckedChangeListener(cc);

		MyOnClickListener oc = new MyOnClickListener();
		//暂时看不懂下面的代码
		TakePhotos.setOnClickListener(oc);
		ViewPhotos.setOnClickListener(oc);
		Record.setOnClickListener(oc);
	}
	
	private void findView(){
        r = (MySurfaceView)findViewById(R.id.mySurfaceViewVideo);
        rockerView = (RockerView)findViewById(R.id.rockerView);
        ballbutton = (BallButton)findViewById(R.id.ballbutton);
        
        TakePhotos = (Button)findViewById(R.id.TakePhoto);
        ViewPhotos = (Button)findViewById(R.id.ViewPhoto);
        Record = (Button)findViewById(R.id.Record);
        
		radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
		radio10=(RadioButton)findViewById(R.id.radio10);  
        radio11=(RadioButton)findViewById(R.id.radio11);
       // radio12=(RadioButton)findViewById(R.id.radio12);
        radio13=(RadioButton)findViewById(R.id.radio13);
	}
	
	private void getMessage(){
		Intent intent = getIntent();
		//从Intent当中根据key取得value
		CameraIp = intent.getStringExtra("CameraIp");		
		CtrlIp= intent.getStringExtra("ControlUrl");		
		CtrlPort=intent.getStringExtra("Port");		
		Log.d("wifirobot", "control is :++++"+CtrlIp);
		Log.d("wifirobot", "CtrlPort is :++++"+CtrlPort);
	}
	
	private class MyRockerChangeListener implements RockerChangeListener{

		@Override
		public void report(float x, float y, double angle) {
			// TODO Auto-generated method stub
			if(mode){
				setAngle(angle);
			}
		}
		
	}
	
	private class MyBallChangeListener implements BallChangeListener{

		@Override
		public void moveToPoint(float y) {
			// TODO Auto-generated method stub
			int message;
			int msgBuffer = 0x90;
			
			if(y > 0){
				message = 0x86;
				msgBuffer = message;
				Log.d("aaaa", "向下");
			}else if(y < 0){
				message = 0x85;
				msgBuffer = message;
				Log.d("aaaa", "向上");
			}else if(y == 0){
				message = 0x90;
				msgBuffer = message;
				Log.d("aaaa", "停");
			}
			
			sendMessage(msgBuffer);
			
		}
		
	}
	
	private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			int message;
			int msgBuffer = 0x00;
			//自动
            if(radio10.getId()==checkedId) 
            {
            	message = 0x69;
				msgBuffer = message;
            } 
            //手动 遥感
            if(radio11.getId()==checkedId)  
            {  
            	message = 0x68;
				msgBuffer = message;
				mode = true;
            }
            //手动
//            if(radio12.getId()==checkedId)  
//            {  	
//            	message = 0x68;
//				msgBuffer = message;
//				mode = false;
//            }
            //停
            if(radio13.getId()==checkedId)  
            {  	
            	message = 0x67;
				msgBuffer = message;
            }
            
            sendMessage(msgBuffer);
            
		}
		
	}
	
	private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			
			case R.id.TakePhoto:
				takePhoto();
				break;
				
			case R.id.Record:
				record();
				break;
				
			case R.id.ViewPhoto:
				viewPhoto();
				break;
			}
		}
		
	}
	
	private void setAngle(double angle){
		int message = 0x00;
		
		if(angle < 22.5 && angle > -22.5){
			message = 0x66;
			Log.d("aaaa", "右");
		}else if(angle < 67.5 && angle > 22.5){
			message = 0x58;
			Log.d("aaaa", "右后");
		}else if(angle < 112.5 && angle > 67.5){
			message = 0x64;
			Log.d("aaaa", "后");
		}else if(angle < 157.5 && angle > 112.5){
			message = 0x57;
			Log.d("aaaa", "左后");
		}else if((angle < 180 && angle > 157.5)||(angle < -157.5 && angle > -180)){
			message = 0x65;
			Log.d("aaaa", "左");
		}else if(angle < -22.5 && angle > -67.5){
			message = 0x56;
			Log.d("aaaa", "右前");
		}else if(angle < -67.5 && angle > -112.5){
			message = 0x63;
			Log.d("aaaa", "前");
		}else if(angle < -112.5 && angle > -157.5){
			message = 0x55;
			Log.d("aaaa", "左前");
		}else if(angle == 200){
			message = 0x70;
			Log.d("aaaa", "停");
		}
		
		sendMessage(message);
		
	}
	
	private void takePhoto(){
		
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false)
		{
			if((getAvailableStore(getExternalStoragePath()) >= (1024 * 1024)))//1兆
			{
				if(null!=MyConstant.handler)
				{
				 Message message = new Message();      
		            message.what = 1;      
		            MyConstant.handler.sendMessage(message);  
				}	
			}
			else{
				Toast.makeText(MyConstant.context, "拍照失败，内存不足！",
						Toast.LENGTH_SHORT).show();
			}
				
		}
		else{
			Toast.makeText(MyConstant.context, "拍照失败，请插入SD卡！",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private void viewPhoto(){
		
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false)
		{
			File fi = new File("/sdcard/demo/");
			if (!fi.exists()) {
				fi.mkdirs();
			}
			//返回一个文件数组，这个数组包含有这个文件下的目录
			File[] fil = fi.listFiles();
			if(fil.length != 0)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(MyVideo.this, MyPictureShowActivity.class);
				MyVideo.this.startActivity(intent);
			}
			else
			{
				Toast.makeText(MyVideo.this, "没有照片，请先拍照再查看！", Toast.LENGTH_SHORT).show();
			}	
		}
		else{
			Toast.makeText(MyConstant.context, "请插入SD卡！",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private void record(){
		
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false)
		{
			if((getAvailableStore(getExternalStoragePath()) >= (1024 * 1024*10)))//10兆
			{
				if ((rec%2)==0) {
					
					Record.setBackgroundResource(R.drawable.recording48);
					isRunning = true;
					index = 0;
					//dataArrayList.clear();	
					recordScreen();
				} 
				if ((rec%2)==1) {
					// VideoCapture.stop();
					Record.setBackgroundResource(R.drawable.record48);
					isRunning = false;
				    caustTime = (System.currentTimeMillis() - startTime) / 1000.0d;					
					Log.e("test", "耗时" + caustTime + "秒");
					imgsToMp4();
					System.out.println("CIndex" + index);
					
					try
					{
						urlConn.getInputStream().close();
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					urlConn.disconnect();
					try 
			        { 
			            Thread.sleep(300); 
			        } catch (InterruptedException e) 
			        { 
			            e.printStackTrace(); 
			        }
				}
				rec++;
			}
			else{
				Toast.makeText(MyConstant.context, "录像失败，内存不足！",
						Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(MyConstant.context, "录像失败，请插入SD卡！",
					Toast.LENGTH_SHORT).show();
		}
	}

	FileOutputStream fileOutputStream = null;
	FileInputStream fileInputStream = null;
	File dataFile;
	String filename;
	void recordScreen() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//sizeArrayList.clear();
					MyFileUtils.deleteDirectoryContent(new MyFileUtils()
							.getSDCardRoot() + tempFilePath + File.separator);
					dataFile = new MyFileUtils().createFileInSDCard("data.txt",
							tempFilePath);
					if (dataFile.exists()) {
						fileOutputStream = new FileOutputStream(dataFile);
					}
				} catch (NoSdcardException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				startTime = System.currentTimeMillis();

				/*localPixelFormat1 = new PixelFormat();
				PixelFormat.getPixelFormatInfo(pixelformat, localPixelFormat1);
				deepth = localPixelFormat1.bytesPerPixel;// 位深				*/				
				mHandler.sendMessage(mHandler.obtainMessage(MSG_STATE,"正在录制视频ing"));
				while (isRunning) {
					if(getAvailableStore(getExternalStoragePath()) >= getAvailableStore2)
					{
						getScreenShotBitmap();
					}
					else{
						Toast.makeText(MyConstant.context, "内存不足,停止录像！",
								Toast.LENGTH_SHORT).show();
						Record.setBackgroundResource(R.drawable.record48);
						isRunning = false;
					    caustTime = (System.currentTimeMillis() - startTime) / 1000.0d;					
						Log.e("test", "耗时" + caustTime + "秒");
						imgsToMp4();
						try
						{
							urlConn.getInputStream().close();
						} catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						urlConn.disconnect();
						try 
				        { 
				            Thread.sleep(300); 
				        } catch (InterruptedException e) 
				        { 
				            e.printStackTrace(); 
				        }
						rec = 0;
						index = 0;
					}
					
				}
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	
	// 将图片转换成视频
		void imgsToMp4() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						if(dataFile==null){
							return;
						}
						fileInputStream = new FileInputStream(dataFile);
						mHandler.sendMessage(mHandler.obtainMessage(MSG_STATE,"结束录制，保存数据中，请耐心等候..."));
						Log.i("test", "图片保存完毕...");
						MyVideoCapture.genMp4(MyVideo.this, "ScreenRecord/temp",
								index,index/caustTime,mHandler);
						System.out.println("iIndex" + index);
						index = 0;
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}
		
		/*PixelFormat localPixelFormat1;
		int deepth; //位深*/		
       //录像
		public void getScreenShotBitmap() {
			/*new Thread(new Runnable() {
				@Override*/
				//public void run() {
				// 视频图片缓冲
				int bufSize = 512 * 1024; 
				byte[] jpg_buf = new byte[bufSize]; // buffer to read jpg
				// 每次最大获取的流
				int readSize = 4096; 
				byte[] buffer = new byte[readSize]; // buffer to read stream

				while (isRunning) {	
					
					try {
						url = new URL(CameraIp);
						// 使用HTTPURLConnetion打开连接
						urlConn = (HttpURLConnection) url.openConnection(); 
						int read = 0;
						int status = 0;
						// jpg数据下标
						int jpg_count = 0; 

						while (true) {
							urlConn.setConnectTimeout(1000);
							//read (byte[] b,int off,int len) 方法， 将输入流中最多 len 个数据字节读入 byte 数组。尝试读取 len 个字节，但读取的字节也可能小于该值。以整数形式返回实际读取的字节数。返回-1表示流读取完了
							read = urlConn.getInputStream().read(buffer, 0,
									readSize);
							//System.out.println("read=" + read);
							if (read > 0) {

								for (int i = 0; i < read; i++) {
									switch (status) {
									// 接收Content-Length:这个字符，不清楚有什么用
									case 0:
										if (buffer[i] == (byte) 'C')
											status++;
										else
											status = 0;
										break;
									case 1:
										if (buffer[i] == (byte) 'o')
											status++;
										else
											status = 0;
										break;
									case 2:
										if (buffer[i] == (byte) 'n')
											status++;
										else
											status = 0;
										break;
									case 3:
										if (buffer[i] == (byte) 't')
											status++;
										else
											status = 0;
										break;
									case 4:
										if (buffer[i] == (byte) 'e')
											status++;
										else
											status = 0;
										break;
									case 5:
										if (buffer[i] == (byte) 'n')
											status++;
										else
											status = 0;
										break;
									case 6:
										if (buffer[i] == (byte) 't')
											status++;
										else
											status = 0;
										break;
									case 7:
										if (buffer[i] == (byte) '-')
											status++;
										else
											status = 0;
										break;
									case 8:
										if (buffer[i] == (byte) 'L')
											status++;
										else
											status = 0;
										break;
									case 9:
										if (buffer[i] == (byte) 'e')
											status++;
										else
											status = 0;
										break;
									case 10:
										if (buffer[i] == (byte) 'n')
											status++;
										else
											status = 0;
										break;
									case 11:
										if (buffer[i] == (byte) 'g')
											status++;
										else
											status = 0;
										break;
									case 12:
										if (buffer[i] == (byte) 't')
											status++;
										else
											status = 0;
										break;
									case 13:
										if (buffer[i] == (byte) 'h')
											status++;
										else
											status = 0;
										break;
									case 14:
										if (buffer[i] == (byte) ':')
											status++;
										else
											status = 0;
										break;
									//case15、16是一张JPEG图片格式的开始标志
									case 15:
										if (buffer[i] == (byte) 0xFF)
											status++;
										jpg_count = 0;
										jpg_buf[jpg_count++] = (byte) buffer[i];
										break;
									case 16:
										if (buffer[i] == (byte) 0xD8) {
											status++;
											jpg_buf[jpg_count++] = (byte) buffer[i];
										} else {	
											if (buffer[i] != (byte) 0xFF)
												status = 15;

										}
										break;
									//case17、18是一张JPEG图片格式的结束标志
									case 17:
										jpg_buf[jpg_count++] = (byte) buffer[i];
										if (buffer[i] == (byte) 0xFF)
											status++;
										//下面的if不知道有什么用
										if (jpg_count >= bufSize)
											status = 0;
										break;
									case 18:
										jpg_buf[jpg_count++] = (byte) buffer[i];
										if (buffer[i] == (byte) 0xD9) {
											status = 0;
											// jpg接收完成
											// 显示图像											
																						
												Bitmap bmp = BitmapFactory.decodeStream(new ByteArrayInputStream(jpg_buf));									
													
													//不知道第三个参数是什么意思,根据给定的 Bitmap 创建 一个新的，缩放后的 Bitmap 
												bmp2 =  Bitmap
														.createScaledBitmap(bmp,
																640, 480,
																false);
											
												if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false)
												{
													if (bmp2 != null) {
														Log.i("test", "截取第" + index + "张图片成功...去保存");
														OutputStream os;
														try {
															File file = new MyFileUtils().createFileInSDCard("videoTemp_"
																	+ index + IMAGE_TYPE, tempFilePath);
															os = new FileOutputStream(file);
															bmp2.compress(CompressFormat.JPEG, 100, os);
															os.flush();
															//os.close();
															// bitmap.recycle();
															// bitmap = null;
															index++;

														} catch (FileNotFoundException e) {
															e.printStackTrace();
														} catch (IOException e) {
															e.printStackTrace();
														} catch (NoSdcardException e) {
															e.printStackTrace();
														}

													}
												}
												else{
													Toast.makeText(MyConstant.context, "录像失败，请插入SD卡！",
															Toast.LENGTH_SHORT).show();
												}
												
												
												
										} else {
											if (buffer[i] != (byte) 0xFF)
												status = 17;
										}
										break;
									default:
										status = 0;
										break;

									}
								}
							}
						}
					} catch (IOException ex) {
						urlConn.disconnect();
						ex.printStackTrace();
					}
				}
			// return null;
				//}
		/*}).start();*/
	}
	
		// 获取SD卡路径  
	public static String getExternalStoragePath() {  
	        // 获取SdCard状态  
	        String state = android.os.Environment.getExternalStorageState();  
	  
	        // 判断SdCard是否存在并且是可用的  
	  
	        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {  
	  
	            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {  
	  
	                return android.os.Environment.getExternalStorageDirectory()  
	                        .getPath();  
	  
	            }  
	  
	        }  
	  
	        return null;  
	  
	    }  

	public static long getAvailableStore(String filePath) {  
			  
	        // 取得sdcard文件路径  
	  
	        StatFs statFs = new StatFs(filePath);  
	  
	        // 获取block的SIZE  
	  
	        long blocSize = statFs.getBlockSize();  
	  
	        // 获取BLOCK数量  
	  
	        // long totalBlocks = statFs.getBlockCount();  
	  
	        // 可使用的Block的数量  
	  
	        long availaBlock = statFs.getAvailableBlocks();  
	  
	        // long total = totalBlocks * blocSize;  
	  
	        long availableSpare = availaBlock * blocSize;  
	  
	        return availableSpare;  
	  
	    }  

	
	public  void vibrator(){
		
			Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
			vibrator.vibrate(new long[]{0,32}, -1); 
		
	}
	
 
	
  public void InitSocket()
  {
		try {
			socket = new Socket();
			//socket = new Socket(InetAddress.getByName(CtrlIp),Integer.parseInt(CtrlPort));
			SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(MyVideo.CtrlIp), Integer.parseInt(MyVideo.CtrlPort));
			socket.connect(socketAddress, 1000);
			Toast.makeText(MyVideo.this,"网络连接成功！",Toast.LENGTH_SHORT).show();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show(); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
			 
			e.printStackTrace();
	       
		}
			 
			try {
				socketWriter= socket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
				
				e.printStackTrace();
			}
		    	//Toast.makeText(this,"初始化网络失败！"+e.getMessage(),Toast.LENGTH_LONG).show();
	  
  }
  
  private void sendMessage(int i){
	  try {
			socket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(CtrlIp), Integer.parseInt(CtrlPort));
			socket.connect(socketAddress, 1000); 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	  
		 try {
			socketWriter= socket.getOutputStream();
			socketWriter.write(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		 try{
			 socketWriter.close();
		 }catch(Exception e){
			 
		 }
  }
  
  

  
//	private long exitTime = 0;
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)   
    {  
		 if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)  
		 {  
			 
			 if(index != 0){
					isRunning = false;
					imgsToMp4();
					Record.setBackgroundResource(R.drawable.record48);
				}
			rec = 0;
			index = 0;
				
		    int message = 0x67;
	    	int msgBuffer = message;
	    			try 
	    		        {
	    		             socketWriter.write(msgBuffer);
	    		             socketWriter.flush();
	    				} 
	    		         catch (Exception e) 
	    		        {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    					Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
	    				}
	    				
	    				if (socketWriter != null) {
	    		        	try {
	    		        		socketWriter.flush();
	    		        		Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
	    		        	} catch (IOException e) {
	    		        		Log.e(TAG, "ON BACK: Couldn't flush output stream.", e);
	    		        	}

	    		        	}

//	    		        	try {
//	    		        		MyService.socket.close();
//	    		        		Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
//	    		        	} catch (IOException e2) {
//	    		        		Log.e(TAG, "ON BACK: Unable to close socket.", e2);
//	    		        	}
		             finish();  
		             System.exit(0);   
		                   
		         return true;  
		 } 
		 if(!mode){
			 int message = 0x00;
			 
				if (keyCode == KeyEvent.KEYCODE_DPAD_UP) { 
					message = 0x63;
		            Toast.makeText(this, "上", Toast.LENGTH_SHORT).show();
		            return false;  
		        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) { 
		        	message = 0x64;
		            Toast.makeText(this, "下", Toast.LENGTH_SHORT).show();  
		            return false;  
		        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) { 
		        	message = 0x65;
		            Toast.makeText(this, "左", Toast.LENGTH_SHORT).show();  
		            return false;  
		        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {  
		        	message = 0x66;
		            Toast.makeText(this, "右", Toast.LENGTH_SHORT).show();  
		            return false;  
		        }
				try {
		             socketWriter.write(message);
		             socketWriter.flush();
				} 
		         catch (Exception e) 
		        {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
				}
		 }
		
		 return super.onKeyDown(keyCode, event);  
    }  

	//Activity创建或者从后台重新回到前台时被调用  
    @Override  
    protected void onStart() {  
        super.onStart();  
        
        System.out.println("start");  
    }  
      
    //Activity从后台重新回到前台时被调用  
    @Override  
    protected void onRestart() {  
        super.onRestart(); 
        System.out.println("restart");  
    }  
      
    //Activity创建或者从被覆盖、后台重新回到前台时被调用  
    @Override  
    protected void onResume() {  
        super.onResume();  
        InitSocket();
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false)
		{
			getAvailableStore2 = getAvailableStore(getExternalStoragePath()) / 2;
		}
		else{
			Toast.makeText(MyConstant.context, "请插入SD卡",
					Toast.LENGTH_SHORT).show();
		}
        
        System.out.println("resume");  
    }  
      
    //Activity窗口获得或失去焦点时被调用,在onResume之后或onPause之后  
    /*@Override 
    public void onWindowFocusChanged(boolean hasFocus) { 
        super.onWindowFocusChanged(hasFocus); 
        Log.i(TAG, "onWindowFocusChanged called."); 
    }*/  
      
    //Activity被覆盖到下面或者锁屏时被调用  
    @Override  
    protected void onPause() {  
        super.onPause();  
        int message;
		int msgBuffer;
		message = 0x70;
		msgBuffer = message;
		
		try 
        {
             socketWriter.write(msgBuffer);
            socketWriter.flush();
		} 
         catch (Exception e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
		}
		
		message = 0x90;
		msgBuffer = message;
		try 
        {
             socketWriter.write(msgBuffer);
             socketWriter.flush();
		} 
         catch (Exception e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
		}
		
		/*try {
			url = new URL(CameraIp);
			// 使用HTTPURLConnetion打开连接
			urlConn = (HttpURLConnection) url.openConnection(); 
			urlConn.setConnectTimeout(1000);
			} catch (IOException ex) {
				urlConn.disconnect();
				ex.printStackTrace();
			}
		try
		{
			urlConn.getInputStream().close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		urlConn.disconnect();
		try 
        { 
            Thread.sleep(300); 
        } catch (InterruptedException e) 
        { 
            e.printStackTrace(); 
        }*/
		
        if (socketWriter != null) {
        	try {
        	socketWriter.flush();
        	Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
        	} catch (IOException e) {
        	Log.e(TAG, "ON PAUSE: Couldn't flush output stream.", e);
        	}

        	}


//        	try {
//        	socket.close();
//        	Toast.makeText(MyVideo.this,"网络连接断开！",Toast.LENGTH_SHORT).show();
//        	} catch (IOException e2) {
//        	Log.e(TAG, "ON PAUSE: Unable to close socket.", e2);
//        	}
        System.out.println("pause");  
        //有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据  
    }  
      
    //退出当前Activity或者跳转到新Activity时被调用  
    @Override  
    protected void onStop() {  
        super.onStop(); 
        try{
        	socketWriter.close();
        	socket.close();
        }catch(Exception e){
        	
        }
        System.out.println("stop");     
    }  
      
    //退出当前Activity时被调用,调用之后Activity就结束了  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
//        if(mHomeKeyEventReceiver != null){
//        	try {
//        		unregisterReceiver(mHomeKeyEventReceiver);
//        	} catch (Exception e) {
//        		Log.e(TAG, "unregisterReceiver homePressReceiver failure :"+e.getCause());
//        		}
//        }  
    }  
      
    /** 
     * Activity被系统杀死时被调用. 
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死. 
     * 另外,当跳转到其他Activity或者按Home键回到主屏时该方法也会被调用,系统是为了保存当前View组件的状态. 
     * 在onPause之前被调用. 
     */  
    @Override  
    protected void onSaveInstanceState(Bundle outState) {  
        outState.putInt("param", param);  
        System.out.println("SaveInstanceState");  
        super.onSaveInstanceState(outState);  
    }  
      
    /** 
     * Activity被系统杀死后再重建时被调用. 
     * 例如:屏幕方向改变时,Activity被销毁再重建;当前Activity处于后台,系统资源紧张将其杀死,用户又启动该Activity. 
     * 这两种情况下onRestoreInstanceState都会被调用,在onStart之后. 
     */  
    @Override  
    protected void onRestoreInstanceState(Bundle savedInstanceState) {  
        param = savedInstanceState.getInt("param");  
        System.out.println("RestoreInstanceState");  
        super.onRestoreInstanceState(savedInstanceState);  
    }  
}
