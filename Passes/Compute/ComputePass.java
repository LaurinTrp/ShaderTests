package Compute;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL42.GL_ALL_BARRIER_BITS;
import static org.lwjgl.opengl.GL42.glMemoryBarrier;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;
import static org.lwjgl.opengl.GL43.glDispatchCompute;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;

import GUI.LWJGL_Main;
import Shader.ComputeShaderProgram;
import Utils.Loader;
import Utils.Loader.ImageLoader.FloatImageParams;

public class ComputePass {

	private boolean init = false;

	private ComputeShaderProgram program;

	private int ssboPositions;
	private float[] positions;

	private int ssboVelocities;
	private float[] velocities;

	private int ssboColors;
	private float[] colors;

	private int particleCount;
	private int width;
	private int height;

	public ComputePass() {
		width = LWJGL_Main.windowWidth;
		height = LWJGL_Main.windowHeight;
		particleCount = width * height;

		positions = new float[particleCount * 4];
		velocities = new float[positions.length];
		colors = new float[positions.length];

		float halfWidth = (width - 1) / 2.0f;
		float halfHeight = (height - 1) / 2.0f;

		int index = 0;
		for (int y = 0; y < height; y++) {
			float mappedY = (y - halfHeight);
			for (int x = 0; x < width; x++) {
				float mappedX = (x - halfWidth);

				positions[index++] = mappedX;
				positions[index++] = mappedY;
				positions[index++] = 0;
				positions[index++] = 1;
			}
		}

		FloatImageParams image = Loader.ImageLoader.getImageRGBAFloat("image.jpg");
		if (image != null) {
			index = 0;
			for (int y = 0; y < height; y++) {
				int srcY = y * image.height / height;
				for (int x = 0; x < width; x++) {
					int srcX = x * image.width / width;
					int srcIndex = (srcY * image.width + srcX) * 4;
					colors[index++] = image.pixels[srcIndex];
					colors[index++] = image.pixels[srcIndex + 1];
					colors[index++] = image.pixels[srcIndex + 2];
					colors[index++] = image.pixels[srcIndex + 3];
				}
			}
		}
	}

	private void init() {
		{
			ssboPositions = glGenBuffers();
			glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssboPositions);

			FloatBuffer buffer = MemoryUtil.memAllocFloat(positions.length);
			buffer.put(positions).flip();
			glBufferData(GL_SHADER_STORAGE_BUFFER, buffer, GL_DYNAMIC_DRAW);
			MemoryUtil.memFree(buffer);

			glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, ssboPositions);
			glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
		}
		{
			ssboVelocities = glGenBuffers();
			glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssboVelocities);

			FloatBuffer buffer = MemoryUtil.memAllocFloat(velocities.length);
			buffer.put(velocities).flip();
			glBufferData(GL_SHADER_STORAGE_BUFFER, buffer, GL_DYNAMIC_DRAW);
			MemoryUtil.memFree(buffer);

			glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, ssboVelocities);
			glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
		}
		{
			ssboColors = glGenBuffers();
			glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssboColors);

			FloatBuffer buffer = MemoryUtil.memAllocFloat(colors.length);
			buffer.put(colors).flip();
			glBufferData(GL_SHADER_STORAGE_BUFFER, buffer, GL_DYNAMIC_DRAW);
			MemoryUtil.memFree(buffer);

			glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, ssboColors);
			glBindBuffer(GL_SHADER_STORAGE_BUFFER, 0);
		}

		program = new ComputeShaderProgram("Compute");
		init = true;
	}

	public void render() {
		if (!init) {
			init();
		}
		if (!init) {
			return;
		}

		{
			glUseProgram(program.getProgramID());

			glUniform2f(program.getUniformLocation("cursorPos"), LWJGL_Main.x, -LWJGL_Main.y);
			glUniform1f(program.getUniformLocation("deltaTime"), 0.02f);
			glUniform2f(program.getUniformLocation("windowSize"), (float) width, (float) height);

			glDispatchCompute(particleCount, 1, 1);
			glMemoryBarrier(GL_ALL_BARRIER_BITS);

			glUseProgram(0);
		}
	}

	public int getSsboPositions() {
		return ssboPositions;
	}

	public int getSsboColors() {
		return ssboColors;
	}

	public int getParticleCount() {
		return particleCount;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void dispose() {
		if (program != null) {
			program.dispose();
		}
		glDeleteBuffers(ssboPositions);
		glDeleteBuffers(ssboVelocities);
		glDeleteBuffers(ssboColors);
		ssboPositions = 0;
		ssboVelocities = 0;
		ssboColors = 0;
		init = false;
	}

}
