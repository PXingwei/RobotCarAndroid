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
					Toast.makeText(MyVideo.this,"��Ƶ����·����"+savePath,Toast.LENGTH_SHORT).show();	
				}
				break;
			case MSG_STATE:
				if(msg.obj!=null){
					Toast.makeText(MyVideo.this,"����״̬��"+(String) msg.obj,Toast.LENGTH_SHORT).show();
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
		//MySurfaceView���е�GetCameraIP��������CameraIp���ݵ�urlstr��
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
		//��ʱ����������Ĵ���
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
		//��Intent���и���keyȡ��value
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
				Log.d("aaaa", "����");
			}else if(y < 0){
				message = 0x85;
				msgBuffer = message;
				Log.d("aaaa", "����");
			}else if(y == 0){
				message = 0x90;
				msgBuffer = message;
				Log.d("aaaa", "ͣ");
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
			//�Զ�
            if(radio10.getId()==checkedId) 
            {
            	message = 0x69;
				msgBuffer = message;
            } 
            //�ֶ� ң��
            if(radio11.getId()==checkedId)  
            {  
            	message = 0x68;
				msgBuffer = message;
				mode = true;
            }
            //�ֶ�
//            if(radio12.getId()==checkedId)  
//            {  	
//            	message = 0x68;
//				msgBuffer = message;
//				mode = false;
//            }
            //ͣ
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
			Log.d("aaaa", "��");
		}else if(angle < 67.5 && angle > 22.5){
			message = 0x58;
			Log.d("aaaa", "�Һ�");
		}else if(angle < 112.5 && angle > 67.5){
			message = 0x64;
			Log.d("aaaa", "��");
		}else if(angle < 157.5 && angle > 112.5){
			message = 0x57;
			Log.d("aaaa", "���");
		}else if((angle < 180 && angle > 157.5)||(angle < -157.5 && angle > -180)){
			message = 0x65;
			Log.d("aaaa", "��");
		}else if(angle < -22.5 && angle > -67.5){
			message = 0x56;
			Log.d("aaaa", "��ǰ");
		}else if(angle < -67.5 && angle > -112.5){
			message = 0x63;
			Log.d("aaaa", "ǰ");
		}else if(angle < -112.5 && angle > -157.5){
			message = 0x55;
			Log.d("aaaa", "��ǰ");
		}else if(angle == 200){
			message = 0x70;
			Log.d("aaaa", "ͣ");
		}
		
		sendMessage(message);
		
	}
	
	private void takePhoto(){
		
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false)
		{
			if((getAvailableStore(getExternalStoragePath()) >= (1024 * 1024)))//1��
			{
				if(null!=MyConstant.handler)
				{
				 Message message = new Message();      
		            message.what = 1;      
		            MyConstant.handler.sendMessage(message);  
				}	
			}
			else{
				Toast.makeText(MyConstant.context, "����ʧ�ܣ��ڴ治�㣡",
						Toast.LENGTH_SHORT).show();
			}
				
		}
		else{
			Toast.makeText(MyConstant.context, "����ʧ�ܣ������SD����",
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
			//����һ���ļ����飬����������������ļ��µ�Ŀ¼
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
				Toast.makeText(MyVideo.this, "û����Ƭ�����������ٲ鿴��", Toast.LENGTH_SHORT).show();
			}	
		}
		else{
			Toast.makeText(MyConstant.context, "�����SD����",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private void record(){
		
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false)
		{
			if((getAvailableStore(getExternalStoragePath()) >= (1024 * 1024*10)))//10��
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
					Log.e("test", "��ʱ" + caustTime + "��");
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
				Toast.makeText(MyConstant.context, "¼��ʧ�ܣ��ڴ治�㣡",
						Toast.LENGTH_SHORT).show();
			}
		}
		else{
			Toast.makeText(MyConstant.context, "¼��ʧ�ܣ������SD����",
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
				deepth = localPixelFormat1.bytesPerPixel;// λ��				*/				
				mHandler.sendMessage(mHandler.obtainMessage(MSG_STATE,"����¼����Ƶing"));
				while (isRunning) {
					if(getAvailableStore(getExternalStoragePath()) >= getAvailableStore2)
					{
						getScreenShotBitmap();
					}
					else{
						Toast.makeText(MyConstant.context, "�ڴ治��,ֹͣ¼��",
								Toast.LENGTH_SHORT).show();
						Record.setBackgroundResource(R.drawable.record48);
						isRunning = false;
					    caustTime = (System.currentTimeMillis() - startTime) / 1000.0d;					
						Log.e("test", "��ʱ" + caustTime + "��");
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

	
	// ��ͼƬת������Ƶ
		void imgsToMp4() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						if(dataFile==null){
							return;
						}
						fileInputStream = new FileInputStream(dataFile);
						mHandler.sendMessage(mHandler.obtainMessage(MSG_STATE,"����¼�ƣ����������У������ĵȺ�..."));
						Log.i("test", "ͼƬ�������...");
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
		int deepth; //λ��*/		
       //¼��
		public void getScreenShotBitmap() {
			/*new Thread(new Runnable() {
				@Override*/
				//public void run() {
				// ��ƵͼƬ����
				int bufSize = 512 * 1024; 
				byte[] jpg_buf = new byte[bufSize]; // buffer to read jpg
				// ÿ������ȡ����
				int readSize = 4096; 
				byte[] buffer = new byte[readSize]; // buffer to read stream

				while (isRunning) {	
					
					try {
						url = new URL(CameraIp);
						// ʹ��HTTPURLConnetion������
						urlConn = (HttpURLConnection) url.openConnection(); 
						int read = 0;
						int status = 0;
						// jpg�����±�
						int jpg_count = 0; 

						while (true) {
							urlConn.setConnectTimeout(1000);
							//read (byte[] b,int off,int len) ������ ������������� len �������ֽڶ��� byte ���顣���Զ�ȡ len ���ֽڣ�����ȡ���ֽ�Ҳ����С�ڸ�ֵ����������ʽ����ʵ�ʶ�ȡ���ֽ���������-1��ʾ����ȡ����
							read = urlConn.getInputStream().read(buffer, 0,
									readSize);
							//System.out.println("read=" + read);
							if (read > 0) {

								for (int i = 0; i < read; i++) {
									switch (status) {
									// ����Content-Length:����ַ����������ʲô��
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
									//case15��16��һ��JPEGͼƬ��ʽ�Ŀ�ʼ��־
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
									//case17��18��һ��JPEGͼƬ��ʽ�Ľ�����־
									case 17:
										jpg_buf[jpg_count++] = (byte) buffer[i];
										if (buffer[i] == (byte) 0xFF)
											status++;
										//�����if��֪����ʲô��
										if (jpg_count >= bufSize)
											status = 0;
										break;
									case 18:
										jpg_buf[jpg_count++] = (byte) buffer[i];
										if (buffer[i] == (byte) 0xD9) {
											status = 0;
											// jpg�������
											// ��ʾͼ��											
																						
												Bitmap bmp = BitmapFactory.decodeStream(new ByteArrayInputStream(jpg_buf));									
													
													//��֪��������������ʲô��˼,���ݸ����� Bitmap ���� һ���µģ����ź�� Bitmap 
												bmp2 =  Bitmap
														.createScaledBitmap(bmp,
																640, 480,
																false);
											
												if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false)
												{
													if (bmp2 != null) {
														Log.i("test", "��ȡ��" + index + "��ͼƬ�ɹ�...ȥ����");
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
													Toast.makeText(MyConstant.context, "¼��ʧ�ܣ������SD����",
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
	
		// ��ȡSD��·��  
	public static String getExternalStoragePath() {  
	        // ��ȡSdCard״̬  
	        String state = android.os.Environment.getExternalStorageState();  
	  
	        // �ж�SdCard�Ƿ���ڲ����ǿ��õ�  
	  
	        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {  
	  
	            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {  
	  
	                return android.os.Environment.getExternalStorageDirectory()  
	                        .getPath();  
	  
	            }  
	  
	        }  
	  
	        return null;  
	  
	    }  

	public static long getAvailableStore(String filePath) {  
			  
	        // ȡ��sdcard�ļ�·��  
	  
	        StatFs statFs = new StatFs(filePath);  
	  
	        // ��ȡblock��SIZE  
	  
	        long blocSize = statFs.getBlockSize();  
	  
	        // ��ȡBLOCK����  
	  
	        // long totalBlocks = statFs.getBlockCount();  
	  
	        // ��ʹ�õ�Block������  
	  
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
			Toast.makeText(MyVideo.this,"�������ӳɹ���",Toast.LENGTH_SHORT).show();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show(); 
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
			 
			e.printStackTrace();
	       
		}
			 
			try {
				socketWriter= socket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
				
				e.printStackTrace();
			}
		    	//Toast.makeText(this,"��ʼ������ʧ�ܣ�"+e.getMessage(),Toast.LENGTH_LONG).show();
	  
  }
  
  private void sendMessage(int i){
	  try {
			socket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(CtrlIp), Integer.parseInt(CtrlPort));
			socket.connect(socketAddress, 1000); 
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	  
		 try {
			socketWriter= socket.getOutputStream();
			socketWriter.write(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
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
	    					Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
	    				}
	    				
	    				if (socketWriter != null) {
	    		        	try {
	    		        		socketWriter.flush();
	    		        		Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
	    		        	} catch (IOException e) {
	    		        		Log.e(TAG, "ON BACK: Couldn't flush output stream.", e);
	    		        	}

	    		        	}

//	    		        	try {
//	    		        		MyService.socket.close();
//	    		        		Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
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
		            Toast.makeText(this, "��", Toast.LENGTH_SHORT).show();
		            return false;  
		        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) { 
		        	message = 0x64;
		            Toast.makeText(this, "��", Toast.LENGTH_SHORT).show();  
		            return false;  
		        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) { 
		        	message = 0x65;
		            Toast.makeText(this, "��", Toast.LENGTH_SHORT).show();  
		            return false;  
		        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {  
		        	message = 0x66;
		            Toast.makeText(this, "��", Toast.LENGTH_SHORT).show();  
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
					Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
				}
		 }
		
		 return super.onKeyDown(keyCode, event);  
    }  

	//Activity�������ߴӺ�̨���»ص�ǰ̨ʱ������  
    @Override  
    protected void onStart() {  
        super.onStart();  
        
        System.out.println("start");  
    }  
      
    //Activity�Ӻ�̨���»ص�ǰ̨ʱ������  
    @Override  
    protected void onRestart() {  
        super.onRestart(); 
        System.out.println("restart");  
    }  
      
    //Activity�������ߴӱ����ǡ���̨���»ص�ǰ̨ʱ������  
    @Override  
    protected void onResume() {  
        super.onResume();  
        InitSocket();
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)?true:false)
		{
			getAvailableStore2 = getAvailableStore(getExternalStoragePath()) / 2;
		}
		else{
			Toast.makeText(MyConstant.context, "�����SD��",
					Toast.LENGTH_SHORT).show();
		}
        
        System.out.println("resume");  
    }  
      
    //Activity���ڻ�û�ʧȥ����ʱ������,��onResume֮���onPause֮��  
    /*@Override 
    public void onWindowFocusChanged(boolean hasFocus) { 
        super.onWindowFocusChanged(hasFocus); 
        Log.i(TAG, "onWindowFocusChanged called."); 
    }*/  
      
    //Activity�����ǵ������������ʱ������  
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
			Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
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
			Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
		}
		
		/*try {
			url = new URL(CameraIp);
			// ʹ��HTTPURLConnetion������
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
        	Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
        	} catch (IOException e) {
        	Log.e(TAG, "ON PAUSE: Couldn't flush output stream.", e);
        	}

        	}


//        	try {
//        	socket.close();
//        	Toast.makeText(MyVideo.this,"�������ӶϿ���",Toast.LENGTH_SHORT).show();
//        	} catch (IOException e2) {
//        	Log.e(TAG, "ON PAUSE: Unable to close socket.", e2);
//        	}
        System.out.println("pause");  
        //�п�����ִ����onPause��onStop��,ϵͳ��Դ���Ž�Activityɱ��,�����б�Ҫ�ڴ˱���־�����  
    }  
      
    //�˳���ǰActivity������ת����Activityʱ������  
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
      
    //�˳���ǰActivityʱ������,����֮��Activity�ͽ�����  
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
     * Activity��ϵͳɱ��ʱ������. 
     * ����:��Ļ����ı�ʱ,Activity���������ؽ�;��ǰActivity���ں�̨,ϵͳ��Դ���Ž���ɱ��. 
     * ����,����ת������Activity���߰�Home���ص�����ʱ�÷���Ҳ�ᱻ����,ϵͳ��Ϊ�˱��浱ǰView�����״̬. 
     * ��onPause֮ǰ������. 
     */  
    @Override  
    protected void onSaveInstanceState(Bundle outState) {  
        outState.putInt("param", param);  
        System.out.println("SaveInstanceState");  
        super.onSaveInstanceState(outState);  
    }  
      
    /** 
     * Activity��ϵͳɱ�������ؽ�ʱ������. 
     * ����:��Ļ����ı�ʱ,Activity���������ؽ�;��ǰActivity���ں�̨,ϵͳ��Դ���Ž���ɱ��,�û���������Activity. 
     * �����������onRestoreInstanceState���ᱻ����,��onStart֮��. 
     */  
    @Override  
    protected void onRestoreInstanceState(Bundle savedInstanceState) {  
        param = savedInstanceState.getInt("param");  
        System.out.println("RestoreInstanceState");  
        super.onRestoreInstanceState(savedInstanceState);  
    }  
}
