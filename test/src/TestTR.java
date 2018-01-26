import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;

class TestTR{
    private static String key = "e57f925d62cd4880b8cf1d688cfaa6f7";
//     info = "今天天气怎么样"
    private static String loc = "哈尔滨市";
//    private static String url = "http://www.tuling123.com/openapi/api"+"?key="+key+"&info=";
    private static String url = "http://www.tuling123.com/openapi/api";
    private static int userid = 0;
    public static void main1(String[] args){
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
//        URL apiUrl = new URL(url+sentence);
        URL apiUrl = new URL(url);
        HttpURLConnection uc = (HttpURLConnection)apiUrl.openConnection();
        uc.setRequestMethod("POST");
//        uc.setUseCaches(false);

        uc.setReadTimeout(5000);
        uc.setConnectTimeout(5000);
        uc.setDoOutput(true);
        uc.setDoInput(true);
        
        OutputStream os = uc.getOutputStream();
        String para = "key="+key
        		+"&info="+sentence
        		+"&loc="+loc
        		+"&userid="+userid;
        os.write(para.getBytes());
        
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
        Gson gs = new Gson();
//        TRMessage msg = ((List<TRMessage>)gs.fromJson(res, new TypeToken<List<TRMessage>>() {}.getType())).get(0);
//        res = res.substring(4, res.length());
        TRMessage msg = gs.fromJson(res.toString(), TRMessage.class);
        return msg.text;
    }
    
    class TRMessage{
    	long code;
    	String text;
    }
}
