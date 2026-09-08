package Render;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;

import Compute.ComputePass;
import Compute.ParticlePass;
import Default.RectanglePass;
import Default.TrianglePass;
import GeometryShader.GeometryShader;
import Metaballs.MetaBallPass;
import Texture.TexturePass;
import Transformation.LightSourcePass;
import Transformation.TransformationPass;
import glm.vec._4.Vec4;

public class Renderer {
	
	private TrianglePass trianglePass;
	private MetaBallPass metaBall;
	
	private RectanglePass rectanglePass;
	private TexturePass texturePass;
	
	private TransformationPass transformationPass;
	
	private LightSourcePass lightSourcePass;
	
	public static Camera camera;
	
	private GeometryShader geometryShader;

	private ComputePass computePass;
	private ParticlePass particlePass;
	
	public static ArrayList<Vec4> lightSourcePositions = new ArrayList<>();
	
	
	public Renderer() {
		camera = new Camera();
		
		trianglePass = new TrianglePass();
		metaBall = new MetaBallPass();
	
		rectanglePass = new RectanglePass();
		texturePass = new TexturePass();
		
		transformationPass = new TransformationPass();
		
		lightSourcePass = new LightSourcePass();
		
		lightSourcePositions.add(lightSourcePass.getLightPosition());
		
		geometryShader = new GeometryShader();

		computePass = new ComputePass();
		particlePass = new ParticlePass();
		
	}
	
	

	public void render() {

		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glEnable(GL_DEPTH_TEST);
//		camera.moveCamera();
		
//		trianglePass.render();
//		rectanglePass.render();
		
//		metaBall.render();
		
//		texturePass.render();
		
//		transformationPass.render();
//		lightSourcePass.render();
	
//		geometryShader.render();

		computePass.render();
		particlePass.setSsboPositions(computePass.getSsboPositions());
		particlePass.setSsboColors(computePass.getSsboColors());
		particlePass.setParticleCount(computePass.getParticleCount());
		particlePass.setSize(computePass.getWidth(), computePass.getHeight());
		particlePass.render();

	}

	public void dispose() {
		trianglePass.dispose();
		metaBall.dispose();

		rectanglePass.dispose();
		texturePass.dispose();
		
		transformationPass.dispose();
		
		lightSourcePass.dispose();
		
		geometryShader.dispose();

		computePass.dispose();
		particlePass.dispose();
	}
	
}
