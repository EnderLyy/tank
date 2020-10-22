package com.mashibing.tank;

import java.awt.*;

public class Bullet {
    private static final int SPEED = Integer.parseInt((String)PropertyMgr.get("bulletSpeed"));
    public static int WIDTH = ResourceMgr.bulletD.getWidth();
    public static int HEIGHT = ResourceMgr.bulletD.getHeight();

    private int x, y;
    private Dir dir;
    //记录子弹位置，用于碰撞检测
    private Rectangle rect = new Rectangle();

    private boolean living = true;
    private Group group = Group.BAD;

    private TankFrame tf = null;

    public Bullet(int x, int y, Dir dir, Group group, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tf = tf;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
    }

    public void paint(Graphics g) {
        if (!living) {
            this.tf.bullets.remove(this);
            return;
        }
        switch (dir) {
            case LEFT:
                g.drawImage(ResourceMgr.bulletL, x, y, null);
                break;
            case UP:
                g.drawImage(ResourceMgr.bulletU, x, y, null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.bulletR, x, y, null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.bulletD, x, y, null);
                break;
        }
        /*Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);*/
        move();
    }

    private void move() {
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

        rect.x = x;
        rect.y = y;

        //子弹飞出边界
        if (x < 0 || y < 0 || x > TankFrame.GAME_WIDTH || y > TankFrame.GAME_HEIGHT) {
            living = false;
        }
    }

    //判断子弹是否和坦克相交
    public void collideWith(Tank tank) {
        if (this.group == tank.getGroup()) return;
        //TODO:用一个Rectangle来记录子弹位置
        //Rectangle bR = new Rectangle(x, y, WIDTH, HEIGHT);
        //Rectangle tR = new Rectangle(tank.getX(), tank.getY(), Tank.WIDTH, Tank.HEIGHT);
        if (rect.intersects(tank.getRect())) {
            this.die();
            tank.die();
            //爆炸在坦克中心位置
            int eX = tank.getX() + Tank.WIDTH / 2 - Explode.WIDTH / 2;
            int eY = tank.getY() + Tank.HEIGHT / 2 - Explode.HEIGHT / 2;
            tf.explodes.add(new Explode(eX, eY, this.tf));
        }
    }

    private void die() {
        this.living = false;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
