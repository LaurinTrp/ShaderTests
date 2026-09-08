package Metaballs;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_READ;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

import GUI.LWJGL_Main;
import Shader.ShaderProgram;

public class MetaBallPass {

	private boolean init = false;
	private int vao = 0, vbo = 0, ebo = 0;
	private ShaderProgram program;

	private int uniformMouseX = 0, uniformMouseY = 0, u_balls = 0;
	private ArrayList<Ball> balls = new ArrayList<Ball>();

	FloatBuffer floatBuffer = FloatBuffer.allocate(2);

	private void init() {
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
			balls.add(new Ball(0.0f, 0.0f, (random.nextInt(200) - 100) / 10000f, (random.nextInt(200) - 100) / 10000f));		
		}
		
		initVAOs();
		loadShaderProgram();

		init = true;
	}

	private void initVAOs() {
		float[] vertices = new float[] {
				// v0 (TL)
				-1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
				// v1 (BL)
				-1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
				// v2 (BR)
				1.0f, -1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
				// v3 (TR)
				1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f };
		int[] elements = new int[] {
				// triangle 1
				0, 1, 2,
				// triangle 2
				2, 3, 0 };

		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		ebo = glGenBuffers();

		glBindVertexArray(vao);
		{
			glBindBuffer(GL_ARRAY_BUFFER, vao);
			glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_READ);

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, elements, GL_DYNAMIC_READ);

			glEnableVertexAttribArray(0);
			glVertexAttribPointer(0, 4, GL_FLOAT, false, 8 * Float.BYTES, 0 * Float.BYTES);

			glEnableVertexAttribArray(1);
			glVertexAttribPointer(1, 4, GL_FLOAT, false, 8 * Float.BYTES, 4 * Float.BYTES);
		}
		glBindVertexArray(0);
	}

	private void loadShaderProgram() {
		program = new ShaderProgram("MetaBall");
		uniformMouseX = program.getUniformLocation("mouse_x");
		uniformMouseY = program.getUniformLocation("mouse_y");
		
		u_balls = program.getUniformLocation("balls");
	}

	public void render() {
		if (!init) {
			init();
		}
		if (!init) {
			return;
		}
		

		float[] buffer = new float[balls.size() * 2];
		int counter = 0;
		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).updatePosition();
			buffer[counter] = balls.get(i).getPosition().x;
			counter++;
			buffer[counter] = balls.get(i).getPosition().y;
			counter++;
		}
		
		{
			glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			glClear(GL_COLOR_BUFFER_BIT);

			{
				glUseProgram(program.getProgramID());
				glUniform1f(uniformMouseX, LWJGL_Main.x);
				glUniform1f(uniformMouseY, LWJGL_Main.y);
				glUniform2fv(u_balls, buffer);
			}
			{
				glBindVertexArray(vao);
				{
					glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
				}
				glBindVertexArray(0);
			}
		}

	}

	public void dispose() {
		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);
		glDeleteBuffers(ebo);

		vao = 0;
		vbo = 0;
		ebo = 0;

		if (program != null) {
			program.dispose();
		}

		init = false;
	}

}
