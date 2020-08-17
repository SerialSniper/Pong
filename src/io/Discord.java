package io;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class Discord {
	private DiscordRichPresence rich;
	
	public Discord() {
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler((user) -> {
			System.out.println("Welcome " + user.username + "#" + user.discriminator + "!");
		}).build();
		
		DiscordRPC.discordInitialize("743408735413862530", handlers, true);
	}
	
	public void updatePresence() {
		rich = new DiscordRichPresence
				.Builder("Playing against AI")
				.setBigImage("icon1024", "Playing Pong")
				.setStartTimestamps(System.currentTimeMillis())
				.build();
		
		DiscordRPC.discordUpdatePresence(rich);
	}
}