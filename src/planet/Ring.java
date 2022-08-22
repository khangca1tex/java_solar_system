package planet;

import static com.jogamp.opengl.GL.GL_FRONT_AND_BACK;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT_AND_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SHININESS;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SPECULAR;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.GLU;

public class Ring extends Planet {

	private float inner;
	private float outter;

	public Ring(String linkTexture, double distanceFromSun, double radius, GLU glu, GL2 gl, float tilt,
			float speedTurnAroundItself, float turnAroundSun, float inner, float outter) {
		super(linkTexture, distanceFromSun, radius, glu, gl, tilt, speedTurnAroundItself, turnAroundSun);

		this.outter = outter;
		this.inner = inner;

	}

	@Override
	public void draw() {

		gl.glEnable(GLLightingFunc.GL_LIGHT0);
		gl.glPushMatrix();
		gl.glRotatef(-20, 0, 1, 0);
		float[] white = { 1.0f, 1.0f, 1.0f, 1.0f };

		gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE, white, 0);
		gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, white, 0);
		gl.glMateriali(GL_FRONT_AND_BACK, GL_SHININESS, 80);

		glu.gluQuadricTexture(quad, true);
		glu.gluQuadricDrawStyle(quad, GLU.GLU_FILL);
		glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);

		texture.enable(gl);
		texture.bind(gl);

		glu.gluDisk(quad, inner, outter, 50, 50);
		gl.glPopMatrix();
		
		gl.glDisable(GLLightingFunc.GL_LIGHT0);

	}

}
