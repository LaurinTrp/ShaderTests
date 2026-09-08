package Compute;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindBufferBase;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL32.GL_PROGRAM_POINT_SIZE;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;

import Shader.ShaderProgram;

public class ParticlePass {

	private boolean init = false;

	private ShaderProgram program;
	private int particleVao;
	private int ssboPositions;
	private int ssboColors;
	private int particleCount;
	private int width;
	private int height;

	private void init() {
		particleVao = glGenVertexArrays();
		glBindVertexArray(particleVao);
		glBindVertexArray(0);

		program = new ShaderProgram("Particle");
		init = true;
	}

	public void render() {
		if (!init) {
			init();
		}
		if (!init) {
			return;
		}

		glEnable(GL_PROGRAM_POINT_SIZE);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		glPointSize(5.0f);

		glUseProgram(program.getProgramID());
		{
			glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, ssboPositions);
			glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, ssboColors);

			glUniform2f(program.getUniformLocation("windowSize"), (float) width, (float) height);

			glBindVertexArray(particleVao);
			{
				glDrawArrays(GL_POINTS, 0, particleCount);
			}
			glBindVertexArray(0);
		}
		glUseProgram(0);

		glDisable(GL_BLEND);
	}

	public void setSsboPositions(int ssboPositions) {
		this.ssboPositions = ssboPositions;
	}

	public void setSsboColors(int ssboColors) {
		this.ssboColors = ssboColors;
	}

	public void setParticleCount(int particleCount) {
		this.particleCount = particleCount;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void dispose() {
		glDeleteVertexArrays(particleVao);
		particleVao = 0;
		if (program != null) {
			program.dispose();
		}
		init = false;
	}

}
