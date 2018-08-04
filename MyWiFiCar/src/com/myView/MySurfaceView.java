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
	//在 创建SurfaceView子类时需要注意，如果该子类不会被应用于layout.xml配置文件中，则子类只需要实现 SurfaceView(Context context)构造即可；如果该子类需要被应用于layout.xml配置文件中，则还必须实现SurfaceView(Context context,AttributeSet attrs)构造函数，并且该函数必须具有public访问修饰符。
	//构造函数
	public MySurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		MyConstant.context = context;
		initialize();
		
		p = new Paint();
		//设置画笔无锯齿(如果不设置可以看到效果很差)
		p.setAntiAlias(true);
		/*SurfaceHolder是一个接口，其作用就像一个关于Surface的监听器。
		提供访问和控制SurfaceView背后的Surface 相关的方法 （providingaccess and control over this SurfaceView's underlying surface），
		它通过三个回调方法，让我们可以感知到Surface的创建、销毁或者改变。
		在SurfaceView中有一个方法getHolder，可以很方便地获得SurfaceView所对应的Surface所对应的SurfaceHolder*/
		sfh = this.getHolder();
		//为SurfaceHolder添加一个SurfaceHolder.Callback回调接口
		/*callback接口:
			 只要继承SurfaceView类并实现SurfaceHolder.Callback接口就可以实现一个自定义的SurfaceView了，SurfaceHolder.Callback在底层的Surface状态发生变化的时候通知View，SurfaceHolder.Callback具有如下的接口：
			 surfaceCreated(SurfaceHolder holder)：当Surface第一次创建后会立即调用该函数。程序可以在该函数中做些和绘制界面相关的初始化工作，一般情况下都是在另外的线程来绘制界面，所以不要在这个函数中绘制Surface。
			 surfaceChanged(SurfaceHolder holder, int format, int width,int height)：当Surface的状态（大小和格式）发生变化的时候会调用该函数，在surfaceCreated调用后该函数至少会被调用一次。*/
		sfh.addCallback(this);
		//设置背景常亮  
		this.setKeepScreenOn(true);
		//允许此控件获取焦点
		setFocusable(true);
		//下面两个好像没什么用 
		this.getWidth();
		this.getHeight();
		//匿名内部类
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
						Toast.makeText(MyConstant.context, "拍照失败，未能获取摄像头图像！",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case 2:
					Toast.makeText(MyConstant.context, "视频连接断开！", Toast.LENGTH_SHORT).show();
					z = 0;
					break;
				case 3:
					//Toast.makeText(MyConstant.context, "视频连接成功！", Toast.LENGTH_SHORT).show();
					z = 1;
					break;
				 default: 
				 break; 
				
				}
				//空方法，这里最好对不需要或者不关心的消息抛给父类，避免丢失消息
				super.handleMessage(msg);
			}

		};

	}
	//预定的初值
	private void initialize() {
		//感觉没新创建对象就调用了方法
		/*获得屏幕尺寸的方法
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
		// 保持屏幕常亮
		this.setKeepScreenOn(true);
	}
	public void saveMyBitmap(String bitName, Bitmap bmp1) {
		
			File f = new File("/sdcard/demo/");
			//判断文件是否存在，判断你指定的路径或着指定的目录文件是否已经存在
			if (!f.exists()) {
				//创建一个目录，它的路径名由当前 File 对象指定，包括任一必须的父路径。 如果该目录(或多级目录)能被创建则为 true；否则为 false。
				f.mkdirs();
			}
			f = new File("/sdcard/demo/" + bitName+System.currentTimeMillis() + ".JPEG");
			try {
				//在指定路径上创建文件
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block

			}
			//创建字节输出流对象
			FileOutputStream fOut = null;
			try {
				//创建字节输出流
				fOut = new FileOutputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Log.d("MySurface", "bitmap is:" + bmp1 + "fout is:" + fOut);
			//可以用Bitmap.compress函数来把Bitmap对象保存成PNG或JPG文件，然后可以用BitmapFactory把文件中的数据读进来再生成Bitmap对象。
			bmp1.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			try {
				//用流的时候，往往有缓冲区，只有缓冲区满的时候，才会将缓冲区里的数据写入到文件中去，只要文件不满或是没有调用flush或是close方法的话，是没法将不满缓冲区的数据写入文件中去的.
				//所以，当你使用流时，又不能用一次就close的话，一定要每次使用append或print或write的之后，要flush一下，保证数据都能写入文件当中去。
				//至于具体用法，很简单，流对象.flush()就可以了。
				//主要用在IO中，即清空缓冲区数据，一般在读写流(stream)的时候，数据是先被读到了内存中，再把数据写到文件中，当你数据读完的时候不代表你的数据已经写完了，因为还有一部分有可能会留在内存这个缓冲区中。这时候如果你调用了close()方法关闭了读写流，那么这部分数据就会丢失，所以应该在关闭读写流之前先flush()。
				fOut.flush();
				Toast.makeText(MyConstant.context, "拍照成功！路径：/SDCard/Demo/",
						Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}

	class DrawVideo extends Thread {
		//构造函数
		public DrawVideo() {
		}

		@Override
		//不知道克隆有什么用
		/*浅复制：将对象在栈中的数据复制一份。
		结果：对对象的基本类型和String（特殊的引用对象）属性变更时不影响clone对象，而对引用属性变更时会影响clone对象，所以想要实现深复制就要自己重写clone方法。*/
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			
			return super.clone();
		}
		public void run() {
			Paint pt = new Paint();
			//设置画笔无锯齿(如果不设置可以看到效果很差)
			pt.setAntiAlias(true);
			//设置绘制的颜色，使用颜色值来表示，该颜色值包括透明度和RGB颜色
			pt.setColor(Color.GREEN);
			//设置绘制文字的字号大小
			pt.setTextSize(20);
			//设置pt的style为STROKE：空心的
			//pt.setStyle(Paint.Style.STROKE);
			//当画笔样式为STROKE(空心画笔)或FILL_OR_STROKE时，设置笔刷的粗细度  
			pt.setStrokeWidth(1);
			// 视频图片缓冲
			int bufSize = 512 * 1024; 
			byte[] jpg_buf = new byte[bufSize]; // buffer to read jpg
			// 每次最大获取的流
			int readSize = 4096; 
			byte[] buffer = new byte[readSize]; // buffer to read stream

			while (isThreadRunning) {
				long Time = 0;
				long Span = 0;
				int fps = 0;
				String str_fps = "0 fps";

				try {
					url = new URL(urlstr);
					// 使用HTTPURLConnetion打开连接
					urlConn = (HttpURLConnection) url.openConnection(); 
					Time = System.currentTimeMillis();

					int read = 0;
					int status = 0;
					// jpg数据下标
					int jpg_count = 0; 

					while (true) {
						
						urlConn.setConnectTimeout(1000);
						//read (byte[] b,int off,int len) 方法， 将输入流中最多 len 个数据字节读入 byte 数组。尝试读取 len 个字节，但读取的字节也可能小于该值。以整数形式返回实际读取的字节数。返回-1表示流读取完了
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

										fps++;
										Span = System.currentTimeMillis()
												- Time;
										if (Span > 1000L) {
											Time = System.currentTimeMillis();
											str_fps = String.valueOf(fps)
													+ " fps";
											fps = 0;
										}
										// 显示图像
										//if (null != canvas)
										try {  
								           
											/*SurfaceHolder 类：
											它是一个用于控制surface的接口，它提供了控制surface 的大小，格式，上面的像素，即监视其改变的。 
											SurfaceView的getHolder()函数可以获取SurfaceHolder对象，Surface 就在SurfaceHolder对象内。虽然Surface保存了当前窗口的像素数据，但是在使用过程中是不直接和Surface打交道的，由SurfaceHolder的Canvas lockCanvas()或则Canvas lockCanvas()函数来获取Canvas对象，通过在Canvas上绘制内容来修改Surface中的数据。如果Surface不可编辑或则尚未创建调用该函数会返回null，在 unlockCanvas() 和 lockCanvas()中Surface的内容是不缓存的，所以需要完全重绘Surface的内容，为了提高效率只重绘变化的部分则可以调用lockCanvas(Rect rect)函数来指定一个rect区域，这样该区域外的内容会缓存起来。在调用lockCanvas函数获取Canvas后，SurfaceView会获取Surface的一个同步锁直到调用unlockCanvasAndPost(Canvas canvas)函数才释放该锁，这里的同步机制保证在Surface绘制过程中不会被改变（被摧毁、修改）。*/
											//得到一个canvas实例 
											canvas = sfh.lockCanvas();
											if (canvas != null) {
											//设置背景为黑色,刷屏
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
												
												//不知道第三个参数是什么意思,根据给定的 Bitmap 创建 一个新的，缩放后的 Bitmap 
												mBitmap = Bitmap
														.createScaledBitmap(bmp,
																width, height,
																false);
												bmp1 =  Bitmap
														.createScaledBitmap(bmp,
																640, 480,
																false);
												//canvas.drawbitmap（）函数你可以理解为新建一个画布， 就好比画家的画板，当然你不往里面添加东西，就什么都没有，它是空的。现在放入参数， bitmap就是导入你已有的图片，比如，画家上次还没画完的一张图片，现在放在画板上，要继续工作。参数，0，0，为设置这张画在画板上的放置的位置，0，0为x轴和y轴的坐标，如果是（0,0），则默认为从从左上角开始放置。下面的一张图片为设置不同参数的时候，这种那个图片放置在这个画板上的位置的变化，你可以看到我设置为（100,10），也就是x坐标100，图片离左边距离为100；y坐标10，图片离上方距离为10，你可以看到左边黑色的部分占了100，上边部分的黑色占了10.最后，就是那个null，null的参数是用来设置画笔的，你没有画笔，当然就不用设了，你这个只是简单的，见图片显示在画板上，并没有实现要在画板上画图的功能 。
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
													//只点上看
													 if((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)){
														
														canvas.drawBitmap(upview64, 584, 408, null);	
														canvas.drawBitmap(downview48, 784, 416, null);
														
													}
													//只点下看
													 else if((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)){
														
														canvas.drawBitmap(downview64, 776, 408, null);
														canvas.drawBitmap(upview48, 592, 416, null);
														
													}
												
													//只点其它位置
													else {
															
															canvas.drawBitmap(downview48, 784, 416, null);
															canvas.drawBitmap(upview48, 592, 416, null);
															
														}
													
												}
												if(pointerCount == 2){
													//上看和下看外点上，上看点上了。或者上看上看。
													if(((((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840))) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
														  | ((((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840))) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648))) 
														  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648)))){ 
														  
														canvas.drawBitmap(upview64, 584, 408, null);
														canvas.drawBitmap(downview48, 784, 416, null);
														
														
													}
												    //上看和下看外点上，下看点上了。或者下看下看。
													else if(((((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840))) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
														  | ((((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840))) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840))) 
														  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840)))){ 
														  
														canvas.drawBitmap(downview64, 776, 408, null);	
														canvas.drawBitmap(upview48, 592, 416, null);
														
														
													}
													//上看下看都点上
													else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
														  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648)))){
														
														canvas.drawBitmap(downview48, 784, 416, null);
														canvas.drawBitmap(upview48, 592, 416, null);
													}
											  
												    //都没点上
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
											
											//只点前
											if((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)){
												canvas.drawBitmap(forward64, 200, 24, null);	
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											
											}
											//只点后
											else if((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)){
												canvas.drawBitmap(back64, 200, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
											//只点左
											 else if((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)){
												canvas.drawBitmap(left64, 8, 216, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											
											}
											//只点右
											 else if((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)){
												canvas.drawBitmap(right64, 392, 216, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											
											}
											//只点上看
											 else if((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)){
												
												canvas.drawBitmap(upview64, 584, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												
											}
											//只点下看
											 else if((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)){
												
												canvas.drawBitmap(downview64, 776, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										
											//只点其它位置
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
											
											 //前后左右都没点上((y0 <= 24) | (y0 >= 88) | (x0 <= 200) | (x0 >= 264)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 200) |(x0 >= 264)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 8) |(x0 >= 72)) & ((y0 <= 216) | (y0 >= 280) | (x0 <= 392) |(x0 >= 456))
											 //前后左右都没点上((y1 <= 24) | (y1 >= 88) | (x1 <= 200) | (x1 >= 264)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 200) |(x1 >= 264)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 8) |(x1 >= 72)) & ((y1 <= 216) | (y1 >= 280) | (x1 <= 392) |(x1 >= 456))
											                  
											//左看、右看没点上((y0 <= 408) | (y0 >= 472) | (x0 <= 584) | (x0 >= 648)) & ((y0 <= 408) | (y0 >= 472) | (x0 <= 776) | (x0 >= 840))
											//左看、右看没点上((y1 <= 408) | (y1 >= 472) | (x1 <= 584) | (x1 >= 648)) & ((y1 <= 408) | (y1 >= 472) | (x1 <= 776) | (x1 >= 840))
											
											 //前和下看都点上
										    if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
										     | (((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)))){
												canvas.drawBitmap(forward64, 200, 24, null);	
												canvas.drawBitmap(downview64, 776, 408, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											
											}
										    //前和上看都点上
											else if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
												  | (((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)))){
												canvas.drawBitmap(forward64, 200, 24, null);
												canvas.drawBitmap(upview64, 584, 408, null);	
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												
												
											}
										    //前点上，上看、下看、后、左、右都没点上。或者前前。
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
											//后和下看都点上
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
												  | (((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)))){
												canvas.drawBitmap(back64, 200, 408, null);
												canvas.drawBitmap(downview64, 776, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
												
											}
										    //后和上看都点上
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
												  | (((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)))){
												canvas.drawBitmap(back64, 200, 408, null);
												canvas.drawBitmap(upview64, 584, 408, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												
												
											}
										    //后点上，上看、下看、前、左、右都没点上。或者后后。
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
											//左和下看都点上
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
												  | (((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)))){
												canvas.drawBitmap(left64, 8, 216, null);
												canvas.drawBitmap(downview64, 776, 408, null);	
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										    //左和上看都点上
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
												  | (((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)))){
												canvas.drawBitmap(left64, 8, 216, null);
												canvas.drawBitmap(upview64, 584, 408, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												
											}
										    //左点上，上看、下看、前、后、右都没点上。或者左左。
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
											//右和下看都点上
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
												  | (((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)))){
												canvas.drawBitmap(right64, 392, 216, null);	
												canvas.drawBitmap(downview64, 776, 408, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
												
											}
										    //右和上看都点上
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648))) 
												  | (((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456)) & ((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)))){
												canvas.drawBitmap(right64, 392, 216, null);	
												canvas.drawBitmap(upview64, 584, 408, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												
											}
										    //右点上，上看、下看、前、后、左都没点上。或者右右。
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
										    //前后左右和右看都没点上，上看点上了。或者上看上看。
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
										    //前后左右和左看都没点上，上看点上了。或者下看下看。
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
										  //前右都点上
											else if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456))) 
												  | (((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)))){

												canvas.drawBitmap(forward64, 208, 32, null);
												canvas.drawBitmap(right64, 400, 224, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //后右都点上
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456))) 
												  | (((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264)))){

												canvas.drawBitmap(back64, 208, 416, null);
												canvas.drawBitmap(right64, 400, 224, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //前左都点上
											else if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72))) 
												 |  (((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)))){
												
												canvas.drawBitmap(forward64, 208, 32, null);
												canvas.drawBitmap(left64, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //后左都点上
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72))) 
												  | (((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264)))){

												canvas.drawBitmap(back64, 208, 416, null);
												canvas.drawBitmap(left64, 16, 224, null);
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //前后都点上
											else if((((y0 >= 24) & (y0 <= 88) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 200) & (x1 <= 264))) 
												  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 200) & (x0 <= 264)) & ((y1 >= 24) & (y1 <= 88) & (x1 >= 200) & (x1 <= 264)))){
												
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //左右都点上
											else if((((y0 >= 216) & (y0 <= 280) & (x0 >= 8) & (x0 <= 72)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 392) & (x1 <= 456))) 
												  | (((y0 >= 216) & (y0 <= 280) & (x0 >= 392) & (x0 <= 456)) & ((y1 >= 216) & (y1 <= 280) & (x1 >= 8) & (x1 <= 72)))){
												
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										  //上看下看都点上
											else if((((y0 >= 408) & (y0 <= 472) & (x0 >= 584) & (x0 <= 648)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 776) & (x1 <= 840))) 
												  | (((y0 >= 408) & (y0 <= 472) & (x0 >= 776) & (x0 <= 840)) & ((y1 >= 408) & (y1 <= 472) & (x1 >= 584) & (x1 <= 648)))){
												
												canvas.drawBitmap(forward48, 208, 32, null);
												canvas.drawBitmap(back48, 208, 416, null);
												canvas.drawBitmap(left48, 16, 224, null);
												canvas.drawBitmap(right48, 400, 224, null);
												canvas.drawBitmap(downview48, 784, 416, null);
												canvas.drawBitmap(upview48, 592, 416, null);
												
											}
										    //都没点上
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
								        } finally {//备注1  
								            if (canvas != null)//备注2  
								                sfh.unlockCanvasAndPost(canvas);  // 画完一副图像，解锁画布
								        } 
										
											/*sfh.unlockCanvasAndPost(canvas);*/// 画完一副图像，解锁画布
										
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