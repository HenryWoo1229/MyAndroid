package test.android.dvb;

import java.util.Calendar;
import java.util.Date;

import test.android.tool.MyTestTools;
import android.view.KeyEvent;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;

public class MyTestCaseHelper extends MyTestTools {

	static final String MAX_CHANNEL_NUM = "113"; // 环境中最大的频道号
	static final String NIT_CHANNEL_NUM = "100"; // 环境中NIT表跟其他频道不通的频道号

	static final String MAIN_FREQUENCY = "443"; // 环境中的主频点值
	static final String PF_NO_CHANNEL = "不存在此频道"; // PF条上不存在此频道的文字
	static final String NIT_UPDATE_TEXT = "机顶盒节目列表有更新"; // PF条上不存在此频道的文字

	static final int EPG_TIME_OUT = 31; // 等待EPG的超时时长

	/**
	 * 根据输入的数字按键字符串，依次按下按键，如key="251"，则按下按键2,5,1
	 * 
	 * @param key
	 *            输入的数字按键
	 */
	public void pressNumKey(String key) {
		UiDevice device = getUiDevice();
		int[] list = new int[key.length()];
		String tmp;

		// 把key按单字符转换为int型数组
		for (int i = 0; i < key.length(); i++) {
			tmp = key.substring(i, i + 1);
			list[i] = Integer.parseInt(tmp);
		}

		for (int i = 0; i < list.length; i++) {
			switch (list[i]) {
			case 0:
				device.pressKeyCode(KeyEvent.KEYCODE_0);
				break;

			case 1:
				device.pressKeyCode(KeyEvent.KEYCODE_1);
				break;

			case 2:
				device.pressKeyCode(KeyEvent.KEYCODE_2);
				break;

			case 3:
				device.pressKeyCode(KeyEvent.KEYCODE_3);
				break;

			case 4:
				device.pressKeyCode(KeyEvent.KEYCODE_4);
				break;

			case 5:
				device.pressKeyCode(KeyEvent.KEYCODE_5);
				break;

			case 6:
				device.pressKeyCode(KeyEvent.KEYCODE_6);
				break;

			case 7:
				device.pressKeyCode(KeyEvent.KEYCODE_7);
				break;

			case 8:
				device.pressKeyCode(KeyEvent.KEYCODE_8);
				break;

			case 9:
				device.pressKeyCode(KeyEvent.KEYCODE_9);
				break;

			}
		}

	}

	/**
	 * 按下菜单键，然后进入主频点设置菜单完成频点设置
	 * 
	 * @param device
	 * @throws UiObjectNotFoundException
	 *             找不到UI控件异常
	 */
	public void setMainFreq(String frequency) throws UiObjectNotFoundException {
		printLog("setMainFreq enter");
		getUiDevice().pressMenu();
		sleep(1000);
		new UiObject(new UiSelector().text("频道搜索"))
				.clickAndWaitForNewWindow(1000);
		new UiObject(new UiSelector().text("主频点设置"))
				.clickAndWaitForNewWindow(1000);
		getUiDevice().pressEnter();
		sleep(1000);
		pressNumKey(frequency); // 依次输入按键
		getUiDevice().pressEnter();
		// getUiDevice().click(1100, 600); // 按键盘的确定
		sleep(1000);

	}

	/**
	 * 按下菜单键，设置完主频点并点击自动搜索，不对搜索结果做判断，判断搜索结果需立即调用isSearchOK()
	 * 
	 * @param frequency
	 *            要设置的主频点MHz, 符号率默认6875, 调制方式默认64QAM
	 * @throws UiObjectNotFoundException
	 *             找不到UI控件异常
	 */
	public void autoSearch(String frequency) throws UiObjectNotFoundException {
		printLog("autoSearch enter");
		setMainFreq(frequency);
		getUiDevice().pressBack();
		sleep(1000);

		// 执行自动搜索
		new UiObject(new UiSelector().text("自动搜索")).click();
	}

