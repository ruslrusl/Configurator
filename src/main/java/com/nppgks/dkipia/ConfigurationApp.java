package com.nppgks.dkipia;

import com.nppgks.dkipia.util.Constant;
import com.nppgks.dkipia.util.Util;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@SpringBootApplication
public class ConfigurationApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConfigurationApp.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
        String path = Util.ISTEST? Constant.FILE.TEST_DIRECTORY : Constant.FILE.DIRECTORY;
        Arrays.stream(new File(path).listFiles()).forEach(File::delete);
    }

}
