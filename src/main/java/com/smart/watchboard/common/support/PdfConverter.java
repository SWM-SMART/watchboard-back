package com.smart.watchboard.common.support;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfConverter {
    public static File convertStringToPdf(String content, String fileName) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            document.add(new Paragraph(content));
        } finally {
            document.close();
        }

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        return saveByteArrayToFile(pdfBytes, fileName);
    }

    public static File saveByteArrayToFile(byte[] bytes, String fileName) throws IOException {
        File outputFile = new File(fileName);
        System.out.println(outputFile.getName());
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(bytes);
        }
        System.out.println(outputFile);
        return outputFile;
    }
}
