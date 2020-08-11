package render;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import main.Pong;

public class Shader {
	private int program, vs, fs;
	
	public Shader(String filename) {
		program = glCreateProgram();
		
		vs = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vs, readFile(filename + ".vs"));
		glCompileShader(vs);
		if(glGetShaderi(vs, GL_COMPILE_STATUS) == 0)
			throw new IllegalStateException(glGetShaderInfoLog(vs));

		fs = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fs, readFile(filename + ".fs"));
		glCompileShader(fs);
		if(glGetShaderi(fs, GL_COMPILE_STATUS) == 0)
			throw new IllegalStateException(glGetShaderInfoLog(fs));
		
		glAttachShader(program, vs);
		glAttachShader(program, fs);
		
		glBindAttribLocation(program, 0, "vertices");
		glBindAttribLocation(program, 1, "textures");
		
		glLinkProgram(program);
		if(glGetProgrami(program, GL_LINK_STATUS) == 0)
			throw new IllegalStateException(glGetProgramInfoLog(fs));
		glValidateProgram(program);
		if(glGetProgrami(program, GL_VALIDATE_STATUS) == 0)
			throw new IllegalStateException(glGetProgramInfoLog(fs));
	}
	
	public void setUniform(String name, int value) {
		int location = glGetUniformLocation(program, name);
		if(location != -1)
			glUniform1i(location, value);
	}

	public void setUniform(String name, Matrix4f value) {
		int location = glGetUniformLocation(program, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		value.get(buffer);
		if(location != -1)
			glUniformMatrix4fv(location, false, buffer);
	}
	
	public void bind() {
		glUseProgram(program);
	}
	
	private String readFile(String filename) {
		StringBuilder string = new StringBuilder();
		BufferedReader br;
		
		try {
			br = new BufferedReader(new InputStreamReader(Pong.class.getResourceAsStream("/res/shaders/" + filename)));
			String line;
			while((line = br.readLine()) != null)
				string.append(line + "\n");
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return string.toString();
	}
}