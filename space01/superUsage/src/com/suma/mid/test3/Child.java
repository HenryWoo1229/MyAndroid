package com.suma.mid.test3;

public class Child extends Father {

	public static void main(String[] args) {
		Father c = new Child();
		Child d = new Child();
		Father a = new Father();
		
	}
	
	@Override
	public void print() {
		super.print();
		System.out.println("Child Print");
	}
	
	public void print2(){
		System.out.println("Child print 2");
	}
}
