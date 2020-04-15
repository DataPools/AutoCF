package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonReader;

public class ConfigHandler {
	
	private File configFile;

	private Gson gson;
	private Config config;
	
	public ConfigHandler(File configFile) {
		this.configFile = configFile;
		gson = new GsonBuilder().setPrettyPrinting().create();
		if(!configFile.exists()) {
			config = new Config();
			try {
				FileWriter filewriter = new FileWriter(configFile);
				gson.toJson(config, filewriter);
				filewriter.close();
			} catch (JsonIOException | IOException e) {
				throwConfigError(e.getMessage());
			}
		}
		else {
			load();
		}
	}
	private void load() {
		JsonReader jsonReader = null;
		try {
			jsonReader = new JsonReader(new FileReader(configFile));
		} catch (FileNotFoundException e) {
			throwConfigError(e.getMessage());
		}
		config = gson.fromJson(jsonReader, Config.class);
	}
	private void throwConfigError(String errorText) {
		System.out.println("ERROR:"+errorText);
		System.exit(0);
	}
	public Config getConfig() {
		return config;
	}

}
