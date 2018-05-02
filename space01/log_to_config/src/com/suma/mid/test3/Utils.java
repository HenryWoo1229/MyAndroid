package com.suma.mid.test3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Hex;

public class Utils {
	static final String KEY_WORD = "StbNgbDealer";
	static final String CPU_WORD = "CPU:";
	static final String MEDIA_WORD = "Boot Media:";
	static final String IPLOADER_WORD = "ipLoaderUpdateUrl[";
	
	public static ArrayList<String> array = new ArrayList<String>();
	public static String[] names = new String[]{ 
			"zoneCode:    ", 
			"factorySn:   ",
			"machineType: ", 
			"regionCode:  ", 
			"batchNum:    ",
			"serialNum:   ",
			"oui:         ",
			"cpu:         ",
			"flash type:  ",
			"hardVersion: ",
			"softVersion: "
			};
	public static String[] mapKeys = new String[names.length];
	private static ArrayList<String> values = new ArrayList<String>();
	public static HashMap<String, String> map = new HashMap<String, String>();
	
    public String changeCharset(String str) throws UnsupportedEncodingException {
        if(str != null) {
            //用源字符编码解码字符串
            byte[] bs = str.getBytes("UTF-8");
            return new String(bs, "GB2312");
        }
        return null;
    }
	
	public void addArray(String key) throws IOException{
		array = getInfoFromSerialNum(key);
		array.add(getDealerInfo(key, "oui", "serialNum"));
		array.add(getInfo(CPU_WORD));
		array.add(getInfo(MEDIA_WORD));
		array.add(getDealerInfo(key, "hardVersion", "softVersion"));
		array.add(getDealerInfo(key, "softVersion", "loaderVersion"));
	}

    public OutputStreamWriter getWriter(File file) throws FileNotFoundException, UnsupportedEncodingException{
    	return new OutputStreamWriter(new FileOutputStream(file), "GBK");
    }
    
	public void createEx1() throws Exception {
		File file = new File(Main.LOADER_INI);
		file.createNewFile();
		OutputStreamWriter writer = getWriter(file);
		
		writer.write("[BasicSetting]\r\n");
		writer.write("指示码="+map.get("zoneCode")+"\r\n");
		writer.write("OUI信息="+map.get("oui")+"\r\n");
		writer.write("厂商认证编号="+map.get("factorySn")+"\r\n");
		String soft = map.get("softVersion");
		writer.write("对应机顶盒软件模型号="+soft.substring(0,5).replace(".", "")+"\r\n");
		writer.write("对应机顶盒软件版本号="+soft.substring(5,soft.length()).replace(".", "")+"\r\n");
		String hard = map.get("hardVersion");
		writer.write("对应机顶盒硬件模型号="+hard.substring(0,5).replace(".", "")+"\r\n");
		writer.write("对应机顶盒硬件版本号="+hard.substring(5,soft.length()).replace(".", "")+"\r\n");
		writer.write("起始区域码="+map.get("regionCode")+"\r\n");
		writer.write("结束区域码="+map.get("regionCode")+"\r\n");
		writer.write("起始终端类型号="+map.get("machineType")+"\r\n");
		writer.write("结束终端类型号="+map.get("machineType")+"\r\n");
		writer.write("起始批次号=0\r\n结束批次号=9999\r\n");
		writer.write("起始序列号="+map.get("serialNum")+"\r\n");
		writer.write("结束序列号="+map.get("serialNum")+"\r\n");
		writer.write("当前文件软件模型号="+soft.substring(0,5).replace(".", "")+"\r\n");
		String fileVer = soft.substring(5,soft.length()).replace(".", "");
		int version = Integer.valueOf(fileVer) + 1;
		writer.write("当前文件软件版本号="+ version +"\r\n");
		
		String lastStr = "私有数据=0\r\n" +
				"loader版本号:=17\r\n" +
				"[Ota设置]\r\n" +
				"flag：=1\r\n" +
				"method：=1\r\n" +
				"原始网络：=3\r\n" +
				"频点：=594000\r\n" +
				"符号率：=6875\r\n" +
				"TS ID：=1\r\n" +
				"DII PID：=99\r\n" +
				"QAM 值：=2\r\n" +
				"Service ID:=310\r\n" +
				"ES PID:=1000\r\n" +
				"网络ID:=16\r\n" +
				"PMT ID:=310";
		writer.write(lastStr);
		writer.flush();
		writer.close();
	}

	public boolean findFile(String filePath) {
		return new File(filePath).exists();
	}

	public boolean findKeyWord(String keyline, String keyword) {
		return keyline.indexOf(keyword) != -1 ? true : false;
	}

