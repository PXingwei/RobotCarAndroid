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

//������ǵ�һ�����棬�ڽ����п���������������ͷ�Ļ�ȡ��Ƶ��������ַ edIP����Ƶ��ַ�ı��� Button_go ������ť
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
	
	
	// Ҫ�洢���ļ���
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
		/*1. MODE_APPEND: ׷�ӷ�ʽ�洢

		2. MODE_PRIVATE: ˽�з�ʽ�洢,����Ӧ���޷�����

		3. MODE_WORLD_READABLE: ��ʾ��ǰ�ļ����Ա�����Ӧ�ö�ȡ

		4. MODE_WORLD_WRITEABLE: ��ʾ��ǰ�ļ����Ա�����Ӧ��д��*/
		sharedPrefrences = this.getSharedPreferences(FILENAME, MODE_PRIVATE);
		String r_cameraip = sharedPrefrences.getString("cameraip", "http://192.168.1.1:8080/?action=stream");
	    String r_controlip = sharedPrefrences.getString("controlip", "192.168.1.1");
	    String r_port = sharedPrefrences.getString("port", "2001");
	    CameraIP.setText(r_cameraip);
	    ControlIP.setText(r_controlip);
	    Port.setText(r_port);

		Button_go = (Button) findViewById(R.id.button_go);
		

		// �������ȡ�û����������(�����ȡ����)
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
				// ����һ��Intent����
				Intent intent = new Intent();
				// ��Intent���������һ����ֵ��
				intent.putExtra("CameraIp", videoUrl);
				intent.putExtra("ControlUrl", controlUrl);
				intent.putExtra("Port", port);
				//��myVideo��û�õ�����
				intent.putExtra("Is_Scale", true);
				// ����Intent����Ҫ������Activity
				intent.setClass(MyMainFrmActivity.this, MyVideo.class);
				// ͨ��Intent������������һ��Activity
				MyMainFrmActivity.this.startActivity(intent);

				/*
				 * Activity.finish() Call this when your activity is done and
				 * should be closed. �����activity������ɵ�ʱ�򣬻���Activity��Ҫ�رյ�ʱ�򣬵��ô˷�����
				 * ������ô˷�����ʱ��
				 * ��ϵͳֻ�ǽ��������Activity�Ƴ���ջ����û�м�ʱ�ĵ���onDestory������������ռ�õ���ԴҲû�б���ʱ�ͷ�
				 * ����Ϊ�Ƴ���ջ�����Ե������ֻ�����ġ�back��������ʱ��Ҳ�������ҵ����Activity��
				 * Activity.onDestory() the system is temporarily destroying
				 * this instance of the activityto save space.
				 * ϵͳ���������Activity��ʵ�����ڴ���ռ�ݵĿռ䡣
				 * ��Activity�����������У�onDestory()�����������������һ��
				 * ����Դ�ռ�Ⱦͱ������ˡ������½����Activity��ʱ�򣬱������´�����ִ��onCreate()������
				 * System.exit(0) ���������˳�����Ӧ�ó���ģ����������Application�ġ�����������ֱ��KO����
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
	* ����������״̬�����ж�
	* 
	* @return true, ���ã� false�� ������
	*/
	private boolean isOpenNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
		return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
		}
	
	/**
	*������þ͵�����һ����Ҫ���еķ����� ���粻������������
	*/
	private void initIntener() {
	// �ж������Ƿ����
	if (isOpenNetwork() == true) {
	// ������ã���ʼ���ء�
	//�������Ҹ��˳���Ҫ����������صķ����������Լ��ĳ��������������á�
	} else {
	AlertDialog.Builder builder = new AlertDialog.Builder(MyMainFrmActivity.this);
	builder.setTitle("û�п��õ�����").setMessage("�Ƿ�������������?");


	builder.setPositiveButton("��", new DialogInterface.OnClickListener() {
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
	}).setNegativeButton("��", new DialogInterface.OnClickListener() {
	@Override
	public void onClick(DialogInterface dialog, int which) {
	dialog.cancel();
	//finish();//��Ϊ���粻���õ�״̬��Ҳ�ǲ����Լ��ĳ���������У� ���Ǹ��ݸ�����Ҫ���á�
	Toast.makeText(MyMainFrmActivity.this, "�����쳣������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
	//������û�������ʱ���ֲ���Ҫ�ֶ����ã�����ʾ������һ����̬ҳ�棬���ݸ�����Ҫ��
	}
	}).show();


	}
	}
	
	 //����Ϊ��һ�·��ؼ�����ʾ���ٰ�һ���˳�����
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		//==�����ȼ���&&�����ȼ���
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			   //�õ��༭������
			   editor = getSharedPreferences(FILENAME, MODE_PRIVATE).edit();
			   String cameraip=CameraIP.getText().toString();
			   String controlip=ControlIP.getText().toString();
			   String port=Port.getText().toString();
			   editor.putString("cameraip", cameraip);
			   editor.putString("controlip", controlip);
			   editor.putString("port", port);
			   editor.commit();
			if ((System.currentTimeMillis() - exitTime) > 2000) 
			// System.currentTimeMillis()���ۺ�ʱ���ã��϶�����2000
			{
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else
			{
				finish();
				System.exit(0);
			}

			return true;
		}
		//�ṩһ������ֵ��Ҫ��û�Լ�дonKeyDown������������Ǿ����ִ�з��أ�
		return super.onKeyDown(keyCode, event);
	}
	
	//Activity�������ߴӺ�̨���»ص�ǰ̨ʱ������  
    @Override  
    protected void onStart() {  
        super.onStart();  
        
       /* System.out.println("start");*/  
    }  
      
    //Activity�Ӻ�̨���»ص�ǰ̨ʱ������  
    @Override  
    protected void onRestart() {  
        super.onRestart(); 
        /*System.out.println("restart");*/  
    }  
      
    //Activity�������ߴӱ����ǡ���̨���»ص�ǰ̨ʱ������  
    @Override  
    protected void onResume() {  
        super.onResume();
        initIntener();
        /*System.out.println("resume");*/  
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
        /*System.out.println("pause");*/  
        //�п�����ִ����onPause��onStop��,ϵͳ��Դ���Ž�Activityɱ��,�����б�Ҫ�ڴ˱���־�����  
    }  
      
    //�˳���ǰActivity������ת����Activityʱ������  
    @Override  
    protected void onStop() {  
        super.onStop();  
      /*  System.out.println("stop");*/     
    }  
      
    //�˳���ǰActivityʱ������,����֮��Activity�ͽ�����  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
       
        /*System.out.println("Destroy"); */ 
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
        /*System.out.println("SaveInstanceState");*/  
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
       /* System.out.println("RestoreInstanceState"); */ 
        super.onRestoreInstanceState(savedInstanceState);  
    } 
}
