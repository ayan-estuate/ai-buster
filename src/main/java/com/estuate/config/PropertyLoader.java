package com.estuate.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyLoader {
    public static Properties load(String path) throws IOException {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream(path)) {
            props.load(input);
        }
        return props;
    }
}
