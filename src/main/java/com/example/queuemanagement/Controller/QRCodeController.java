package com.example.queuemanagement.Controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Controller dedicated to generating QR codes.
 */
@RestController
@RequestMapping("/api/qr")
public class QRCodeController {

    /**
     * Generates a QR code image from a given URL.
     * @param url The URL to encode into the QR code.
     * @return A PNG image of the QR code.
     */
    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@RequestParam("url") String url) {
        try {
            // Use ZXing to generate the QR code matrix
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 250, 250);

            // Write the matrix to a byte array output stream as a PNG
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();

            // Return the byte array as a successful response
            return ResponseEntity.ok(pngData);

        } catch (WriterException | IOException e) {
            // In case of an error, log it and return a server error status
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}