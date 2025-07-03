package org.kosa.sbb.file;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileTokenService {

    private final FileTokenRepository fileTokenRepository;

    @Transactional
    public String getToken() {
        final String token = UUID.randomUUID().toString();

        FileToken fileToken = FileToken.builder()
                .token(token)
                .status(0)
                .makeDate(LocalDateTime.now())
                .build();

        fileTokenRepository.save(fileToken);

        return token;
    }

    @Transactional
    public int updateUseStatus(String token) {
        return fileTokenRepository.findByToken(token)
                .map(fileToken -> {
                    fileToken.setStatus(1); // 사용 완료 처리
                    return 1;
                })
                .orElse(0); // 존재하지 않으면 0 반환
    }
}