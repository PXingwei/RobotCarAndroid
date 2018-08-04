package com.myView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;

import com.Constant.MyConstant;
import com.mywificar.MyVideo;
import com.mywificar.R;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder.Callback;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewDebug.FlagToString;
import android.widget.Toast;


public class MySurfaceView extends SurfaceView implements Callback {
	private SurfaceHolder sfh;
	private Canvas canvas;
	URL videoUrl;
	private String urlstr;
	HttpURLConnection conn;
	URL url = null;
	HttpURLConnection urlConn = null;
	Bitmap bmp;
	Bitmap bmp1;
	private Paint p;
	InputStream inputstream = null;
	private Bitmap mBitmap;
	private static int mScreenWidth;
	private static int mScreenHeight;
	public boolean Is_Scale = false;
	private boolean isThreadRunning = true; 
	public int flag;
	public float x0;
	public float y0;
	public float x1;
	public float y1;
	public int pointerCount; 
	public int z = 0;
	/*private  Socket socket;
	OutputStream socketWriter ;*/
	//�� ����SurfaceView����ʱ��Ҫע�⣬��������಻�ᱻӦ����layout.xml�����ļ��У�������ֻ��Ҫʵ�� SurfaceView(Context context)���켴�ɣ������������Ҫ��Ӧ����layout.xml�����ļ��У��򻹱���ʵ��SurfaceView(Context context,AttributeSet attrs)���캯�������Ҹú����������public�������η���
	//���캯��
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		MyConstant.context = context;
		initialize();
		
