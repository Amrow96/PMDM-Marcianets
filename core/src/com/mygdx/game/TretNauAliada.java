package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class TretNauAliada extends Sprite implements Runnable {
    private float posY;
    private int dirY = 1;
    private boolean impacte = false;

    public TretNauAliada (Texture texture, int srcX, int srcY, int srcWidth, int srcHeight, float posX) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        this.setX(posX);
        posY = this.getY() + this.getHeight() + MyGdxGame.nau.getHeight() + 15;
        this.setY(posY);
    }

    @Override
    public void run() {
        try {
            while (posY < MyGdxGame.screenHeight && impacte == false) {
                posY += 2 * dirY;
                Thread.sleep(2);
                this.setY(posY);

                impacte = calcularColisions();

                //Elimina el tret si col·lisiona amb una nau enemiga.
                if ((impacte) || (posY >= MyGdxGame.screenHeight)) {
                    synchronized (MyGdxGame.trets) {
                        MyGdxGame.trets.remove(this);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Error. ");
        }
    }

    /**
     * Comprova si el tret col·lisiona amb la nau enemiga.
     * @return 'boolean'. Retorna TRUE si col·lisiona i FALSE si no col·lisiona.
     */
    public boolean calcularColisions() {
        boolean col = false;

        for (NauEnemiga ne : MyGdxGame.nausEnemigues) {
            float nauEnemigaX = ne.getX() + ne.getWidth();
            float nauEnemigaY = ne.getY() + ne.getHeight();

            if (ne.viva == true) {
                if ((this.getX() >= ne.getX() && this.getX() <= nauEnemigaX) && (this.getY() >= ne.getY() && this.getY() <= nauEnemigaY)) {
                    col = true;
                    // Resta una vida de la nau enemiga.
                    ne.vida -= 1;
                    MyGdxGame.puntuacio += 10;
                }
            }
        }

        return col;
    }
}
