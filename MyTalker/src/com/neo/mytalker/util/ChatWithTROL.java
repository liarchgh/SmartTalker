package com.neo.mytalker.util;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class ChatWithTROL{
    private final static String key = "e57f925d62cd4880b8cf1d688cfaa6f7";
//     info = "今天天气怎么样"
    private static String loc = "哈尔滨市";
//    private static String url = "http://www.tuling123.com/openapi/api"+"?key="+key+"&info=";
    private final static String url = "http://www.tuling123.com/openapi/api";
    private final static int userid = 0;
//    public static void main(String[] args){
//        String sentence = null;
//        Scanner is = new Scanner(System.in);
//        while(true){
//            sentence = is.nextLine();
//            if(sentence.equals("exit")){
//                break;
//            }
//            try{
//                System.out.println(sendToRobot(sentence));
//            }
//            catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//        is.close();
//    }

    public static String sendToRobot(String sentence) throws Exception{
//        URL apiUrl = new URL(url+sentence);
    	Map<String, String>paras = new HashMap<String, String>();
    	paras.put("key", key);
    	paras.put("info", sentence);
    	paras.put("loc", loc);
    	paras.put("userid", ""+userid);
    	String res = NetUtil.doPostString(url, paras);
//        URL apiUrl = new URL(url);
//        HttpURLConnection uc = (HttpURLConnection)apiUrl.openConnection();
//        uc.setRequestMethod("POST");
////        uc.setUseCaches(false);
//
//        uc.setReadTimeout(5000);
//        uc.setConnectTimeout(5000);
//        uc.setDoOutput(true);
//        uc.setDoInput(true);
//        
//        OutputStream os = uc.getOutputStream();
//        String para = "key="+key
//        		+"&info="+sentence
//        		+"&loc="+loc
//        		+"&userid="+userid;
//        os.write(para.getBytes());
//        
////        uc.setRequestProperty("accept", "*/*");
////        uc.setRequestProperty("connection", "Keep-Alive");
////  jj   uc.setRequestProperty("user-agent",
////                "Mozilla/4.0 (compatible; MSIE 6.0; iindows NT 5.1;SV1)");
////        uc.setDoOutput(true);
////        uc.setDoInput(true);
//        
////        uc.setRequestProperty(sentence, value);
////        uc.setRequestProperty("key", key);
////        uc.setRequestProperty("info", sentence);
////        uc.setRequestProperty("loc", loc);
////        uc.setRequestProperty("userid", ""+userid);
////        uc.connect();
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
//        String temp = null;
//        StringBuffer res = new StringBuffer();
//        while((temp = br.readLine())!=null){
//            res.append(temp);
//        }
//        uc.disconnect();
        Gson gs = new Gson();
//        TRMessage msg = ((List<TRMessage>)gs.fromJson(res, new TypeToken<List<TRMessage>>() {}.getType())).get(0);
//        res = res.substring(4, res.length());
        TRMessage msg = gs.fromJson(res, TRMessage.class);
        return msg.text;
    }
    
    public static String getLoc() {
		return loc;
	}

	public static void setLoc(String loc) {
		ChatWithTROL.loc = loc;
	}

	class TRMessage{
    	long code;
    	String text;
    }
}
