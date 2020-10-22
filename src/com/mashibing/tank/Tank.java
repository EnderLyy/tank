package com.mashibing.tank;

import java.awt.*;
import java.util.Random;

public class Tank {
    private int x, y;
    private Dir dir = Dir.DOWN;
    private static final int SPEED = 2;

    private boolean living = true;
    private Group group = Group.BAD;
    //记录坦克位置，用于碰撞检测
    private Rectangle rect = new Rectangle();


    public static int WIDTH = ResourceMgr.goodTankU.getWidth();
    public static int HEIGHT = ResourceMgr.goodTankU.getHeight();

    //random.nextInt(n) ==> 该值介于[0,n)的区间
    private Random random = new Random();

    private boolean moving = false;
    private TankFrame tf = null;

    public Tank(int x, int y, Dir dir, Group group, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tf = tf;
        this.moving = group == Group.GOOD ? false : true;

        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public void paint(Graphics g) {
        if (!living) {
            this.tf.tanks.remove(this);
            return;
        }
        switch (dir) {
            case LEFT:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankL : ResourceMgr.badTankL, x, y, null);
                break;
            case UP:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankU : ResourceMgr.badTankU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankR : ResourceMgr.badTankR, x, y, null);
                break;
            case DOWN:
                g.drawImage(this.group == Group.GOOD ? ResourceMgr.goodTankD : ResourceMgr.badTankD, x, y, null);
                break;
        }
        move();
    }

    private void move() {
        if (!moving) return;
        //敌方坦克 随机移动+射击
        if (this.group == Group.BAD) {
            //TODO 优化随机概率
            if (random.nextInt(100) > 95) {
                this.fire();
            }
            if (random.nextInt(100) > 95) {
                this.dir = Dir.values()[random.nextInt(4)];
            }

            boundsCheck();
        }

        switch (dir) {
            case LEFT:
                x -= SPEED;
                x = Math.max(2, x);
                break;
            case UP:
                y -= SPEED;
                y = Math.max(28, y);
                break;
            case RIGHT:
                x += SPEED;
                x = Math.min(TankFrame.GAME_WIDTH - Tank.WIDTH - 2, x);
                break;
            case DOWN:
                y += SPEED;
                y = Math.min(TankFrame.GAME_HEIGHT - Tank.HEIGHT - 2, y);
                break;
        }
        //更新位置
        rect.x = x;
        rect.y = y;
    }

    private void boundsCheck() {
        /**
         * 坦克边界检测 敌方坦克反方向移动
         */
        if (x < 0) {
            dir = Dir.RIGHT;
        }
        if (y < 0) {
            dir = Dir.DOWN;
        }
        if (x > TankFrame.GAME_WIDTH - Tank.WIDTH) {
            dir = Dir.LEFT;
        }
        if (y > TankFrame.GAME_HEIGHT - Tank.HEIGHT) {
            dir = Dir.UP;
        }
    }


    public void fire() {
        //子弹在坦克中心位置
        int bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
        int bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
        //根据图片调整子弹位置
        switch (dir) {
            case LEFT:
                bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2 - 40;
                bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
                break;
            case UP:
                bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
                bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2 - 40;
                break;
            case RIGHT:
                bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2 + 40;
                bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
                break;
            case DOWN:
                bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
                bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2 + 40;
                break;
        }

        tf.bullets.add(new Bullet(bX, bY, this.dir, this.group, this.tf));
        if (this.group == Group.GOOD) new Thread(() -> new Audio("audio/tank_fire.wav").play()).start();
    }

    public void die() {
        this.living = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public static int getSPEED() {
        return SPEED;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }
}
