package com.mashibing.tank;

import java.awt.*;
import java.util.Random;

public class Tank {
    private int x, y;
    private Dir dir = Dir.DOWN;
    private static final int SPEED = 1;

    private boolean living = true;
    private Group group = Group.BAD;
    //记录坦克位置，用于碰撞检测
    private Rectangle rect = new Rectangle();


    public static int WIDTH = ResourceMgr.tankD.getWidth();
    public static int HEIGHT = ResourceMgr.tankD.getHeight();

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
        }
        switch (dir) {
            case LEFT:
                g.drawImage(ResourceMgr.tankL, x, y, null);
                break;
            case UP:
                g.drawImage(ResourceMgr.tankU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.tankR, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.tankD, x, y, null);
                break;
        }
        /*Color c = g.getColor();
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 50, 50);
        g.setColor(c);*/
        move();
    }

    private void move() {
        if (!moving) return;

        //坦克边界检测
        if (x < 0) {
            if(this.group == Group.GOOD && this.dir == Dir.LEFT) return;
            dir = Dir.RIGHT;
        }
        if (y < 0) {
            if(this.group == Group.GOOD && this.dir == Dir.UP) return;
            dir = Dir.DOWN;
        }
        if (x > TankFrame.GAME_WIDTH - Tank.WIDTH) {
            if(this.group == Group.GOOD && this.dir == Dir.RIGHT) return;
            dir = Dir.LEFT;
        }
        if (y > TankFrame.GAME_HEIGHT - Tank.HEIGHT) {
            if(this.group == Group.GOOD && this.dir == Dir.DOWN) return;
            dir = Dir.UP;
        }

        if (this.group == Group.BAD) {
            //TODO 优化随机概率
            //敌方坦克 随机移动+射击
            if (random.nextInt(20) > 18) {
                this.fire();
            }
            switch (random.nextInt(200)) {
                case 19:
                    dir = Dir.LEFT;
                    break;
                case 15:
                    dir = Dir.UP;
                    break;
                case 10:
                    dir = Dir.RIGHT;
                    break;
                case 5:
                    dir = Dir.DOWN;
                    break;
                default:
                    break;
            }
        }
        switch (dir) {
            case LEFT:
                x -= SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
        }
        //更新位置
        rect.x = x;
        rect.y = y;
    }


    public void fire() {
        //子弹在坦克中心位置
        int bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2;
        int bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2;
        //根据图片调整子弹位置
        switch (dir) {
            case LEFT:
                bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2 - 20;
                bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2 + 3;
                break;
            case UP:
                bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2 + 1;
                bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2 - 20;
                break;
            case RIGHT:
                bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2 + 20;
                bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2 + 4;
                break;
            case DOWN:
                bX = this.x + Tank.WIDTH / 2 - Bullet.WIDTH / 2 - 2;
                bY = this.y + Tank.HEIGHT / 2 - Bullet.HEIGHT / 2 + 20;
                break;
        }

        tf.bullets.add(new Bullet(bX, bY, this.dir, this.group, this.tf));
    }

    public void die() {
        this.living = false;

        //爆炸在坦克中心位置
        int eX = this.x + Tank.WIDTH / 2 - Explode.WIDTH / 2;
        int eY = this.y + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
        tf.explodes.add(new Explode(eX, eY, this.tf));
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
