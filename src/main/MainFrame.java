package main;

import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.FPSAnimator;

import camera.Camera;
import planet.Planet;
import planet.Ring;
import planet.Sun;

import static com.jogamp.opengl.GL2.*;

public class MainFrame implements GLEventListener, KeyListener, MouseMotionListener {

	private JFrame frame;
	private GLJPanel panel;
	private FPSAnimator fps;

	private GLU glu;

	private double xSun;
	private double ySun;
	private double zSun;

	private ArrayList<Planet> listPlanet;

	public boolean isOrbitOn;
	private boolean changeView;
	private float currentSpeed;
	private int numOfStartisTargeted = -1;
	private int startOffset = 1;

	// Camera
	private Camera camera;

	private final int WIDTH = 1280;
	private final int HEIGHT = 720;

	public MainFrame() {

		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);

		panel = new GLJPanel(capabilities);
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.addGLEventListener(this);
		panel.addKeyListener(this);
		panel.addMouseMotionListener(this);

		frame = new JFrame("Solar System");
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		glu = new GLU();
		listPlanet = new ArrayList<>();

		fps = new FPSAnimator(panel, 60);
		fps.start();

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		xSun = 0;
		ySun = 0;
		zSun = 0;

		gl.glEnable(GL_LIGHTING);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glShadeModel(GL_SMOOTH);

		camera = new Camera(gl, glu, xSun, ySun, zSun);
		camera.getView();

		addPlanet(gl);

		randomStar(gl);

