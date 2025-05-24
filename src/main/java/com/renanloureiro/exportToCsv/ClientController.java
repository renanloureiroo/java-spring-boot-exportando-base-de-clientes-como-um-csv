package com.renanloureiro.exportToCsv;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clientes", description = "Operações relacionadas a clientes")
public class ClientController {
    @Autowired
    private ExportClientsToCsvFileUseCase exportClientsToCsvFileUseCase;


    @Operation(summary = "Exportar clientes para CSV", description = "Exporta todos os clientes cadastrados em um arquivo CSV para download.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arquivo CSV gerado com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro ao gerar o arquivo CSV")
    })
    @GetMapping(value = "/export", produces = "text/csv")
    public ResponseEntity<InputStreamResource> exportClientsToCsv() throws IOException {
        InputStream inputStream = exportClientsToCsvFileUseCase.exportToCsvAsInputStream();
        String fileName = generateFileName("");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(inputStream));
    }

    @Operation(summary = "Exportar clientes para CSV (streaming)", description = "Exporta todos os clientes cadastrados em um arquivo CSV para download, usando streaming direto.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arquivo CSV gerado com sucesso (streaming)"),
        @ApiResponse(responseCode = "500", description = "Erro ao gerar o arquivo CSV")
    })
    @GetMapping(value = "/export-stream", produces = "text/csv")
    public ResponseEntity<StreamingResponseBody> exportClientsToCsvStreaming() {
        String fileName = generateFileName("-stream");
        StreamingResponseBody responseBody = outputStream -> {
            exportClientsToCsvFileUseCase.exportToCsvStreaming(outputStream);
        };
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(responseBody);
    }

    private String generateFileName(String suffix) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "export-clients-" + timestamp + suffix + ".csv";
    }
} 