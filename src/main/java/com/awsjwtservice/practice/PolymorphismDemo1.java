package com.awsjwtservice.practice;


class A{
    public String x(){return "A.x()";}
}
class B extends A{
    public String x(){return "B.x()";}
    public String y(){return "B.y()";}
}
class B2 extends A{
    public String x(){return "B2.x()";}
}

class B3 extends A{
    public String x(){return "B3.x()";}
    public String z(){return "B3.z()";}
}

public class PolymorphismDemo1 {
    public static void main(String[] args) {
        A objA = new B();
        A objA2 = new B2();
        B objB = new B();
        System.out.println(objA.x());
        System.out.println(((B) objA).y());  // 캐스팅 방식으로 type A에서 B로 바꾼후 y() 멤버를 접근할수있다
        System.out.println(objB.y());
        System.out.println(objA2.x());
    }
}

