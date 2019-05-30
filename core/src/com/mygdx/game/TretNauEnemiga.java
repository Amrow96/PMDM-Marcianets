package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TretNauEnemiga extends Sprite implements Runnable {
    private float posX;
    private float posY;
    private float destinoX;
    private float destinoY;
    private double destX;
    private double destY;
    private double dist;
    private boolean impacte = false;
    private int i = 0;

    public TretNauEnemiga(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight, float destinoX, float destinoY, float origenX, float origenY) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        this.destinoX = destinoX;
        this.destinoY = destinoY;
        this.posX = origenX;
        this.posY = origenY;
        this.setX(posX);
        this.setY(posY);
    }

    public void run() {
        try {
            while (posY > (MyGdxGame.nau.getHeight() + 15) && impacte == false) {
                destX = destinoX - posX;
                destY = destinoY - posY;

                dist = Math.sqrt(destX * destX + destY * destY);
                destX = destX / dist;
                destY = destY / dist;
              //  posX += destX ;
                posY += destY ;


                Thread.sleep(3);
                this.setY(posY);
                this.setX(posX);
                impacte = calcularColisions();

                //Elimina el tret si col·lisiona amb una nau enemiga.
                if ((impacte) || (posY <= ((MyGdxGame.nau.getHeight() * 1.5) + 15))) {
                    synchronized (MyGdxGame.tretsEnemics) {
                        MyGdxGame.tretsEnemics.remove(this);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Error. ");
        }
    }

    public boolean calcularColisions() {
        boolean col = false;

        float nauX = MyGdxGame.nau.getX() + MyGdxGame.nau.getWidth();
        float nauY = MyGdxGame.nau.getY() + MyGdxGame.nau.getHeight();

        if (MyGdxGame.vides.size() > 0) {
            if ((this.getX() >= MyGdxGame.nau.getX() && this.getX() <= nauX) && (this.getY() >= MyGdxGame.nau.getY() && this.getY() <= nauY)) {
                col = true;
                // Resta una vida de la nau amiga.
                MyGdxGame.vida -= 1;
            }
        }

        return col;
    }

}