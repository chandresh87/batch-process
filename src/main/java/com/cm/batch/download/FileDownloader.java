package com.cm.batch.download;

import java.io.IOException;

/**
 * @author chandresh.mishra
 */
public interface FileDownloader {

    String downloadFile() throws IOException;
}
