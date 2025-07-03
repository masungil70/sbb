package org.kosa.sbb.file;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "file_upload")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUpload {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int fileId;

	private String token;

	@Column(name = "original_filename")
	private String originalFilename;

	@Column(name = "real_filename")
	private String realFilename;

	@Column(name = "content_type")
	private String contentType;

	private long size;

}
