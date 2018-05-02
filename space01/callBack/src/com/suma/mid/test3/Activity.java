package com.suma.mid.test3;

import com.suma.mid.test3.View.OnClickListener;

public class Activity {
	
	public static void main(String[] args) {
		
		System.out.println("通知有信件寄出");
		new Button().setOnclickListner(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("读取信的内容");
				v = new View();
				v.syso();
			}
		});
		System.out.println("通知完毕");
	}
}
