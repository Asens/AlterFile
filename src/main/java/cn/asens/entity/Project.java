package cn.asens.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Asens
 * create 2017-09-09 12:10
 **/
@Entity
@Table(name = "project")
public class Project {
    public final static Integer  HAS_INITIALIZED=1;
    public final static Integer  NOT_INITIALIZED=0;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name="base_path")
    private String basePath;
    @Column(name="name")
    private String name;

    @Column
    private Integer initialized;

    @Column(name="exclude_path")
    private String excludePath;

    @OneToMany(targetEntity = Project.class,fetch = FetchType.LAZY)
    @JoinColumn(name="project_id")
    private Set<ProjectFile> fileSet;

    @Column(name="remote_path")
    private String remotePath;

    @Column(name="server_upload_path")
    private String serverUploadPath;

    private String tomcatUsername;

    private String tomcatPassword;

    private String reloadPath;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ProjectFile> getFileSet() {
        return fileSet;
    }

    public void setFileSet(Set<ProjectFile> fileSet) {
        this.fileSet = fileSet;
    }

    public Integer getInitialized() {
        return initialized;
    }

    public void setInitialized(Integer initialized) {
        this.initialized = initialized;
    }

    public String getExcludePath() {
        return excludePath;
    }

    public void setExcludePath(String excludePath) {
        this.excludePath = excludePath;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getServerUploadPath() {
        return serverUploadPath;
    }

    public void setServerUploadPath(String serverUploadPath) {
        this.serverUploadPath = serverUploadPath;
    }
}
