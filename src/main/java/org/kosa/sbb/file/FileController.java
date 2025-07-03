package org.kosa.sbb.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.kosa.sbb.answer.AnswerForm;
import org.kosa.sbb.user.SiteUser;
import org.kosa.sbb.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.kosa.sbb.file.FileTokenService;
import org.kosa.sbb.file.FileUploadService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileController {

	private final FileTokenService fileTokenService;
	private final FileUploadService fileUploadService;

	@PostMapping(value = "/imageUpload")
	@ResponseBody
	public Map<String, Object> image(MultipartHttpServletRequest request) throws Exception {

		// ckeditor는 이미지 업로드 후 이미지 표시하기 위해 uploaded 와 url을 json 형식으로 받아야 함
		// ckeditor 에서 파일을 보낼 때 upload : [파일] 형식으로 해서 넘어옴, upload라는 키 이용하여 파일을 저장 한다
		MultipartFile file = request.getFile("upload");
		String token = request.getParameter("token");

		System.out.println("token = " + token);

		// 이미지 첨부 파일을 저장한다
		int file_id = fileUploadService.fileUploadProcess(token, file);

		// 이미지를 현재 경로와 연관된 파일에 저장하기 위해 현재 경로를 알아냄
		String uploadPath = request.getServletContext().getContextPath() + "/files/" + file_id;

		Map<String, Object> result = new HashMap<>();
		result.put("uploaded", true); // 업로드 완료
		result.put("url", uploadPath); // 업로드 파일의 경로

		return result;
	}

	// 첨부파일을 다운로드
	@GetMapping("/files/{fileNo}")
	public void downloadFile(@PathVariable("fileNo") int fileNo, HttpServletResponse response) throws Exception {
		OutputStream out = response.getOutputStream();

		FileUpload fileUploadVO = fileUploadService.findById(fileNo);

		if (fileUploadVO == null) {
			response.setStatus(404);
		} else {

			String originName = fileUploadVO.getOriginalFilename();
			originName = URLEncoder.encode(originName, "UTF-8");
			// 다운로드 할 때 헤더 설정
			response.setHeader("Cache-Control", "no-cache");
			response.addHeader("Content-disposition", "attachment; fileName=" + originName);
			response.setContentLength((int) fileUploadVO.getSize());
			response.setContentType(fileUploadVO.getContentType());

			// 파일을 바이너리로 바꿔서 담아 놓고 responseOutputStream에 담아서 보낸다.
			FileInputStream input = new FileInputStream(new File(fileUploadVO.getRealFilename()));

			// outputStream에 8k씩 전달
			byte[] buffer = new byte[1024 * 8];

			while (true) {
				int count = input.read(buffer);
				if (count < 0)
					break;
				out.write(buffer, 0, count);
			}
			input.close();
			out.close();
		}
	}

}
