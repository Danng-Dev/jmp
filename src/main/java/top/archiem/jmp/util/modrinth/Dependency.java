package top.archiem.jmp.util.modrinth;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

class Dependency {
    @SerializedName("version_id")
    private final String versionId;
    @SerializedName("project_id")
    private final String projectId;
    @SerializedName("file_name")
    private final String fileName;
    @SerializedName("dependency_type")
    private final String dependencyType;

    public Dependency(String versionId, String projectId, String fileName, String dependencyType) {
        this.versionId = versionId;
        this.projectId = projectId;
        this.fileName = fileName;
        this.dependencyType = dependencyType;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDependencyType() {
        return dependencyType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(versionId, that.versionId) &&
                Objects.equals(projectId, that.projectId) &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(dependencyType, that.dependencyType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(versionId, projectId, fileName, dependencyType);
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "versionId='" + versionId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", dependencyType='" + dependencyType + '\'' +
                '}';
    }
}
