package com.suma.mid.test3;

public class View {
	private OnClickListener mListner;
	private View view;

	
	public interface OnClickListener {
		public void onClick(View v);
	}
	
	public void setOnclickListner(OnClickListener listner){
		System.out.println("快递员告知有信件");
		mListner = listner;
		mListner.onClick(this.view);
	}
	
	public void syso(){
		System.out.println("View syso");
	}
	
	
}
