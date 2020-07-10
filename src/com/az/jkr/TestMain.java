package com.az.jkr;

public class TestMain {

	public final int x;
	
	public TestMain(int x)
	{
		this.x = x;
	}
	public static void main(String[] args) {
		
		System.out.println(GameObject.lerp(99.5f, 0, .5f,false));

	}

}
