package com.conf;

import java.io.*;
import java.util.Optional;

public class Demo {
    public static void main(String[] args) throws IOException {
        Optional<String> f = Optional.of("file1.java");
        File f1 = new File(f.get());
    }
}
