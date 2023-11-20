package com.smart.watchboard.common.support;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Service
@Transactional
@RequiredArgsConstructor
public class AudioConcatenator {

    private final AwsS3Uploader awsS3Uploader;

    public void concatenateAudioFiles(MultipartFile multipartFile1, Long documentId, Long fileId) throws IOException {
// 오디오 파일 처음 만드는거면 파일을 먼저 생성해야함, 파일 존재하면 파일 아이디도 있다.
        // Read the data from the first MultipartFile
        byte[] data1 = multipartFile1.getBytes();

        // Read the data from the second MultipartFile
        //byte[] data2 = multipartFile2.getBytes();
        String outputPath = "/Users/kms/Downloads/output.wav";
        String fileName = "output.wav"; // 파일 이름
        String contentType = "audio/wav"; // 컨텐츠 타입

        // s3업로드 이어붙인 후 어떻게? multipartfile로 바꿔야 하나
        if (fileId != null) {
            byte[] data2 = awsS3Uploader.getFileContent(fileId);
            // Merge the data into a single byte array
            byte[] mergedData = mergeWavData(data2, data1);
            MultipartFile multipartFile = convertByteArrayToMultipartFile(mergedData, fileName, contentType);
            //awsS3Uploader.uploadFile(multipartFile, documentId, fileId);
            writeWavFile(outputPath, mergedData);
        } else {
            //awsS3Uploader.uploadFile(multipartFile1, documentId, fileId);
            writeWavFile(outputPath, data1);
        }

//        // Merge the data into a single byte array
//        byte[] mergedData = mergeWavData(data1, data2);
//
//        // Create a new WAV file and write the combined data to it
//        String outputPath = "/Users/kms/Downloads/output.wav";
//        writeWavFile(outputPath, mergedData);
    }

    private byte[] mergeWavData(byte[] data1, byte[] data2) {
        // Calculate the total length of the merged WAV file
        int totalLength = data1.length + data2.length - 44; // Subtract 44 bytes for the header

        // Create a byte array for the merged WAV data
        byte[] mergedData = new byte[totalLength];

        // Copy the first WAV data (excluding the header)
        System.arraycopy(data1, 44, mergedData, 0, data1.length - 44);

        // Copy the second WAV data (excluding the header)
        System.arraycopy(data2, 44, mergedData, data1.length - 44, data2.length - 44);

        return mergedData;
    }

    private void writeWavFile(String outputFilePath, byte[] mergedData) throws IOException {
        // Create a new WAV file
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            // Write the WAV file header
            byte[] header = createWavHeader(mergedData.length);
            fos.write(header);

            // Write the merged WAV data (excluding the header)
            fos.write(mergedData);
        }
    }

    private byte[] createWavHeader(int dataLength) {
        byte[] header = new byte[44];

        // WAV header constants
        String riffHeader = "RIFF";
        String waveHeader = "WAVEfmt ";
        int headerSize = 16;
        short audioFormat = 1; // PCM
        short numChannels = 1; // Mono
        int sampleRate = 44100; // 44.1 kHz
        int bitsPerSample = 16; // Bits per sample

        // Calculate the file size excluding the first 8 bytes (RIFF and file size)
        int fileSize = dataLength + 36;

        // Fill in the header
        System.arraycopy(riffHeader.getBytes(), 0, header, 0, 4);
        ByteBuffer.wrap(header, 4, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(fileSize);
        System.arraycopy(waveHeader.getBytes(), 0, header, 8, 8);
        ByteBuffer.wrap(header, 16, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(headerSize);
        ByteBuffer.wrap(header, 20, 2).order(ByteOrder.LITTLE_ENDIAN).putShort(audioFormat);
        ByteBuffer.wrap(header, 22, 2).order(ByteOrder.LITTLE_ENDIAN).putShort(numChannels);
        ByteBuffer.wrap(header, 24, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(sampleRate);
        ByteBuffer.wrap(header, 28, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(sampleRate * numChannels * bitsPerSample / 8);
        ByteBuffer.wrap(header, 32, 2).order(ByteOrder.LITTLE_ENDIAN).putShort((short)(numChannels * bitsPerSample / 8));
        ByteBuffer.wrap(header, 34, 2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) bitsPerSample);
        System.arraycopy("data".getBytes(), 0, header, 36, 4);
        ByteBuffer.wrap(header, 40, 4).order(ByteOrder.LITTLE_ENDIAN).putInt(dataLength);

        return header;
    }

    public MultipartFile convertByteArrayToMultipartFile(byte[] byteArray, String fileName, String contentType) {
        return new MockMultipartFile(fileName, fileName, contentType, byteArray);
    }
}
