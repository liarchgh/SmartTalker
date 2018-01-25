package com.neo.mytalker.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class NetUtil {

	public static String doGetString(String url, Map<String, String>paras) throws IOException {
		//use url and parameter to get realurl
		StringBuffer realUrl = new StringBuffer(url);
		if(paras != null) {
			for(String key : paras.keySet()) {
				if(realUrl.length() > url.length()+1) {
					realUrl.append('&');
				}
				else {
					realUrl.append('&');
				}
				realUrl.append(key+"="+paras.get(key));
			}
		}

		//open connection
		URL apiUrl = new URL(realUrl.toString());
		HttpURLConnection huc = (HttpURLConnection)apiUrl.openConnection();
		huc.setRequestMethod("GET");
		huc.connect();

		//receive data as data
		InputStreamReader os = new InputStreamReader(huc.getInputStream());
		char[] byar = new char[1024];
		StringBuffer res = new StringBuffer();
		while(true) {
			int len = os.read(byar, 0, byar.length);
			if(len <= 0) {
				break;
			}
			res.append(new String(byar));
		}
		return res.toString();
	}

	public static String doPostString(String url, Map<String, String>paras) throws IOException {
        URL apiUrl = new URL(url);
        HttpURLConnection uc = (HttpURLConnection)apiUrl.openConnection();
        uc.setRequestMethod("POST");
//        uc.setUseCaches(false);

        uc.setReadTimeout(5000);
        uc.setConnectTimeout(5000);
        uc.setDoOutput(true);
        uc.setDoInput(true);
        
        OutputStream os = uc.getOutputStream();
        StringBuffer parasString = new StringBuffer();
        for(String key : paras.keySet()) {
        	if(parasString.length() > 0) {
        		parasString.append('&');
        	}
        	parasString.append(key+"="+paras.get(key));
        }

        try {
			os.write(parasString.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        uc.setRequestProperty("accept", "*/*");
//        uc.setRequestProperty("connection", "Keep-Alive");
//  jj   uc.setRequestProperty("user-agent",
//                "Mozilla/4.0 (compatible; MSIE 6.0; iindows NT 5.1;SV1)");
//        uc.setDoOutput(true);
//        uc.setDoInput(true);
        
//        uc.setRequestProperty(sentence, value);
//        uc.setRequestProperty("key", key);
//        uc.setRequestProperty("info", sentence);
//        uc.setRequestProperty("loc", loc);
//        uc.setRequestProperty("userid", ""+userid);
//        uc.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String temp = null;
        StringBuffer res = new StringBuffer();
        while((temp = br.readLine())!=null){
            res.append(temp);
        }
        uc.disconnect();
		return res.toString();
	}
}
