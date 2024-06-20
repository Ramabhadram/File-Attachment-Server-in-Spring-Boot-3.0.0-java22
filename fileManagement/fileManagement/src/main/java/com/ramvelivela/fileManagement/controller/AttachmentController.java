package com.ramvelivela.fileManagement.controller;

import com.ramvelivela.fileManagement.ResponseData;
import com.ramvelivela.fileManagement.entity.Attachment;
import com.ramvelivela.fileManagement.service.AttachmentService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import static java.util.Base64.*;

@RestController
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {

        this.attachmentService = attachmentService;
    }
    @Autowired
    HttpServletResponse response;

    @PostMapping("/upload")
    public ResponseData uploadFile(@RequestParam("file")MultipartFile file) throws Exception {
        Attachment attachment = attachmentService.saveAttachment(file);
        String downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                             .path("/download/")
                             .path(String.valueOf(attachment.getId()))
                             .toUriString();

        return new ResponseData(attachment.getFileName(),
                downloadURl,
                file.getContentType(),
                file.getSize());
    }

    @PostMapping(value = "/upload2", consumes = "multipart/form-data")
    public ResponseData uploadFile2(@RequestParam("file")MultipartFile file) throws Exception {
        Attachment attachment = attachmentService.saveAttachment(file);
        String downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/download/")
                .path(String.valueOf(attachment.getId()))
                .toUriString();

        return new ResponseData(attachment.getFileName(),
                downloadURl,
                file.getContentType(),
                file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Attachment attachment = attachmentService.getAttachment(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }

        @ResponseBody
        @GetMapping("/getList")
        public  List<ResponseData>  getList() throws Exception {
            return attachmentService.getAttachmentslist();

    }


    @GetMapping("/getFileBinary/{id}")
    public String getFile(@PathVariable("id") String fileId){
        try {
            Attachment file = attachmentService.getAttachment(fileId);
            byte[] content = file.getData();
//            response.reset();
//            response.setContentType(file.getFileType());
//            response.setHeader("Content-Disposition", "attachment;filename="+file.getFileName());
//            BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());
//            outStream.write(content);
//            outStream.close();

//            return "File retrieved successfully";
            return getMimeEncoder().encodeToString(content);
        }
        catch(Exception e){
            return "Could not retrieve the file";
        }
    }


}
