package com.pkk.wetravelserver.services.storage;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class StreamingService {

    @Value("${app.storage.path}")
    private String pathToStorage;

    public ResponseEntity<StreamingResponseBody> loadPartialMediaFile(String storagePath, String rangeValues, Logger logger) throws IOException {
        logger.debug("Range values {}", rangeValues);

        Path filePath = Paths.get(storagePath);
        if (!filePath.toFile().exists()) {
            throw new FileNotFoundException("The media file does not exists");
        }

        long rangeStart = 0L, rangeEnd = 0L, fileSize = Files.size(filePath);
        if (!StringUtils.hasText(rangeValues)) {
            //Returns file from beginning
            return loadPartialMediaFile(filePath, 0, fileSize, fileSize);
        }

        //Parsing range values. Expected form: 0-499 by RFC 7233 2.3. Byte Ranges
        String[] range = rangeValues.split("-");
        if (range.length == 2){
            rangeStart = Long.parseLong(range[0]);
            rangeEnd = Long.parseLong(range[1]);
            logger.info("Parsed range values: rangeStart {}, rangeEnd {}", rangeStart, rangeEnd);
            return loadPartialMediaFile(filePath, rangeStart, rangeEnd, fileSize);
        }
        return ResponseEntity.internalServerError().build();
    }

    ResponseEntity<StreamingResponseBody> loadPartialMediaFile(Path filePath, final long fileStartPos, final long fileEndPos, long fileSize) throws IOException {
        byte[] buffer = new byte[1024];
        String mimeType = Files.probeContentType(filePath);
        final HttpHeaders responseHeaders = new HttpHeaders();
        String contentLength = String.valueOf((fileEndPos - fileStartPos) + 1);
        responseHeaders.add("Content-Type", mimeType);
        responseHeaders.add("Content-Length", contentLength);
        responseHeaders.add("Accept-Ranges", "bytes");
        responseHeaders.add("Content-Range", String.format("bytes %d-%d/%d", fileStartPos, fileEndPos, fileSize));

        StreamingResponseBody responseStream = outputStream -> {
            RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r");
            try (file) {
                long pos = fileStartPos;
                file.seek(pos);
                while (pos < fileEndPos) {
                    file.read(buffer);
                    outputStream.write(buffer);
                    pos += buffer.length;
                }
                outputStream.flush();
            } catch (IOException ignored) {
                //TODO
            }
        };
        return new ResponseEntity<>(responseStream, responseHeaders, HttpStatus.PARTIAL_CONTENT);
    }

}
