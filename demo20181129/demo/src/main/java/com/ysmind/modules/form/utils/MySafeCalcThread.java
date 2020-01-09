package com.ysmind.modules.form.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class MySafeCalcThread {

	private static AtomicInteger count = new AtomicInteger(0);  
    
    public synchronized static int calc() {  
        //if ((count.get()) < 1000) {  
            int c = count.incrementAndGet();// 自增1,返回更新值  
            return c;
        //}  
              
    }  
}
