package com.invoicekit.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class QrCodeUtil {

    public static byte[] generateQrCode(String text) {
        try {
            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix bitMatrix = writer.encode(
                    text,
                    BarcodeFormat.QR_CODE,
                    200,
                    200
            );

            BufferedImage image = new BufferedImage(
                    200,
                    200,
                    BufferedImage.TYPE_INT_RGB
            );

            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    image.setRGB(
                            x,
                            y,
                            bitMatrix.get(x, y)
                                    ? 0x000000
                                    : 0xFFFFFF
                    );
                }
            }

            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();

            ImageIO.write(image, "png", baos);

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("QR generation failed");
        }
    }
}