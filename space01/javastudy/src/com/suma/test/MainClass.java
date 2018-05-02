package com.suma.test;

import java.net.URL;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		A a = new A();
		try {
			A aa = (A)Class.forName(A.class.getName()).newInstance();    //反射方式实例化类
			A aaa = (A)Class.forName("com.suma.test.A").newInstance();
			a.printSth();
			aa.printSth();
			aaa.printSth();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

}

class A{
	void printSth(){
		System.out.println("im A");
	}
}
class B{
	
}
class C{
	
}
