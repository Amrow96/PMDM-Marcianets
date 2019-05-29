package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

public class NauEnemiga extends Sprite implements Runnable {
    private float posX;
    private float posY;
    private int dirX;
    private int dirY;
    private Texture vivaTexture;
    private Texture eliminadaTexture = new Texture("nau2e.png");
    private boolean hasDir = false;
    public boolean viva = true;
    public static boolean mostrar = true;
    public int vida = 10;
    private int disparo = 0;
    private int limite = 0;

    public NauEnemiga(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight, int posX) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        this.vivaTexture = texture;
        this.posX = posX;
        this.posY = MyGdxGame.screenHeight/2;
    }

    public void run() {
        this.setX(posX);
        this.setY(posY);
        try {
            while (true) {
                if (hasDir != true) {
                    generarDireccions();
                    hasDir = true;
                } else {
                    if (vida > 0) {
                        moverse();
                        comprobarLimits();
                        comprobarColision();
                        Thread.sleep(10);
                        disparar();
                    } else {
                        viva = false;
                        this.setTexture(eliminadaTexture);
                        this.setRegionWidth(eliminadaTexture.getWidth());
                        this.setRegionHeight(eliminadaTexture.getHeight());
                        Thread.sleep(2000);
                        this.setX(999999);
                        Thread.sleep(7000);
                        regenerarNau();
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Error.");
        }
    }

    public void generarDireccions() {
        Random r = new Random();
        int rand = r.nextInt(4);

        switch (rand) {
            case 1:
                dirX = 1;
                dirY = 1;
                break;
            case 2:
                dirX = -1;
                dirY = -1;
                break;
            case 3:
                dirX = -1;
                dirY = 1;
                break;
            case 0:
                dirX = 1;
                dirY = -1;
                break;
        }
    }

    public void moverse() {
        posX += dirX;
        this.setX(posX);
        posY += dirY;
        this.setY(posY);

    }

    public void comprobarLimits() {
        boolean encontrado = false;

        if (posX <= super.getWidth() || posX >= (MyGdxGame.screenWidth - super.getWidth())) {
            dirX *= -1;
            encontrado = true;
        }
        if (posY <= MyGdxGame.nauAliada.getHeight() + 10 || posY >= (MyGdxGame.screenHeight - super.getHeight())) {
            dirY *= -1;
            encontrado = true;
        }
        if (encontrado) {
            moverse();
        }
    }


    public void comprobarColision() {
        float nauX = MyGdxGame.nau.getX();
        float nauY = MyGdxGame.nau.getY();
        float nauWidth = MyGdxGame.nau.getWidth();
        float nauHeight = MyGdxGame.nau.getHeight();

        if ((posX >= nauX && posX <= (nauX + nauWidth)) && (posY >= nauY && posY <= (nauY + nauHeight))) {
            synchronized (MyGdxGame.vides) {
                MyGdxGame.vides.remove(0);
            }
            vida = 0;
            MyGdxGame.vida = 100;
        }
    }

    public void regenerarNau() {
        int tempX;

        do {
            Random random = new Random();
            tempX = random.nextInt((int)(MyGdxGame.screenWidth - this.getWidth()));
        } while ((tempX < this.getWidth()) || (tempX > (MyGdxGame.screenWidth - this.getWidth())));

        posX = tempX;
        this.setTexture(vivaTexture);
        this.setRegionWidth(vivaTexture.getWidth());
        this.setRegionHeight(vivaTexture.getHeight());
        vida = 10;
        viva = true;
        mostrar = true;
    }

    public void disparar() {
        if (limite == 0) {
            Random r = new Random();
            limite = r.nextInt(500 - 250) + 250;
            System.out.println(limite);
        }

        if (disparo == limite && posY > (MyGdxGame.screenHeight / 2)) {
            float destinoX = MyGdxGame.nau.getX() + (MyGdxGame.nau.getWidth()/2);
            float destinoY = MyGdxGame.nau.getY() + (MyGdxGame.nau.getHeight()/2);

            TretNauEnemiga tretNauEnemiga = new TretNauEnemiga(MyGdxGame.tret, 0, 0, MyGdxGame.tret.getWidth(), MyGdxGame.tret.getHeight(), destinoX, destinoY, posX, posY);
            synchronized (MyGdxGame.tretsEnemics) {
                MyGdxGame.tretsEnemics.add(tretNauEnemiga);
            }
            Thread tret = new Thread(tretNauEnemiga);
            tret.start();
            disparo = 0;
        } else {
            if (disparo >= limite) {
                disparo = 0;
            }
            disparo++;
        }
    }
}