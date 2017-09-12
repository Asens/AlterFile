package cn.asens.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Asens
 * create 2017-09-09 11:48
 **/
@Entity
@Table(name = "project_file")
public class ProjectFile {
    public final static Integer STATUS_DEFAULT=0;
    public final static Integer STATUS_ADD=1;
    public final static Integer STATUS_MODIFY=2;
    public final static Integer STATUS_DELETE=3;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "absolute_path")
    private String absolutePath;

    @Column(name = "last_modify")
    private Long lastModify;

    @Column
    private Integer status;

    @Column(name="project_id")
    private Integer projectId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Long getLastModify() {
        return lastModify;
    }

    public void setLastModify(Long lastModify) {
        this.lastModify = lastModify;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
