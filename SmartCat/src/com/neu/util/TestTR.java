package com.neu.util;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

class TestTR{
    private static String key = "e57f925d62cd4880b8cf1d688cfaa6f7";
//     info = "今天天气怎么样"
    private static String loc = "哈尔滨市";
    private static String url = "http://www.tuling123.com/openapi/api"+"?key="+key+"&info=";
    private static int userid = 0;
    public static void main(String[] args){
        String sentence = null;
        Scanner is = new Scanner(System.in);
        while(true){
            sentence = is.nextLine();
            if(sentence.equals("exit")){
                break;
            }
            try{
                System.out.println(sendToRobot(sentence));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        is.close();
    }

    public static String sendToRobot(String sentence) throws Exception{
        URL apiUrl = new URL(url+sentence);
        HttpURLConnection uc = (HttpURLConnection)apiUrl.openConnection();
        uc.setRequestMethod("GET");
        uc.setUseCaches(false);
//        uc.setRequestProperty("accept", "*/*");
//        uc.setRequestProperty("connection", "Keep-Alive");
//        uc.setRequestProperty("user-agent",
//                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//        uc.setDoOutput(true);
//        uc.setDoInput(true);
        
//        uc.setRequestProperty("key", key);
//        uc.setRequestProperty("info", sentence);
//        uc.setRequestProperty("loc", loc);
//        uc.setRequestProperty("userid", ""+userid);
        uc.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String res = null, temp = null;
        while((temp = br.readLine())!=null){
            res += temp;
        }
        return res;
    }
}
