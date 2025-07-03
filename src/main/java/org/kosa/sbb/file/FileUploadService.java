package org.kosa.sbb.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    @Value("${spring.servlet.multipart.location}")
    private String CURR_IMAGE_REPO_PATH;

    private final FileUploadRepository fileUploadRepository;
    private final FileTokenRepository fileTokenRepository;
    private final ServletContext application;

    public int fileUploadProcess(String token, MultipartFile file) throws Exception {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(
                File.separator + "yyyy" + File.separator + "MM" + File.separator + "dd");

        String realFolder = format.format(now.getTime());
        File realPath = new File(CURR_IMAGE_REPO_PATH + realFolder);

        if (!realPath.exists())
            realPath.mkdirs();

        String fileNameReal = UUID.randomUUID().toString();
        File realFile = new File(realPath, fileNameReal);
        file.transferTo(realFile);

        FileUpload fileUpload = FileUpload.builder()
                .token(token)
                .originalFilename(file.getOriginalFilename())
                .realFilename(realFile.getAbsolutePath())
                .contentType(file.getContentType())
                .size(file.getSize())
                .build();

        return fileUploadRepository.save(fileUpload).getFileId();
    }

    public FileUpload findById(int fileId) {
        return fileUploadRepository.findById(fileId).orElse(null);
    }

    @Transactional
    public void updateUseStatus(Map<String, Object> param) {
        String token = (String) param.get("token");
        String editor = (String) param.get("editor");

        String filesURL = application.getContextPath() + "/files/";
        List<FileUpload> fileUploads = fileUploadRepository.findByToken(token);

        List<FileUpload> deleteImageList = fileUploads.stream()
                .filter(fileUpload -> !editor.contains(filesURL + fileUpload.getFileId()))
                .collect(Collectors.toList());

        // 실파일 삭제
        deleteImageList.forEach(fileUpload -> new File(fileUpload.getRealFilename()).delete());

        // 토큰 상태 수정
        fileTokenRepository.findByToken(token).ifPresent(t -> t.setStatus(1));

        // DB 삭제
        List<Integer> deleteIds = deleteImageList.stream()
                .map(FileUpload::getFileId)
                .collect(Collectors.toList());
        fileUploadRepository.deleteByFileIdIn(deleteIds);
    }
}