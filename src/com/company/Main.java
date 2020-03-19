package com.company;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        String msg = "(A+B*C)*(B+D)";
        System.out.println(getResult(msg));
    }

    private static String unknowCal(String a1, String a2, char operator) throws Exception {
        switch (operator) {
            case '+':
                return a1 + "+" + a2;
            case '*':
                return multi(a1,a2);
            default:
                break;
        }
        throw new Exception("illegal operator!");
    }

    public static String multi(String a1,String a2){
        if(a1.contains("+")){
            String left1 = a1.substring(0,a1.indexOf("+"));
            String right1 = a1.substring(a1.indexOf("+")+1);
            if(a2.contains("+")){
                String left2 = a2.substring(0,a2.indexOf("+"));
                String right2 = a2.substring(a2.indexOf("+")+1);
                System.out.println(left1+" "+right1+" "+left2+" "+right2);
                return left1+"*"+left2+"+"+left1+"*"+right2+"+"+right1+"*"+left2+"+"+right1+"*"+right2;
            }else{
                return left1+"*"+a2+"+"+right1+"*"+a2;
            }
        }else if(a2.contains("+")){
            String left2 = a2.substring(0,a2.indexOf("+"));
            String right2 = a2.substring(a2.indexOf("+")+1);
            return a1+"*"+left2+"+"+a1+"*"+right2;
        }
        else{
            return a1 + "*" + a2;
        }
    }

    private static int getPriority(String s) throws Exception {
        if(s==null) return 0;
        switch(s) {
            case "(":return 1;
            case "+":return 2;
            case "*":return 3;
            default:break;
        }
        throw new Exception("illegal operator!");
    }
    public static String getResult(String expr) throws Exception {
        System.out.println("计算"+expr);
        /*未知数栈*/
        Stack<String> unknown = new Stack<>();
        /*符号栈*/
        Stack<String> operator = new Stack<String>();
        operator.push(null);// 在栈顶压人一个null，配合它的优先级，目的是减少下面程序的判断

        /* 将expr打散为运算数和运算符 */
        Pattern p1 = Pattern.compile("(?<![a-zA-Z])-?[a-zA-Z]+(\\.[a-zA-Z]+)?|[+\\-*/()]");
        Matcher m1 = p1.matcher(expr);
        while(m1.find()){
            String temp = m1.group();
            if(temp.matches("[+\\-*/()]")) {//遇到符号
                if(temp.equals("(")) {//遇到左括号，直接入符号栈
                    operator.push(temp);
                    System.out.println("符号栈更新："+operator);
                }else if(temp.equals(")")){//遇到右括号，"符号栈弹栈取栈顶符号b，数字栈弹栈取栈顶数字a1，数字栈弹栈取栈顶数字a2，计算a2 b a1 ,将结果压入数字栈"，重复引号步骤至取栈顶为左括号，将左括号弹出
                    String b = null;
                    while(!(b = operator.pop()).equals("(")) {
                        System.out.println("符号栈更新："+operator);
                        String a1 = unknown.pop();
                        String a2 = unknown.pop();
                        System.out.println("未知栈更新："+unknown);
                        unknown.push(unknowCal(a2, a1, b.charAt(0)));
                        System.out.println("未知栈更新："+unknown);
                    }
                    System.out.println("符号栈更新："+operator);
                }else {//遇到运算符，满足该运算符的优先级大于栈顶元素的优先级压栈；否则计算后压栈
                    while(getPriority(temp) <= getPriority(operator.peek())) {
                        String a1 = unknown.pop();
                        String a2 = unknown.pop();
                        String b = operator.pop();
                        System.out.println("符号栈更新："+operator);
                        System.out.println("未知栈更新："+unknown);
                        unknown.push(unknowCal(a2, a1, b.charAt(0)));
                        System.out.println("未知栈更新："+unknown);
                    }
                    operator.push(temp);
                    System.out.println("符号栈更新："+operator);
                }
            }else{
                unknown.push(temp);
                System.out.println("未知栈更新："+unknown);
            }

        }

        while(operator.peek()!=null) {//遍历结束后，符号栈数字栈依次弹栈计算，并将结果压入数字栈
            String a1 = unknown.pop();
            String a2 = unknown.pop();
            String b = operator.pop();
            System.out.println("符号栈更新："+operator);
            System.out.println("未知栈更新："+unknown);
            unknown.push(unknowCal(a2, a1, b.charAt(0)));
            System.out.println("未知栈更新："+unknown);
        }
        return unknown.pop()+"";
    }
}