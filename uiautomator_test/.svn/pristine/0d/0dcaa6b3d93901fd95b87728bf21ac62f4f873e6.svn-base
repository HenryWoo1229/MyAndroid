package test.android.dvb;

public class EPG extends MyTestCaseHelper {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		int i = 0;
		while (i < 4) {
			getUiDevice().pressBack(); // 连按几次back键，确保菜单不处于打开状态
			printLog("Press Key Back.");
			i++;
		}

	}

	/**
	 * 测试目的：频道列表中是否能搜到EPG信息
	 * 
	 * @param 步骤1
	 *            执行自动搜索，搜索失败则fail
	 * @param 步骤2
	 *            切台到001，打开频道列表-EPG菜单，判断是否搜到EPG，搜不到则fail，否则pass
	 * 
	 * @param 编写者
	 *            吴昊
		 */
	public void test01_EPGSearch() {
		String tag = "test01_EPGSearch ";
		printLog(" enter");

		try {
			String freq = "251";
			autoSearch(freq);
			printLog("excute autoSearch " + freq);

			// 搜索失败，则测试fail
			if (!isSearchOK()) {
				setTestResult(tag, false, "自动搜索失败");
				return;
			}

			pressNumKey("001"); // 切台到001
			getUiDevice().pressEnter(); // 隐藏PF
			getUiDevice().pressEnter(); // 打开频道列表
			getUiDevice().pressDPadRight(); // 打开EPG列表

			if (!isEPGOK()) {
				setTestResult(tag, false, "没有搜到EPG");
				return;
			}

			// 打印搜索成功
			setTestResult(tag, true, "");

		} catch (Exception e) {
			setTestResult(tag, false, e.getMessage());
		}

	}


}
