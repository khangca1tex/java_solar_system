package planet;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Planet {

	protected Texture texture;
	private double distanceFromSun;
	private double radius;
	protected GLU glu;
	protected GL2 gl;
	protected GLUquadric quad;

	private URL textureURL;

	private float speed = 1;
	protected float tilt;
	protected float currentSpeedTurnAroundItself;
	protected float currentTurnAroundSun;
	protected float speedTurnAroundItself;
	protected float turnAroundSun;

	private boolean orbit;

	private ArrayList<Planet> listSatellite;

	public Planet(String linkTexture, double distanceFromSun, double radius, GLU glu, GL2 gl, float tilt,
			float speedTurnAroundItself, float turnAroundSun) {
		try {

			textureURL = getClass().getClassLoader().getResource(linkTexture);
			BufferedImage img = ImageIO.read(textureURL);
			ImageUtil.flipImageVertically(img);

			this.texture = TextureIO.newTexture(new File(textureURL.toURI()), true);
			this.texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
			this.texture.setTexParameterf(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

		} catch (Exception e) {
			e.printStackTrace();
		}

		this.distanceFromSun = distanceFromSun;
		this.radius = radius;
		this.glu = glu;
		this.gl = gl;

		quad = glu.gluNewQuadric();

		this.tilt = tilt;
		this.speedTurnAroundItself = speedTurnAroundItself;
		this.turnAroundSun = turnAroundSun;

	}

	public void setOrbit(boolean orbit) {
		this.orbit = orbit;
	}

	public void addSatellite(Planet s) {
		if (listSatellite == null) {
			listSatellite = new ArrayList<>();
		}
		listSatellite.add(s);
	}

	public void update() {
		currentTurnAroundSun += turnAroundSun * speed;
		currentSpeedTurnAroundItself += speedTurnAroundItself * speed;

		if (orbit) {
			gl.glPushMatrix();

			glu.gluQuadricDrawStyle(quad, GLU.GLU_LINE);
			gl.glColor4f(1.0f, 0.0f, 0.0f, 0.0f);
			glu.gluDisk(quad, distanceFromSun, distanceFromSun, 50, 50);

			gl.glPopMatrix();
		}

		gl.glRotatef(currentTurnAroundSun, 0, 0, 1);// rotate around sun

		gl.glTranslated(distanceFromSun, 0, 0);// translate around sun

		gl.glRotatef(currentSpeedTurnAroundItself, 0, 0, 1);// rotate itself

		gl.glRotatef(tilt, 0, 1, 0);

	}

	public void draw() {


		float[] white = { 1.0f, 1.0f, 1.0f, 1.0f };

		gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, white, 0);
		gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, white, 0);
		gl.glMateriali(GL_FRONT_AND_BACK, GL_SHININESS, 80);

		glu.gluQuadricTexture(quad, true);
		glu.gluQuadricDrawStyle(quad, GLU.GLU_FILL);
		glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);

		texture.enable(gl);
		texture.bind(gl);

		glu.gluSphere(quad, radius, 50, 50);

		texture.disable(gl);

		if (listSatellite != null) {
			for (Planet s : listSatellite) {
				gl.glPushMatrix();
				s.update();
				s.draw();
				gl.glPopMatrix();
			}
		}
		glu.gluDeleteQuadric(quad);

	}

	public void setSpeed(float currentSpeed) {
		this.speed = currentSpeed;
	}

	public double getDistanceFromSun() {
		return distanceFromSun;
	}

	public float xForCamera() {

		double radian = Math.cos((currentTurnAroundSun  /180) *Math.PI);

		float currentX = (float) ( (radian * distanceFromSun));

		return (float) (currentX);
	}

	public float yForCamera() {
		double radian = Math.sin((currentTurnAroundSun /180) *Math.PI);

		float currentY = (float) ((radian * distanceFromSun));

		return (float) (currentY);
	}

	public double getRadius() {
		return radius;
	}

}
