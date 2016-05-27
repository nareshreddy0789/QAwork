package com.apple.carnival.api.request;

import org.testng.annotations.Test;

public class TestNGTest {

	@Test(threadPoolSize =2, invocationCount =4,  timeOut = 20000)
	//@Test(invocationCount =10,  timeOut = 10000)
    //This method will be run a total of 5 times using 4 threads
	//@Test
    public void TestCaseOne() throws InterruptedException
    {
                System.out.println("In Test Case One - Functional + BVT");
                Thread.sleep(2000);
                System.out.println("Slept Well ? ");
        
    }
	
	@Test
    public void TestCaseTwo() throws InterruptedException
    {
                System.out.println("In Test Case One - Functional + BVT");
                Thread.sleep(2000);
                System.out.println("Slept Well ? ");
        
    }

}
