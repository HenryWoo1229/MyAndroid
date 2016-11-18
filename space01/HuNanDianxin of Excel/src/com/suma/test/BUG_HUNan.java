package com.suma.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class BUG_HUNan {

	List<String> steps = new ArrayList<String>(); // 分步骤(最后合成为复现步骤用)
	List<String> basic = new ArrayList<String>(); // 待写入Excel的要素

	static String title = ""; // 缺陷标题
	String name = "余松"; // 缺陷发现者
	String condition = ""; // 前置条件
	String step = ""; // 复现步骤
	String result = ""; // 实际结果
	String expect = ""; // 预期结果
	String probability = ""; // 复现概率
	String discription = ""; // 缺陷描述
	String bugDate = "";
	String state = "01.新建";
	static String id = ""; // bugID
	String module = ""; // 所属模块
	String comment = ""; // 备注

	String baseDir = "/data/Bug_Center/"; // 基文件夹路径

	/**
	 * 设置缺陷描述的内容
	 */
	public void setDiscription() {
		this.discription = condition + step + result + expect;
	}

	/**
	 * 设置缺陷基本元素的内容
	 */
	public void setBasic() {
		this.basic.add(id); // id
		// this.basic.add(title);
		this.basic.add(discription);
		this.basic.add(""); // 业务账号
		this.basic.add(bugDate);
		this.basic.add(""); // 发现版本
		this.basic.add(name);
		this.basic.add(state);
		this.basic.add(""); // 解决版本
		this.basic.add(""); // 优先级
		this.basic.add(""); // 所属模块
		this.basic.add(""); // 备注

	}

	/**
	 * 负责搜集BUG各要素的基础信息
	 */
	public void collectInfo() {

		Scanner scanner = new Scanner(System.in);
		// System.out.println("请输入'发现者'：");
		// name = scanner.next().replace("�", ""); // Android
		// Shell里删除中文的时候会多出来这个乱码
		// System.out.println("===>请输入'缺陷标题'的内容：");
		// title = scanner.next().replace("�", "");
		System.out.println("===>请输入'前置条件'的内容：");
		condition = "\n\n前置条件:\n" + " " + scanner.next().replace("�", "");

		//
		// while (true) {
		// System.out.println("===>请输入'复现概率'(输入0-100整数)：");
		// int prob = scanner.nextInt();
		// if (prob > 100 || prob < 0) {
		// System.out.println("输入的概率超过了100或者小于0");
		// continue;
		// }
		// probability = "复现概率:" + prob;
		// break;
		// }

		int i = 0;
		while (true) {
			System.out.println("===>请输入'复现步骤" + (i + 1) + "'的内容：");
			steps.add(" " + (i + 1) + "、" + scanner.next().replace("�", "")
					+ "\n");
			System.out.println("===>是否添加新的步骤?(输入1添加，0不添加)");
			if (scanner.next().equals("0")) {
				for (int j = 0; j < steps.size(); j++)
					step = step + steps.get(j);
				step = "\n\n复现步骤:\n" + step;
				break;
			}
			i++;
		}
		System.out.println("===>请输入'实际结果'的内容：");
		result = "\n实际结果:\n" + " " + scanner.next().replace("�", "");
		System.out.println("请输入'预期结果'的内容：");
		expect = "\n\n预期结果:\n" + " " + scanner.next().replace("�", "");

		setDiscription();
		bugDate = dateForBug();
	}

	public void showBug() {

		System.out.println("\nBUG展示如下:");
		System.out.println("1、前置条件：" + condition);

		int next = 2;
		for (int i = 0; i < steps.size(); i++) {
			System.out
					.println((i + 2) + "、复现步骤" + (i + 1) + "：" + steps.get(i));
			next = i + 3;
		}
		System.out.println(next + "、实际结果：" + result);
		System.out.println((next + 1) + "、预期结果：" + result);

	}

	/**
	 * 获取BUG发现日期
	 * 
	 * @return 年/月/日
	 */
	public String dateForBug() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR); // 获取年;
		int month = calendar.get(Calendar.MONTH); // 获取月;
		int date = calendar.get(Calendar.DATE); // 获取天;
		boolean flag = false;

		// 处理系统日期是默认值的情况

		System.out.println("year is " + year);
		while (true) {
			if (year < 2016) {
				System.out
						.println("获取的年份小于2016，请手动输入年月日，以英文字符逗号隔开(如2016,3,24)");

				while (true) {
					flag = true;
					String str = new Scanner(System.in).next();
					String[] strs = str.split(",");
					if (strs.length != 3) {
						System.out
								.println("输入的年月日格式不对，请确认逗号是英文字符，且输入格式为2016,3,24，请重新输入:");
						continue;
					}
					if (Integer.valueOf(strs[1]) < 1
							|| Integer.valueOf(strs[1]) > 12) {
						System.out.println("输入的月份不在1-12范围内，请重新输入年月日:");
						continue;
					}
					if (Integer.valueOf(strs[2]) < 1
							|| Integer.valueOf(strs[2]) > 31) {
						System.out.println("输入的月份不在1-31范围内，请重新输入年月日:");
						continue;
					}
					year = Integer.valueOf(strs[0]);
					month = Integer.valueOf(strs[1]);
					date = Integer.valueOf(strs[2]);
					break;

				}
			} else {
				if (flag)
					return year + "/" + month + "/" + date;
				else
					return year + "/" + (month + 1) + "/" + date;
			}
		}
	}
}
