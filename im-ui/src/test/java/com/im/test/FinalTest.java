package com.im.test;

import java.util.Random;

public class FinalTest {

	static Random rand=new Random();
	private final int value1=9;
	private static final int value2=10;
	private static final Test test=new Test();
	private Test test2=new Test();
	private final int[] a={1,2,3,4,5,6};
	
	private final int i4=rand.nextInt(20);
	private static final int i5=rand.nextInt(20);
	public String toString()
	{
		return i4+""+i5+"";
	}
	
	public static void main(String[] args)
	{
		FinalTest data=new FinalTest();
		 System.out.println(data.test.getI());
		 data.test.setI(15);
		 System.out.println(data.test.getI());
	}
}
