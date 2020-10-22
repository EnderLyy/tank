package com.mashibing.tank;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankFrame extends Frame {
    Tank myTank = new Tank(15, 400, Dir.DOWN, Group.GOOD,this);
    List<Bullet> bullets = new ArrayList<>();
    List<Tank> tanks = new ArrayList<>();
    List<Explode> explodes = new ArrayList<>();

    static  final int GAME_WIDTH = Integer.parseInt((String)PropertyMgr.get("gameWidth"));
    static  final int GAME_HEIGHT = Integer.parseInt((String)PropertyMgr.get("gameHeight"));
    public TankFrame() {
        //单位是像素
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setResizable(false);
        setTitle("tank war");
        setVisible(true);
        //添加按键监听事件
        addKeyListener(new KeyAdapter() {
            boolean bL = false;
            boolean bU = false;
            boolean bR = false;
            boolean bD = false;

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        bL = true;
                        break;
                    case KeyEvent.VK_UP:
                        bU = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        bR = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        bD = true;
                        break;
                    default:
                        break;
                }
                setMainTankDir();
//                new Thread(()->new Audio("audio/tank_move.wav").play()).start();

            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        bL = false;
                        break;
                    case KeyEvent.VK_UP:
                        bU = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        bR = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        bD = false;
                        break;
                    case KeyEvent.VK_CONTROL:
                        myTank.fire();
                        break;
                    default:
                        break;
                }
                setMainTankDir();
            }

            private void setMainTankDir() {
                if(!bL && !bU && !bR && !bD){
                    myTank.setMoving(false);
                }else {
                    myTank.setMoving(true);
                    if (bL) myTank.setDir(Dir.LEFT);
                    if (bU) myTank.setDir(Dir.UP);
                    if (bR) myTank.setDir(Dir.RIGHT);
                    if (bD) myTank.setDir(Dir.DOWN);
                }
            }
        });

        //添加窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * 用双缓冲消除画面闪烁的问题
     *
     * update()方法会在paint()方法之前调用
     */
    Image offScreenImge = null;
    @Override
    public void update(Graphics g) {
        if(offScreenImge == null){
            offScreenImge = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }
        Graphics gOffScreen = offScreenImge.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImge, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.WHITE);
        g.drawString("子弹的数量：" + bullets.size(), 10,50);
        g.drawString("敌人的数量：" + tanks.size(), 10,70);
        g.drawString("爆炸的数量：" + explodes.size(), 10,90);
        g.setColor(c);
        myTank.paint(g);
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).paint(g);
        }
        for (int i = 0; i < tanks.size(); i++) {
            tanks.get(i).paint(g);
        }
        for (int i = 0; i < explodes.size(); i++) {
            explodes.get(i).paint(g);
        }

        //子弹和坦克 碰撞检测
        for (int i = 0; i < bullets.size(); i++) {
            for (int j = 0; j < tanks.size(); j++) {
                bullets.get(i).collideWith(tanks.get(j));
            }
        }
    }
}
