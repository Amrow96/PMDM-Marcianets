package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	// Constants
	public static final int MAX_NAUS_ENEMIGUES = 7;
	public static final int MOVIMENT = 15;
	public static final int NUM_VIDES = 3;
	public static final int VIDA_JUGADOR = 100;

	// Tamany de la pantalla.
	public static float screenWidth;
	public static float screenHeight;

	// Textures del joc.
	public static Texture nauAliada;
	public static Texture nauEnemiga;
	public static Texture tret;

	// Sprites del joc.
	public static Sprite nau;
	public static NauEnemiga nauEnem;

	// Informació de la partida.
	public static int puntuacio;
	public static int vida;

	// Vectors
	public static ArrayList<Sprite> vides;
	public static ArrayList<TretNauAliada> trets;
	public static ArrayList<NauEnemiga> nausEnemigues;
	public static ArrayList<TretNauEnemiga> tretsEnemics;

	// Objecte SpriteBatch que permet dibuixar en la pantalla
	private SpriteBatch batch;	// Objecte SpriteBatch que permet dibuixar en la pantalla

	// Posició de la nau aliada a la pantalla.
	private float posX;

	// Objecte que permet escriure text a la pantalla
	BitmapFont font;
	ShapeRenderer shapeRenderer;

	/**
	 * Crea els objectes i els hi dóna una posició inicial
	 */
	@Override
	public void create () {
		// Creem l'objecte que permetrà situar objectes a la pantalla
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		Gdx.input.setInputProcessor(this);

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		vides = new ArrayList<Sprite>();
		trets = new ArrayList<TretNauAliada>();
		nausEnemigues = new ArrayList<NauEnemiga>();
		tretsEnemics = new ArrayList<TretNauEnemiga>();

		nauAliada = new Texture("nau1.png");
		nauEnemiga = new Texture("nau2.png");
		tret = new Texture("tret.png");

		posX = (screenWidth / 2);

		vida = VIDA_JUGADOR;
		puntuacio = 0;

		generarVides();
		generarNausEnemigues();

		// Creem el Sprite per la nau.
		nau = new Sprite(nauAliada, 0, 0, nauAliada.getWidth(), nauAliada.getHeight());
		nau.setX(posX);
		nau.setY(nauAliada.getHeight() + 15);


		//nau = new Sprite(nauTexture, 0, 0, nauTexture.getWidth(), nauTexture.getHeight());
		//nauEnemiga = new Sprite(enemigaTexture, 0, 0, enemigaTexture.getWidth(), enemigaTexture.getHeight());

		// Creem l'objecte per dibuixar a la pantalla
		font = new BitmapFont();
	}

	public static void generarVides() {
		int distancia = 0;

		for (int i = 0; i < NUM_VIDES; i++) {
			distancia += nauAliada.getWidth() + 10;
			nau = new Sprite(nauAliada, 0, 0, nauAliada.getWidth(), nauAliada.getHeight());
			nau.setX(screenWidth - distancia);
			nau.setY(5);

			vides.add(nau);
		}
	}

	public static void generarNausEnemigues() {
		int tempX;

		for (int i = 0; i < MAX_NAUS_ENEMIGUES; i++) {
			do {
				Random random = new Random();
				tempX = random.nextInt((int)(screenWidth - nauEnemiga.getWidth()));
			} while ((tempX < nauEnemiga.getWidth()) || (tempX > (screenWidth - nauEnemiga.getWidth())));

			nauEnem = new NauEnemiga(nauEnemiga, 0, 0, nauEnemiga.getWidth(), nauEnemiga.getHeight(), tempX);
			nausEnemigues.add(nauEnem);
			Thread thread = new Thread(nauEnem);
			thread.start();
		}
	}

	/**
	 * Renderitza, o sigui, redibuixa la pantalla. Aquest mètode es cridant de manera reiterada i
	 * constant i s'encarrega de refrescar la pantalla.
	 */
	@Override
	public void render () {
		// Netejem la pantalla, deixant un fons vermell
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Iniciem el dibuix de la pantalla
		batch.begin();

		if (vida == 0 && vides.size() > 0) {
			vides.remove(0);
			vida = VIDA_JUGADOR;
		}
		if (vides.size() > 0) {
			for (Sprite vida : vides) {
				vida.draw(batch);
			}

			for (TretNauAliada t : trets) {
				t.draw(batch);
			}

			for (NauEnemiga ne : nausEnemigues) {
				ne.draw(batch);
			}

			for (TretNauEnemiga tne : tretsEnemics) {
				tne.draw(batch);
			}

			nau.draw(batch);

			// Dibuixem el text
			font.draw(batch, "SCORE: " + puntuacio + " VIDES: " + vides.size() + " VIDA: " + vida, 15, 20);
		} else {
			font.draw(batch, "GAME OVER", screenWidth/2, screenHeight/2);
		}

		// Acabem el dibuix de la pantalla
		batch.end();

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(1, 1, 1, 1);
		shapeRenderer.line(0, nauAliada.getHeight() + 10, screenWidth, nauAliada.getHeight() + 10);
		shapeRenderer.end();
	}

	/**
	 * Controla quan premem una tecla qualsevol del teclat. En el nostre cas, movem el porter a la
	 * dreta o esquerra en funció de la tecla polsada.
	 * @param keycode	Codi corresponent a la tecla polsada
	 * @return
	 */
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			// Cursor (fletxa) esquerre
			case Input.Keys.DPAD_LEFT:
				if (posX > 0) {
					posX -= MOVIMENT;
					nau.setX(posX);
				}
				break;

			// Cursor (fletxa) dret
			case Input.Keys.DPAD_RIGHT:
				if ((posX + MOVIMENT) < (screenWidth - nauAliada.getWidth() / 2)) {
					posX += MOVIMENT;
					nau.setX(posX);
				}
				break;
			case Input.Keys.SPACE:
				if (trets.size() < 20) {
					TretNauAliada nouTtret = new TretNauAliada(tret, 0, 0, tret.getWidth(), tret.getHeight(), nau.getX() + nau.getWidth() / 2);
					trets.add(nouTtret);
					Thread thread = new Thread(nouTtret);
					thread.start();
					System.out.println(trets.size());
				}
		}

		return false;
	}

	/**
	 * Es pot utilitzar per controlar quan es deixa anar una tecla prèviament polsada
	 * @param keycode
	 * @return
	 */
	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	/**
	 * Controla quan toquem la pantalla. En el nostre cas, utilitzem el mètode per saber la posició
	 * inicial del porter abans de moure'l.
	 * @param screenX	// Posició X on l'usuari ha tocat la pantalla
	 * @param screenY	// Posició Y on l'usuari ha tocat la pantalla
	 * @param pointer
	 * @param button
	 * @return
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

	/**
	 * Controla quan aixequem el dit després d'arrossegar-lo per la pantalla. En el nostre cas,
	 * utilitzem el mètode per saber la posició on hem aixecat el dit, la comparem amb la posició on
	 * va prèmer el dit i calculem el desplaçament del porter, situant-lo en la nova posició.
	 * @param screenX	// Posició X on l'usuari ha tocat la pantalla
	 * @param screenY	// Posició Y on l'usuari ha tocat la pantalla
	 * @param pointer
	 * @param button
	 * @return
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

	/**
	 * Controla la posició del dit quan l'anem arrossegant per la pantalla. En el nostre cas,
	 * utilitzem el mètode anar redibuixant la posició del porter a mesura que desplacem el dit.
	 * @param screenX	// Posició X on l'usuari ha tocat la pantalla
	 * @param screenY	// Posició Y on l'usuari ha tocat la pantalla
	 * @param pointer
	 * @return
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}
