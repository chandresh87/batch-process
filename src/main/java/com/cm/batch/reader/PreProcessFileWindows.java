package com.cm.batch.reader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * @author chandresh.mishra
 */
@Component
@Profile("dev")
public class PreProcessFileWindows implements PreProcessFile {

    private final Logger logger = LoggerFactory.getLogger(PreProcessFileWindows.class);
    @Value("${windows.bash.path:C:\\Program Files\\Git\\git-bash.exe}")
    private String bashPath;

    @Override
    public ExitStatus processFile(String fileName, String workingDir) throws InterruptedException, IOException {

        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        logger.info("OS windows {} :", isWindows);
        String command = String.format("tail -1 %s > input_footer.txt && head -n -1 %s > input_body.txt", fileName, fileName);
        logger.info("command {} and working dir{} :", command, workingDir);
        ProcessBuilder builder = new ProcessBuilder();

        builder.command(bashPath, "-c", command);
        builder.directory(new File(workingDir));
        Process process = builder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        ExitStatus status = exitCode == 0 ? ExitStatus.COMPLETED : ExitStatus.FAILED;

        return status;
    }


    private static class StreamGobbler implements Runnable {
        private final InputStream inputStream;
        private final Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }
}
