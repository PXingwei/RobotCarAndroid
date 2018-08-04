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
	 * �����������ʾ����������ͼƬ��ͨ��gallery�ؼ�����SD��ָ��·���е�ͼƬ һһ����������ʾ����Ļ�ϡ�
	 */
	private List<String> ImageList;
	private String[] list;
	//private ImageSwitcher mSwitcher;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pictureshow);
		//�õ�ͼƬ�Ĵ洢·��
		ImageList = getSD();
		//��ImageListת����List����
		list = ImageList.toArray(new String[ImageList.size()]);

		/*mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		//����ImageSwitcher��ͨ��������ʵ�ֵ�.ΪImageSwitcher����ViewFactory,��this��ʵ����һ��ViewFactory
		mSwitcher.setFactory(this);
		//���õ��뵭��Ч��
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
		mSwitcher.setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				// Toast.makeText(BgPictureShowActivity.this, "�����",
				// Toast.LENGTH_SHORT).show();

			}

		});*/
		//��װ��Gallery��� 
		Gallery g = (Gallery) findViewById(R.id.mygallery);
		//������������ͼ�����ݵ�ImageAdapter����  ������Gallery�����Adapter���� 
		g.setAdapter(new ImageAdapter(this, getSD()));
		//ʵ��onItemSelected������ΪImageSwitcher���������ͼƬԴ
		/*g.setOnItemSelectedListener(this);*/
		
		g.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				Toast.makeText(MyPictureShowActivity.this, "��" + (position + 1) + "����Ƭ" + "����" + list.length + "����Ƭ", Toast.LENGTH_SHORT).show();
			}
		});
	}
	//��SD���л�ȡ��ԴͼƬ��·��
	private static List<String> getSD()
	{

		List<String> it = new ArrayList<String>();
		File f = new File("/sdcard/demo/");
		//����һ���ļ����飬����������������ļ��µ�Ŀ¼
		File[] files = f.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			File file = files[i];
			if (getImageFile(file.getPath()))
				it.add(file.getPath());
		}
		return it;
	}
	//�ж��Ƿ���Ӧ��ͼƬ��ʽ
	private static boolean getImageFile(String fName)
	{
		boolean re;
		//ȡ����չ��
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
			//ʹ��res/values/attrs.xml�е�<declare-styleable>���� ��Gallery����,��ö����Լ������ã�Ȼ��Ϳ����á�a���ĸ��ַ�������ȡ��Ӧ������ֵ��
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery);
			//ȡ��Gallery���Ե�Index id��������
			mGalleryItemBackground = a.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
			//�ö����styleable�����ܹ�����ʹ��
			a.recycle();
		}
		//һ��Ҫ��д�ķ���getCount,���ش����ͼƬID����ĳ��ȣ�Ҳ����ͼƬ����������Ȼ�����飬��ôһ��Ҫע��Խ������
		public int getCount()
		{
			return lis.size();
		}
		//һ��Ҫ��д�ķ���getItem,����position����Ӧ��ͼƬ����
		public Object getItem(int position)
		{
			return position;
		}
		// һ��Ҫ��д�ķ���getItemId,ȡ���б�����ָ��λ�õ��й�����ID
		public long getItemId(int position)
		{
			return position;
		}
		//һ��Ҫ��д�ķ���getView,����һView����,���ؾ���λ�õ�ImageView����
		//��Gallery���Ҫ��ʾĳһ��ͼ��ʱ���ͻ����getView������������ǰ��ͼ��������position����������÷���.һ��getView�������ڷ���ÿһ����ʾͼ��������ImageView����
		//ϵͳ��ʾ�б�ʱ������ʵ����һ�������������ｫʵ�����Զ�����������������ֶ��������ʱ�������ֶ�ӳ�����ݣ�����Ҫ��дgetView��������
		//position��ѡ��ͼƬ�ǵ�λ�ã��ڼ���ͼƬ����convertView���Ƴ���Ļ��imageView��parent��gallery�Ĳ����ļ�
		public View getView(int position, View convertView, ViewGroup parent)
		{
			
			ImageView i = new ImageView(mContext);
			//��ʾָ��λ��ͼ��
			Bitmap bm = BitmapFactory.decodeFile(lis.get(position).toString());
			//�趨ͼƬ��ImageView���� 
			i.setImageBitmap(bm);
			//��ͼƬ������������/��С��View�Ĵ�С��ʾ,�����趨ͼƬ�Ŀ��
			i.setScaleType(ImageView.ScaleType.FIT_XY);
			//��֪�����ó�480*360��ʲô�ã�Ҳ��֪����ʲôЧ��,�����趨Layout�Ŀ��
			i.setLayoutParams(new Gallery.LayoutParams(480, 360)); 
			//����Gallery����ı������,�趨Gallery����ͼ
			i.setBackgroundResource(mGalleryItemBackground);
			//����imageView���
			return i;
		}
	}
	//ʵ��onItemSelected������ΪImageSwitcher���������ͼƬԴ
	//ѡ��Gallery��ĳ��ͼ��ʱ����ImageSwitcher����зŴ���ʾ��ͼ��
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
	// ImageSwitcher�����Ҫ�������������һ��View����һ��ΪImageView����
	// ����ʾͼ��
	// makeView���ص��ǵ�ǰ��Ҫ��ʾ��ImageView�ؼ�����������ImageSwitcher��
	public View makeView()
	{
		ImageView i = new ImageView(this);
		// ���ñ���ɫ(��ɫ)
		i.setBackgroundColor(0xFF000000);
		//����ͼƬ��ʾ�����ŷ�ʽ����ͼƬ����������/��С��View�Ŀ�ȣ�������ʾ
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		//����ӦͼƬ��С��������ʾ��ͼƬ�������������䷽ʽ
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}*/
	//ScaleType���÷�
    //CENTER/center  ��ͼƬ��ԭ��size������ʾ����ͼƬ��/����View�ĳ�/�����ȡͼƬ�ľ��в�����ʾ
    //CENTER_CROP/centerCrop  ����������ͼƬ��size������ʾ��ʹ��ͼƬ�� (��)���ڻ����View�ĳ�(��)
    //CENTER_INSIDE/centerInside  ��ͼƬ����������������ʾ��ͨ����������С ��ԭ����sizeʹ��ͼƬ��/����ڻ�С��View�ĳ�/��
    //FIT_CENTER/fitCenter  ��ͼƬ����������/��С��View�Ŀ�ȣ�������ʾ
    //FIT_END/fitEnd   �� ͼƬ����������/��С��View�Ŀ�ȣ���ʾ��View���²���λ��
    //FIT_START/fitStart  �� ͼƬ����������/��С��View�Ŀ�ȣ���ʾ��View���ϲ���λ��
    //FIT_XY/fitXY  ��ͼƬ �������� ����/��С��View�Ĵ�С��ʾ
    //MATRIX/matrix �þ���������
}