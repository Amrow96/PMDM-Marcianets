package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter implements Runnable, InputProcessor {
	private SpriteBatch batch;
	private SpriteBatch batchDisparo;
	private int AMPLADA;
	private int ALTURA;
	private Sprite jugador;
	private Sprite disparo;
	private Texture imgplayer;
	private Texture imgDisparo;
	private int yDisparo;
	private int x;
	private boolean fideljoc = false;

	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);

		AMPLADA =  Gdx.graphics.getWidth();
		ALTURA = Gdx.graphics.getHeight();



		batchDisparo = new SpriteBatch();
		imgDisparo = new Texture("tret.png");

		batch = new SpriteBatch();
		imgplayer = new Texture("nau1.png");

		jugador = new Sprite(imgplayer,0,0,imgplayer.getWidth(),imgplayer.getHeight());
		jugador.setX((AMPLADA - jugador.getWidth())/2);
		jugador.setY(20);

		Thread t = new Thread(this);
			t.start();


	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//pintem i coloquem al mig a la nau jugadora
		batch.begin();
		jugador.draw(batch);
		batch.end();

		batchDisparo.begin();

		batchDisparo.draw(imgDisparo,(jugador.getX()+(imgplayer.getWidth()/2)),yDisparo);
		batchDisparo.end();

	}

	@Override
	public void dispose () {
		batch.dispose();
		imgplayer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
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
		if(screenX<(AMPLADA/2)){
			for(x = ((int) jugador.getX()); x > 0; x--){//Esquerra
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else{
			for(x = 0; x < AMPLADA-imgplayer.getWidth(); x++){//Dreta
				try {
					Thread.sleep(2);
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

	@Override
	public void run() {
		while(fideljoc == false){
			for (yDisparo = 40; yDisparo < ALTURA; yDisparo++) {
				try {
					Thread.sleep(5);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
	}
}
