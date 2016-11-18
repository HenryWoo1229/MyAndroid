import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Main {

	static String[] params = new String[] { "newurl", "urlType", "checkTime" };
	static String[] personalParams = new String[] { "updateType", "speedLimit",
			"subUrl" };
	static String[] iniConfigs = new String[] { "oui", "updatingSoftVersions",
			"hardVersions" }; // 顺序必须是oui 软件版本 硬件版本，与getVersionIni返回的数据顺序对应起来
	static String[] snElements = new String[] { "zoneCode", "factorySn",
			"machineType", "regionCode", "batchNums", "serialNums" }; // 按照序列号位置对应含义的顺序写的元素
	final static String SPACE = "   "; // 序列号烧写结果txt文件里要split的空格个数，根据该文件来调整，目前是三个
	final static String STARS = "\\*\\*\\*"; // 序列号烧写结果txt文件里要split的*个数，根据该文件来调整，目前是三个
												// *是正则表达式里的符号，所以要加\\以示区分

	final static String UPDATE_CFG = "update.cfg";
	final static String CONFIG_INI = "config.ini";
	final static String BASE_PATH = System.getProperty("user.dir") + "\\";
	final static String IMGPATH = BASE_PATH + "suma_upg.img";

	static HashMap<String, String> map = new HashMap<String, String>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JsonFormatTool jf = new JsonFormatTool();
		String str = jf.formatJson(ParseJson(getJsonString(BASE_PATH
				+ UPDATE_CFG), BASE_PATH + CONFIG_INI));
		buildConfig(str);
		System.out.println(BASE_PATH);

	}

	/**
	 * 从原始标准的update.cfg文件中读取json，并存入一个字符串中
	 * 
	 * @param path
	 * @return json格式字符串，待处理
	 */
	public static String getJsonString(String path) {
		String jsonstr = "";
		try {
			BufferedReader read = getFileContent(path);

			String line = "";
			while ((line = read.readLine()) != null) {
				jsonstr = jsonstr + line;
			}

			read.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonstr;
	}

	/**
	 * 处理json字符串，并提示用户修改数据，是一个核心方法
	 * 
	 * @param jsonStr
	 * @return 可以直接供buildConfig方法使用的字符串
	 */
	public static String ParseJson(String jsonStr, String iniPath) {

		JSONObject jb = JSONObject.fromObject(jsonStr);
		JSONArray ja = jb.getJSONArray("updateInfo");
		JSONObject jab = ja.getJSONObject(ja.size() - 1); // 修改位于updateinfo中的最后一个参数列表
		Scanner scanner = new Scanner(System.in);
		System.out.println("");

		// params参数的修改
		for (int i = 0; i < params.length; i++) {
			System.out.println("===>如下参数如需修改则输入1，不改则输入其他\n\"" + params[i]
					+ "\": " + jb.getString(params[i]));
			if (scanner.next().equals("1")) {
				System.out.println("===>在此输入修改后的值(不用填写对应的参数名)，相同的部分可复制粘贴进来");
				jb.put(params[i], scanner.next());
			}
			System.out
					.println("********************************************************************");
		}
		// personalParams参数的修改
		for (int i = 0; i < personalParams.length; i++) {
			System.out.println("===>如下参数如需修改则输入1，不改则输入其他\n\""
					+ personalParams[i] + "\": "
					+ jab.getString(personalParams[i]));
			if (scanner.next().equals("1")) {
				System.out.println("===>在此输入修改后的值(不用填写对应的参数名)，相同的部分可复制粘贴进来");
				jab.put(personalParams[i], scanner.next());
			}
			System.out
					.println("********************************************************************");
		}

		// iniConfigs参数的修改
		List<String> list = getVersionIni(iniPath);
		for (int i = 0; i < iniConfigs.length; i++) {
			if (iniConfigs[i].equals("updatingSoftVersions")) {
				JSONArray updatingSoftVersions = (JSONArray) jab
						.get("updatingSoftVersions");
				updatingSoftVersions.set(updatingSoftVersions.size() - 1,
						list.get(i));
				continue;
			}
			if (iniConfigs[i].equals("hardVersions")) {
				JSONArray hardVersions = (JSONArray) jab.get("hardVersions");
				hardVersions.set(hardVersions.size() - 1, list.get(i));
				continue;
			}

			jab.put(iniConfigs[i], Integer.valueOf(list.get(i))); // oui is
																	// Integer
		}

		System.out.println("===>请将序列号烧写结果LOG文档拖入命令行中...");
		handleSN(scanner.next());
		for (int i = 0; i < snElements.length; i++) {
			if (snElements[i].equals("machineType")) {
				JSONArray machineType = (JSONArray) jab.get(snElements[i]);
				machineType.set(machineType.size() - 1, map.get(snElements[i]));
				continue;
			}
			if (snElements[i].equals("regionCode")) {
				JSONArray regionCode = (JSONArray) jab.get(snElements[i]);
				regionCode.set(regionCode.size() - 1, map.get(snElements[i]));
				continue;
			}
			if (snElements[i].equals("batchNums")) {
				JSONArray batchNums = (JSONArray) jab.get(snElements[i]);
				batchNums.set(batchNums.size() - 1, map.get(snElements[i]));
				continue;
			}                          
			if (snElements[i].equals("serialNums")) {
				JSONArray serialNums = (JSONArray) jab.get(snElements[i]);
				serialNums.set(serialNums.size() - 1, map.get(snElements[i]));
				continue;
			}

			jab.put(snElements[i], Integer.valueOf(map.get(snElements[i])));
		}

		try {
			jab.put("md5Code", getMD5(IMGPATH));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return jb.toString();
	}

	/**
	 * 将处理好的json格式字符串写入cfg文件中
	 * 
	 * @param content
	 */
	public static void buildConfig(String content) {
		String updateName = "update_auto.cfg";
		File newFile = new File(BASE_PATH + updateName);
		try {
			if (!newFile.exists())
				newFile.createNewFile();
			FileWriter fw = new FileWriter(BASE_PATH + updateName);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回序列号烧写工具下的ini文件中的参数，分别对应oui 软件版本号 硬件版本号，如下是返回的list中三个元素： 346 02.04.00.02
	 * 02.01.01.00
	 * 
	 * @param path
	 *            ini文件的路径
	 * @return
	 */
	public static List<String> getVersionIni(String path) {
		List<String> list = new ArrayList<String>();
		List<String> result = new ArrayList<String>();
		try {
			BufferedReader read = getFileContent(path);
			String line = "";
			int index = 1;
			while ((line = read.readLine()) != null) {
				switch (index) {
				case 1:
					break;
				case 2: // oui
					list.add(line);
					break;
				case 3: // 软件主版本
					list.add(line);
					break;
				case 4: // 软件次版本
					list.add(line);
					break;
				case 5: // 硬件主版本
					list.add(line);
					break;
				case 6: // 硬件次版本
					list.add(line);
					break;
				}
				index++;
			}
			int len = getValue(list.get(1)).length();
			result.add(getValue(list.get(0)));
			result.add(getValue(list.get(1)).substring(0, 2)
					+ "." // 给版本号中间加点好，匹配cfg文件的格式
					+ getValue(list.get(1)).substring(2, len) + "."
					+ getValue(list.get(2)).substring(0, 2) + "."
					+ getValue(list.get(2)).substring(2, len));
			result.add(getValue(list.get(3)).substring(0, 2) + "."
					+ getValue(list.get(3)).substring(2, len) + "."
					+ getValue(list.get(4)).substring(0, 2) + "."
					+ getValue(list.get(4)).substring(2, len));
			read.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 通过序列号烧写工具的烧写日志获取到SN
	 * 
	 * @param path
	 * @return
	 */
	public static String getSN(String path) {
		String sn = "";
		try {
			BufferedReader read = getFileContent(path);
			String line = "";
			String str = "";
			while ((line = read.readLine()) != null)
				str = line;

			String[] strs = str.split(SPACE);
			sn = strs[1].split(STARS)[0];
			read.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sn;
	}

	/**
	 * 将SN按每个字段的含义拆分，并存入map里
	 * @param path
	 * @return
	 */
	private static HashMap<String, String> handleSN(String path) {
		String sn = getSN(path);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i <= 6; i = i + 2)
			list.add(sn.substring(i, i + 2));
		list.add(sn.substring(8, 12));
		list.add(sn.substring(12, sn.length()));

		for (int i = 0; i < snElements.length; i++)
			map.put(snElements[i], list.get(i));

		return map;
	}

	/**
	 * 格式化序列号工具下config.ini文件中的元素用的，返回的是等号右边的值，仅供方法getVersionIni调用
	 * 
	 * @param str
	 * @return
	 */
	private static String getValue(String str) {
		String[] tmp = str.split("=");
		return tmp[tmp.length - 1];
	}

	private static BufferedReader getFileContent(String path)
			throws FileNotFoundException {
		File file = new File(path);
		InputStream input = new FileInputStream(file);
		InputStreamReader reader = new InputStreamReader(input);
		return new BufferedReader(reader);
	}

	/**
	 * 获取文件的MD5码
	 * 
	 * @param filePath
	 *            文件的路径
	 * @return MD5字符串
	 * @throws NoSuchAlgorithmException
	 */
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
