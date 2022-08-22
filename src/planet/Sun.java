package planet;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.GLU;

public class Sun extends Planet{

	
	public Sun(String linkTexture, double distanceFromSun, double radius, GLU glu, GL2 gl, float tilt,
			float speedTurnAroundItself, float turnAroundSun) {
		super(linkTexture, distanceFromSun, radius, glu, gl, tilt, speedTurnAroundItself, turnAroundSun);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw() {
		
		gl.glEnable(GLLightingFunc.GL_LIGHT0);
		super.draw();
		gl.glDisable(GLLightingFunc.GL_LIGHT0);
	}
}
