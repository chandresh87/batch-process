package com.cm.batch.reader;

import org.springframework.batch.core.ExitStatus;

import java.io.IOException;

/**
 * @author chandresh.mishra
 */
public interface PreProcessFile {

    ExitStatus processFile(String fileName, String workingDir) throws InterruptedException, IOException;
}
