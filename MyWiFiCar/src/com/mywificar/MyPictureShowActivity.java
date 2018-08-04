package com.mywificar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery.LayoutParams;

public class MyPictureShowActivity extends Activity
/*implements AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory*/
{
	/*
	 * 
	 * 这个类用于显示保存下来的图片，通过gallery控件，将SD卡指定路径中的图片 一一读出，并显示在屏幕上。
	 */
	private List<String> ImageList;
	private String[] list;
	//private ImageSwitcher mSwitcher;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pictureshow);
		//得到图片的存储路径
		ImageList = getSD();
		//将ImageList转换成List数组
		list = ImageList.toArray(new String[ImageList.size()]);

		/*mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		//创建ImageSwitcher是通过工厂来实现的.为ImageSwitcher设置ViewFactory,此this其实就是一个ViewFactory
		mSwitcher.setFactory(this);
		//设置淡入淡出效果
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		mSwitcher.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				// Toast.makeText(BgPictureShowActivity.this, "点击了",
				// Toast.LENGTH_SHORT).show();

			}

		});*/
		//　装载Gallery组件 
		Gallery g = (Gallery) findViewById(R.id.mygallery);
		//创建用于描述图像数据的ImageAdapter对象  并设置Gallery组件的Adapter对象 
		g.setAdapter(new ImageAdapter(this, getSD()));
		//实现onItemSelected方法，为ImageSwitcher组件设置其图片源
		/*g.setOnItemSelectedListener(this);*/
		
		g.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				Toast.makeText(MyPictureShowActivity.this, "第" + (position + 1) + "张照片" + "，共" + list.length + "张照片", Toast.LENGTH_SHORT).show();
			}
		});
	}
	//从SD卡中获取资源图片的路径
	private static List<String> getSD()
	{

		List<String> it = new ArrayList<String>();
		File f = new File("/sdcard/demo/");
		//返回一个文件数组，这个数组包含有这个文件下的目录
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			File file = files[i];
			if (getImageFile(file.getPath()))
				it.add(file.getPath());
		}
		return it;
	}
	//判断是否相应的图片格式
	private static boolean getImageFile(String fName)
	{
		boolean re;
		//取得扩展名
		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
		if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")|| end.equals("JPEG")|| end.equals("PNG")|| end.equals("JPG")|| end.equals("GIF")|| end.equals("BMP"))
		{
			re = true;
		} else
		{
			re = false;
		}
		return re;
	}

	public class ImageAdapter extends BaseAdapter
	{

		int mGalleryItemBackground;
		private Context mContext;
		private List<String> lis;
		public ImageAdapter(Context c, List<String> li)
		{
			mContext = c;
			lis = li;
			//使用res/values/attrs.xml中的<declare-styleable>定义 的Gallery属性,获得对属性集的引用，然后就可以用“a”的各种方法来获取相应的属性值了
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery);
			//取得Gallery属性的Index id（不懂）
			mGalleryItemBackground = a.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
			//让对象的styleable属性能够反复使用
			a.recycle();
		}
		//一定要重写的方法getCount,返回传入的图片ID数组的长度，也就是图片的总数。既然是数组，那么一定要注意越界问题
		public int getCount()
		{
			return lis.size();
		}
		//一定要重写的方法getItem,返回position所对应的图片对象
		public Object getItem(int position)
		{
			return position;
		}
		// 一定要重写的方法getItemId,取得列表中与指定位置的行关联的ID
		public long getItemId(int position)
		{
			return position;
		}
		//一定要重写的方法getView,传回一View对象,返回具体位置的ImageView对象
		//当Gallery组件要显示某一个图像时，就会调用getView方法，并将当前的图像索引（position参数）传入该方法.一般getView方法用于返回每一个显示图像的组件（ImageView对象）
		//系统显示列表时，首先实例化一个适配器（这里将实例化自定义的适配器）。当手动完成适配时，必须手动映射数据，这需要重写getView（）方法
		//position是选择图片是的位置（第几个图片），convertView是移出屏幕的imageView，parent是gallery的布局文件
		public View getView(int position, View convertView, ViewGroup parent)
		{
			
			ImageView i = new ImageView(mContext);
			//显示指定位置图像
			Bitmap bm = BitmapFactory.decodeFile(lis.get(position).toString());
			//设定图片给ImageView对象 
			i.setImageBitmap(bm);
			//把图片不按比例扩大/缩小到View的大小显示,重新设定图片的宽高
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			//不知道设置成480*360有什么用，也不知道是什么效果,重新设定Layout的宽高
			i.setLayoutParams(new Gallery.LayoutParams(480, 360)); 
			//设置Gallery组件的背景风格,设定Gallery背景图
			i.setBackgroundResource(mGalleryItemBackground);
			//传回imageView物件
			return i;
		}
	}
	//实现onItemSelected方法，为ImageSwitcher组件设置其图片源
	//选中Gallery中某个图像时，在ImageSwitcher组件中放大显示该图像
	/*public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		String photoURL = list[position];
		Log.i("A", String.valueOf(position));

		mSwitcher.setImageURI(Uri.parse(photoURL));

	}

	public void onNothingSelected(AdapterView<?> parent)
	{
		// TODO Auto-generated method stub

	}
	// ImageSwitcher组件需要这个方法来创建一个View对象（一般为ImageView对象）
	// 来显示图像
	// makeView返回的是当前需要显示的ImageView控件，用于填充进ImageSwitcher中
	public View makeView()
	{
		ImageView i = new ImageView(this);
		// 设置背景色(黑色)
		i.setBackgroundColor(0xFF000000);
		//设置图片显示的缩放方式，把图片按比例扩大/缩小到View的宽度，居中显示
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		//自适应图片大小，设置显示的图片对相对容器的填充方式
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}*/
	//ScaleType的用法
    //CENTER/center  按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
    //CENTER_CROP/centerCrop  按比例扩大图片的size居中显示，使得图片长 (宽)等于或大于View的长(宽)
    //CENTER_INSIDE/centerInside  将图片的内容完整居中显示，通过按比例缩小 或原来的size使得图片长/宽等于或小于View的长/宽
    //FIT_CENTER/fitCenter  把图片按比例扩大/缩小到View的宽度，居中显示
    //FIT_END/fitEnd   把 图片按比例扩大/缩小到View的宽度，显示在View的下部分位置
    //FIT_START/fitStart  把 图片按比例扩大/缩小到View的宽度，显示在View的上部分位置
    //FIT_XY/fitXY  把图片 不按比例 扩大/缩小到View的大小显示
    //MATRIX/matrix 用矩阵来绘制
}