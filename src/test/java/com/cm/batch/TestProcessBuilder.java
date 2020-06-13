package com.cm.batch;


import com.cm.batch.download.MockFileDownloader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

/**
 * @author chandresh.mishra
 */
@ExtendWith(SpringExtension.class)
public class TestProcessBuilder {

    @Test
    public void test() throws IOException {
        MockFileDownloader mockFileDownloader = new MockFileDownloader("D:/Data/Study/spring-batch", "testbucket");
        mockFileDownloader.downloadFile();
    }
}
