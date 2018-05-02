package com.suma.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class BUG {

	static List<String> steps = new ArrayList<String>(); // 分步骤(最后合成为复现步骤用)
	static List<String> basic = new ArrayList<String>(); // 待写入Excel的要素
	static List<String> content = new ArrayList<String>(); // 主要内容
	static List<String> bugDisc = new ArrayList<String>(); // BUG描述的内容

	static String title = ""; // 缺陷标题
	static String name = ""; // 缺陷发现者
	static String condition = ""; // 前置条件
	static String step = ""; // 复现步骤
	static String result = ""; // 实际结果
	static String expect = ""; // 预期结果
	static String probability = ""; // 复现概率
	static String discription = ""; // 缺陷描述
	static String bugDate = ""; // 缺陷发现日期
	static String pm = ""; // 提交给
	static String chipType = ""; // 芯片类型
	static String machineType = ""; // 硬件类型

	static String baseDir = "/sdcard/Bug_Center/"; // U盘文件夹路径
	static String backDir = "/data/Bug_Center/"; // 机顶盒内备份文件夹路径

	public static void main(String[] args) {
		BUG abc = new BUG();
		abc.basic.add("缺陷描述");
		abc.basic.add("发现者吴昊");
		abc.collectInfo();
	}

	/**
	 * /** 负责搜集BUG各要素的基础信息
	 * 
	 * @param list
	 */
	public static void collectInfo() {
		System.out.println("==========================================>");
		content.clear();
		bugDisc.clear();
		steps.clear();
		step = "";

		String[] bug = new String[] { "复现概率", "前置条件", "复现步骤", "实际结果", "预期结果" };
		String disc = "";
		String str = "";

		Scanner scanner = new Scanner(System.in);
		for (int m = 0; m < basic.size(); m++) {

			// ================写缺陷描述 start
			if (basic.get(m).equals("缺陷描述")) {
				for (int i = 0; i < bug.length; i++) {

					if (bug[i].equals("复现步骤")) {
						int k = 0;
						scanner.nextLine();
						while (true) {
							System.out.println("===>请输入'复现步骤" + (k + 1)
									+ "'的内容：(直接回车跳过此步)");
							String tmpstr = scanner.nextLine().replace("�", "");
							if (tmpstr.equals("")) {
								for (int j = 0; j < steps.size(); j++)
									step = step + steps.get(j);
								step = "\n复现步骤:\n" + step;
								break;
							}
							steps.add(" " + (k + 1) + "、" + tmpstr + "\n");
							k++;
						}
						bugDisc.add(step);
						continue;
					}

					if (bug[i].equals("复现概率")) {
						System.out.println("===>请输入'复现概率(0-100, 直接回车表示必现)'：");
						scanner.nextLine();
						while (true) {
							String prob = scanner.nextLine();
							if (prob.equals("")) {
								prob = "必现";
								bugDisc.add("复现概率:" + prob);
								System.out.println("复现概率:必现");
								break;
							}
							try {
								if (Integer.valueOf(prob) > 100
										|| Integer.valueOf(prob) < 0) {
									System.out.println("===>输入的概率超过了100或者小于0");
									continue;
								}
							} catch (Exception e) {
								System.out.println("===>输入的不是数字，请重新输入...");
								continue;
							}

							bugDisc.add("复现概率:" + prob + "%");
							break;
						}
						continue;
					}

					System.out.println("===>请输入'" + bug[i] + "'：");
					if (bug[i].equals("前置条件"))
						bugDisc.add("\n前置条件:\n" + " "
								+ scanner.next().replace("�", ""));
					if (bug[i].equals("实际结果"))
						bugDisc.add("实际结果:\n" + " "
								+ scanner.next().replace("�", ""));
					if (bug[i].equals("预期结果"))
						bugDisc.add("\n预期结果:\n" + " "
								+ scanner.next().replace("�", ""));

				}
				for (int i = 0; i < bugDisc.size(); i++) {
					disc = disc + bugDisc.get(i);
				}
				content.add(disc);
				continue;
			}
			// ================写缺陷描述 end
			if (basic.get(m).equals("ID")) {
				content.add("ID"); // 这里还取不到excel的行号，所以先占坑，后面补上
				continue;
			}
			if (basic.get(m).equals("发现日期") && bugDate.equals("")) {
				bugDate = dateForBug();
				content.add(bugDate);
				continue;
			}
			if (basic.get(m).equals("发现日期") && !bugDate.equals("")) {
				content.add(bugDate);
				System.out.println("===>已自动添加发现日期为:" + bugDate);
				continue;
			}
			if (basic.get(m).equals("缺陷生命周期")) {
				content.add("01.新建");
				continue;
			}
			if (basic.get(m).equals("严重等级")) {
				System.out.println("===>请输入'严重等级', a表示A类，b表示B类，直接回车表示C类：");
				scanner.nextLine();
				String keyValue = scanner.nextLine();
				if (keyValue.equals("a"))
					content.add("A类错误");
				else if (keyValue.equals("b"))
					content.add("B类错误");
				else if (keyValue.equals(""))
					content.add("C类错误");
				else {
					System.out.println("===>输入的字符有误...按照A类BUG处理");
					content.add("A类错误");
				}
				continue;
			}

			if (basic.get(m).equals("发现者") && !name.equals("")) {
				content.add(name);
				System.out.println("===>已自动添加发现者为:" + name);
				continue;
			}
			if (basic.get(m).equals("提交给") && !pm.equals("")) {
				content.add(pm);
				System.out.println("===>已自动添加提交给为:" + pm);
				continue;
			}
			if (basic.get(m).equals("芯片平台") && !chipType.equals("")) {
				content.add(chipType);
				System.out.println("===>已自动添加芯片平台为:" + chipType);
				continue;
			}
			if (basic.get(m).equals("硬件平台") && !machineType.equals("")) {
				content.add(machineType);
				System.out.println("===>已自动添加硬件平台为:" + machineType);
				continue;
			}

			System.out.println("===>请输入'" + basic.get(m) + "'：");
			str = scanner.next().replace("�", "");
			if (basic.get(m).equals("缺陷标题"))
				title = str;
			if (basic.get(m).equals("发现者"))
				name = str;
			if (basic.get(m).equals("提交给"))
				pm = str;
			if (basic.get(m).equals("芯片平台"))
				chipType = str;
			if (basic.get(m).equals("硬件平台"))
				machineType = str;

			content.add(str);

		}
		showBug();
	}

	/**
	 * 展示输入的BUG内容，供方法collectInfo调用
	 */
	public static void showBug() {

		System.out.println();
		System.out
				.println("**********************BUG描述开始***********************");

		for (int i = 0; i < basic.size(); i++) {
			if (basic.get(i).equals("ID"))
				continue;
			System.out.println(basic.get(i) + ":");
			System.out.println(content.get(i));
			System.out.println("");
		}
		System.out
				.println("**********************BUG描述结尾***********************");
		System.out.println();

	}

	/**
	 * 获取BUG发现日期
	 * 
	 * @return 年/月/日
	 */
	public static String dateForBug() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR); // 获取年;
		int month = calendar.get(Calendar.MONTH); // 获取月;
		int date = calendar.get(Calendar.DATE); // 获取天;
		boolean flag = false;

		// 处理系统日期是默认值的情况

		while (true) {

			if (year < 2016) {

				if (!bugDate.equals(""))
					return bugDate;

				System.out
						.println("===>系统日期有误，请手动输入年月日，长度必须是8位，如：20160401 表示2016年4月1日");

				try {
					while (true) {
						flag = true;
						String str = new Scanner(System.in).next().replace("�",
								"");
						if (str.length() != 8) {
							System.out.println("===>日期长度有误，请按要求重新输入...");
							continue;
						}

						String getYear = str.substring(0, 4);
						String getMonth = str.substring(4, 6);
						String getDay = str.substring(6, 8);

						year = Integer.valueOf(getYear);
						month = Integer.valueOf(getMonth);
						date = Integer.valueOf(getDay);
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				if (flag) {
					bugDate = year + "/" + month + "/" + date;
					flag = false;
					return bugDate;
				} else
					return year + "/" + (month + 1) + "/" + date;
			}
		}

	}
}
