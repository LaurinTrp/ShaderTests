package Shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL43;

import GUI.LWJGL_Main;

public class ComputeShaderProgram {

	private int programID = 0;
	private int computeID = 0;

	public ComputeShaderProgram(String path) {
		path = LWJGL_Main.PATHS.SHADER_PATH + path + "/";
		computeID = loadShader(path + "compute.comp", GL43.GL_COMPUTE_SHADER);

		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, computeID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
	}

	private int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append(System.getProperty("line.separator"));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader.");
			System.exit(-1);
		}

		return shaderID;
	}

	public int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}

	public void dispose() {
		GL20.glDeleteShader(computeID);
		GL20.glDeleteProgram(programID);
	}

	public int getProgramID() {
		return programID;
	}

}
