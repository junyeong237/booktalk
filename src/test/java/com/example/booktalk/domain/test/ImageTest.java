package com.example.booktalk.domain.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface ImageTest extends CommonTest{

    MultipartFile FILE = new MultipartFile() {
        @Override
        public String getName() {
            return "name";
        }

        @Override
        public String getOriginalFilename() {
            return "test-image.jpg";
        }

        @Override
        public String getContentType() {
            return "image/jpeg";
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getSize() {
            return 0;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return "test image content".getBytes();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {

        }
    };
}
