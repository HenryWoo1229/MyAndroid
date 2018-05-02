package com.suma.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class MyExcel extends BUG {

	static boolean flag = false;

	/**
	 * 读取参数配置表，并返回要填写的缺陷参数
	 * 
	 * @param path
	 *            参数配置表的路径
	 * @return 缺陷参数集合。如果没有找到配置表则返回null
	 */
	public static List<String> readConfig(String path) {
		File txt = new File(path);
		try {
			FileInputStream input = new FileInputStream(txt);
			InputStreamReader reader = new InputStreamReader(input);
			BufferedReader read = new BufferedReader(reader);
			String line = "";
			while (null != (line = read.readLine()))
				basic.add(line);
			if (basic.size() == 0) {
				System.out.println("参数配置表为空，请重新填写后再启动程序~~~~~");
				return null;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return basic;
	}

	/**
	 * 抓打印，记录按键
	 * 
	 * @param name
	 *            待保存的文件名
	 */
	public static void logcat(String name, String cmdline) {
		Process pid = null;
		if (cmdline.equals(""))
			cmdline = "logcat -c; logcat -v time";
		System.out.println("cmdline=====" + cmdline);
		String[] cmd = { "/system/bin/sh", "-c", cmdline };
		try {

			String bugFolder = baseDir + name;
			createFolder(bugFolder); // 创建log对应缺陷的文件夹
			System.out.println(bugFolder + "/" + name + ".log");
			createFile(bugFolder + "/" + name + ".log");

			FileWriter fw = new FileWriter(new File(bugFolder + "/" + name
					+ ".log"));
			BufferedWriter bw = new BufferedWriter(fw);

			FileWriter shfw = new FileWriter(new File(bugFolder + "/" + name
					+ ".sh"));
			BufferedWriter shbw = new BufferedWriter(shfw);
			shbw.write("#! /system/bin/sh \n\n");
			shbw.write("# This is an auto-generate file\n\n");

			pid = Runtime.getRuntime().exec(cmd);
			if (pid != null) {
				BufferedReader input = new BufferedReader(
						new InputStreamReader(pid.getInputStream()));
				String line = "";
				RecordKey rk = new RecordKey();
				String find1 = null;
				String find2 = null;
				String line1 = null;
				String line2 = null;
				int rec = 0;
				while ((line = input.readLine()) != null) {
					System.out.println(line);
					bw.write(line + "\n");

					// ---------------------------记录按键start------------------------------------
					int ind = cmdline.indexOf("-v time");
					if (ind != -1) {
						find1 = rk.findKeyCode(line);
						if (find1 != null) {
							line1 = line;
							if (find2 == null) {
								find2 = find1;
								line2 = line1;
								shbw.write("echo 'input keyevent " + find2
										+ "'" + "\n");
								shbw.write("input keyevent " + find2 + "\n");
								rec = 1;
							} else if (find2 != "") {
								int delay = rk.timeHandler(line2, line1);
								shbw.write("echo 'sleep " + delay + "'" + "\n");
								shbw.write("sleep " + delay + "\n");
								shbw.write("echo 'input keyevent " + find1
										+ "'" + "\n");
								shbw.write("input keyevent " + find1 + "\n");
								find2 = find1;
								line2 = line1;
							}
						}
					}
					// ---------------------------记录按键end------------------------------------

					if (flag) {
						System.out.println("===>Log保存路径:" + baseDir + name
								+ "/" + name + ".log");
						if (ind == -1) {
							shbw.write("echo 'Cannot record key without using logcat -v time.'"
									+ "\n");
							System.out
									.println("===>没有使用logcat -v time抓日志，无法记录按键...");
						}
						if (rec == 0)
							shbw.write("echo 'There is no KeyEvent been sended.'"
									+ "\n");
						break;
					}
				}
				input.close();
				bw.close();
				shbw.close();

			} else
				System.out.println("pid is null, cannot save log!!!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 截图
	 * 
	 * @param name
	 *            待保存的文件名
	 */
	public static void screencap(String name) {
		String bugFolder = baseDir + name;
		createFolder(bugFolder); // 创建对应缺陷的文件夹

		String filePath = baseDir + name + "/" + name + ".png";
		String[] cmd = { "/system/bin/sh", "-c", "screencap -p " + filePath };

		try {
			Runtime.getRuntime().exec(cmd);
			System.out.println("===>截图保存路径:" + baseDir + name + "/" + name
					+ ".png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 同时抓包和抓打印(待完善)
	 * 
	 * @param name
	 *            待保存的文件名
	 */
	public static void tcpdump_Logcat(String name) {
		String bugFolder = baseDir + name;
		createFolder(bugFolder); // 创建对应缺陷的文件夹

		String filePathPcap = baseDir + name + "/" + name + ".pcap";
		String filePathLog = baseDir + name + "/" + name + ".log";
		String[] cmd = {
				"/system/bin/sh",
				"-c",
				"tcpdump -i any -s 0 -w " + filePathPcap
						+ " & logcat -c;logcat -v time > " + filePathLog };

		try {
			Runtime.getRuntime().exec(cmd);
			System.out.println("===>抓包保存路径:" + baseDir + name + "/" + name
					+ ".pcap");
			System.out.println("===>打印保存路径:" + baseDir + name + "/" + name
					+ ".log");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void setCellStyle(HSSFCellStyle setBorder, HSSFFont font) {

		setBorder.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		setBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		setBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		setBorder.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

		font.setFontHeightInPoints((short) 10); // 设置字体大小
		font.setFontName("宋体"); // 设置字体
		setBorder.setFont(font);
	}

	public static void writeBug() {

		File myxls = new File(baseDir + "BUG.xls");
		File myxlsbak = new File(backDir);
		if (!myxls.exists()) {
			System.out.println("文件 " + baseDir + "BUG.xls 不存在，请确认...");
			System.exit(0);
		}
		if (!myxlsbak.exists()) {
			myxlsbak.mkdir();
		}
		collectInfo();
		POIFSFileSystem poifsFileSystem = null;
		try {
			poifsFileSystem = new POIFSFileSystem(new FileInputStream(myxls));
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
			HSSFSheet sheet = hssfWorkbook.getSheet("Sheet1");
			HSSFCellStyle setBorder = hssfWorkbook.createCellStyle();
			HSSFFont font = hssfWorkbook.createFont();
			setCellStyle(setBorder, font); // 设置好cell的样式

			int row = sheet.getFirstRowNum();
			HSSFRow firstRow = null;
			if (row == 0)
				firstRow = sheet.createRow(row);
			for (int i = 0; i < basic.size(); i++) {
				firstRow.createCell(i);
				firstRow.getCell(i).setCellValue(basic.get(i));
			}

			while (true) {
				HSSFRow rowdata = sheet.getRow(row);
				if (null != rowdata) { // 找到空行再往下走
					row++;
					continue;
				}

				rowdata = sheet.createRow(row);

				for (int j = 0; j < content.size(); j++) {
					if (content.get(j).equals("ID"))
						content.set(j, "bug" + (row + 1)); // 给ID赋值，取的是当前行号+1

					rowdata.createCell(j);
					// 填写数据
					rowdata.getCell(j).setCellValue(content.get(j));
					rowdata.getCell(j).setCellStyle(setBorder);
				}
				FileOutputStream out = new FileOutputStream(myxls);
				hssfWorkbook.write(out);
				out.close();
				hssfWorkbook.close();

				break;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据指定路径，创建文件
	 * 
	 * @param path
	 *            文件路径
	 * @return true 创建成功 false 创建失败
	 */
	public static boolean createFile(String path) {
		boolean ret = false;
		File file = new File(path);
		// if (!file.isDirectory())
		try {
			ret = file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 根据指定路径，创建文件夹
	 * 
	 * @param path
	 *            文件夹路径
	 * @return true 创建成功 false 创建失败
	 */
	public static boolean createFolder(String path) {
		boolean ret = false;
		File file = new File(path);
		// if (file.isDirectory())
		ret = file.mkdir();
		return ret;
	}

	public static boolean isExists(String path) {
		File f = new File(path);
		return f.exists();
	}

	public static void init() {
		System.out.println("");
		System.out
				.println("********************************************************");
		System.out
				.println("*******************Android集成BUG编写系统****************");
		System.out
				.println("*********************测试三组 研发六部********************");
		System.out
				.println("*********************************************************");
		System.out.println("");

		String[] cmd = { "/system/bin/sh", "-c", "mkdir " + backDir }; // 构造备份文件夹
		File f = new File(backDir);
		Runtime run = Runtime.getRuntime();

		try {
			int i = 0;
			while (true) {   //尝试3次去创建目录
				if (!f.exists() && i < 3) {
					run.exec(cmd);
					i++;
					Thread.sleep(500);
				}
				else
					break;
			}

			System.out.println("===>请输入U盘根路径,直接回车将使用默认路径/sdcard");//=============================
			String usbPath = new Scanner(System.in).nextLine();
			if (!usbPath.equals(""))
				baseDir = usbPath + "/Bug_Center/";

			if (!isExists(baseDir + "config.txt")) {
				System.out.println("===>文件 " + baseDir
						+ "config.txt 不存在，请确认...");
				return;
			}
			List<String> configList = readConfig(baseDir + "config.txt");
			if (configList == null) {
				System.out.println("===>文件 " + baseDir
						+ "config.txt 为空，请按说明填入参数...");
				return;
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		init();
		while (true) {

			try {
				Scanner input = new Scanner(System.in);

				writeBug(); // 开始写BUG

				// 备份数据到机顶盒中
				String[] cpBUGXLS = { "/system/bin/sh", "-c",
						"cp -rf " + baseDir + "BUG.xls " + backDir };
				Runtime.getRuntime().exec(cpBUGXLS);
				System.out.println("===>BUG.xls 备份完毕...备份路径:" + backDir
						+ "BUG.xls");

				if (title.equals("")) {
					Calendar calendar = Calendar.getInstance();
					int year = calendar.get(Calendar.YEAR); // 获取年;
					int month = calendar.get(Calendar.MONTH); // 获取月;
					int date = calendar.get(Calendar.DATE); // 获取天;
					int hour = calendar.get(Calendar.HOUR_OF_DAY); // 获取小时
					int min = calendar.get(Calendar.MINUTE); // 获取分钟
					int sec = calendar.get(Calendar.SECOND); // 获取秒
					title = "" + year + (month + 1) + date + "_" + hour + min
							+ sec;
				}

				System.out
						.println("===>输入要执行的log命令然后回车, 直接回车将执行 logcat -c; logcat -v time，再按回车则停止抓并会自动保存");
				String cmdLine = new Scanner(System.in).nextLine();
				new Thread(new Runnable() {
					public void run() {
						new Scanner(System.in).nextLine();
						flag = true;
					}
				}).start();

				logcat(title, cmdLine); // 开始抓logcat

				// 备份数据到机顶盒
				String[] cmd = { "/system/bin/sh", "-c",
						"cp -rf " + baseDir + title + "  " + backDir };
				Runtime runcmd = Runtime.getRuntime();
				runcmd.exec(cmd);
				System.out.println("===>Log备份完毕...备份路径:" + backDir + title
						+ "/" + title + ".log");

				System.out.println("===>将机顶盒界面调整到需要的位置，直接回车截图，输入其他不截图");
				String isScreen = input.nextLine();
				if (isScreen.equals("")) {
					screencap(title); // 开始记录截图
					Thread.sleep(1500);
					runcmd.exec(cmd);
					System.out.println("===>截图备份完毕...备份路径:" + backDir + title
							+ "/" + title + ".png");
				}

				System.out.println("");
				flag = false;

			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
