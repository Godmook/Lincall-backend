package com.Capstone.Lincall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.io.IOException;

@EnableAsync
@SpringBootApplication
public class LincallApplication {

	public static void main(String[] args) throws IOException {
		File file = new File("../image");
		if(!file.exists())
			file.mkdir();

		file = new File("../image/profile");
		if(!file.exists())
			file.mkdir();

		SpringApplication.run(LincallApplication.class, args);
	}

}
