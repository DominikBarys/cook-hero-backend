package com.barysdominik.fileservice.service;

import com.barysdominik.fileservice.entity.image.Image;
import com.barysdominik.fileservice.exception.FtpException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FtpService {

    @Value("${ftp.server}")
    private String FTP_SERVER;
    @Value("${ftp.username}")
    private String FTP_USERNAME;
    @Value("${ftp.password}")
    private String FTP_PASSWORD;
    @Value("${ftp.origin}")
    private String FTP_ORIGIN_DIRECTORY;
    @Value("${ftp.port}")
    private int FTP_PORT;

    @PostConstruct
    public void initFtpServer() {
        try {
            FTPClient ftpClient = getFtpConnection();
            ftpClient.setConnectTimeout(5000);
            createDirectories(ftpClient);
            ftpClient.logout();
            ftpClient.disconnect();
        } catch (IOException e) {
            throw new FtpException("Something went wrong while initializing the ftp server");
        }
    }

    public void createDirectories(FTPClient ftpClient) throws IOException {
        List<String> directoriesToCreate = Arrays.stream(FTP_ORIGIN_DIRECTORY.split("/"))
                .toList().stream()
                .filter(name -> name != "")
                .toList();
        String parentDirectoryInFTP = "/";
        List<String> filesInParentDirectory;
        String directoryPath = "";

        for (int i = 0; i < directoriesToCreate.size(); i++) {
            filesInParentDirectory = Arrays.stream(ftpClient.listNames(parentDirectoryInFTP)).toList();
            directoryPath += directoriesToCreate.get(i);
            if (!filesInParentDirectory.contains(directoryPath)) {
                ftpClient.makeDirectory(directoryPath);
            }
            directoryPath += "/";
        }
    }

    public ResponseEntity<?> saveImage(MultipartFile file) {
        try {
            if (file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).equals("png")) {
                uploadFileToFtp(file);
                return ResponseEntity.ok().body("DZIALA, SUKCES");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MediaType not supported");
        } catch (IOException ioException) {
            log.error("IOException: {}", ioException.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File dont exist");
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cannot save file");
        }
    }

    private FTPClient getFtpConnection() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(10000);
        ftpClient.setControlKeepAliveTimeout(300);
        ftpClient.connect(FTP_SERVER, FTP_PORT);
        ftpClient.login(FTP_USERNAME, FTP_PASSWORD);

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }

    public Image uploadFileToFtp(MultipartFile file) throws IOException, FtpException {
        FTPClient ftpClient = getFtpConnection();
        LocalDate localDate = LocalDate.now();
        String remoteFilePath = FTP_ORIGIN_DIRECTORY + "/" + localDate + "/" + file.getOriginalFilename();
        boolean uploaded = streamFile(file, ftpClient, remoteFilePath);
        if (!uploaded) {
            createDateDirectory(ftpClient, localDate);
            if (!streamFile(file, ftpClient, remoteFilePath)) {
                throw new FtpException("Cannot connect to server");
            }
        }
        ftpClient.logout();
        ftpClient.disconnect();

        return Image.builder()
                .path(remoteFilePath)
                .shortId(UUID.randomUUID().toString().replace("-", "").substring(0, 12))
                .isUsed(false)
                .createdAt(LocalDate.now())
                .build();
    }

    private boolean streamFile(MultipartFile file, FTPClient ftpClient, String remoteFilePath) throws IOException {
        InputStream inputStream = file.getInputStream();
        boolean isUploaded = ftpClient.storeFile(remoteFilePath, inputStream);
        inputStream.close();
        return isUploaded;
    }

    private void createFolder(FTPClient client) throws IOException {
        client.makeDirectory(FTP_ORIGIN_DIRECTORY);
    }

    private void createDateDirectory(FTPClient ftpClient, LocalDate localDate) throws IOException {
        ftpClient.makeDirectory(FTP_ORIGIN_DIRECTORY + "/" + localDate);
    }

    public ByteArrayOutputStream getFile(Image image) throws IOException {
        FTPClient ftpClient = getFtpConnection();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean isDownloaded = ftpClient.retrieveFile(image.getPath(), outputStream);
        ftpClient.logout();
        ftpClient.disconnect();
        if (isDownloaded) {
            return outputStream;
        }
        throw new FtpException("An error has occurred while trying to download image with path: '" +
                image.getPath() + "' from ftp server");
    }

    public boolean deleteFile(String path) throws IOException {
        FTPClient ftpClient = getFtpConnection();
        boolean isDeleted = ftpClient.deleteFile(path);
        ftpClient.logout();
        ftpClient.disconnect();
        return isDeleted;
    }

}