	public String readFile(File file, String keyword) throws IOException {
		BufferedReader read = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));

		String line = null;
		while ((line = read.readLine()) != null) {
			if (findKeyWord(line, keyword)) {
				read.close();
				return line;
			}
		}
		read.close();
		return null;
	}
	
	public String getInfo(String word) throws IOException{
		String line = readFile(new File(Main.STB_LOG), word);
		if(line != null){
			int index = line.indexOf(":");
			return line.substring(index + 1).trim();
		}
		return null;
	}

	public void createIPCfg(String key) throws IOException{
		File file = new File(Main.IP_CFG);
		file.createNewFile();
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
		
		int index = key.indexOf(IPLOADER_WORD);
		int begin = index + IPLOADER_WORD.length();
		int end = key.indexOf("]terminalManagerUrl");
		
		String newurl = key.substring(begin, end);

		LinkedHashMap<String, Object> subMap = new LinkedHashMap<String, Object>();   //该map是按顺序插入的
		subMap.put("fileName", "suma_upg.img");
		subMap.put("updateType", 1);
		subMap.put("speedLimit", 0);
		subMap.put("updatingSoftVersions", new String[]{map.get("softVersion")});
		subMap.put("oui", Integer.valueOf(map.get("oui")));
		subMap.put("zoneCode", Integer.valueOf(map.get("zoneCode")));
		subMap.put("factorySn", Integer.valueOf(map.get("factorySn")));
		subMap.put("loaderFlag", 15);
		subMap.put("regionCode", new String[]{map.get("regionCode")});
		subMap.put("machineType", new String[]{map.get("machineType")});
		subMap.put("batchNums", new String[]{map.get("batchNum")});
		subMap.put("serialNums", new String[]{map.get("serialNum")});
		subMap.put("hardVersions", new String[]{map.get("hardVersion")});
		String fileVer = map.get("softVersion").substring(5,map.get("softVersion").length()).replace(".", "");
		int version = Integer.valueOf(fileVer) + 1;
		subMap.put("softVersion", version);
		subMap.put("subUrl", "");
		subMap.put("md5Code", "");
		
		JSONArray subArray = JSONArray.fromObject(subMap);
		JSONObject jo = new JSONObject();
		
		jo.put("newurl", newurl);
		jo.put("urlType", 1);
		jo.put("checkTime", 5);
		jo.put("updateInfo", subArray);
		
		writer.write(new JsonFormatTool().formatJson(jo.toString()));
		writer.flush();
		writer.close();
	}
	
	public void createCfg(String key) throws Exception {
		File file = new File(Main.BASE_CFG);
		file.createNewFile();
		
		OutputStreamWriter writer = getWriter(file);
		writer.write("[智能终端机顶盒Base.txt]\r\n\r\n");

		if(names.length >= array.size()){
			
			for (int i = 0; i < names.length; i++) {
				if(i < 5){
					writer.write(names[i] + "" + Integer.valueOf(array.get(i)) + "\r\n");
					continue;
				}
				writer.write(names[i] + "" + array.get(i) + "\r\n");
			}
		}
		else {
			writer.close();
			throw new Exception("names lenght over arraylist");
		}
		
		
		
		writer.close();

	}

	public String getDealerInfo(String key, String key_target, String key_help){

		int index_start = key.indexOf(key_target);
		int index_end = key.indexOf(key_help);

		if (index_start != -1) {
			int offset = key_target.length();
			int len_start = index_start + offset + 1;
			int len_end = index_end - 1;

			return key.substring(len_start, len_end);
		}
		return null;

	}

	public ArrayList<String> getInfoFromSerialNum(String key) {
		String sn = getDealerInfo(key, "serialNum", "hardVersion");
		if (sn != null) {
			if (sn.length() == 19) { // 非省网项目
				for(int i = 0; i < sn.length(); i = i+2){
					if(i == 8){  //截批次号 
						values.add(sn.substring(i, i + 4));
						i = i + 2;
					}
					else if(i == 12){ //截序列号
						values.add(sn.substring(i, sn.length()));
						break;
					}
					else
						values.add(sn.substring(i, i + 2));
					
				}

				for (int i = 0; i < values.size(); i++)
					array.add(values.get(i));

				return array;
			} else if (sn.length() == 18) { // 省网项目，暂不处理
				System.out.println("sn.len is 18, Proj Hubei does not handle now");
			} else {
				System.out.println("sn.len is " + sn.length()
						+ " , not handle now");
			}
		}
		return null;
	}
	
	public static String getMD5(String filePath)
			throws NoSuchAlgorithmException {
		File file = new File(filePath);
		MessageDigest MD5 = MessageDigest.getInstance("MD5");
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1) {
				MD5.update(buffer, 0, length);
			}

			return new String(Hex.encodeHex(MD5.digest())).toUpperCase();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


}
