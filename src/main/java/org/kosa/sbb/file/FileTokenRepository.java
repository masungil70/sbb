package org.kosa.sbb.file;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileTokenRepository extends JpaRepository<FileToken, Long> {

    // 특정 토큰 삭제
    void deleteByToken(String token);

    // 토큰 리스트 삭제 (in절)
    void deleteByTokenIn(List<String> tokens);

    // 조건에 따른 토큰 조회
    @Query("SELECT f FROM FileToken f WHERE f.status = 0 AND f.makeDate < :time")
    List<FileToken> findTokensBeforeTime(@Param("time") LocalDateTime time);

    // 토큰으로 조회
    Optional<FileToken> findByToken(String token);
}