	/**
	 * 按照指定频率，符号率，调制方式进行自动搜索
	 * 
	 * @param frequency
	 *            频率MHz
	 * @param symbolRate
	 *            符号率KS/s
	 * @param qam
	 *            调制方式 16 32 64 128 256
	 * @throws UiObjectNotFoundException
	 */
	public void autoSearch(String frequency, String symbolRate, String qam)
			throws UiObjectNotFoundException {
		printLog("autoSearch enter");
		setMainFreq(frequency);
		getUiDevice().pressDPadDown();
		sleep(1000);
		getUiDevice().pressEnter();
		sleep(500);
		pressNumKey(symbolRate);
		getUiDevice().pressEnter();
		sleep(1000);
		getUiDevice().pressDPadDown();
		sleep(1000);

		// 判断调制方式是否跟指定的一致
		int i = 0;
		while (!new UiObject(new UiSelector().textContains("QAM")).getText()
				.equals("QAM" + qam)) {
			if (i < 5)
				getUiDevice().pressDPadLeft();
			else
				getUiDevice().pressDPadRight();
			i++;
		}

		getUiDevice().pressBack();
		sleep(1000);

		// 执行自动搜索
		new UiObject(new UiSelector().text("自动搜索")).click();
	}

	/**
	 * 按下菜单键，设置完主频点并点击自动搜索，不对搜索结果做判断，判断搜索结果需立即调用isSearchOK()
	 * 
	 * @param frequency
	 *            要设置的主频点
	 * @throws UiObjectNotFoundException
	 *             找不到UI控件异常
	 */
	public void pressChannelEmpty(String frequency)
			throws UiObjectNotFoundException {
		printLog("pressChannelEmpty enter");
		setMainFreq(frequency);
		pressBacks(4);
		sleep(1000);

		new UiObject(new UiSelector().text("是")).click();

	}

	/**
	 * 按下菜单键，进入手动搜索菜单设置完频点，并执行搜索，不对搜索结果做判断，判断搜索结果需立即调用isSearchOK()
	 * 
	 * @param frequency
	 *            要搜索的频点
	 * @throws UiObjectNotFoundException
	 */
	public void manualSearch(String frequency) throws UiObjectNotFoundException {
		printLog("manualSearch enter");
		getUiDevice().pressMenu();
		sleep(1000);
		new UiObject(new UiSelector().text("频道搜索"))
				.clickAndWaitForNewWindow(1000);
		new UiObject(new UiSelector().text("手动搜索"))
				.clickAndWaitForNewWindow(1000);

		getUiDevice().pressEnter();
		pressNumKey(frequency); // 依次输入按键
		sleep(1000);
		getUiDevice().pressEnter();
		// getUiDevice().click(1100, 600); // 按键盘的确定
		sleep(1000);
		getUiDevice().pressDPadDown();
		getUiDevice().pressDPadDown();
		getUiDevice().pressDPadDown();
		getUiDevice().pressEnter();
	}

	/**
	 * 判断是否搜索成功，方法先判断是否有搜索失败，如有则返回false；然后判断搜索完成，如没有则返回false，最后判断能否切台到001，
	 * 切失败则返回false，否则返回true
	 * 
	 * 调用方法后不需要额外添加等待时间
	 * 
	 * @return true 表明搜索成功；
	 * 
	 *         false 表明搜索不成功
	 * @throws UiObjectNotFoundException
	 *             找不到UI控件异常
	 */
	public boolean isSearchOK() throws UiObjectNotFoundException {
		//
		UiObject searchFail = new UiObject(
				new UiSelector().textContains("搜索失败"));
		UiObject searchCompl = new UiObject(
				new UiSelector().textContains("搜索完成"));

		//
		UiObject progress = new UiObject(new UiSelector().textContains("%"));
		String progressOld = "0%";
		String progressNew = "0%";
		printLog("search channel progress 0%");

		Calendar timeOut = Calendar.getInstance();
		while (true) {
			progressNew = progress.getText();
			if (!progressOld.equals(progressNew)) { // 处理进度条的显示
				printLog("search channel progress " + progressNew);
				progressOld = progressNew;
			}
			if (searchFail.exists()) { // 搜索失败的内容存在，则false
				printLog("search failed !!!");
				return false;
			} else if (progressNew.equals("100%")) { // 进度达到100%
				Calendar before = Calendar.getInstance();
				while (true) {
					if (searchCompl.exists()) { // 搜索完成的内容存在
						break;
					} else if (Calendar.getInstance().getTimeInMillis()
							- before.getTimeInMillis() > 10000) { // timed
						// out
						printLog("no 'search complete' text !!!");
						return false;
					}
				}
				printLog("search complete !!!");
				sleep(500);
				getUiDevice().pressEnter(); // 按确定键关闭搜索完成按钮
				sleep(1000);
				pressNumKey("001");
				printLog("change channel to 001");
				sleep(1500);
				if (new UiObject(new UiSelector().text("001")).exists()) { // 切台到001成功
					printLog("channel 001 exists");
					return true;
				} else {
					printLog("channel 001 not exists");
					return false;
				}
			} else if (progressNew.equals("0%")
					&& (Calendar.getInstance().getTimeInMillis()
							- timeOut.getTimeInMillis() > 30000)) { // 防止卡在0%处不动了
				printLog("卡在0%处超过30秒");
				return false;
			} else if (Calendar.getInstance().getTimeInMillis()
					- timeOut.getTimeInMillis() > 300000) { // 搜索过程整体超时时间
				printLog("搜索过程超时，大于300秒");
				return false;
			}
		}

	}

