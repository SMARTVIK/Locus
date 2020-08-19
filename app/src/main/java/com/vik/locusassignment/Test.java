package com.vik.locusassignment;

public class Test {
    //constructor
    //primary constructor -> no argument
    public Test() {

        System.out.println("I am inside constructor");

    }

    public Test(String name) {

        System.out.println("I am inside constructor " + name); //String concatenation

    }

    private void playMusic() {

        Test test = new Test("");

        return;
    }

}
