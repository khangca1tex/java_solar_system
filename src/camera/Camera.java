package camera;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera {
	private double eyeX;
	private double eyeY;
	private double eyeZ;

	private double centerX;
	private double centerY;
	private double centerZ;

	private double upX;
	private double upY;
	private double upZ;

	private double FOV = 110;

	private GL2 gl;
	private GLU glu;

	public Camera(GL2 gl, GLU glu, double centerX, double centerY, double centerZ) {

		this.gl = gl;
		this.glu = glu;

		this.centerX = centerX;
		this.centerY = centerY;
		this.centerZ = centerZ;

		resetCamera();

	}

	public void resetCamera() {
		eyeX = 0;
		eyeY = 0;
		eyeZ = 900;

		centerX = 0;
		centerY = 0;
		centerZ = 0;

		upX = 0;
		upY = 1;
		upZ = 0;
	}

	public void changePositionOfEye(double eX, double eY, double eZ) {
		eyeX = eX;
		eyeY = eY;
		eyeZ = eZ;
	}

	public void changePositionToView(double x, double y, double z) {
		centerX = x;
		centerY = y;
		centerZ = z;
	}
	
	public void changeVector(double x, double y, double z) {
		upX = x;
		upY = y;
		upZ = z;
	}

	public void changeFOV(double index) {
		FOV += index;
		if (FOV == 0) {
			FOV = -10;
		}
	}

	public void getView() {
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(FOV, 1, 1, 10000);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	public void getCurrentCamera() {
		glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
	}
}