	/**
	 * 判断频道数据是否为空
	 * 
	 * @return true 表明频道数据为空；
	 * 
	 *         false 表明结果不确定
	 * @throws UiObjectNotFoundException
	 *             找不到UI控件异常
	 */
	public boolean isChannelNull() throws UiObjectNotFoundException {
		UiObject chanEmpty = new UiObject(
				new UiSelector().textContains("频道列表为空"));

		if (chanEmpty.exists()) {
			printLog("channel list null");
			return true;
		}
		return false;
	}

	/**
	 * 按下菜单键，进入清除频道，输入密码后确定，不对清除结果做判断，判断结果需立即调用isClearDataOK()
	 * 
	 * @param password
	 *            密码
	 * @throws UiObjectNotFoundException
	 *             找不到UI控件异常
	 */
	public void clearChannelData(String password)
			throws UiObjectNotFoundException {
		printLog("clearChannelData enter");
		getUiDevice().pressMenu();
		new UiObject(new UiSelector().text("频道管理")).click();
		sleep(500);
		new UiObject(new UiSelector().text("清除频道"))
				.clickAndWaitForNewWindow(1000);
		getUiDevice().pressEnter();
		pressNumKey(password);
		getUiDevice().pressBack();
		new UiObject(new UiSelector().text("确定")).click();
	}

	/**
	 * 判断清除数据是否成功，需要与清除数据的方法clearChannelData搭配使用
	 * 
	 * @return true 清除数据成功，捕获到“正在清除数据”和“清除数据成功”的提示，并且频道数据为空;
	 * 
	 *         false 清除数据失败，上述提示有一个没捕获到，或者捕获到了以后，判断频道数据不为空
	 * @throws UiObjectNotFoundException
	 */
	public boolean isClearDataOK() throws UiObjectNotFoundException {
		UiObject ui1 = new UiObject(new UiSelector().text("正在清除数据"));
		UiObject ui2 = new UiObject(new UiSelector().text("清除数据成功"));
		boolean ui1flag = false;
		boolean ui2flag = false;

		int i = 0;
		while (true) {
			printLog("clear data progress " + (i++));
			sleep(200);
			if (ui1.exists())
				ui1flag = true;
			if (ui2.exists())
				ui2flag = true;

			// ===============================分割线=============================

			if (ui1flag && ui2flag) { // 捕获到2个提示信息
				pressBacks(5); // 连续按几次back键
				if (isChannelNull()) // 频道数据不为空
					return true;
				else {
					return false;
				}
			} else if (i > 30) { // 重复查找，如果仍然没找到再抛出失败提示
				if (!ui1flag && ui2flag) {
					printLog("no 'cleaning data' text");
					return false;
				} else if (!ui2flag && ui1flag) {
					printLog("no 'clean data successfully' text");
					return false;
				} else if (!ui2flag && !ui1flag) {
					printLog("no 'clean data successfully' and 'cleaning data' text");
					return false;
				}
			}

		}
	}

	/**
	 * 重复按下Back键
	 * 
	 * @param times
	 *            重复按键次数
	 */
	public void pressBacks(int times) {
		int i = 0;
		while (i < times) {
			getUiDevice().pressBack();
			printLog("Press Key Back.");
			i++;
		}
	}

