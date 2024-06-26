package ec.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.http.Part;

public class FileUploadManager {

    public static String saveUploadedFiles(String appPath, String saveDir, List<Part> fileParts) throws IOException {
        String savePath = appPath + File.separator + saveDir;
        String uploadedFilePath = null;

        for (Part filePart : fileParts) {
            String fileName = extractFileName(filePart);
            if (fileName != null && !fileName.isEmpty()) {
                File fileSaveDir = new File(savePath);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }
                filePart.write(savePath + File.separator + fileName);
                uploadedFilePath = saveDir + File.separator + fileName;
            }
        }

        return uploadedFilePath;
    }

    private static String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            }
        }
        return null;
    }
}
