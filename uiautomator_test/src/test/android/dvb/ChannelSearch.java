package test.android.dvb;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;

public class ChannelSearch extends MyTestCaseHelper {

	static String id = "";

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
		id = "";
		printLog("setUp out...");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		printLog("tearDown in...");

		if (id.equals("[test04010101dvb010114]")) {
			printLog("将主频点调制方式还原为QAM64");
			setMainFreq("443");
			getUiDevice().pressDPadDown();
			sleep(1000);
			getUiDevice().pressEnter();
			sleep(500);
			pressNumKey("6875");
			getUiDevice().pressEnter();
			sleep(1000);
			getUiDevice().pressDPadDown();
			sleep(1000);

			// 判断调制方式是否跟指定的一致
			int i = 0;
			while (!new UiObject(new UiSelector().textContains("QAM"))
					.getText().equals("QAM64")) {
				if (i < 5)
					getUiDevice().pressDPadLeft();
				else
					getUiDevice().pressDPadRight();
				i++;
			}
			pressBacks(1);
		}

		if (id.equals("[test04010101dvb010115]")) {
			printLog("将主频点符号率还原为6875");
			setMainFreq("443");
			getUiDevice().pressDPadDown();
			sleep(1000);
			getUiDevice().pressEnter();
			sleep(500);
			pressNumKey("6875");
			getUiDevice().pressEnter();
			sleep(1000);

			pressBacks(1);
		}
		printLog("tearDown out...");

	}

	public void test04010101dvb010101() {
		String tag = "[test04010101dvb010101]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test04010101dvb010101);

		try {
			clearChannelData("1234");
			// 清空频道数据，不判断结果
			if (!isClearDataOK()) {
				setTestResult(tag, false, "清空频道数据失败");
				return;
			}

			sleep(2000); // 习惯性等几秒再向下操作

			new UiObject(new UiSelector().text("否")).click();
			if (!new UiObject(new UiSelector().text("菜单")).exists()
					|| isChannelNull()) {
				// 如果没有打开菜单，或者打开菜单后频道列表为空没有隐藏
				printLog("menu exists? "
						+ new UiObject(new UiSelector().text("菜单")).exists());
				printLog("isChannel? " + isChannelNull());
				setTestResult(tag, false, "频道列表为空的否按钮没有呼出菜单或没有隐藏对话框");
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

			// 打印成功 setTestResult(tag, true, "");

		} catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}

	}

	public void test04010101dvb010102() {
		String tag = "[test04010101dvb010102]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test04010101dvb010102);

		try {
			String freq1 = "451";
			String freq2 = "459";

			// 步骤1
			autoSearch(freq1);
			printLog("excute autoSearch " + freq1);

			// 搜索失败，则测试fail
			if (!isSearchOK()) {
				setTestResult(tag, false, "自动搜索失败");
				return;
			}

			String[] channels = new String[] { "1", "2", "3" };
			for (String i : channels) {
				printLog("changeChannel ret = " + changeChannel(i));
				if (!new UiObject(new UiSelector().textContains("F" + freq1))
						.exists()) {
					setTestResult(tag, false, freq1 + "频点下的频道名称不包含'F" + freq1
							+ "'的部分,无法判断结果");
				}
			}

			// 步骤2
			autoSearch(freq2);
			printLog("excute autoSearch " + freq2);

			// 搜索失败，则测试fail
			if (!isSearchOK()) {
				setTestResult(tag, false, "自动搜索失败");
				return;
			}

			for (String i : channels) {
				printLog("changeChannel ret = " + changeChannel(i));
				if (!new UiObject(new UiSelector().textContains("F" + freq2))
						.exists()) {
					setTestResult(tag, false, freq2 + "频点下的频道名称不包含'F" + freq2
							+ "'的部分,无法判断结果");
				}
			}

			// 打印成功
			setTestResult(tag, true, "");

		} catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}

	}

	// 搜索的频点714不存在
	public void atest04010101dvb010110() {
		String tag = "[test04010101dvb010110]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test04010101dvb010110);

		try {
			String freq = "714";
			autoSearch(freq);
			printLog(tag + "excute autoSearch " + freq);

			if (!isSearchOK()) {
				setTestResult(tag, true, "");
			} else {
				printLog(tag + " search is ok");
				setTestResult(tag, false, "搜索不存在的频点，没有搜索失败提示");
			}

		}

		catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}
	}

	// 搜索的频点586 TS未勾选SDT
	public void atest04010101dvb010111() {
		String tag = "[test04010101dvb010111]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test04010101dvb010111);

		try {
			String freq = "586";
			autoSearch(freq);
			printLog("excute autoSearch " + freq);

			if (isSearchOK()) {
				setTestResult(tag, false, "出现搜索成功提示");
			}
			setTestResult(tag, true, "");

		}

		catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}
	}

	// 搜索的频点578 NIT未勾选TS
	public void atest04010101dvb010112() {
		String tag = "[test04010101dvb010112]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test04010101dvb010112);

		try {
			String freq = "578";
			autoSearch(freq);
			printLog("excute autoSearch " + freq);

			if (isSearchOK()) {
				setTestResult(tag, false, "出现搜索成功提示");
			}
			setTestResult(tag, true, "");

		}

		catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}
	}

	// 搜索的频点570 不发NIT表
	public void atest04010101dvb010113() {
		String tag = "[test04010101dvb010113]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test04010101dvb010113);

		try {
			String freq = "570";
			autoSearch(freq);
			printLog("excute autoSearch " + freq);

			if (isSearchOK()) {
				setTestResult(tag, false, "出现搜索成功提示");
			}
			setTestResult(tag, true, "");

		}

		catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}
	}

	public void test04010101dvb010114() {
		String tag = "[test04010101dvb010114]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test04010101dvb010114);

		id = tag; // 为了进入teardown里进行后续还原设置

		try {
			String[] qams = new String[] { "16", "32", "128", "256" };
			for (String qam : qams) {
				autoSearch(MAIN_FREQUENCY, "6875", qam);
				printLog("excute autoSearch " + MAIN_FREQUENCY
						+ "MHz, 6875Ks/s, QAM" + qam);

				if (isSearchOK()) {
					setTestResult(tag, false, "出现搜索成功提示");
				}
				pressBacks(4);
			}
			setTestResult(tag, true, "");
		}

		catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}
	}

	public void test04010101dvb010115() {
		String tag = "[test04010101dvb010115]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test04010101dvb010115);

		id = tag; // 为了进入teardown里进行后续还原设置

		try {
			String[] symbolRates = new String[] { "5500", "1000", "7200" };
			for (String symbolRate : symbolRates) {
				autoSearch(MAIN_FREQUENCY, symbolRate, "64");
				printLog("excute autoSearch " + MAIN_FREQUENCY + "MHz, "
						+ symbolRate + "Ks/s, QAM64");

				if (isSearchOK()) {
					setTestResult(tag, false, "出现搜索成功提示");
				}
				pressBacks(4);
			}
			setTestResult(tag, true, "");
		}

		catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}
	}

	public void test04010101dvb010116() {
		String tag = "[test04010101dvb010116]";
		printLog("========================== " + tag
				+ " start ==========================\n");
		printLog(TestCaseDisc.test04010101dvb010116);

		try {
			autoSearch("252");
			printLog("excute autoSearch 252MHz");

			if (isSearchOK()) {
				setTestResult(tag, false, "出现搜索成功提示");
			}
			setTestResult(tag, true, "");
		}

		catch (Exception e) {
			setTestResult(tag, false, "出现异常:" + e.getMessage());
		} finally {
			printLog("\n========================== " + tag
					+ " end ==========================");
		}
	}
}
