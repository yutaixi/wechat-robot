package com.im.test;


public class  Test {
	
	private int i=10;
	private int j=11;
	
	
	
	public int getI() {
		return i;
	}



	public void setI(int i) {
		this.i = i;
	}



	public int getJ() {
		return j;
	}



	public void setJ(int j) {
		this.j = j;
	}



	public static void main(String[] args)
	{
//		boolean a=true;
//		boolean b=false;
//		
//		System.out.println(a&a);
//		System.out.println(a&b);
		
		Integer i=new Integer(10);
		Integer j=10;
		System.out.println(Integer.toBinaryString(i)); 
		 
		
	}
}