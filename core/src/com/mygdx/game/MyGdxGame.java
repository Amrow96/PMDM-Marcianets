package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter implements  InputProcessor {
	private SpriteBatch batch;
	private SpriteBatch batchDisparo;
	private SpriteBatch batchEnemic;
	private int AMPLADA;
	private int ALTURA;
	private Sprite jugador;
	private Sprite enemy;
	private Texture imgplayer;
	private Texture imgEnemigo;
	private Texture imgDisparo;
	private int yDisparo;
	private int xDisparo;
	private int[] xEnemigo;
	private int[] yEnemigo;
	private int x;
	private boolean fideljoc = false;

	@Override
	public void create() {
		Gdx.input.setInputProcessor(this);

		AMPLADA = Gdx.graphics.getWidth();
		ALTURA = Gdx.graphics.getHeight();


		yEnemigo = new int[7];
		xEnemigo = new int[7];

		batchDisparo = new SpriteBatch();
		imgDisparo = new Texture("tret.png");

		batch = new SpriteBatch();
		imgplayer = new Texture("nau1.png");
//		x = (int) (AMPLADA - jugador.getWidth()) / 2;



		batchEnemic = new SpriteBatch();
		imgEnemigo = new Texture("nau2.png");

		jugador = new Sprite(imgplayer,0,0,imgplayer.getWidth(),imgplayer.getHeight());
		jugador.setX((AMPLADA - jugador.getWidth()) / 2);
		jugador.setY(20);


		//thread disparo
		Thread t = new Thread(){
			public void run(){
				while (fideljoc == false) {
					//Moviment perpetu de la bala
					for (yDisparo = 40; yDisparo < ALTURA; yDisparo++) {
						try {
							Thread.sleep(5);

						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		t.start();

		Thread thread = new Thread() {
			public void run() {
				while (fideljoc == false) {
					int modificador = 0;
					for (int i = 0; i < xEnemigo.length; i++) {
						if (yEnemigo[i] > 200) {
							yEnemigo[i]--;
							yEnemigo[i] -= modificador;
						} else {
							yEnemigo[i]++;
							yEnemigo[i] += modificador;
						}

						if (xEnemigo[i] > (AMPLADA - jugador.getWidth())) {
							xEnemigo[i]--;
							xEnemigo[i]-= modificador;

						} else {
							xEnemigo[i]++;
							xEnemigo[i]+= modificador;


						}
						modificador++;
						try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}
		};
		thread.start();

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//pintem i coloquem al mig a la nau jugadora
		batch.begin();
		batch.draw(imgplayer,x ,40);
		batch.end();

		//Imprimim tots els enemics
		for (int i = 0; i < xEnemigo.length; i++) {
			batchEnemic.begin();
			batchEnemic.draw(imgEnemigo, (xEnemigo[i] = (AMPLADA / 2)),( yEnemigo[i] = 800));
			batchEnemic.end();
		}

		batchDisparo.begin();
		batchDisparo.draw(imgDisparo, (x + (jugador.getWidth() / 2)), yDisparo);
		batchDisparo.end();

	}

	@Override
	public void dispose() {
		batch.dispose();
		imgplayer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode){
			case Input.Keys.DPAD_LEFT:
				for (x = ((int) jugador.getX()); x > 0; x--) {//Esquerra
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			case Input.Keys.DPAD_RIGHT:
				for (x = ((int) jugador.getX()); x < AMPLADA - imgplayer.getWidth(); x++) {//Dreta
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		int time = 10;
		if (screenX < (AMPLADA / 2)) {
			for (x = ((int) jugador.getX()); x > 0; x--) {//Esquerra
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			for (x = ((int) jugador.getX()); x < AMPLADA - imgplayer.getWidth(); x++) {//Dreta
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
