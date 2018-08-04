package com.mywificar;

import android.R.integer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.os.Build;

import java.net.URL;














import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;

//这个类是第一个界面，在界面中可以输入无线摄像头的获取视频数据流地址 edIP：视频地址文本框 Button_go 启动按钮
public class MyMainFrmActivity extends Activity
{
	private int param = 1;
	private static final String TAG = "mHomeKeyEventReceiver"; 
	EditText CameraIP, ControlIP, Port;
	Button Button_go;
	String videoUrl, controlUrl, port;
	public static String CameraIp;
	private SharedPreferences sharedPrefrences;
	private Editor editor;
	
	
	// 要存储的文件名
	private static final String FILENAME = "filename";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_main_frm);
		
		CameraIP = (EditText) findViewById(R.id.editIP);
		ControlIP = (EditText) findViewById(R.id.ip);
		Port = (EditText) findViewById(R.id.port);
		/*1. MODE_APPEND: 追加方式存储

		2. MODE_PRIVATE: 私有方式存储,其他应用无法访问

		3. MODE_WORLD_READABLE: 表示当前文件可以被其他应用读取

		4. MODE_WORLD_WRITEABLE: 表示当前文件可以被其他应用写入*/
		sharedPrefrences = this.getSharedPreferences(FILENAME, MODE_PRIVATE);
		String r_cameraip = sharedPrefrences.getString("cameraip", "http://192.168.1.1:8080/?action=stream");
	    String r_controlip = sharedPrefrences.getString("controlip", "192.168.1.1");
	    String r_port = sharedPrefrences.getString("port", "2001");
	    CameraIP.setText(r_cameraip);
	    ControlIP.setText(r_controlip);
	    Port.setText(r_port);

		Button_go = (Button) findViewById(R.id.button_go);
		

		// 不清楚获取用户焦点的意义(请求获取焦点)
		//Button_go.requestFocusFromTouch();
		
		Button_go.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{	
				editor = getSharedPreferences(FILENAME, MODE_PRIVATE).edit();
				   String cameraip1=CameraIP.getText().toString();
				   String controlip1=ControlIP.getText().toString();
				   String port1=Port.getText().toString();
				   editor.putString("cameraip", cameraip1);
				   editor.putString("controlip", controlip1);
				   editor.putString("port", port1);
				   editor.commit();
				videoUrl = CameraIP.getText().toString();
				controlUrl = ControlIP.getText().toString();
				port = Port.getText().toString();
				Vibrator vibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
				vibrator.vibrate(new long[]{0,32}, -1);
				if(isOpenNetwork()){
				// TODO Auto-generated method stub
				// 生成一个Intent对象
				Intent intent = new Intent();
				// 在Intent对象当中添加一个键值对
				intent.putExtra("CameraIp", videoUrl);
				intent.putExtra("ControlUrl", controlUrl);
				intent.putExtra("Port", port);
				//在myVideo中没用到缩放
				intent.putExtra("Is_Scale", true);
				// 设置Intent对象要启动的Activity
				intent.setClass(MyMainFrmActivity.this, MyVideo.class);
				// 通过Intent对象启动另外一个Activity
				MyMainFrmActivity.this.startActivity(intent);

				/*
				 * Activity.finish() Call this when your activity is done and
				 * should be closed. 在你的activity动作完成的时候，或者Activity需要关闭的时候，调用此方法。
				 * 当你调用此方法的时候
				 * ，系统只是将最上面的Activity移出了栈，并没有及时的调用onDestory（）方法，其占用的资源也没有被及时释放
				 * 。因为移出了栈，所以当你点击手机上面的“back”按键的时候，也不会再找到这个Activity。
				 * Activity.onDestory() the system is temporarily destroying
				 * this instance of the activityto save space.
				 * 系统销毁了这个Activity的实例在内存中占据的空间。
				 * 在Activity的生命周期中，onDestory()方法是他生命的最后一步
				 * ，资源空间等就被回收了。当重新进入此Activity的时候，必须重新创建，执行onCreate()方法。
				 * System.exit(0) 这玩意是退出整个应用程序的，是针对整个Application的。将整个进程直接KO掉。
				 */

				/*finish();
				System.exit(0);*/
			}
				else{
					initIntener();
				}
			}
		});

	}
	
	/**
	* 对网络连接状态进行判断
	* 
	* @return true, 可用； false， 不可用
	*/
	private boolean isOpenNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
		return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
		}
	
	/**
	*网络可用就调用下一步需要进行的方法， 网络不可用则需设置
	*/
	private void initIntener() {
	// 判断网络是否可用
	if (isOpenNetwork() == true) {
	// 网络可用，则开始加载。
	//这里是我个人程序要进行网络加载的方法，根据自己的程序而定，灵活运用。
	} else {
	AlertDialog.Builder builder = new AlertDialog.Builder(MyMainFrmActivity.this);
	builder.setTitle("没有可用的网络").setMessage("是否对网络进行设置?");


	builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
	@Override
	public void onClick(DialogInterface dialog, int which) {
	Intent intent = null;


	try {
	String sdkVersion = android.os.Build.VERSION.SDK;
	if (Integer.valueOf(sdkVersion) > 10) {
	intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
	} else {
	intent = new Intent();
	ComponentName comp = new ComponentName("com.android.settings",
	"com.android.settings.WirelessSettings");
	intent.setComponent(comp);
	intent.setAction("android.intent.action.VIEW");
	}
	MyMainFrmActivity.this.startActivity(intent);
	} catch (Exception e) {
	// Log.w(TAG,
	// "open network settings failed, please check...");
	e.printStackTrace();
	}
	}
	}).setNegativeButton("否", new DialogInterface.OnClickListener() {
	@Override
	public void onClick(DialogInterface dialog, int which) {
	dialog.cancel();
	//finish();//因为网络不可用的状态，也是不让自己的程序结束运行， 这是根据个人需要设置。
	Toast.makeText(MyMainFrmActivity.this, "网络异常，加载失败！", Toast.LENGTH_SHORT).show();
	//这里是没有网络的时候，又不需要手动设置，则显示出来的一个静态页面，根据个人需要。
	}
	}).show();


	}
	}
	
	 //以下为按一下返回键后，提示“再按一次退出程序”
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		//==的优先级比&&的优先级高
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			   //得到编辑器对象
			   editor = getSharedPreferences(FILENAME, MODE_PRIVATE).edit();
			   String cameraip=CameraIP.getText().toString();
			   String controlip=ControlIP.getText().toString();
			   String port=Port.getText().toString();
			   editor.putString("cameraip", cameraip);
			   editor.putString("controlip", controlip);
			   editor.putString("port", port);
			   editor.commit();
			if ((System.currentTimeMillis() - exitTime) > 2000) 
			// System.currentTimeMillis()无论何时调用，肯定大于2000
			{
				Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else
			{
				finish();
				System.exit(0);
			}

			return true;
		}
		//提供一个返回值（要是没自己写onKeyDown方法，下面的那句就是执行返回）
		return super.onKeyDown(keyCode, event);
	}
	
	//Activity创建或者从后台重新回到前台时被调用  
    @Override  
    protected void onStart() {  
        super.onStart();  
        
       /* System.out.println("start");*/  
    }  
      
    //Activity从后台重新回到前台时被调用  
    @Override  
    protected void onRestart() {  
        super.onRestart(); 
        /*System.out.println("restart");*/  
    }  
      
    //Activity创建或者从被覆盖、后台重新回到前台时被调用  
    @Override  
    protected void onResume() {  
        super.onResume();
        initIntener();
        /*System.out.println("resume");*/  
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
        /*System.out.println("pause");*/  
        //有可能在执行完onPause或onStop后,系统资源紧张将Activity杀死,所以有必要在此保存持久数据  
    }  
      
    //退出当前Activity或者跳转到新Activity时被调用  
    @Override  
    protected void onStop() {  
        super.onStop();  
      /*  System.out.println("stop");*/     
    }  
      
    //退出当前Activity时被调用,调用之后Activity就结束了  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
       
        /*System.out.println("Destroy"); */ 
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
        /*System.out.println("SaveInstanceState");*/  
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
       /* System.out.println("RestoreInstanceState"); */ 
        super.onRestoreInstanceState(savedInstanceState);  
    } 
}
