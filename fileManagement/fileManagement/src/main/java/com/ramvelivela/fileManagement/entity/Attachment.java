package com.ramvelivela.fileManagement.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ext.SqlBlobSerializer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import org.hibernate.annotations.UuidGenerator;

@Entity
@Data
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private String id;

    private String fileName;
    private String fileType;

    @Lob
    @Column(columnDefinition="LONGBLOB")
    @JsonSerialize(using = SqlBlobSerializer.class)
    private byte[] data;

    public Attachment(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}
