package com.smart.watchboard.common.support;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.web.multipart.MultipartFile;

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

    public static Integer countPdfPage(MultipartFile pdfFile) throws IOException {
        byte[] bytes = pdfFile.getBytes();
        PdfReader reader = new PdfReader(bytes);

        // PDF 파일 전체 페이지 수 구하기
        int pageCount = reader.getNumberOfPages();

        return pageCount;
    }

    public static Boolean checkPageLimit(int page) {
        if (page > 30) {
            return false;
        }
        return true;
    }
}
