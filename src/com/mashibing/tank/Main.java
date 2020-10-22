package com.mashibing.tank;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TankFrame tf = new TankFrame();
        //初始化敌方坦克
        int initTankCount = Integer.parseInt((String)PropertyMgr.get("initTankCount"));
        for (int i = 0; i < initTankCount; i++) {
            tf.tanks.add(new Tank(50 + i * 80, 200, Dir.DOWN, Group.BAD, tf));
        }
        //背景音乐 循环播放
        //new Thread(()->new Audio("audio/war1.wav").loop()).start();

        while (true) {
            Thread.sleep(50);
            //重新调用paint()方法
            tf.repaint();
        }
    }
}
