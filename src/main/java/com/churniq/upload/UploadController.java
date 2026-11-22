
package com.churniq.upload;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/datasets")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping(
        value = "/{id}/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<UploadResponse> uploadCSV(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        UploadResponse response = uploadService.upload(id, file);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
