package com.x2bee.api.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.x2bee.common.base.upload.AttacheFileKind;
import com.x2bee.common.base.upload.UploadReqDto;
import com.x2bee.common.base.upload.UploadResDto;
import com.x2bee.common.base.upload.Uploader;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class VideoUploaderTest {
	@Autowired
	private Uploader videoUploader;
	
	
	@SuppressWarnings("unchecked")
	@Test
	void upload() {
		MockMultipartFile file = new MockMultipartFile(
	        "file", 
	        "hello.txt", 
	        MediaType.TEXT_PLAIN_VALUE, 
	        "Hello, World!".getBytes()
	    );
		
		// 파일업로드 파라미터
		UploadReqDto uploadReqDto = new UploadReqDto();
        uploadReqDto.setAttacheFileKind(AttacheFileKind.SYSTEM);
        uploadReqDto.setTempPathYn(false);

        // 업로드
        Map<String, Object> retMap = videoUploader.upload(file, uploadReqDto);

		Assertions.assertEquals(retMap.get("cd"), "00");
		
		// 파일업로드 결과
		if ("00".equals(retMap.get("cd")) ) { // S3 정상 업로드인경우
			UploadResDto uploadResDto = (UploadResDto)((Map<String, Object>)retMap.get("data")).get("data");

			Map<String,String> fileInfo = new HashMap<>();
			fileInfo.put("I_FILE_TITLE", uploadResDto.getOrgFileName()); //사용
			fileInfo.put("I_FILE_NM", uploadResDto.getFileName()); //사용안함
			fileInfo.put("I_FILE_URL", uploadResDto.getFullPath());//사용
			fileInfo.put("I_FILE_TEMP_URL", uploadResDto.getTempFullPath());//사용
			fileInfo.put("I_FILE_PATH", uploadResDto.getPath());//사용안함
			fileInfo.put("I_FILE_SIZE", uploadResDto.getSize().toString());
			fileInfo.put("I_FILE_EXT", uploadResDto.getExtension());

			log.debug("[I_FILE_TITLE]"+fileInfo.get("I_FILE_TITLE"));	//원본 파일명
			log.debug("[I_FILE_NM]"+fileInfo.get("I_FILE_NM"));			//파일명
			log.debug("[I_FILE_URL]"+fileInfo.get("I_FILE_URL"));		//파일URL
			log.debug("[I_FILE_PATH]"+fileInfo.get("I_FILE_PATH"));		//파일경로
			log.debug("[I_FILE_SIZE]"+fileInfo.get("I_FILE_SIZE"));		//파일사이즈
			log.debug("[I_FILE_EXT]"+fileInfo.get("I_FILE_EXT"));		//파일확장자
			log.debug("[I_FILE_TEMP_URL]"+fileInfo.get("I_FILE_TEMP_URL"));		//임시파일URL
			
		}

	}
	
	@Test
	void downloadFile() throws IOException {
		String fullPath = "static/resources-api/upload_root/system/2021/12/07/d3e167ca-7f57-4800-ba1f-4d713fd01a15.txt";
		String originalFileName = "hello.txt";

		ResponseEntity<byte[]> response = videoUploader.downloadFile(fullPath, originalFileName);
		
		Assertions.assertNotNull(response);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	void deleteFile() {
		// 파일업로드
		MockMultipartFile file = new MockMultipartFile(
	        "file", 
	        "hello.txt", 
	        MediaType.TEXT_PLAIN_VALUE, 
	        "Hello, World! To be deleted.".getBytes()
	    );
		UploadReqDto uploadReqDto = new UploadReqDto();
        uploadReqDto.setAttacheFileKind(AttacheFileKind.SYSTEM);
        uploadReqDto.setTempPathYn(false);
        // 업로드
        Map<String, Object> retMap = videoUploader.upload(file, uploadReqDto);

        Assertions.assertEquals(retMap.get("cd"), "00");

        // update 된 파일 Key값
		UploadResDto uploadResDto = (UploadResDto)((Map<String, Object>)retMap.get("data")).get("data");
    	String uploadFileUrl = uploadResDto.getFullPath();

    	log.debug("uploadFileUrl: {}", uploadFileUrl);
    	
    	videoUploader.deleteFile(Arrays.asList(uploadFileUrl));

	}
	
}