		p = new Paint();
		//���û����޾��(��������ÿ��Կ���Ч���ܲ�)
		p.setAntiAlias(true);
		/*SurfaceHolder��һ���ӿڣ������þ���һ������Surface�ļ�������
		�ṩ���ʺͿ���SurfaceView�����Surface ��صķ��� ��providingaccess and control over this SurfaceView's underlying surface����
		��ͨ�������ص������������ǿ��Ը�֪��Surface�Ĵ��������ٻ��߸ı䡣
		��SurfaceView����һ������getHolder�����Ժܷ���ػ��SurfaceView����Ӧ��Surface����Ӧ��SurfaceHolder*/
		sfh = this.getHolder();
		//ΪSurfaceHolder���һ��SurfaceHolder.Callback�ص��ӿ�
		/*callback�ӿ�:
			 ֻҪ�̳�SurfaceView�ಢʵ��SurfaceHolder.Callback�ӿھͿ���ʵ��һ���Զ����SurfaceView�ˣ�SurfaceHolder.Callback�ڵײ��Surface״̬�����仯��ʱ��֪ͨView��SurfaceHolder.Callback�������µĽӿڣ�
			 surfaceCreated(SurfaceHolder holder)����Surface��һ�δ�������������øú�������������ڸú�������Щ�ͻ��ƽ�����صĳ�ʼ��������һ������¶�����������߳������ƽ��棬���Բ�Ҫ����������л���Surface��
			 surfaceChanged(SurfaceHolder holder, int format, int width,int height)����Surface��״̬����С�͸�ʽ�������仯��ʱ�����øú�������surfaceCreated���ú�ú������ٻᱻ����һ�Ρ�*/
		sfh.addCallback(this);
		//���ñ�������  
		this.setKeepScreenOn(true);
		//����˿ؼ���ȡ����
		setFocusable(true);
		//������������ûʲô�� 
		this.getWidth();
		this.getHeight();
		//�����ڲ���
		MyConstant.handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					if(null != bmp1)
					{
						saveMyBitmap("snapshot",bmp1);
					}
					else
					{
						Toast.makeText(MyConstant.context, "����ʧ�ܣ�δ�ܻ�ȡ����ͷͼ��",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case 2:
					Toast.makeText(MyConstant.context, "��Ƶ���ӶϿ���", Toast.LENGTH_SHORT).show();
					z = 0;
					break;
				case 3:
					//Toast.makeText(MyConstant.context, "��Ƶ���ӳɹ���", Toast.LENGTH_SHORT).show();
					z = 1;
					break;
				 default: 
				 break; 
				
				}
				//�շ�����������öԲ���Ҫ���߲����ĵ���Ϣ�׸����࣬���ⶪʧ��Ϣ
				super.handleMessage(msg);
			}

		};

	}
	//Ԥ���ĳ�ֵ
	private void initialize() {
		//�о�û�´�������͵����˷���
		/*�����Ļ�ߴ�ķ���
		 * public Point getDisplayMetrics() {   
		  DisplayMetrics dm = new DisplayMetrics();   
		  dm = getApplicationContext().getResources().getDisplayMetrics();   
		  int screenWidth = dm.widthPixels;   
		  int screenHeight = dm.heightPixels;   
    	  return new Point(screenWidth ,screenHeight );  
			}  */
		DisplayMetrics dm = getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		// ������Ļ����
		this.setKeepScreenOn(true);
	}
	public void saveMyBitmap(String bitName, Bitmap bmp1) {
		
			File f = new File("/sdcard/demo/");
			//�ж��ļ��Ƿ���ڣ��ж���ָ����·������ָ����Ŀ¼�ļ��Ƿ��Ѿ�����
			if (!f.exists()) {
				//����һ��Ŀ¼������·�����ɵ�ǰ File ����ָ����������һ����ĸ�·���� �����Ŀ¼(��༶Ŀ¼)�ܱ�������Ϊ true������Ϊ false��
				f.mkdirs();
			}
			f = new File("/sdcard/demo/" + bitName+System.currentTimeMillis() + ".JPEG");
			try {
				//��ָ��·���ϴ����ļ�
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block

			}
			//�����ֽ����������
			FileOutputStream fOut = null;
			try {
				//�����ֽ������
				fOut = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Log.d("MySurface", "bitmap is:" + bmp1 + "fout is:" + fOut);
			//������Bitmap.compress��������Bitmap���󱣴��PNG��JPG�ļ���Ȼ�������BitmapFactory���ļ��е����ݶ�����������Bitmap����
			bmp1.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			try {
				//������ʱ�������л�������ֻ�л���������ʱ�򣬲ŻὫ�������������д�뵽�ļ���ȥ��ֻҪ�ļ���������û�е���flush����close�����Ļ�����û��������������������д���ļ���ȥ��.
				//���ԣ�����ʹ����ʱ���ֲ�����һ�ξ�close�Ļ���һ��Ҫÿ��ʹ��append��print��write��֮��Ҫflushһ�£���֤���ݶ���д���ļ�����ȥ��
				//���ھ����÷����ܼ򵥣�������.flush()�Ϳ����ˡ�
				//��Ҫ����IO�У�����ջ��������ݣ�һ���ڶ�д��(stream)��ʱ���������ȱ��������ڴ��У��ٰ�����д���ļ��У��������ݶ����ʱ�򲻴�����������Ѿ�д���ˣ���Ϊ����һ�����п��ܻ������ڴ�����������С���ʱ������������close()�����ر��˶�д������ô�ⲿ�����ݾͻᶪʧ������Ӧ���ڹرն�д��֮ǰ��flush()��
				fOut.flush();
				Toast.makeText(MyConstant.context, "���ճɹ���·����/SDCard/Demo/",
						Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}

	class DrawVideo extends Thread {
		//���캯��
		public DrawVideo() {
		}

		@Override
		//��֪����¡��ʲô��
		/*ǳ���ƣ���������ջ�е����ݸ���һ�ݡ�
		������Զ���Ļ������ͺ�String����������ö������Ա��ʱ��Ӱ��clone���󣬶����������Ա��ʱ��Ӱ��clone����������Ҫʵ����ƾ�Ҫ�Լ���дclone������*/
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			
			return super.clone();
		}
		public void run() {
			Paint pt = new Paint();
			//���û����޾��(��������ÿ��Կ���Ч���ܲ�)
			pt.setAntiAlias(true);
			//���û��Ƶ���ɫ��ʹ����ɫֵ����ʾ������ɫֵ����͸���Ⱥ�RGB��ɫ
			pt.setColor(Color.GREEN);
			//���û������ֵ��ֺŴ�С
			pt.setTextSize(20);
			//����pt��styleΪSTROKE�����ĵ�
			//pt.setStyle(Paint.Style.STROKE);
			//��������ʽΪSTROKE(���Ļ���)��FILL_OR_STROKEʱ�����ñ�ˢ�Ĵ�ϸ��  
			pt.setStrokeWidth(1);
			// ��ƵͼƬ����
			int bufSize = 512 * 1024; 
			byte[] jpg_buf = new byte[bufSize]; // buffer to read jpg
			// ÿ������ȡ����
			int readSize = 4096; 
			byte[] buffer = new byte[readSize]; // buffer to read stream

			while (isThreadRunning) {
				long Time = 0;
				long Span = 0;
				int fps = 0;
				String str_fps = "0 fps";

				try {
					url = new URL(urlstr);
					// ʹ��HTTPURLConnetion������
					urlConn = (HttpURLConnection) url.openConnection(); 
					Time = System.currentTimeMillis();

					int read = 0;
					int status = 0;
					// jpg�����±�
					int jpg_count = 0; 

					while (true) {
						
						urlConn.setConnectTimeout(1000);
						//read (byte[] b,int off,int len) ������ ������������� len �������ֽڶ��� byte ���顣���Զ�ȡ len ���ֽڣ�����ȡ���ֽ�Ҳ����С�ڸ�ֵ����������ʽ����ʵ�ʶ�ȡ���ֽ���������-1��ʾ����ȡ����
						read = urlConn.getInputStream().read(buffer, 0,
								readSize);
						if (read == 0) {
							
							Message message = new Message();      
				            message.what = 2;      
				            MyConstant.handler.sendMessage(message); 
						}
						if (read > 0) {
							if(z == 0){
							Message message = new Message();      
				            message.what = 3;      
				            MyConstant.handler.sendMessage(message);
				            }
							
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

										fps++;
										Span = System.currentTimeMillis()
												- Time;
										if (Span > 1000L) {
											Time = System.currentTimeMillis();
											str_fps = String.valueOf(fps)
													+ " fps";
											fps = 0;
										}
										// ��ʾͼ��
										//if (null != canvas)
										try {  
								           
											/*SurfaceHolder �ࣺ
											����һ�����ڿ���surface�Ľӿڣ����ṩ�˿���surface �Ĵ�С����ʽ����������أ���������ı�ġ� 
											SurfaceView��getHolder()�������Ի�ȡSurfaceHolder����Surface ����SurfaceHolder�����ڡ���ȻSurface�����˵�ǰ���ڵ��������ݣ�������ʹ�ù������ǲ�ֱ�Ӻ�Surface�򽻵��ģ���SurfaceHolder��Canvas lockCanvas()����Canvas lockCanvas()��������ȡCanvas����ͨ����Canvas�ϻ����������޸�Surface�е����ݡ����Surface���ɱ༭������δ�������øú����᷵��null���� unlockCanvas() �� lockCanvas()��Surface�������ǲ�����ģ�������Ҫ��ȫ�ػ�Surface�����ݣ�Ϊ�����Ч��ֻ�ػ�仯�Ĳ�������Ե���lockCanvas(Rect rect)������ָ��һ��rect��������������������ݻỺ���������ڵ���lockCanvas������ȡCanvas��SurfaceView���ȡSurface��һ��ͬ����ֱ������unlockCanvasAndPost(Canvas canvas)�������ͷŸ����������ͬ�����Ʊ�֤��Surface���ƹ����в��ᱻ�ı䣨���ݻ١��޸ģ���*/
											//�õ�һ��canvasʵ�� 
											canvas = sfh.lockCanvas();
											if (canvas != null) {
											//���ñ���Ϊ��ɫ,ˢ��
											canvas.drawColor(Color.BLACK);

											Bitmap bmp = BitmapFactory.decodeStream(new ByteArrayInputStream(jpg_buf));
											
											Bitmap forward48 = BitmapFactory.decodeResource(getResources(),R.drawable.forward48);
											Bitmap back48 = BitmapFactory.decodeResource(getResources(),R.drawable.back48);
											Bitmap left48 = BitmapFactory.decodeResource(getResources(),R.drawable.left48);
											Bitmap right48 = BitmapFactory.decodeResource(getResources(),R.drawable.right48);
											/*Bitmap leftview48 = BitmapFactory.decodeResource(getResources(),R.drawable.leftview48);
											Bitmap rightview48 = BitmapFactory.decodeResource(getResources(),R.drawable.rightview48);*/
											Bitmap upview48 = BitmapFactory.decodeResource(getResources(),R.drawable.upview48);
											Bitmap downview48 = BitmapFactory.decodeResource(getResources(),R.drawable.downview48);
											
											Bitmap forward64 = BitmapFactory.decodeResource(getResources(),R.drawable.forward64);
											Bitmap back64 = BitmapFactory.decodeResource(getResources(),R.drawable.back64);
											Bitmap left64 = BitmapFactory.decodeResource(getResources(),R.drawable.left64);
											Bitmap right64 = BitmapFactory.decodeResource(getResources(),R.drawable.right64);
											/*Bitmap leftview64 = BitmapFactory.decodeResource(getResources(),R.drawable.leftview64);
											Bitmap rightview64 = BitmapFactory.decodeResource(getResources(),R.drawable.rightview64);*/
											Bitmap upview64 = BitmapFactory.decodeResource(getResources(),R.drawable.upview64);
											Bitmap downview64 = BitmapFactory.decodeResource(getResources(),R.drawable.downview64);
											
											Bitmap camera64 = BitmapFactory.decodeResource(getResources(),R.drawable.camera64);
											Bitmap photo48 = BitmapFactory.decodeResource(getResources(),R.drawable.photo48);
											
											int width = mScreenWidth;
											int height = mScreenHeight;
												
											float rate_width = (float) mScreenWidth/ (float) bmp.getWidth();
											float rate_height = (float) mScreenHeight/ (float) bmp.getHeight();

//											if (Is_Scale) {
//												if (rate_width > rate_height)
//													width = (int) ((float) bmp
//															.getWidth() * rate_height);
//												if (rate_width < rate_height)
//													height = (int) ((float) bmp
//															.getHeight() * rate_width);
//
//											}
												
												//��֪��������������ʲô��˼,���ݸ����� Bitmap ���� һ���µģ����ź�� Bitmap 
												mBitmap = Bitmap
														.createScaledBitmap(bmp,
																width, height,
																false);
												bmp1 =  Bitmap
														.createScaledBitmap(bmp,
																640, 480,
																false);
												//canvas.drawbitmap����������������Ϊ�½�һ�������� �ͺñȻ��ҵĻ��壬��Ȼ�㲻��������Ӷ�������ʲô��û�У����ǿյġ����ڷ�������� bitmap���ǵ��������е�ͼƬ�����磬�����ϴλ�û�����һ��ͼƬ�����ڷ��ڻ����ϣ�Ҫ����������������0��0��Ϊ�������Ż��ڻ����ϵķ��õ�λ�ã�0��0Ϊx���y������꣬����ǣ�0,0������Ĭ��Ϊ�Ӵ����Ͻǿ�ʼ���á������һ��ͼƬΪ���ò�ͬ������ʱ�������Ǹ�ͼƬ��������������ϵ�λ�õı仯������Կ���������Ϊ��100,10����Ҳ����x����100��ͼƬ����߾���Ϊ100��y����10��ͼƬ���Ϸ�����Ϊ10������Կ�����ߺ�ɫ�Ĳ���ռ��100���ϱ߲��ֵĺ�ɫռ��10.��󣬾����Ǹ�null��null�Ĳ������������û��ʵģ���û�л��ʣ���Ȼ�Ͳ������ˣ������ֻ�Ǽ򵥵ģ���ͼƬ��ʾ�ڻ����ϣ���û��ʵ��Ҫ�ڻ����ϻ�ͼ�Ĺ��� ��
												canvas.drawBitmap(
														mBitmap,
														(mScreenWidth - width) / 2,
														(mScreenHeight - height) / 2,
														null);
											
											canvas.drawText(str_fps, 776, 256, pt);
											
											/*canvas.drawBitmap(forward48, 64, 300, null);
											canvas.drawBitmap(back48, 64, 416, null);
											canvas.drawBitmap(left48, 6, 358, null);
											canvas.drawBitmap(right48, 122, 358, null);
											canvas.drawBitmap(rightview48, 750, 416, null);
											canvas.drawBitmap(leftview48, 660, 416, null);*/
											
											//canvas.drawBitmap(camera64, 8, 8, null);
											//canvas.drawBitmap(photo48, 776, 16, null);
											flag = MyVideo.flag;
											x0 = MyVideo.x0;
											y0 = MyVideo.y0;
											x1 = MyVideo.x1;
											y1 = MyVideo.y1;
											pointerCount = MyVideo.pointerCount; 
											if(flag == 0){
												if(pointerCount == 0) {
													
													canvas.drawBitmap(downview48, 784, 416, null);
													canvas.drawBitmap(upview48, 592, 416, null);
													
												}
												if (pointerCount == 1){
													//ֻ���Ͽ�
													 if((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)){
														
														canvas.drawBitmap(upview64, 584, 408, null);	
														canvas.drawBitmap(downview48, 784, 416, null);
														
													}
													//ֻ���¿�
													 else if((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)){
														
														canvas.drawBitmap(downview64, 776, 408, null);
														canvas.drawBitmap(upview48, 592, 416, null);
														
													}
												
													//ֻ������λ��
													else {
															
															canvas.drawBitmap(downview48, 784, 416, null);
															canvas.drawBitmap(upview48, 592, 416, null);
															
														}
													
												}
												if(pointerCount == 2){
													//�Ͽ����¿�����ϣ��Ͽ������ˡ������Ͽ��Ͽ���
													if(((((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840))) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
														  | ((((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840))) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648))) 
														  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648)))){ 
														  
														canvas.drawBitmap(upview64, 584, 408, null);
														canvas.drawBitmap(downview48, 784, 416, null);
														
														
													}
												    //�Ͽ����¿�����ϣ��¿������ˡ������¿��¿���
													else if(((((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840))) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
														  | ((((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840))) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840))) 
														  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840)))){ 
														  
														canvas.drawBitmap(downview64, 776, 408, null);	
														canvas.drawBitmap(upview48, 592, 416, null);
														
														
													}
													//�Ͽ��¿�������
													else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
														  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648)))){
														
														canvas.drawBitmap(downview48, 784, 416, null);
														canvas.drawBitmap(upview48, 592, 416, null);
													}
											  
												    //��û����
													else {
														
														canvas.drawBitmap(downview48, 784, 416, null);
														canvas.drawBitmap(upview48, 592, 416, null);
														
													}
													
												}
													
												
											}
										if(flag == 1){
										if(pointerCount == 0) {
											 	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
												
											}
										if(pointerCount == 1){
											
											//ֻ��ǰ
											if((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)){
												canvas.drawBitmap(forward64, 200, 24, null);	
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											
											}
											//ֻ���
											else if((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)){
												canvas.drawBitmap(back64, 200, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
											//ֻ����
											 else if((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)){
												canvas.drawBitmap(left64, 8, 216, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											
											}
											//ֻ����
											 else if((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)){
												canvas.drawBitmap(right64, 392, 216, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											
											}
											//ֻ���Ͽ�
											 else if((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)){
												
												canvas.drawBitmap(upview64, 584, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												
											}
											//ֻ���¿�
											 else if((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)){
												
												canvas.drawBitmap(downview64, 776, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										
											//ֻ������λ��
											else {
													canvas.drawBitmap(forward48, 208, 32, null);
													canvas.drawBitmap(back48, 208, 416, null);
													canvas.drawBitmap(left48, 16, 224, null);
													canvas.drawBitmap(right48, 400, 224, null);
													canvas.drawBitmap(downview48, 784, 416, null);
													canvas.drawBitmap(upview48, 592, 416, null);
													
													
												}
											
											
										}
										
										
										
										 if(pointerCount == 2){
											
											 //ǰ�����Ҷ�û����((y0 <= 24) | (y0 >= 88) | (x0 <= 200) | (x0 >= 264)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 200) |(x0 >= 264)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 8) |(x0 >= 72)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 392) |(x0 >= 456))
											 //ǰ�����Ҷ�û����((y1 <= 24) | (y1 >= 88) | (x1 <= 200) | (x1 >= 264)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 200) |(x1 >= 264)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 8) |(x1 >= 72)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 392) |(x1 >= 456))
											                  
											//�󿴡��ҿ�û����((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840))
											//�󿴡��ҿ�û����((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840))
											
											 //ǰ���¿�������
										    if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
										     | (((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)))){
												canvas.drawBitmap(forward64, 200, 24, null);	
												canvas.drawBitmap(downview64, 776, 408, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											
											}
										    //ǰ���Ͽ�������
											else if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
												  | (((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)))){
												canvas.drawBitmap(forward64, 200, 24, null);
												canvas.drawBitmap(upview64, 584, 408, null);	
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												
												
											}
										    //ǰ���ϣ��Ͽ����¿��������Ҷ�û���ϡ�����ǰǰ��
										    else if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & (((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 200) |(x1 >= 264)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 8) |(x1 >= 72)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 392) |(x1 >= 456)))) 
										    	  | (((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)) & (((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 200) |(x0 >= 264)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 8) |(x0 >= 72)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 392) |(x0 >= 456)))) 
										    	  | (((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)))){ 
										    	 
										    	canvas.drawBitmap(forward64, 200, 24, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
											//����¿�������
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
												  | (((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)))){
												canvas.drawBitmap(back64, 200, 408, null);
												canvas.drawBitmap(downview64, 776, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
												
											}
										    //����Ͽ�������
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
												  | (((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)))){
												canvas.drawBitmap(back64, 200, 408, null);
												canvas.drawBitmap(upview64, 584, 408, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												
												
											}
										    //����ϣ��Ͽ����¿���ǰ�����Ҷ�û���ϡ����ߺ��
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & (((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840)) & ((y1 <= 24) | (y1 >= 88) | (x1 <= 200) | (x1 >= 264)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 8) |(x1 >= 72)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 392) |(x1 >= 456)))) 
												  | (((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264)) & (((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840)) & ((y0 <= 24) | (y0 >= 88) | (x0 <= 200) | (x0 >= 264)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 8) |(x0 >= 72)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 392) |(x0 >= 456)))) 
												  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264)))){ 
												  
												canvas.drawBitmap(back64, 200, 408, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
											//����¿�������
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
												  | (((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)))){
												canvas.drawBitmap(left64, 8, 216, null);
												canvas.drawBitmap(downview64, 776, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										    //����Ͽ�������
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
												  | (((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)))){
												canvas.drawBitmap(left64, 8, 216, null);
												canvas.drawBitmap(upview64, 584, 408, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												
											}
										    //����ϣ��Ͽ����¿���ǰ�����Ҷ�û���ϡ���������
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & (((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840)) & ((y1 <= 24) | (y1 >= 88) | (x1 <= 200) | (x1 >= 264)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 200) |(x1 >= 264)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 392) |(x1 >= 456)))) 
												  | (((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72)) & (((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840)) & ((y0 <= 24) | (y0 >= 88) | (x0 <= 200) | (x0 >= 264)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 200) |(x0 >= 264)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 392) |(x0 >= 456)))) 
												  | (((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72)))){ 
												  
												canvas.drawBitmap(left64, 8, 216, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
												
											}
											//�Һ��¿�������
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
												  | (((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)))){
												canvas.drawBitmap(right64, 392, 216, null);	
												canvas.drawBitmap(downview64, 776, 408, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
												
											}
										    //�Һ��Ͽ�������
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
												  | (((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)))){
												canvas.drawBitmap(right64, 392, 216, null);	
												canvas.drawBitmap(upview64, 584, 408, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												
											}
										    //�ҵ��ϣ��Ͽ����¿���ǰ������û���ϡ��������ҡ�
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & (((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840)) & ((y1 <= 24) | (y1 >= 88) | (x1 <= 200) | (x1 >= 264)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 200) |(x1 >= 264)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 8) |(x1 >= 72)))) 
												  | (((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456)) & (((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840)) & ((y0 <= 24) | (y0 >= 88) | (x0 <= 200) | (x0 >= 264)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 200) |(x0 >= 264)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 8) |(x0 >= 72)))) 
												  | (((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456)))){ 
												 
												canvas.drawBitmap(right64, 392, 216, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										    //ǰ�����Һ��ҿ���û���ϣ��Ͽ������ˡ������Ͽ��Ͽ���
											else if(((((y0 <= 24) | (y0 >= 88) | (x0 <= 200) | (x0 >= 264)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 200) |(x0 >= 264)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 8) |(x0 >= 72)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 392) |(x0 >= 456)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840))) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
												  | ((((y1 <= 24) | (y1 >= 88) | (x1 <= 200) | (x1 >= 264)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 200) |(x1 >= 264)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 8) |(x1 >= 72)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 392) |(x1 >= 456)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840))) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648))) 
												  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648)))){ 
												  
												canvas.drawBitmap(upview64, 584, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												
											}
										    //ǰ�����Һ��󿴶�û���ϣ��Ͽ������ˡ������¿��¿���
											else if(((((y0 <= 24) | (y0 >= 88) | (x0 <= 200) | (x0 >= 264)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 200) |(x0 >= 264)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 8) |(x0 >= 72)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 392) |(x0 >= 456)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648))) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
												  | ((((y1 <= 24) | (y1 >= 88) | (x1 <= 200) | (x1 >= 264)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 200) |(x1 >= 264)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 8) |(x1 >= 72)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 392) |(x1 >= 456)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648))) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840))) 
												  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840)))){ 
												  
												canvas.drawBitmap(downview64, 776, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
												
											}
										  //ǰ�Ҷ�����
											else if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456))) 
												  | (((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)))){

												canvas.drawBitmap(forward64, 208, 32, null);
												canvas.drawBitmap(right64, 400, 224, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //���Ҷ�����
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456))) 
												  | (((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264)))){

												canvas.drawBitmap(back64, 208, 416, null);
												canvas.drawBitmap(right64, 400, 224, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //ǰ�󶼵���
											else if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72))) 
												 |  (((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)))){
												
												canvas.drawBitmap(forward64, 208, 32, null);
												canvas.drawBitmap(left64, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //���󶼵���
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72))) 
												  | (((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264)))){

												canvas.drawBitmap(back64, 208, 416, null);
												canvas.drawBitmap(left64, 16, 224, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //ǰ�󶼵���
											else if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264))) 
												  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)))){
												
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //���Ҷ�����
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456))) 
												  | (((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72)))){
												
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //�Ͽ��¿�������
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
												  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648)))){
												
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										    //��û����
											else {
												

												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										
											 
									}
										 
										}
									
										if(flag == 2){
											
										}
								}  
										} catch (Exception e) {  
								            Log.v("MySurfaceView", "draw is Error!");  
								        } finally {//��ע1  
								            if (canvas != null)//��ע2  
								                sfh.unlockCanvasAndPost(canvas);  // ����һ��ͼ�񣬽�������
								        } 
										
											/*sfh.unlockCanvasAndPost(canvas);*/// ����һ��ͼ�񣬽�������
										
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
					Message message = new Message();      
		            message.what = 2;      
		            MyConstant.handler.sendMessage(message);  
					Log.e("Ping", "123456...");
					urlConn.disconnect();
					ex.printStackTrace();
				}
			}

		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		 System.out.println("SurfaceView + changed");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
		
		isThreadRunning=false;
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
		
		 System.out.println("SurfaceView + destroyed");
	}

	public void GetCameraIP(String p) {
		urlstr = p;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		
		
		isThreadRunning=true;
		new DrawVideo().start();
		 System.out.println("SurfaceView + created");
	}
	
	
	
