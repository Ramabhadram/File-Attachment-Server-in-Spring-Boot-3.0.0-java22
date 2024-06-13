package com.ramvelivela.fileManagement.service;

import com.ramvelivela.fileManagement.ResponseData;
import com.ramvelivela.fileManagement.entity.Attachment;
import com.ramvelivela.fileManagement.repository.AttachmentRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class AttachmentServiceImpl implements AttachmentService{

    private final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
       String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       try {
            if(fileName.contains("..")) {
                throw  new Exception("Filename contains invalid path sequence "
                + fileName);
            }

            Attachment attachment = new Attachment(fileName,file.getContentType(),file.getBytes());
             return attachmentRepository.save(attachment);

       } catch (Exception e) {
           System.out.println(e.getMessage());
            throw new Exception("Could not save File: " + fileName);
       }
    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository
                .findById(fileId)
                .orElseThrow(
                        () -> new Exception("File not found with Id: " + fileId));
    }

    @Override
    public List<ResponseData> getAttachmentslist() throws Exception {
        return  attachmentRepository
                .findAll()
                .stream()
                .map(a -> new ResponseData(a.getFileName(),
                          ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(String.valueOf(a.getId())).toUriString(),
                          a.getFileType(),0))
                .toList();
               // .orElseThrow(() -> new Exception("not found "));
    }
}
