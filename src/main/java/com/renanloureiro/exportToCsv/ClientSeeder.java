package com.renanloureiro.exportToCsv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientSeeder implements CommandLineRunner {
    
    @Autowired
    private ClientRepository clientRepository;

    private static final int TOTAL_CLIENTS = 1_000_000;
    private static final int BATCH_SIZE = 10_000;
    private static final Logger logger = LoggerFactory.getLogger(ClientSeeder.class);

    @Override
    public void run(String... args) throws Exception {
        if (clientRepository.count() > 0) {
            logger.info("Seed já executado. Clientes já existem no banco.");
            return;
        }
        logger.info("Iniciando seed de clientes...");
        for (int i = 0; i < TOTAL_CLIENTS; i += BATCH_SIZE) {
            List<Client> batch = new ArrayList<>(BATCH_SIZE);
            for (int j = 0; j < BATCH_SIZE && (i + j) < TOTAL_CLIENTS; j++) {
                long id = i + j + 1;
                batch.add(new Client(null, "Cliente " + id, "cliente" + id + "@email.com"));
            }
            clientRepository.saveAll(batch);
            logger.info("Inseridos: {}", Math.min(i + BATCH_SIZE, TOTAL_CLIENTS));
        }
        logger.info("Seed de clientes finalizado!");
    }
} 