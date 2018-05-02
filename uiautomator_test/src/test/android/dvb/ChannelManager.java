package test.android.dvb;

public class ChannelManager extends MyTestCaseHelper {

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
	 * 测试目的：有节目数据的情况下，输入正确密码时，测试清除频道数据是否成功
	 * 
	 * @param 步骤1
	 *            判断频道列表是否不为空，为空则先执行一次手动搜索，如搜索失败，则测试fail
	 * @param 步骤2
	 *            进入清除频道数据菜单，输入默认密码，验证清除后是否弹出“正在清除数据”，“清除数据成功”字样，如没有，则fail
	 * @param 步骤3
	 *            判断频道数据是否为空，如为空，则测试pass，不为空，则fail
	 * 
	 * 
	 * @param 编写者
	 *            吴昊
	 */
	public void test01_ClearChannelData() {
		String tag = "test01_ClearChannelData ";
		printLog(tag + " enter");

		try {
			// 没有频道数据，先进行搜索
			if (isChannelNull()) {
				manualSearch("251"); // 手动搜索
				if (!isSearchOK()) { // 搜索不成功，测试fail
					setTestResult(tag, false, "");
					return;
				}
			}

			getUiDevice().pressBack(); // 按返回键，确保没有菜单被打开
			getUiDevice().pressBack();

			clearChannelData("1234"); // 清除频道数据
			if (!isClearDataOK()) {
				setTestResult(tag, false, "");
			} else {
				setTestResult(tag, true, "");
			}

		} catch (Exception e) {
			printLog("err msg: " + e.getMessage());
			setTestResult(tag, false, "");
		}
	}

}
