package edu.arapahoe.csc245;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.*;

public class CSC245_Project3_Secure {
	// Java Coding Guidelines: Use meaningful symbolic constants to represent literal values in program logic.
	private static final String API_KEY = "put API key here";

	public static Map<String, Object> jsonToMap(String str) {
		// Java Coding Guidelines: Minimize the scope of variables.
		return new Gson().fromJson(
				str,
				new TypeToken<HashMap<String, Object>>() {
				}.getType()
		);
	}

	public static String getTempForCity(String cityString) {
		try {
			// Java Coding Guidelines: Minimize the scope of variables.
			String urlString =  "https://api.openweathermap.org/data/2.5/weather?q=" +
					cityString +
					"&appid=" +
					API_KEY +
					"&units=imperial";

			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream())
			);

			StringBuilder result = new StringBuilder();
			String line;

			// Java Coding Guidelines: Use braces for the body of an if, for, or while statement.
			// Java Coding Guidelines: Do not place a semicolon immediately following an if, for, or while condition.
			while((line = reader.readLine()) != null) {
				result.append(line);
			}

			System.out.println(result);

			Map<String, Object> responseMap = jsonToMap(result.toString());
			Map<String, Object> mainMap = jsonToMap(responseMap.get("main").toString());

			// The CERT Oracle Secure Coding Standard for Java: FIO14-J. Perform proper cleanup at program termination.
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					reader.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}));

			return mainMap.get("temp").toString();

		} catch(IOException e) {
			e.printStackTrace();
			return "Temp not available (API problem?).";
		}
	}

	public static void main(String[] args) {
		// Java Coding Guidelines: Minimize the scope of variables.
		// Java Coding Guidelines: Do not place a semicolon immediately following an if, for, or while condition.
		// Java Coding Guidelines: Do not declare more than one variable per declaration.
		System.out.println("Current temperature in Castle Rock is: " + getTempForCity("CASTLE ROCK") + " degrees.");
		// Java Coding Guidelines: Do not attempt to help the garbage collector by setting local reference variables to null.
		// The CERT Oracle Secure Coding Standard for Java: FIO14-J. Perform proper cleanup at program termination.
		Runtime.getRuntime().exit(0);
	}
}
