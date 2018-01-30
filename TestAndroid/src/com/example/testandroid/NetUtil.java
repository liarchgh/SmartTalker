package com.example.testandroid;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class NetUtil {
	public static void doGetMusic(final String url, final String filePath) throws IOException {
		//use url and parameter to get realurl
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try {
					//open connection
					URL apiUrl = new URL(url);
					HttpURLConnection huc = (HttpURLConnection)apiUrl.openConnection();
					huc.setRequestMethod("GET");
					huc.setConnectTimeout(5000);
					huc.connect();

			//		File file = new File(filePath);
			//Log.i("file path", filePath);
			//		if(!file.exists()) {
			//			if(file.mkdir()) {
			//				return;
			//			}
			//		}
			//		OutputStreamWriter osm = new OutputStreamWriter(new FileOutputStream(file));
					FileOutputStream osm = new FileOutputStream(filePath);

					//receive data as data
					InputStream is = huc.getInputStream();
					byte[] byar = new byte[1024];
			//		StringBuffer res = new StringBuffer();

					while(true) {
						int len = is.read(byar);
//System.out.println(len);
						if(len <= 0) {
							break;
						}
						osm.write(byar, 0, len);
			//			res.append(new String(byar));
					}
					osm.flush();
					osm.close();
					is.close();
			//		return res.toString();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static String doGetString(String url, Map<String, String> paras) throws IOException {
		// use url and parameter to get realurl
		StringBuffer realUrl = new StringBuffer(url);
		if (paras != null) {
			for (String key : paras.keySet()) {
				if (realUrl.length() > url.length() + 1) {
					realUrl.append('&');
				} else {
					realUrl.append('&');
				}
				realUrl.append(key + "=" + paras.get(key));
			}
		}

		// open connection
		URL apiUrl = new URL(realUrl.toString());
		HttpURLConnection huc = (HttpURLConnection) apiUrl.openConnection();
//		URLConnection huc = apiUrl.openConnection();

//		if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
//			System.setProperty("http.keepAlive", "false");
//		}
		huc.setRequestProperty("charset", "utf-8");
		huc.setRequestProperty("Accept-Encoding", "utf-8");
//		huc.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		huc.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
//		huc.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//		huc.setRequestProperty("Connection", "keep-alive");
//		huc.setRequestProperty("Host", "api.imjad.cn");
//		huc.setRequestProperty("Upgrade-Insecure-Requests", "1");
//				User-Agent	Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36
		huc.setRequestMethod("GET");
		huc.connect();

		// receive data as data
		InputStreamReader os = new InputStreamReader(huc.getInputStream());
		char[] byar = new char[1024];
		StringBuffer res = new StringBuffer();
		while (true) {
//			int len = os.read(byar, 0, byar.length);
			int len = os.read(byar);
			if (len <= 0) {
				break;
			}
			res.append(new String(byar, 0, len));
		}
		return res.toString();
	}

	public static String doPostString(String url, Map<String, String> paras) throws IOException {
		URL apiUrl = new URL(url);
		HttpURLConnection uc = (HttpURLConnection) apiUrl.openConnection();
		uc.setRequestMethod("POST");
		// uc.setUseCaches(false);

		uc.setReadTimeout(5000);
		uc.setConnectTimeout(5000);
		uc.setDoOutput(true);
		uc.setDoInput(true);

		OutputStream os = uc.getOutputStream();
		StringBuffer parasString = new StringBuffer();
		for (String key : paras.keySet()) {
			if (parasString.length() > 0) {
				parasString.append('&');
			}
			parasString.append(key + "=" + paras.get(key));
		}

		try {
			os.write(parasString.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// uc.setRequestProperty("accept", "*/*");
		// uc.setRequestProperty("connection", "Keep-Alive");
		// jj uc.setRequestProperty("user-agent",
		// "Mozilla/4.0 (compatible; MSIE 6.0; iindows NT 5.1;SV1)");
		// uc.setDoOutput(true);
		// uc.setDoInput(true);

		// uc.setRequestProperty(sentence, value);
		// uc.setRequestProperty("key", key);
		// uc.setRequestProperty("info", sentence);
		// uc.setRequestProperty("loc", loc);
		// uc.setRequestProperty("userid", ""+userid);
		// uc.connect();

		BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String temp = null;
		StringBuffer res = new StringBuffer();
		while ((temp = br.readLine()) != null) {
			res.append(temp);
		}
		uc.disconnect();
		return res.toString();
	}

	public static byte[] doGetByteArray(String url, Map<String, String> paras) throws IOException {
Log.i("music", "do get ba");
		// use url and parameter to get realurl
		StringBuffer realUrl = new StringBuffer(url);
		if (paras != null) {
			for (String key : paras.keySet()) {
				if (realUrl.length() > url.length() + 1) {
					realUrl.append('&');
				} else {
					realUrl.append('&');
				}
				realUrl.append(key + "=" + paras.get(key));
			}
		}

		// open connection
		URL apiUrl = new URL(realUrl.toString());
		HttpURLConnection huc = (HttpURLConnection) apiUrl.openConnection();
		huc.setRequestProperty("charset", "utf-8");
		huc.setRequestProperty("Accept-Encoding", "utf-8");
		huc.setRequestMethod("GET");
		huc.connect();

		// receive data as data
		InputStream is = huc.getInputStream();
		byte[] byar = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		Bitmap bm = Bitmap.createBitmap();
//		bm.compress(format, quality, stream)
		while (true) {
			int len = is.read(byar);
			if (len <= 0) {
Log.i("music", "len:"+len);
				break;
			}
//			res.append(new String(byar, 0, len));
			bos.write(byar, 0, len);
		}
//		return BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.size());
		return bos.toByteArray();
	}
	
	public static Bitmap doGetBitmap(String url, Map<String, String> paras) {
		byte[] ba = null;
		try {
			ba = doGetByteArray(url, paras);
Log.i("music", "image ba size:"+ba.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ba == null) {
			return null;
		}
		return BitmapFactory.decodeByteArray(ba, 0, ba.length);
	}
}
