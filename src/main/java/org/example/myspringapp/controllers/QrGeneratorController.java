package org.example.myspringapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.example.myspringapp.models.ApiResponse;
import org.example.myspringapp.models.QrRequest;
import org.example.myspringapp.models.QR;
import org.example.myspringapp.services.QRService;
import org.example.myspringapp.utils.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@RestController
@RequestMapping("/api/qr")
public class QrGeneratorController {

    private static final Logger logger = LoggerFactory.getLogger(QrGeneratorController.class);

    @Autowired
    private QRService qrService;

    @Operation(
        summary = "Generate QR code as JPEG",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping(value = "/generate", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> generateQr(
            @RequestBody QrRequest qrRequest) {
        logger.info("QR generation requested for: {}", qrRequest);
        try {
            byte[] imageBytes = qrService.generateQR(qrRequest);
            logger.info("QR code generated successfully");
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } catch (Exception e) {
            logger.error("QR code generation failed", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "List all QR codes for the authenticated user",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/list-my-qrs")
    public ResponseEntity<ApiResponse<List<QR>>> listAllQrs() {
        logger.info("Listing all QRs for authenticated user");
        List<QR> qrs = qrService.listAllQrs();
        logger.info("Found {} QRs", qrs.size());
        return ResponseEntity.ok(new ApiResponse<>(ApiConstants.API_VERSION, qrs));
    }

}
