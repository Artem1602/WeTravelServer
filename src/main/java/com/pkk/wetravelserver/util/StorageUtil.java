package com.pkk.wetravelserver.util;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import org.slf4j.Logger;

@Component
public class StorageUtil {

    public File initUserIdDir(String fullPath, Logger logger) {
        File userIdDirectory = new File(fullPath);
        if (!userIdDirectory.exists()) {
            logger.info("Personal user folder creation status: {}", userIdDirectory.mkdir());
        }
        return userIdDirectory;
    }

    public Boolean saveMultipartFile(MultipartFile multipartFile, File userIdDirectory, String fileName, Logger logger) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream();
             OutputStream outputStream = new FileOutputStream(userIdDirectory.getPath() + File.separator + fileName)
        ) {
            IOUtils.copy(inputStream, outputStream);
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
