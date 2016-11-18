package jing.love.i;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

public class MyBaby {
	private static final String CUR_PATH = System.getProperty("user.dir");
	private static ArrayList<String> dirs = new ArrayList<String>();
	private static ArrayList<String> fs = new ArrayList<String>();
	private static ArrayList<String> dirNames = new ArrayList<String>();
	private static ArrayList<String> fileNames = new ArrayList<String>();
	private static String newDir = CUR_PATH + File.separator + "重复的文件和文件夹";

	// private static String newDir = CUR_PATH;

	public MyBaby() {
	}

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		long before = calendar.getTimeInMillis();

		Thread time = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					System.err.println("文件数量较多，请耐心等待...");

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		time.setDaemon(true); // 守护线程在主线程死后也立即死掉
		time.start();

		File d = new File(newDir);
		if (d.exists())
			d.delete();

		traverseFolder2(CUR_PATH);
		d.mkdir();

		try {
			File allFiles = new File(CUR_PATH + File.separator + "所有重复的.txt");

			if (allFiles.exists())
				allFiles.delete();
			doSomething(dirs, dirNames, 0);
			System.out.println("=========================完成了一半");
			doSomething(fs, fileNames, -1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				long after = calendar.getTimeInMillis();
				System.out.println();
				System.out.println("一共有" + dirNames.size() + "个重复的文件夹");
				System.out.println("一共有" + fileNames.size() + "个重复的文件");
				System.out.println("一共处理了" + dirs.size() + "个文件夹");
				System.out.println("一共处理了" + fs.size() + "个文件");
				long timeoff = after - before;
				System.out.println(timeoff);
				System.out.println("一共耗时" + (timeoff / 60) + "分"
						+ (timeoff % 60) + "秒");
				System.err.println("完成!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				System.in.read();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void doSomething(ArrayList<String> all,
			ArrayList<String> names, int isDir) throws IOException {
		int total = all.size();
		String str;
		if (isDir == 0) {
			str = "文件夹";
		} else {
			str = "文件";
		}

		// System.out.println("================所有的"+str);
		// for(String s: all)
		// System.out.println(s);

		if (total == 0) {
			System.out.println("目录及子目录下没有" + str);
			return;
		}

		for (int i = 0; i < all.size(); i++) {
			String name1 = all.get(i);
			for (int j = i + 1; j < all.size(); j++) {
				String name2 = all.get(j);
				if (findSame(name1, name2)) {
					System.out.println("Add: " + name2);
					names.add(name2);
				}
			}

			int in = names.size();
			if (in > 0) {
				String lastName = names.get(in - 1);
				if (findSame(lastName, name1)) {
					System.out.println("Add: " + name1);
					names.add(name1);
				}

				int last = names.size() - 1;
				String n = names.get(last);
				for (int k = 0; k < all.size(); k++) {
					if (findSame(n, all.get(k))) {
						all.remove(k);
						k--; // =============================
						i = -1; // ======================
					}
				}
			}
		}

		int after = all.size();
		int ret = names.size();
		if (ret != 0) {
			System.out.println("\n================重复的" + str);
			File allFiles = new File(CUR_PATH + File.separator + "所有重复的.txt");

			if (!allFiles.exists())
				allFiles.createNewFile();
			OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(
					allFiles, true), "GBK");
			w.write("\r\n============重复的" + str + "\r\n");
			for (int b = 0; b < names.size() - 1; b++) {
				String n1 = names.get(b);
				String n2 = names.get(b + 1);

				System.out.println(n1);
				System.out.println(n2);

				w.write(n1 + "\r\n");
				if (!findSame(n1, n2)) {
					w.write("\r\n");
				}
				w.write(n2 + "\r\n");
			}
			w.flush();
			w.close();

			boolean flag = false;
			for (int m = 0; m < names.size() - 1;) {
				if (flag)
					break;

				String ele = names.get(m);
				OutputStreamWriter writer = writeFile(dealName(ele, isDir));
				writer.write(ele + "\r\n");

				for (int n = m + 1; n < names.size(); n++) {
					if (findSame(ele, names.get(n))) {
						writer.write(names.get(n) + "\r\n");
						writer.flush();

					} else {
						m = n;
						writer.flush();
						writer.close();
						break;
					}

					if (n == names.size() - 1) {
						flag = true;
						writer.close();
					}
				}
			}
		} else {
			System.out.println("\n目录及子目录下没有重复的" + str);
		}

		if (after + ret == total)
			System.err.println("");
		else {
			System.err.println("error!!!");
		}

	}

	public static String dealName(String name, int isDir) {
		int index = name.lastIndexOf("\\");
		String subname1 = name.substring(index + 1, name.length());
		if (isDir == 0) {
			subname1 = "重复文件夹 " + subname1 + ".txt";
		} else {
			subname1 = "重复文件 " + subname1 + ".txt";
		}
		return subname1;
	}

	public static OutputStreamWriter writeFile(String fileName)
			throws IOException {
		File file = new File(newDir + File.separator + fileName);
		if (file.exists())
			file.delete();
		file.createNewFile();
		OutputStreamWriter writer = getWriter(file);

		return writer;
	}

	public static OutputStreamWriter getWriter(File file)
			throws UnsupportedEncodingException, FileNotFoundException {
		return new OutputStreamWriter(new FileOutputStream(file), "GBK");
	}

	public static boolean findSame(String name1, String name2) {
		int index1 = name1.lastIndexOf("\\");
		int index2 = name2.lastIndexOf("\\");
		String subname1 = name1.substring(index1 + 1, name1.length());
		String subname2 = name2.substring(index2 + 1, name2.length());

		if (subname1.equals(subname2))
			return true;

		return false;
	}

	public static void traverseFolder2(String path) {

		File file = new File(path);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files == null || files.length == 0) {
				// System.out.println("文件夹是空的!");
				return;
			} else {
				for (File file2 : files) {
					if (file2.isDirectory()) {
						dirs.add(file2.getAbsolutePath()); // 获取所有文件夹
						traverseFolder2(file2.getAbsolutePath());
					} else {
						fs.add(file2.getAbsolutePath()); // 获取所有文件
					}
				}
			}
		} else {
			// System.out.println("文件不存在!");
		}
	}
}
