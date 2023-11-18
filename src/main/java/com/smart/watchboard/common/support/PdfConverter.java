package com.smart.watchboard.common.support;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PdfConverter {
    public static File convertStringToPdf(String content, String fileName) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            BaseFont objBaseFont = BaseFont.createFont("src/main/resources/templates/NanumGothic.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font objFont = new Font(objBaseFont, 12);
            //Font koreanFont = FontFactory.getFont("NanumGothic", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            document.add(new Paragraph(content, objFont));
        } finally {
            document.close();
        }

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();
        return saveByteArrayToFile(pdfBytes, fileName);
    }

    public static File saveByteArrayToFile(byte[] bytes, String fileName) throws IOException {
        File outputFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(bytes);
        }
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