	/**
	 * 搜索epg信息，通过查找”无节目事件“来判定是否存在
	 * 
	 * @return true epg信息存在
	 * 
	 *         false epg信息不存在
	 */
	public boolean isEPGOK() {
		UiObject ui1 = new UiObject(new UiSelector().text("获取节目事件中"));
		UiObject ui2 = new UiObject(new UiSelector().text("无节目事件"));
		UiObject ui3 = new UiObject(new UiSelector().textContains("星期"));
		Calendar calc = Calendar.getInstance();
		long before = calc.getTimeInMillis(); // 用来判断超时的起始时间

		printLog("getting epg...");
		int i = 0;
		while (true) {
			if (calc.getTimeInMillis() - before > 30) {
				printLog("30s timed out...");
				return false;
			}

			if (ui1.exists())
				continue;
			else {
				if (ui3.exists()) { // epg列表打开状态
					if (!ui2.exists()) {
						printLog("got epg info...");
						return true;
					} else {
						printLog("no epg info...");
						return false;
					}
				} else { // epg列表关闭状态
					if (i > 2) {
						printLog("尝试重新打开epg列表3次均失败, 焦点状态已偏离");
						return false;
					}
					printLog("epg列表已关闭，尝试重新打开...");
					pressBacks(4);
					sleep(500);
					getUiDevice().pressEnter(); // 关闭pf条
					sleep(1000);
					getUiDevice().pressEnter(); // 打开频道列表
					if (!new UiObject(new UiSelector().textContains("星期"))
							.exists()) {
						getUiDevice().pressDPadRight(); // 打开EPG列表
					}
					i++;
					continue;
				}
			}
		}
	}

	/**
	 * 对指定频道号进行EPG的检查，如果全都存在则返回true，一旦某个不存在则停止检查，并返回false
	 * 
	 * @return true 指定的频道epg信息都存在 false 有频道的epg信息不存在
	 */
	public boolean checkEPG(String[] channels) {
		pressBacks(4); // 按n次返回键

		int times = 0;
		while (times < channels.length) {
			changeChannel(channels[times]); // 切台
			sleep(10000); // 等到PF条自动隐藏
			getUiDevice().pressEnter(); // 打开频道列表
			if (!new UiObject(new UiSelector().textContains("星期")).exists()) {
				getUiDevice().pressDPadRight(); // 打开EPG列表
			}

			sleep(200);
			if (!isEPGOK()) // 判断是否有节目事件
				return false;

			pressBacks(3); // 按n次返回键关闭菜单
			times++;
		}
		return true;
	}

	/**
	 * 切台到指定频道，切台成功返回true，其他返回false
	 * 
	 * @param num
	 *            频道号
	 * @return true 切台成功 false 其他
	 */
	public boolean changeChannel(String num) {
		UiObject ui1 = new UiObject(
				new UiSelector().text(ChannelSearch_Smoke.PF_NO_CHANNEL));

		pressNumKey(num);
		printLog("change channel to " + num);

		if (num.length() < 3)
			getUiDevice().pressEnter();
		sleep(500);
		String text = num;
		if (num.length() == 1)
			text = "00" + num;
		if (num.length() == 2)
			text = "0" + num;

		if (new UiObject(new UiSelector().text(text)).exists() && !ui1.exists()) {
			printLog("channel exists");
			return true;
		} else {
			printLog("channel not exists");
			return false;
		}

		//
		// sleep(1000);
		// getUiDevice().pressEnter();
		// getUiDevice().pressEnter(); // 呼出pf条
		//
		// if (ui1.exists()) {
		// printLog("channel not exists");
		// return false;
		// } else if (ui2.exists()) {
		// printLog("channel exists");
		// return true;
		// } else {
		// printLog("当前不是全屏直播界面或菜单被打开，无法切台");
		// return false;
		// }

	}

	/**
	 * 按下指定次数的指定按键
	 * 
	 * @param keyCode
	 *            按键键值
	 * @param times
	 *            按键次数
	 */
	public void pressKeyCode(int keyCode, int times) {
		int i = 0;
		printLog("press keyCode " + keyCode + ", " + times + " times");
		while (i < times) {
			getUiDevice().pressKeyCode(keyCode);
			i++;
		}
	}

	/**
	 * 检查节目列表更新提示框是否弹出，超时时间30秒
	 * 
	 * @return true 提示框弹出 false 超时时间内提示框未弹出
	 */
	public boolean isNITUpdate() {
		int time_out = 30; // 超时时间秒
		int time = 0;

		printLog("waiting for nit update text pop-up " + time_out
				+ " seconds...");
		while (true) {
			if (new UiObject(
					new UiSelector()
							.textContains(ChannelSearch_Smoke.NIT_UPDATE_TEXT))
					.exists()) {
				printLog("NIT update text pop-up !!!");
				return true;
			}
			if (time > time_out)
				break;
			sleep(1000);
			time++;
		}
		printLog("no NIT update text pop-up !!!");
		return false;
	}

}