		currentSpeed = 1;

	}

	public void addPlanet(GL2 gl) {
		float tilt1 = 20f;
		float speed1 = 0.4f;
		float aroundSun1 = 0.6f;

		float tilt2 = 25f;
		float speed2 = 0.3f;
		float aroundSun2 = 0.5f;

		float tilt3 = 20f;
		float speed3 = 0.3f;
		float aroundSun3 = 0.45f;

		float tilt4 = 20f;
		float speed4 = 0.25f;
		float aroundSun4 = 0.3f;

		float tilt5 = 25f;
		float speed5 = 0.2f;
		float aroundSun5 = 0.24f;

		float tilt6 = 20f;
		float speed6 = 0.15f;
		float aroundSun6 = 0.18f;

		float tilt7 = 25f;
		float speed7 = 0.12f;
		float aroundSun7 = 0.15f;

		float tilt8 = 20f;
		float speed8 = 0.1f;
		float aroundSun8 = 0.1f;

		Planet sun = new Sun("images/sun.jpg", 0, 60, glu, gl, 15, 0.05f, 0.0f);
		Planet mercury = new Planet("images/mercury.jpg", 120, 10, glu, gl, tilt1, speed1, aroundSun1);
		Planet venus = new Planet("images/venus.jpg", 180, 13, glu, gl, tilt2, speed2, aroundSun2);
		Planet earth = new Planet("images/earth.jpg", 250, 18, glu, gl, tilt3, speed3, aroundSun3);
		Planet mars = new Planet("images/mars.jpg", 350, 15, glu, gl, tilt4, speed4, aroundSun4);
		Planet jupiter = new Planet("images/jupiter.jpg", 550, 35, glu, gl, tilt5, speed5, aroundSun5);
		Planet saturn = new Planet("images/saturn.jpg", 650, 31, glu, gl, tilt6, speed6, aroundSun6);
		Planet uranus = new Planet("images/uranus.jpg", 750, 27, glu, gl, tilt7, speed7, aroundSun7);
		Planet neptune = new Planet("images/neptune.jpg", 850, 25, glu, gl, tilt8, speed8, aroundSun8);

		Planet moon = new Planet("images/moon.jpg", 50, 5, glu, gl, -10, 0.1f, 0.15f);
		earth.addSatellite(moon);

		Ring r = new Ring("images/ring.jpg", 0, 0, glu, gl, 15, 0, 0, 45, 65);
		jupiter.addSatellite(r);

		Ring r2 = new Ring("images/asteroids.jpg", 0, 0, glu, gl, 0, 0, 0, 400, 450);
		sun.addSatellite(r2);

		listPlanet.add(sun);
		listPlanet.add(mercury);
		listPlanet.add(venus);
		listPlanet.add(earth);
		listPlanet.add(mars);
		listPlanet.add(jupiter);
		listPlanet.add(saturn);
		listPlanet.add(uranus);
		listPlanet.add(neptune);

		for (int i = 0; i < listPlanet.size(); i++) {
			gl.glPushMatrix();
			gl.glPushAttrib(GL_TEXTURE_BIT);

			gl.glNewList(startOffset + i, GL_COMPILE);
			listPlanet.get(i).draw();
			gl.glEndList();

			gl.glPopAttrib();
			gl.glPopMatrix();

		}

	}

	public void configLight(GL2 gl) {

		float ambient[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };

		float p[] = { 0f, 0, 0, 1f };

		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, specular, 0);
		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, p, 0);
		gl.glEnable(GL2.GL_LIGHT1);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glClear(GL_CLEAR_BUFFER);

		drawStar(gl);

		camera.getView();
		configLight(gl);
		gl.glLoadIdentity();

		camera.getCurrentCamera();

		if (changeView)
			gl.glRotatef(-90, 1, 0, 0);

		if (numOfStartisTargeted == -1)
			gl.glRotatef(15, 1, 0, 0);// rotate to easily view

		for (int i = 0; i < listPlanet.size(); i++) {
			gl.glPushMatrix();
			gl.glPushAttrib(GL_TEXTURE_BIT);

			listPlanet.get(i).setOrbit(isOrbitOn);
			listPlanet.get(i).setSpeed(currentSpeed);
			listPlanet.get(i).update();
			gl.glCallList(startOffset + i);

			gl.glPopAttrib();
			gl.glPopMatrix();

		}

		if (numOfStartisTargeted != -1) {
			float x = listPlanet.get(numOfStartisTargeted).xForCamera();
			float y = listPlanet.get(numOfStartisTargeted).yForCamera();
			float radius = (float) listPlanet.get(numOfStartisTargeted).getRadius();

			gl.glLoadIdentity();
			camera.changeVector(1, 0, 1);
			camera.changePositionOfEye(x - radius - radius / 2, y - radius - radius / 2, 0);
			camera.changePositionToView(x, y, 0);

		}

	}

	public void drawStar(GL2 gl) {

		gl.glEnable(GLLightingFunc.GL_LIGHT0);

		gl.glCallList(10);

		gl.glDisable(GLLightingFunc.GL_LIGHT0);
	}

	public void randomStar(GL2 gl) {

		gl.glNewList(10, GL_COMPILE);

		GLUquadric quadric = glu.gluNewQuadric();
		for (int i = 0; i < 1000; i++) {

			float xPos;
			float yPos;
			float zPos;
			if (Math.random() > 0.5) {
				xPos = 1000;

			} else {
				xPos = -1000;
			}
			if (Math.random() > 0.5) {
				yPos = 1000;

			} else {
				yPos = -1000;
			}
			if (Math.random() > 0.5) {
				zPos = 300;

			} else {
				zPos = -300;
			}
			float x = (float) (Math.random() * xPos);
			float y = (float) (Math.random() * yPos);
			float z = (float) (Math.random() * zPos);

			gl.glPushMatrix();

			gl.glTranslatef(x, y, z);
			gl.glColor3f(1, 1, 1);

			glu.gluSphere(quadric, 1, 10, 10);

			glu.gluDeleteQuadric(quadric);
			gl.glPopMatrix();

		}

		gl.glEndList();

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

	}

	@Override
	public void keyTyped(java.awt.event.KeyEvent e) {

	}

	@Override
	public void keyPressed(java.awt.event.KeyEvent e) {

		switch (e.getKeyCode()) {
		case java.awt.event.KeyEvent.VK_C:
			if (numOfStartisTargeted == -1)
				changeView = (changeView) ? false : true;
			break;

		case java.awt.event.KeyEvent.VK_O:
			isOrbitOn = (isOrbitOn) ? false : true;
			break;

		case java.awt.event.KeyEvent.VK_DOWN:
			camera.changeFOV(-10);
			break;
		case java.awt.event.KeyEvent.VK_UP:
			camera.changeFOV(10);
			break;

		case java.awt.event.KeyEvent.VK_0:
			changeView = false;
			numOfStartisTargeted = 0;
			break;
		case java.awt.event.KeyEvent.VK_1:
			changeView = false;
			numOfStartisTargeted = 1;
			break;
		case java.awt.event.KeyEvent.VK_2:
			changeView = false;
			numOfStartisTargeted = 2;
			break;
		case java.awt.event.KeyEvent.VK_3:
			changeView = false;
			numOfStartisTargeted = 3;
			break;
		case java.awt.event.KeyEvent.VK_4:
			changeView = false;
			numOfStartisTargeted = 4;
			break;
		case java.awt.event.KeyEvent.VK_5:
			changeView = false;
			numOfStartisTargeted = 5;
			break;
		case java.awt.event.KeyEvent.VK_6:
			changeView = false;
			numOfStartisTargeted = 6;
			break;
		case java.awt.event.KeyEvent.VK_7:
			changeView = false;
			numOfStartisTargeted = 7;
			break;
		case java.awt.event.KeyEvent.VK_8:
			changeView = false;
			numOfStartisTargeted = 8;
			break;

		case java.awt.event.KeyEvent.VK_SPACE:
			numOfStartisTargeted = -1;
			camera.resetCamera();
			break;

		case java.awt.event.KeyEvent.VK_R:
			currentSpeed = 1;
			break;

		case java.awt.event.KeyEvent.VK_LEFT:
			currentSpeed -= 0.5;

			if (currentSpeed < 0)
				currentSpeed = 0;
			break;

		case java.awt.event.KeyEvent.VK_RIGHT:
			currentSpeed += 0.5;

			if (currentSpeed > 10)
				currentSpeed = 10;
			break;
		}

	}

	@Override
	public void keyReleased(java.awt.event.KeyEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (numOfStartisTargeted == -1) {
			camera.changePositionToView(x - (WIDTH / 2), y - (HEIGHT / 2), 0);
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	public static void main(String[] args) {
		new MainFrame();
	}

}