/*	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
  
        int pointCount = event.getpointerCount();  
        System.out.println(pointCount);  
        x0 = event.getX(event.getPointerId(0));  
        y0 = event.getY(event.getPointerId(0));  

        x1 = event.getX(event.getPointerId(1));  
        y1 = event.getY(event.getPointerId(1));

  
            switch (event.getAction()) {  
            	case MotionEvent.ACTION_POINTER_1_DOWN:  
            		
            		System.out.println("ACTION_POINTER_1_DOWN");  
            		
            		break;  
                case MotionEvent.ACTION_POINTER_2_DOWN:  
                	
                    System.out.println("ACTION_POINTER_2_DOWN");  
                    break;  
                case MotionEvent.ACTION_POINTER_1_UP:  
                	x0 = 0;
                	y0 = 0;
                	x1 = 0;
                	y1 = 0;
                    System.out.println("ACTION_POINTER_1_UP");  
                    break;  
                case MotionEvent.ACTION_POINTER_2_UP: 
                	x0 = 0;
                	y0 = 0;
                	x1 = 0;
                	y1 = 0;
                    System.out.println("ACTION_POINTER_2_UP");  
                    break;  
                case MotionEvent.ACTION_MOVE: {    	
                    System.out.println("ACTION_MOVE");  
                    break;  
                }  
                default :
                	break;
            } 
            System.out.println(x0+" "+x1+" "+y0+" "+y1);
            return true;  
    }*/
	
	
	/*@Override
    public boolean onTouchEvent(MotionEvent event) {
        pointerCount = event.getpointerCount();
        int action = event.getAction();
        
        if (pointerCount == 1) {
        	x0 = event.getX(0);
            y0 = event.getY(0);
            switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
    
                System.out.println("ACTION_DOWN pointerCount=" + pointerCount);
                break;
            case MotionEvent.ACTION_UP:
            	x0 = 0;
            	y0 = 0;
                System.out.println("ACTION_UP pointerCount=" + pointerCount);
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("ACTION_MOVE pointerCount=" + pointerCount);
                break;
            }
        }
        if (pointerCount == 2) {
        	x0 = event.getX(0);
            y0 = event.getY(0);
            x1 = event.getX(1);
            y1 = event.getY(1);
            switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("ACTION_DOWN pointerCount=" + pointerCount);
                break;
            case MotionEvent.ACTION_UP:
            	x0 = 0;
            	y0 = 0;
            	x1 = 0;
            	y1 = 0;
                System.out.println("ACTION_UP pointerCount=" + pointerCount);
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println("ACTION_MOVE pointerCount=" + pointerCount);
                break;
            case MotionEvent.ACTION_POINTER_1_DOWN:
                System.out.println("ACTION_POINTER_1_DOWN pointerCount=" + pointerCount);
                break;
            case MotionEvent.ACTION_POINTER_1_UP:
                System.out.println("ACTION_POINTER_1_UP pointerCount=" + pointerCount);
                break;
            case MotionEvent.ACTION_POINTER_2_DOWN:
                System.out.println("ACTION_POINTER_2_DOWN pointerCount=" + pointerCount);
                break;
            case MotionEvent.ACTION_POINTER_2_UP:
                System.out.println("ACTION_POINTER_2_UP pointerCount=" + pointerCount);
                break;
            }
        }
        //return super.onTouchEvent(event);
        return true;
    }*/
}