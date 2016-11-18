package com.suma.mid.test3;

import java.io.File;

public class Main {
	private static final String BASE_PATH = System.getProperty("user.dir");
	private static final String STB_LOG_NAME = "stb.log";
	static final String STB_LOG = BASE_PATH + File.separator + STB_LOG_NAME;
	private static final String BASE_CFG_NAME = "base.txt";
	static final String BASE_CFG = BASE_PATH + File.separator + BASE_CFG_NAME;
	static final String LOADER_INI = BASE_PATH + File.separator + "ex1.ini";
	static final String IP_CFG = BASE_PATH + File.separator + "update.cfg";

	public void start() throws Exception {
		Utils utils = new Utils();

		if (!utils.findFile(STB_LOG)) {
			throw new Exception(STB_LOG + " Not Found!");
		}

		File stblogFile = new File(STB_LOG);
		String keyword = utils.readFile(stblogFile, Utils.KEY_WORD);
		if (keyword == null) {
			throw new Exception(STB_LOG_NAME + " keyword: "
					+ Utils.KEY_WORD + " Not Found!");
		}

		utils.addArray(keyword);
		for(int i = 0; i < Utils.names.length; i ++){
			String t = Utils.names[i].trim();
			Utils.mapKeys[i] = t.substring(0, t.length() - 1);
			Utils.map.put(Utils.mapKeys[i], Utils.array.get(i));
		}
		utils.createCfg(keyword);
		utils.createEx1();
		
		String keyword2 = utils.readFile(stblogFile, Utils.IPLOADER_WORD);
		if (keyword2 == null) {
			throw new Exception(STB_LOG_NAME + " keyword: "
					+ Utils.IPLOADER_WORD + " Not Found!");
		}
		keyword2 = keyword2.replace("\\", "");
		utils.createIPCfg(keyword2);

	}

	@SuppressWarnings("finally")
	public static void main(String[] args) {
		Main main = new Main();
		try {
			System.out.println("转换开始...");
			main.start();

			if (new File(BASE_PATH + File.separator + "suma_upg.img").exists()) {
				String md5 = Utils.getMD5(BASE_PATH + File.separator
						+ "suma_upg.img");
				System.out.println("MD5: " + md5);
			}

			if (args.length > 0) {
				String md5 = Utils.getMD5(args[0]);
				System.out.println("MD5: " + md5);
			}
			
			System.out.println("转换完毕...");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
//			while(true){}
		}

	}

}
