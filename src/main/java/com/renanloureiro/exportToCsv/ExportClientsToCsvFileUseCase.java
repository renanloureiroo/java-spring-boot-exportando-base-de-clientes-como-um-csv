package com.renanloureiro.exportToCsv;

import com.opencsv.CSVWriter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.*;
import java.sql.SQLException;
import java.util.stream.Stream;

@Service
public class ExportClientsToCsvFileUseCase {
    @Autowired
    private ClientRepository clientRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String TEMP_DIR = "/app/temp";
    private static final int FETCH_SIZE = 1000;
    private static final Logger logger = LoggerFactory.getLogger(ExportClientsToCsvFileUseCase.class);

    @Transactional(readOnly = true)
    public InputStream exportToCsvAsInputStream() throws IOException {
        String filePath = getFilePath();
        try (Writer fileWriter = new FileWriter(filePath)) {
            writeClientsToCsv(fileWriter);
        }
        return new FileInputStream(filePath) {
            @Override
            public void close() throws IOException {
                super.close();
                File tempFile = new File(filePath);
                if (!tempFile.delete()) {
                    logger.warn("Falha ao deletar arquivo temporário: {}", filePath);
                }
            }
        };
    }

    @Transactional(readOnly = true)
    public void exportToCsvStreaming(OutputStream outputStream) throws IOException {
        try (Writer writer = new OutputStreamWriter(outputStream)) {
            writeClientsToCsv(writer);
        }
    }

    private void writeClientsToCsv(Writer writer) throws IOException {
        try (CSVWriter csvWriter = new CSVWriter(
                writer,
                ';',
                CSVWriter.DEFAULT_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            csvWriter.writeNext(new String[]{"id", "name", "email"}, false);
            setFetchSize(FETCH_SIZE);
            try (Stream<Client> clientStream = clientRepository.streamAll()) {
                clientStream.forEach(client ->
                        csvWriter.writeNext(new String[]{
                                String.valueOf(client.getId()),
                                client.getName(),
                                client.getEmail()
                        }, false)
                );
            }
        }
    }

    private void setFetchSize(int fetchSize) {
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            try {
                connection.setAutoCommit(false);
                connection.createStatement().setFetchSize(fetchSize);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getFilePath() {
        String timestamp = String.valueOf(java.time.Instant.now().toEpochMilli());
        String directory = getDirectory();
        return directory + "/export-clients-" + timestamp + ".csv";
    }

    private String getDirectory() {
        File dir = new File(TEMP_DIR);
        if (!dir.exists()) dir.mkdirs();
        return dir.getAbsolutePath();
    }
} 