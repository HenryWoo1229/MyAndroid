import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) {
//		String key = "01-01 00:00:16.706 D/ServiceBoot( 2106): readConfig time=20127,config=[Config@e56a5b21]appVersion[30D1F591742DF105]configVersion[214]isThird[false]isNgbVersion[true]isPrint[true]isServiceCheck[true]isAppRuleStart[true]defaultLaunchApp[com.suma.midware.home/.localset.view.third.Main3DActivity]isSanpingStart[true]version[4]androidVersion[0]isIvideoStart[false]appRuleProviderID[1]appRuleAuthID[100]appVerifySign[1]appUpdateAddr[http://update.sumavision.cc:90/dp-12/update/hubei/xianning/]ivideoRegion[]ivideoServerIP[192.166.62.25]ivideoPort[8080]esgType[0]esgCheckUrl[]isDlnaStart[true]isDlnaTimeAdjust[false]cutPictureQuality[100]maxCutPictureSize[0]isCutPictureSaveOnly[false]ipLoaderUpdateUrl[http://update.sumavision.cc:90/dp-12/iploader/hubei/xianning/]terminalManagerUrl[http://192.166.162.222/drm/]terminalManagerType[-1]terminalType[1],params={";
//		int index = key.indexOf("ipLoaderUpdateUrl[");
//		int last = key.indexOf("]terminalManagerUrl");
//		System.out.println(key.substring(index+"ipLoaderUpdateUrl[".length(), last));
//		
//		HashMap<String, Object> subMap = new HashMap<String, Object>();
//		subMap.put("fileName", "suma_upg.img");
//		subMap.put("updateType", 1);
//		subMap.put("updatingSoftVersions", new String[] { "02.00.00.02" });
//
//		JSONArray subArray = JSONArray.fromObject(subMap);
//		JSONObject jo = new JSONObject();
//
//		jo.put("newurl",
//				"http://update.sumavision.test:90/dp-12/iploader/hubei/xianning/");
//		jo.put("urlType", 1);
//		jo.put("updateInfo", subArray);
//
//		System.out.println(new JsonFormatTool().formatJson(jo.toString()));
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
		
		String line = null;
		System.out.println("test >");
		try {
			while((line = reader.readLine()) != null){
				if(line.equals("exit"))
					break;
				
				System.out.println("test >");
				writer.write(line);
				writer.flush();
			}
			
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
