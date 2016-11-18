import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class RecordKey {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RecordKey rk = new RecordKey();

		File file = new File(
				"E:\\Android_WorkSpace\\space01\\recordAndroidKey\\lib\\log.log");
		File filew = new File(
				"E:\\Android_WorkSpace\\space01\\recordAndroidKey\\lib\\write.sh");
		try {
			filew.createNewFile();

			FileWriter fw = new FileWriter(filew);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("#! /system/bin/sh \n\n");
			FileInputStream input = new FileInputStream(file);
			BufferedReader read = new BufferedReader(new InputStreamReader(
					input));
			String line = "";
			String find1 = null;
			String find2 = null;
			String line1 = null;
			String line2 = null;
			while ((line = read.readLine()) != null) {
				find1 = rk.findKeyCode(line);
				if (find1 != null) {
					line1 = line;
					if (find2 == null) {
						find2 = find1;
						line2 = line1;
						bw.write("echo 'input keyevent " + find2 + "'" + "\n");
						bw.write("input keyevent " + find2 + "\n");
					} else if (find2 != "") {
						int delay = rk.timeHandler(line2, line1);
						bw.write("echo 'sleep " + delay + "'" + "\n");
						bw.write("sleep " + delay + "\n");
						bw.write("echo 'input keyevent " + find1 + "'" + "\n");
						bw.write("input keyevent " + find1 + "\n");
						find2 = find1;
						line2 = line1;
					}
				}
			}
			read.close();
			bw.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 返回logcat -v time输出的两行日志的时间差
	 * 
	 * @param line1
	 *            logcat -v time输出的一条日志1
	 * @param line2
	 *            logcat -v time输出的一条日志2
	 * @return 时间差，单位秒。如果line2比line1时间距离现在更远，则返回错误值-1
	 */
	public int timeHandler(String line1, String line2) {
		String str1 = line1.substring(6, 14);
		String str2 = line2.substring(6, 14);

		int hour1 = Integer.valueOf(str1.substring(0, 2));
		int hour2 = Integer.valueOf(str2.substring(0, 2));

		int min1 = Integer.valueOf(str1.substring(3, 5));
		int min2 = Integer.valueOf(str2.substring(3, 5));

		int sec1 = Integer.valueOf(str1.substring(6, 8));
		int sec2 = Integer.valueOf(str2.substring(6, 8));

		long time1 = hour1 * 3600 + min1 * 60 + sec1;
		long time2 = hour2 * 3600 + min2 * 60 + sec2;

		if ((time2 - time1) < 0)
			return -1;

		return (int) (time2 - time1);
	}

	/**
	 * 查找ott或者dvb机顶盒按键的键值(两类项目的按键日志不完全一致)
	 * 
	 * @param line
	 *            logcat -v time输出的一条日志
	 * @return 返回按键键值；无键值信息则返回null
	 */
	public String findKeyCode(String line) {
		int indexott = line.indexOf("keycode:");
		int indexott2 = line.indexOf("realAction: true");
		int indexdvb = line.indexOf("onKeyDown keyCode");
		if (indexott != -1 && indexott2 != -1)
			return line.substring((indexott + 9), (indexott + 11));
		if (indexdvb != -1)
			return line.substring((indexdvb + 20), (indexdvb + 22));

		return null;
	}

}
