package com.renanloureiro.exportToCsv;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c FROM Client c")
    Stream<Client> streamAll();
} 