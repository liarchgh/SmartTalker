import java.util.List;

public class Main {
	public static void main(String[] args) {
//		String url = "http://music.163.com/song/media/outer/url?id=640565.mp3",
//		String url = "http://m10.music.126.net/20180125195931/ee756ab17ddbd41974892602221c3c7d/ymusic/7871/8cd1/a025/29d4f2e4f8a1a3289f3a319c2df700c9.mp3",
//			filePath = "d:/llab.mp3";
//		try {
//			NetUtil.doGetMusic(url, filePath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		List<MusicEntity>ms = GetMusicUrl.searchMusicByKey("青花瓷");
		ms.clear();
	}
}
