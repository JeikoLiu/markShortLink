package com.jeiko.shortlink_demo.admin;

import java.util.Scanner;

public class Main {
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        // int group = Integer.parseInt(scanner.nextLine());
        int group = scanner.nextInt();
        // 多组测试用例
        for (int i = 0; i < group; i++) {
//            String input1[] = (scanner.nextLine()).split(" ");
//            int param[] = new int[3];
//            for (int j = 0; j < 3; j++) {
//                param[j] = Integer.parseInt(input1[j]);
//            }
//            int n = param[0];
//            long k = param[1];
//            long x = param[2];
            int n = scanner.nextInt();
            long k = scanner.nextInt();
            long x = scanner.nextInt();
//            String input2[] = (scanner.nextLine()).split(" ");
            int []a = new int[n];
            for (int j = 0; j < n; j++) {
//                a[j] = Integer.parseInt(input2[j]);
                a[j] = scanner.nextInt();
            }

            int mex = 0;
            int []mexList = new int[n+1];
            // 判断数字是否存在当前的子数组中
            boolean []present = new boolean[n+2];
            // 从后往前遍历寻找mex
            for (int j = n-1; j >=0; j--) {
                present[a[j]] = true;
                while(present[mex]) {
                    mex++;
                }
                mexList[j] = mex;
            }

            // 最坏情况下执行n次删除操作
            long minConst = x * n;
            long currentConst = 0;

            for (int j = 0; j < n+1; j++) {
                minConst = Math.min(minConst, k * mexList[j] + currentConst);
                if (j < n) {
                    currentConst += x;
                }
                //currentConst += x;
            }
            System.out.println(minConst);
        }
    }
}
