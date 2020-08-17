package io.sound;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.EXTThreadLocalContext.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.*;
import java.nio.*;
import java.util.*;

import javax.sound.sampled.*;

import org.lwjgl.openal.*;

public final class SoundCollection extends HashMap<String, Integer> {
	private static final long serialVersionUID = 1L;
	private long device, context;
	private int source;
	
	public SoundCollection() {
		device = alcOpenDevice((ByteBuffer)null);
		
		if(device == NULL)
			throw new IllegalStateException("Failed to open the default device.");

		ALCCapabilities deviceCaps = ALC.createCapabilities(device);

		if(!deviceCaps.OpenALC10)
			throw new IllegalStateException();

		context = alcCreateContext(device, (IntBuffer)null);
		alcSetThreadContext(context);
		AL.createCapabilities(deviceCaps);

		source = alGenSources();
	}
	
	private int get(String string) {
		return (int)get((Object)string);
	}
	
	public void destroy() {
		alDeleteSources(source);
		
		for(String i : keySet())
			alDeleteBuffers(get(i));
		
		alcMakeContextCurrent(NULL);
		AL.setCurrentThread(null);
		alcDestroyContext(context);
		alcCloseDevice(device);
	}
	
	public SoundCollection add(String filename) {
		int id = alGenBuffers();
		
		WaveData waveData = WaveData.create(SoundCollection.class.getResourceAsStream("/res/sfx/" + filename + ".wav"));
		
		alBufferData(id, AL_FORMAT_MONO16, waveData.data, waveData.samplerate);
		put(filename, id);
		waveData.dispose();
		
		return this;
	}
	
	public void play(String filename) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				alcMakeContextCurrent(context);
				
				alSourcei(source, AL_BUFFER, get(filename));
				alSourcePlay(source);
				
				while(true)
					if(alGetSourcei(source, AL_SOURCE_STATE) == AL_STOPPED)
						break;
				
				alSourceStop(source);
			}
		}).start();
	}
	
	static void checkALCError(long device) {
        int err = alcGetError(device);
        if(err != ALC_NO_ERROR) {
            throw new RuntimeException(alcGetString(device, err));
        }
    }
	
    static void checkALError() {
        int err = alGetError();
        if(err != AL_NO_ERROR) {
            throw new RuntimeException(alGetString(err));
        }
    }
}