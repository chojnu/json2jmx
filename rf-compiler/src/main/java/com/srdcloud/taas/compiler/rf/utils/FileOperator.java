package com.srdcloud.taas.compiler.rf.utils;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FileOperator {

    @Value("${token.path}")
    private String tokenPath;

    public String getfileinfo() {
        String rstr = "";

        try {
            FileSystemResource resource = new FileSystemResource(tokenPath);
            BufferedReader br = new BufferedReader(new FileReader(resource.getFile()));
            String str = null;
            while ((str = br.readLine()) != null) {
                rstr += str;
            }
            br.close();
        } catch (IOException e) {
            //todo loginfo
        }
        return rstr;
    }

    public Boolean writeFile(String filename, String t) {
        FileSystemResource resource = new FileSystemResource(tokenPath + filename);
        try {
            FileWriter fileWriter = (new FileWriter(resource.getFile()));
            fileWriter.write(t);
            fileWriter.close();
        } catch (IOException e) {
            //todo loginfo
            return false;
        }
        return true;
    }
}