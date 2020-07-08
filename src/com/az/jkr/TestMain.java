package com.az.jkr;

public class TestMain {

	public final int x;
	
	public TestMain(int x)
	{
		this.x = x;
	}
	public static void main(String[] args) {
		
		Object[] arr = {new Player(1,1), new Wall(2,2), new Wall(3,3)};
		GameObject[] brr = (GameObject[])arr;
		for (GameObject go : brr)
		{
			System.out.println(go.getId());
		}

	}

}
