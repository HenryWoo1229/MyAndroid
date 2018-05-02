package com.suma.mid.test3;

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.RowLayout;

import java.awt.GridLayout;
import java.io.*;

public class MainClass {
	private static String fn = "";
	private static Display display;
	private static Shell shell;
	private static Text text;
	private Button newButton;
	
	private Button openButton;
	private Button saveButton;
	private Button delButton;
	private Button quitButton;

	
	
	public MainClass() {
		display = new Display();
		// 基本对话框
		shell = new Shell(display, SWT.DIALOG_TRIM);
		shell.setText("Note pad");  //窗口标题
		shell.setSize(600, 400);    //窗口初始尺寸
		
//		newButton = new Button(shell, SWT.PUSH);
//		newButton.setLocation(2, 5);
//		newButton.setSize(50, 20);
//		newButton.setText("new");

		Group group = new Group(shell, SWT.NONE);
		group.setText("Test");
		group.setBounds(10, 10 + 50, 300, 40); 
		
		text = new Text(shell, SWT.NONE);
		text.setEditable(false);
		text.setLocation(10, 30);
		text.setText("指示码:");
		text.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		text.setFont(new Font(Display.getCurrent(),"宋体",9,0));
		text.setSize(50, 20);
		
		Text editText = new Text(shell, SWT.BORDER);
		editText.setLocation(100, 27);
		editText.setSize(80, 20);
		editText.setFont(new Font(Display.getCurrent(),"宋体",9,0));
		
//		openButton = new Button(shell, SWT.PUSH);
//		openButton.setLocation(60, 5);
//		openButton.setSize(50, 20);
//		openButton.setText("open");
		
//		text = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
//		text.setEnabled(false);
//		text.setSize(shell.getClientArea().width - 4,
//				shell.getClientArea().height - 35);
//		text = new Text(shell, SWT.NONE);
//		text.setEditable(false);
//		text.setLocation(2, 30);
//
//		text.setText("文本");
//		text.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
//		text.setFont(new Font(Display.getCurrent(),"宋体",11,0));
		
//		newButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent event) {
//				fn = "";
//				shell.setText("第一个SWT程序");
//				text.setText("");
//			}
//		});
//		openButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent event) {
//				FileDialog dlg = new FileDialog(shell, SWT.OPEN);
//				String fileName = dlg.open();
//				try {
//					if (fileName != null) {
//						// 打开指定文件
//						FileInputStream fis = new FileInputStream(fileName);
//						text.setText("");
//						BufferedReader in = new BufferedReader(
//								new InputStreamReader(fis));
//						String s = null;
//						// 将指定文件一行一行的加入记事本
//						while ((s = in.readLine()) != null)
//							text.append(s + "/r/n");
//						fn = fileName;
//						shell.setText(fn);
//						MessageBox successBox = new MessageBox(shell);
//						successBox.setMessage("打开文件成功");
//						successBox.setText("信息");
//						successBox.open();
//						in.close();
//					}
//				} catch (NullPointerException en) {
//					System.out.println("warning No file selected!");
//				} catch (Exception e) {
//					MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR);
//					errorBox.setText("错误");
//					errorBox.setMessage("打开文件失败!");
//					errorBox.open();
//				}
//			}
//		});
//		saveButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent event) {
//				try {
//					String fileName = null;
//					if (fn.equals("")) {
//						FileDialog dlg = new FileDialog(shell, SWT.SAVE);
//						fileName = dlg.open();
//						FileOutputStream fos = new FileOutputStream(fileName);
//						OutputStreamWriter out = new OutputStreamWriter(fos);
//						out.write(text.getText());
//						out.close();
//						alertMsg(shell, "创建文件成功");
//						if (fileName != null) {
//							fn = fileName;
//							shell.setText(fn);
//						}
//					} else {
//						FileOutputStream fos = new FileOutputStream(fn);
//						OutputStreamWriter out = new OutputStreamWriter(fos);
//						out.write(text.getText());
//						out.close();
//					}
//				} catch (NullPointerException en) {
//					System.out.println("warning No file selected!");
//				} catch (Exception e) {
//					e.printStackTrace();
//					MessageBox errorBox = new MessageBox(shell, SWT.ICON_ERROR);
//					errorBox.setText("错误");
//					errorBox.setMessage("保存文件失败!");
//					errorBox.open();
//				}
//			}
//		});
//		quitButton.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				System.out.println(e.toString());
//				display.dispose();
//			}
//		});
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				// System.out.println(display.msg.toString());
				display.sleep();
			}
		}
//		System.out.println("closed");
		display.dispose();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainClass();
	}

	public static void alertMsg(Shell shell, String msg) {
		MessageBox alertBox = new MessageBox(shell);
		alertBox.setMessage(msg);
		alertBox.setText("信息");
		alertBox.open();
	}
}
