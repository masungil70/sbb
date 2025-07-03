package org.kosa.sbb.file;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class FileToken {

	@Id
	@Column(nullable = false, unique = true)
	private String token;

	@Builder.Default
	private Integer status = 0;

	@Column(name = "make_date")
	private LocalDateTime makeDate;

}
