package com.ali.explorer.aspect;

public class AspectImplementaion {

    @LogExecutionTime
    public void serve() throws  InterruptedException{
        Thread.sleep(2000);
    }

    public static void main(String[] args) throws InterruptedException {
        AspectImplementaion aspectImplementaion = new AspectImplementaion();
        aspectImplementaion.serve();
    }
}
