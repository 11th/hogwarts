package ru.hogwarts.school.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@Entity
public class Avatar {
    @Id
    @GeneratedValue
    private long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    @Lob
    @JdbcTypeCode(Types.BINARY)
    private byte[] data;
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public void setId(long id) {
        this.id = id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public Student getStudent() {
        return student;
    }
}
