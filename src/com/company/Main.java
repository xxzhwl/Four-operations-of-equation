package com.company;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @PackageName:com.company
 * @ClassName:Mains
 * @Description:
 * @Author:ZWL
 * @Date:2020/3/19 16：56
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String msg = "(a+b*c)*(b+d*e)";
        String result = getResult(msg);
        System.out.println("去括号结果：result="+result);
        ArrayList<String> slist = new ArrayList<>();
        while(result.contains("+")){
            int x = result.indexOf("+");
            String temp = result.substring(0,x);
            String temp1 = result.substring(0,x+1);
            result = result.replace(temp1,"");
            slist.add(temp);
        }
        slist.add(result);
        ArrayList<String> noRepeat = removeRepeat(slist);
        System.out.println("最终化简结果：result="+getResult(noRepeat));
    }

    private static String getResult(ArrayList<String> noRepeat){
        ArrayList<ArrayList> re = new ArrayList<>();
        for(String s : noRepeat){
            ArrayList<String> bs = new ArrayList<>();
            while(s.contains("*")){
                int x = s.indexOf("*");
                String temp = s.substring(0,x);
                String temp1 = s.substring(0,x+1);
                s = s.replace(temp1,"");
                bs.add(temp);
            }
            bs.add(s);
            re.add(bs);
        }
        for(int i = 0 ;i<re.size();i++){
            ArrayList<String> arrayList = re.get(i);
            for(int j = i+1; j<re.size();j++){
                ArrayList<String> arrayListTemp = re.get(j);
                if(ifContain(arrayList,arrayListTemp)){
                    re.remove(arrayListTemp);
                }else if(ifContain(arrayListTemp,arrayList)){
                    re.remove(arrayList);
                }
            }
        }
        String result = "";
        int i = 0;
        for(ArrayList<String>  strings : re){
            String one = "";
            int j = 0;
            for(String s : strings){
                if(j == 0){
                    one = s;
                }else{
                    one = one + "*" + s;
                }
                j++;
            }
            if(i == 0){
                result = one;
            }else {
                result = result+ "+" + one;
            }
            i++;
        }
        return result;
    }//去除同操作的中间事件

    private static boolean ifContain(ArrayList<String> s1,ArrayList<String> s2){
        boolean flag = true;
        for(String s1temp : s1){
            if(!s2.contains(s1temp)){
                flag = false;
            }
        }
        return flag;
    }//检查两个中间事件是否有重复

    private static ArrayList<String> removeRepeat(ArrayList<String> slist){
        ArrayList<String> result = new ArrayList<>();
        for(String stemp : slist){
            ArrayList<String> slistTemp = new ArrayList<>();
            while(stemp.contains("*")){
                int x = stemp.indexOf("*");
                String temp = stemp.substring(0,x);
                String temp1 = stemp.substring(0,x+1);
                stemp = stemp.replace(temp1,"");
                slistTemp.add(temp);
            }
            slistTemp.add(stemp);
            String[] sarray = new String[slistTemp.size()];
            for(int i = 0 ; i<sarray.length; i++){
                sarray[i] = slistTemp.get(i);
            }

            List<String> list = new ArrayList<>();
            for (String i : sarray) {
                if (!list.contains(i)) {//boolean contains(Object o):如果列表包含指定的元素，则返回 true
                    list.add(i);
                }
            }
            String sresult = "";
            int i = 0;
            for(String s : list){
                if(i==0){
                    sresult = s;
                }else {
                    sresult =  sresult+"*"+s;
                }
                i++;
            }
            result.add(sresult);
        }
        return result;
    }//删除单个中间事件中可能重复的基本事件


    private static String unknowCal(String a1, String a2, char operator) throws Exception {
        switch (operator) {
            case '+':
                return a1 + "+" + a2;
            case '*':
                return multiWon(a1,a2);
            default:
                break;
        }
        throw new Exception("illegal operator!");
    }

    private static String multiWon(String a1,String a2){
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> listSub1 = new ArrayList<String>();
        List<String> listSub2 = new ArrayList<String>();
        while(a1.contains("+")){
            int x = a1.indexOf("+");
            String temp = a1.substring(0,x);
            String temp1 = a1.substring(0,x+1);
            a1 = a1.replace(temp1,"");
            listSub1.add(temp);
        }
        listSub1.add(a1);
        while(a2.contains("+")){
            int x = a2.indexOf("+");
            String temp = a2.substring(0,x);
            String temp1 = a2.substring(0,x+1);
            a2 = a2.replace(temp1,"");
            listSub2.add(temp);
        }
        listSub2.add(a2);
        list.add(listSub1);
        list.add(listSub2);

        List<List<String>> resultD = new ArrayList<List<String>>();
        descartes(list, resultD, 0, new ArrayList<String>());
        String result = "";
        int i = 0;
        for(List<String> arrayLists : resultD){
            String stemp = "";
            int j = 0;
            for(String s : arrayLists){
                if(j==0){
                    stemp = s;
                }else{
                    stemp = stemp + "*" + s;
                }
                j++;
            }
            if(i==0){
                result = stemp;
            }else {
                result = result + "+" + stemp;
            }
            i++;
        }
        return result;
    }

    private static void descartes(List<List<String>> dimvalue, List<List<String>> result, int layer, List<String> curList) {
        if (layer < dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                descartes(dimvalue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    descartes(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    result.add(list);
                }
            }
        }
    }

    private static int getPriority(String s) throws Exception {
        if(s==null) {
            return 0;
        }
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
//                    System.out.println("符号栈更新："+operator);
                }else if(temp.equals(")")){//遇到右括号，"符号栈弹栈取栈顶符号b，未知栈弹栈取栈顶数字a1，未知栈弹栈取栈顶未知字符串a2，计算a2 b a1 ,将结果压入未知栈"，重复引号步骤至取栈顶为左括号，将左括号弹出
                    String b = null;
                    while(!(b = operator.pop()).equals("(")) {
//                        System.out.println("符号栈更新："+operator);
                        String a1 = unknown.pop();
                        String a2 = unknown.pop();
//                        System.out.println("未知栈更新："+unknown);
                        unknown.push(unknowCal(a2, a1, b.charAt(0)));
//                        System.out.println("未知栈更新："+unknown);
                    }
//                    System.out.println("符号栈更新："+operator);
                }else {//遇到运算符，满足该运算符的优先级大于栈顶元素的优先级压栈；否则计算后压栈
                    while(getPriority(temp) <= getPriority(operator.peek())) {
                        String a1 = unknown.pop();
                        String a2 = unknown.pop();
                        String b = operator.pop();
//                        System.out.println("符号栈更新："+operator);
//                        System.out.println("未知栈更新："+unknown);
                        unknown.push(unknowCal(a2, a1, b.charAt(0)));
//                        System.out.println("未知栈更新："+unknown);
                    }
                    operator.push(temp);
//                    System.out.println("符号栈更新："+operator);
                }
            }else{
                unknown.push(temp);
//                System.out.println("未知栈更新："+unknown);
            }

        }

        while(operator.peek()!=null) {//遍历结束后，符号栈未知栈依次弹栈计算，并将结果压入未知栈
            String a1 = unknown.pop();
            String a2 = unknown.pop();
            String b = operator.pop();
//            System.out.println("符号栈更新："+operator);
//            System.out.println("未知栈更新："+unknown);
            unknown.push(unknowCal(a2, a1, b.charAt(0)));
//            System.out.println("未知栈更新："+unknown);
        }
        return unknown.pop()+"";
    }
}