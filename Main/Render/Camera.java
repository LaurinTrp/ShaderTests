package Render;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

import GUI.LWJGL_Main;
import glm.Glm;
import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

public class Camera {
	private Vec3 cameraPosition = new Vec3(0.0f, 0.0f, 3.0f);
	private Vec3 cameraFront = new Vec3(0.0f, 0.0f, -1.0f);
	private Vec3 cameraUp = new Vec3(0.0f, 1.0f, 0.0f);
	private Vec3 cameraRight = new Vec3(1.0f, 0.0f, 0.0f);
	
	private Vec3 direction = new Vec3();

	private float angleHorizontal = 0.0f;
	private float angleVertical = 0.0f;

	private Mat4 view = new Mat4();
	private Mat4 projectionMatrix = new Mat4();

	private final float cameraSpeed = 0.05f;

	public Camera() {
		view = Glm.lookAt_(cameraPosition, Utils.Glm.add(cameraPosition, cameraFront), cameraUp);
		projectionMatrix = Glm.perspective_(45.0f, (float)LWJGL_Main.windowWidth/(float)LWJGL_Main.windowHeight, 0.1f, 100.0f);
	}
	
	private void translation() {
		if (LWJGL_Main.keyHandler.isPressed(GLFW_KEY_W)) {
			cameraPosition = Utils.Glm.add(cameraPosition, (Utils.Glm.times(cameraFront, cameraSpeed)));
		}
		if (LWJGL_Main.keyHandler.isPressed(GLFW_KEY_S)) {
			cameraPosition = Utils.Glm.subtract(cameraPosition, (Utils.Glm.times(cameraFront, cameraSpeed)));
		}

		cameraRight = Utils.Glm.cross(cameraFront, cameraUp);

		if (LWJGL_Main.keyHandler.isPressed(GLFW_KEY_A)) {
			cameraPosition = Utils.Glm.subtract(cameraPosition, (Utils.Glm.times(cameraRight, cameraSpeed)));
		}
		if (LWJGL_Main.keyHandler.isPressed(GLFW_KEY_D)) {
			cameraPosition = Utils.Glm.add(cameraPosition, (Utils.Glm.times(cameraRight, cameraSpeed)));
		}

		if (LWJGL_Main.keyHandler.isPressed(GLFW_KEY_Q)) {
			cameraPosition = Utils.Glm.add(cameraPosition, (Utils.Glm.times(cameraUp, cameraSpeed)));
		}
		if (LWJGL_Main.keyHandler.isPressed(GLFW_KEY_X)) {
			cameraPosition = Utils.Glm.subtract(cameraPosition, (Utils.Glm.times(cameraUp, cameraSpeed)));
		}
	}
	
	private void rotation() {

		angleVertical += LWJGL_Main.mouseHandler.getYoffset();
		angleHorizontal += LWJGL_Main.mouseHandler.getXoffset();

		if(angleVertical <= -89f) {
			angleVertical = -89f;
		}
		if(angleVertical >= 89f) {
			angleVertical = 89f;
		}
		
		cameraFront.set(Math.sin(Math.toRadians(angleHorizontal)), Math.sin(Math.toRadians(angleVertical)),
				-Math.cos(Math.toRadians(angleHorizontal)));
	}
	
	public void moveCamera() {
		
		updateProjectionMatrix();
		
		rotation();
		translation();
		
		
		
		view = Glm.lookAt_(cameraPosition, Utils.Glm.add(cameraPosition, cameraFront), cameraUp);
	}
	
	private void updateProjectionMatrix() {
		projectionMatrix = Glm.perspective_((float)Math.toRadians(45), (float)LWJGL_Main.windowWidth/(float)LWJGL_Main.windowHeight, 0.1f, 100f);
	}
	
	public Mat4 getView() {
		return view;
	}
	
	public Mat4 getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public Vec3 getCameraPosition() {
		return cameraPosition;
	}

}
