package test.android.dvb;

import android.view.KeyEvent;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;

public class ChannelSearch_Smoke extends MyTestCaseHelper {

	static String pngPath = "/sdcard/uiautomator/ChannelSearch_Smoke/";   //注意路径格式
	static String tag = "";
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		printLog("setUp in...");
		int i = 0;
		while (i < 4) {
			getUiDevice().pressBack(); // 连按几次back键，确保菜单不处于打开状态
			printLog("Press Key Back.");
			i++;
		}
		printLog("setUp out...");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		printLog("tearDown in...");
		
		printLog("ret = "+screenCap(pngPath + tag + "/end.png"));   //固定在结束时抓图1次
		
		printLog("tearDown out...");
	}

	public void test01_autoSearch() {
		tag = "[test01_autoSearch]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test01_autoSearch);
		printLog("ret = "+screenCap(pngPath + tag + "/start.png"));   //固定在开始时抓图1次

		try {
			// 频道列表不为空，测试fail
			if (!isChannelNull()) {
				printLog("channel is not null");
				setTestResult(tag, false, "首次开机频道列表不为空");
				return;
			}

			// 频道列表为空，执行搜索
			pressChannelEmpty(MAIN_FREQUENCY);
			printLog("excute autoSearch " + MAIN_FREQUENCY);

			// 搜索失败，则测试fail
			if (!isSearchOK()) {
				setTestResult(tag, false, "自动搜索失败");
				return;
			}

			// 切台到两位数频道
			if (!changeChannel("59")) {
				setTestResult(tag, false, "切台频道不存在");
				return;
			}
			if (!changeChannel(MAX_CHANNEL_NUM)) {
				setTestResult(tag, false, "切台频道不存在");
				return;
			}

			// 打印搜索成功
			setTestResult(tag, true, "");

		} catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}

	}

	public void test02_getEPG() {
		tag = "[test02_getEPG]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test02_getEPG);
		printLog("ret = "+screenCap(pngPath + tag + "/start.png"));   //固定在开始时抓图1次

		try {
			// 执行自动搜索
//			autoSearch(MAIN_FREQUENCY);
//			printLog("excute autoSearch " + MAIN_FREQUENCY);
//
//			// 搜索失败，则测试fail
//			if (!isSearchOK()) {
//				setTestResult(tag, false, "自动搜索失败");
//				return;
//			}

			String[] channels = new String[] { "1", "10", "30" }; // 选择几个不同频点的频道
			if (!checkEPG(channels)) { // 检查是否存在EPG
				setTestResult(tag, false, "没有搜到EPG");
			}
			//
			// 打印搜索成功
			setTestResult(tag, true, "");

		} catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}

	}

	public void test03_volume() {
		tag = "[test03_volume]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test03_volume);
		printLog("ret = "+screenCap(pngPath + tag + "/start.png"));   //固定在开始时抓图1次

		try {
			// 执行自动搜索
			autoSearch(MAIN_FREQUENCY);
			printLog("excute autoSearch " + MAIN_FREQUENCY);

			// 搜索失败，则测试fail
			if (!isSearchOK()) {
				setTestResult(tag, false, "自动搜索失败");
				return;
			}

			pressKeyCode(KeyEvent.KEYCODE_VOLUME_DOWN, 10);
			if (!new UiObject(new UiSelector().text("00")).exists()) {
				setTestResult(tag, false, "音量值没有调整到0");
				return;
			}

			pressKeyCode(KeyEvent.KEYCODE_VOLUME_UP, 20);
			if (!new UiObject(new UiSelector().text("15")).exists()) {
				setTestResult(tag, false, "音量值没有调整到15");
				return;
			}

			sleep(6000); // 等待音量条自动隐藏
			pressKeyCode(KeyEvent.KEYCODE_VOLUME_MUTE, 1);
			if (!new UiObject(new UiSelector().text("静音")).exists()) {
				setTestResult(tag, false, "静音图标没有显示");
				return;
			}

			sleep(2000);
			pressKeyCode(KeyEvent.KEYCODE_VOLUME_MUTE, 1);
			if (!new UiObject(new UiSelector().text("15")).exists()) {
				setTestResult(tag, false, "静音图标没有隐藏");
				return;
			}

			// 打印搜索成功
			setTestResult(tag, true, "");

		} catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}

	}

	/**
	 * 本用例需要提前播发NIT版本号不同于其他频道的码流
	 */
	public void test04_chanListUpdate() {
		tag = "[test04_chanListUpdate]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test04_chanListUpdate);
		printLog("ret = "+screenCap(pngPath + tag + "/start.png"));   //固定在开始时抓图1次

		try {
			// 执行自动搜索
			autoSearch(MAIN_FREQUENCY);
			printLog("excute autoSearch " + MAIN_FREQUENCY);

			// 搜索失败，则测试fail
			if (!isSearchOK()) {
				setTestResult(tag, false, "自动搜索失败");
				return;
			}

			if (!changeChannel(NIT_CHANNEL_NUM))
				setTestResult(tag, false, "切台到NIT频道" + NIT_CHANNEL_NUM
						+ "失败, 频道不存在");

			if (!isNITUpdate()) {
				setTestResult(tag, false, "节目列表更新提示框没有在超时时间内弹出");
				return;
			}

			sleep(1000);
			new UiObject(new UiSelector().text("确定")).click(); // 点击NIT更新提示框的确定按钮
			if (!isSearchOK()) {
				setTestResult(tag, false, "NIT更新提示框触发的自动搜索失败");
				return;
			}

			// 打印搜索成功
			setTestResult(tag, true, "");

		} catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}

	}

	/**
	 * 测试目的：测试手动搜索是否成功
	 * 
	 * @param 步骤1
	 *            ：进入手动搜索菜单，输入频点，开始搜索，若搜索失败，则测试fail；成功，则pass
	 * 
	 * @param 编写者
	 *            吴昊
	 * 
	 *            public void test02_ManualSearch() { tag =
	 *            "test02_ManualSearch "; printLog(tag + " enter");
	 * 
	 *            try { String freq = "251"; manualSearch(freq); printLog(tag +
	 *            "excute ManualSearch " + freq);
	 * 
	 *            // 搜索失败，则测试fail boolean searchResult = isSearchOK(); if
	 *            (!searchResult) { setTestResult(tag, false); return; }
	 * 
	 *            // 打印搜索成功 setTestResult(tag, true);
	 * 
	 *            } catch (Exception e) { printLog("err msg: " +
	 *            e.getMessage()); setTestResult(tag, false); } }
	 */

	/**
	 * 测试目的：测试手动搜索不存在的频点是否提示搜索失败
	 * 
	 * @param 步骤1
	 *            进入手动搜索，设置一个不存在的频点
	 * @param 步骤2
	 *            点击开始搜索，若搜索失败并且有搜索失败提示，则测试pass；否则fail
	 * 
	 * @param 编写者
	 *            吴昊
	 * 
	 *            public void test04_ManualSearchFail() { tag =
	 *            "test04_ManualSearchFail "; printLog(tag + " enter");
	 * 
	 *            try { String freq = "514"; manualSearch(freq); printLog(tag +
	 *            "excute manualSearch " + freq);
	 * 
	 *            // 搜索成功，则测试fail if (isSearchOK()) { printLog(tag +
	 *            " search is ok"); setTestResult(tag, false); return; }
	 * 
	 *            // 打印搜索成功 setTestResult(tag, true);
	 * 
	 *            } catch (Exception e) { printLog("err msg: " +
	 *            e.getMessage()); setTestResult(tag, false); } }
	 */
}
