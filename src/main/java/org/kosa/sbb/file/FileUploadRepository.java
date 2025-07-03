package org.kosa.sbb.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileUploadRepository extends JpaRepository<FileUpload, Integer> {
    List<FileUpload> findByToken(String token);

    void deleteByFileIdIn(List<Integer> fileIds);

    List<FileUpload> findByTokenIn(List<String> tokens);

    void deleteByTokenIn(List<String> tokens);
}