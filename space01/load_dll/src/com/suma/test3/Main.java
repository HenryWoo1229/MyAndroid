package com.suma.test3;

import com.jacob.activeX.ActiveXComponent;

public class Main {

	public static void main(String[] args) {
	    ActiveXComponent xl = new ActiveXComponent("Excel.Application");
	    Object xlo = xl.getObject();
	}

}
