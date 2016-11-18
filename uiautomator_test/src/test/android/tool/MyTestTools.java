package test.android.tool;

import java.io.File;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import android.util.Log;

public class MyTestTools extends UiAutomatorTestCase {

	/**
	 * 把打印同时输出到控制台和android的Log输出中，此方法输出默认tag为Test
	 * 
	 * @param s
	 */
	public void printLog(String s) {
		System.out.println(s);
		Log.i("TEST", s);
	}

	/**
	 * 打印输出测试结果是pass还是fail, 并给出结论说明
	 * 
	 * @param tag
	 *            对应用例的tag标签
	 * @param result
	 *            对应用例的测试结果，true为pass，false为failed
	 * @param reason
	 *            测试失败的原因
	 */
	public void setTestResult(String tag, boolean result, String reason) {
		String ret = " 测试结论: ";
		if (result) {
			printLog(ret + tag + " pass !!!");
		} else {
			printLog(ret + tag + " failed !!! 原因:" + reason);
			fail();
		}
	}

	/**
	 * 按照指定的路径进行截图，格式默认为png (路径中间目录不存在时会自动创建)
	 * 
	 * @param path
	 *            截图文件存放路径
	 * @return true 截图成功 false 其他
	 */
	public boolean screenCap(String path) {
		File png = new File(path);

		// 创建目录 如果不存在 ---start
		int index = path.lastIndexOf("/");
		if (index == -1) {
			printLog("/ is not exists");
			return false;
		}
		String tmpPath = path.substring(0, index);

		File file = new File(tmpPath);
		if (!file.exists()) {
			printLog("tmpPath = " + tmpPath);
			printLog("mkdirs ret = " + file.mkdirs());
		}
		// 创建目录 如果不存在 ---end

		printLog("screen Captured...");
		return getUiDevice().takeScreenshot(png);
	}

}
