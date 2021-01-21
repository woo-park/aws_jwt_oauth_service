package com.awsjwtservice.practice;

//public class ConstantDemo {
//    public static void main(String[] args) {
//
//
//        int type = 1;
//
//        switch (type) {
//            case 1:
//                System.out.println(57);
//                break;
//            case 2:
//                System.out.println(34);
//                break;
//            case 3:
//                System.out.println(93);
//                break;
//        }
//    }
//}



enum Fruit{
    APPLE("red"), PEACH("pink"), BANANA("yellow");
    private String color;
    Fruit(String color){
        System.out.println("Call Constructor "+this);
        this.color = color;
    }
    String getColor(){
        return this.color;
    }
}

enum Company{
    GOOGLE, APPLE, ORACLE;
}

public class ConstantDemo {

    public static void main(String[] args) {
        for(Fruit f : Fruit.values()){
            System.out.println(f+", "+f.getColor());
        }
    }
}

