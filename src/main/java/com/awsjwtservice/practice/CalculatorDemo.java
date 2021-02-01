package com.awsjwtservice.practice;

abstract class Calculator{
    int left, right;
    public void setOprands(int left, int right){
        this.left = left;
        this.right = right;
    }
    int _sum() {
        return this.left + this.right;
    }

    int _difference() {
        return this.left - this.right;
    }

    public abstract void difference();
    public abstract void sum();
    public abstract void avg();
    public void run(){

        sum();
        avg();
        difference();
    }
}
class CalculatorDecoPlus extends Calculator {
    public void difference(){
        System.out.println("- difference:"+_difference());
    }
    public void sum(){
        System.out.println("+ sum :"+_sum());
    }
    public void avg(){
        System.out.println("+ avg :"+(this.left+this.right)/2);
    }
}
class CalculatorDecoMinus extends Calculator {
    public void difference() {
        System.out.println("- difference :"+_difference());
    }
    public void sum(){
        System.out.println("- sum :"+_sum());
    }
    public void avg(){
        System.out.println("- avg :"+(this.left+this.right)/2);
    }
}
public class CalculatorDemo {

    public static void execute(Calculator cal){
        // polymorphism, 다양성을 사용해서 CalculatorDecoPlus 의
        // data type 을 Calculator 타입으로 정의함 으로써,
        // execute method 가 Calculator type 으로 받은후,


        /*
        * 메소드 execute 입장에서는 매개변수로 전달된 값이 Calculator이거나 그 자식이라면 메소드 run을 가지고 있다는 것을 보장 받을 수 있게 되는 것이다.
        * 이 맥락에서의 다형성이란 하나의 클래스(Calculator)가 다양한 동작 방법(ClaculatorDecoPlus, ClaculatorDecoMinus)을 가지고 있는데 이것을 다형성이라고 할 수 있겠다.
        * */
        System.out.println("실행결과");
        cal.run();
    }


    public static void main(String[] args) {
        Calculator c1 = new CalculatorDecoPlus();
        c1.setOprands(10, 20);
//        c1.run();

        Calculator c2 = new CalculatorDecoMinus();
        c2.setOprands(10, 30);
//        c2.run();

        execute(c1);
        execute(c2);
    }

}