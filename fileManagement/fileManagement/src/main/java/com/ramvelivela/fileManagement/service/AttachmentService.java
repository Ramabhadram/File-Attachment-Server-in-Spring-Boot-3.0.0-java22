package com.ramvelivela.fileManagement.service;

import com.ramvelivela.fileManagement.ResponseData;
import com.ramvelivela.fileManagement.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    Attachment saveAttachment(MultipartFile file) throws Exception;

    Attachment getAttachment(String fileId) throws Exception;

    List<ResponseData> getAttachmentslist() throws Exception;
}
