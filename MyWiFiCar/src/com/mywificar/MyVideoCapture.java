package com.mywificar;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.googlecode.javacv.FFmpegFrameRecorder;
import com.googlecode.javacv.FrameRecorder.Exception;
import com.googlecode.javacv.cpp.opencv_core;
import com.myUtil.MyFileUtils;
import com.myUtil.MyFileUtils.NoSdcardException;

public class MyVideoCapture {
	private static int switcher = 0;// 录像键
	private static boolean isPaused = false;// 暂停键
	private static String filepath = "";
	private static String filename = null;
	private static Context context;

	public static void genMp4(Context context, String path, final int maxIndex,
			final double frameRate,final Handler handler) {
		// init
		System.out.println("maxIndex" + maxIndex);
		MyVideoCapture.context = context;
		if (path != null) {
			filepath = path;
		}
		filename = "test_" + System.currentTimeMillis() + ".mp4";
		String temp = null;
		try {
			temp = new MyFileUtils().getSDCardRoot() + "ScreenRecord"
					+ File.separator + filename;
			/*temp = new MyFileUtils().getSDCardRoot() + "demo"
					+ File.separator + filename;*/
		} catch (NoSdcardException e1) {
			e1.printStackTrace();
		}
		final String savePath = temp;

		switcher = 1;
		new Thread(new Runnable() {

			@Override
			public void run() {
				Log.d("test", "开始将图片转成视频啦...frameRate=" + frameRate);
				try {
					new MyFileUtils().creatSDDir(filepath);
					
					handler.sendMessage(handler.obtainMessage(MyVideo.MSG_STATE,"开始将图片转成视频，请稍后...."));

					String tempFilePath = new MyFileUtils().getSDCardRoot()
							+ filepath + File.separator;
					Log.i("test", "tempFilePath=" + tempFilePath);
					Bitmap testBitmap = getImageByPath(tempFilePath
							+ "videoTemp_0" + MyVideo.IMAGE_TYPE);

					FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(
							savePath, testBitmap.getWidth(),
							testBitmap.getHeight());
					System.out.println("record" + 1);
					
					recorder.setFormat("mp4");
					
					System.out.println("record" + 2);
					
					recorder.setFrameRate(frameRate);// 录像帧率
					
					System.out.println("record" + 3);
					
					recorder.start();
					
					System.out.println("record" + 4);

					int index = 0;
					while (index < maxIndex) {
						opencv_core.IplImage image = cvLoadImage(tempFilePath
								+ "videoTemp_" + index
								+ MyVideo.IMAGE_TYPE);
						recorder.record(image);
						System.out.println("index++" + index);
						index++;
					}
					Log.d("test", "录制完成....");
					handler.sendMessage(handler.obtainMessage(MyVideo.MSG_STATE,"保存视频成功"));
					recorder.stop();
					handler.sendMessage(handler.obtainMessage(MyVideo.MSG_SAVE_SUCCESS, savePath));
					MyFileUtils.deleteDirectoryContent(tempFilePath);
				} catch (MyFileUtils.NoSdcardException e) {
					e.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void stop() {
		switcher = 0;
		isPaused = false;
	}

	public static void pause() {
		if (switcher == 1) {
			isPaused = true;
		}
	}

	public static void restart() {
		if (switcher == 1) {
			isPaused = false;
		}
	}

	public static boolean isStarted() {
		if (switcher == 1) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isPaused() {
		return isPaused;
	}

	private static Bitmap getImageFromAssetsFile(String filename) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(filename);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	private static Bitmap getImageByPath(String path) {
		return BitmapFactory.decodeFile(path);
	}
}
