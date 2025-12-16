package top.archiem.jmp.util.modrinth;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

class ModrinthVersion {
    @SerializedName("game_versions")
    private final List<String> gameVersions;
    private final List<String> loaders;
    private final String id;
    @SerializedName("project_id")
    private final String projectId;
    @SerializedName("author_id")
    private final String authorId;
    private final boolean featured;
    private final String name;
    @SerializedName("version_number")
    private final String versionNumberRaw; // Store raw string, convert to SemanticVersion on demand
    private final String changelog;
    @SerializedName("changelog_url")
    private final String changelogUrl;
    @SerializedName("date_published")
    private final Date datePublished;
    private final int downloads;
    @SerializedName("version_type")
    private final String versionType;
    private final String status;
    @SerializedName("requested_status")
    private final String requestedStatus;
    private final List<Files> files;
    private final List<Dependency> dependencies;

    // Cached SemanticVersion for performance
    private transient SemanticVersion semanticVersion;

    public ModrinthVersion(List<String> gameVersions, List<String> loaders, String id, String projectId, String authorId, boolean featured, String name, String versionNumberRaw, String changelog, String changelogUrl, Date datePublished, int downloads, String versionType, String status, String requestedStatus, List<Files> files, List<Dependency> dependencies) {
        this.gameVersions = gameVersions != null ? Collections.unmodifiableList(gameVersions) : Collections.emptyList();
        this.loaders = loaders != null ? Collections.unmodifiableList(loaders) : Collections.emptyList();
        this.id = id;
        this.projectId = projectId;
        this.authorId = authorId;
        this.featured = featured;
        this.name = name;
        this.versionNumberRaw = versionNumberRaw;
        this.changelog = changelog;
        this.changelogUrl = changelogUrl;
        this.datePublished = datePublished != null ? (Date) datePublished.clone() : new Date(0); // Defensive copy
        this.downloads = downloads;
        this.versionType = versionType;
        this.status = status;
        this.requestedStatus = requestedStatus;
        this.files = files != null ? Collections.unmodifiableList(files) : Collections.emptyList();
        this.dependencies = dependencies != null ? Collections.unmodifiableList(dependencies) : Collections.emptyList();
    }

    public List<String> getGameVersions() {
        return gameVersions;
    }

    public List<String> getLoaders() {
        return loaders;
    }

    public String getId() {
        return id;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public boolean isFeatured() {
        return featured;
    }

    public String getName() {
        return name;
    }

    public SemanticVersion getVersionNumber() {
        if (semanticVersion == null) {
            semanticVersion = SemanticVersion.fromString(versionNumberRaw);
        }
        return semanticVersion;
    }

    public String getChangelog() {
        return changelog;
    }

    public String getChangelogUrl() {
        return changelogUrl;
    }

    public Date getDatePublished() {
        return (Date) datePublished.clone(); // Defensive copy
    }

    public int getDownloads() {
        return downloads;
    }

    public String getVersionType() {
        return versionType;
    }

    public String getStatus() {
        return status;
    }

    public String getRequestedStatus() {
        return requestedStatus;
    }

    public List<Files> getFiles() {
        return files;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public String getUrl() {
        return "https://modrinth.com/plugin/" + projectId + "/version/" + id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModrinthVersion that = (ModrinthVersion) o;
        return featured == that.featured && downloads == that.downloads &&
                Objects.equals(gameVersions, that.gameVersions) &&
                Objects.equals(loaders, that.loaders) &&
                Objects.equals(id, that.id) &&
                Objects.equals(projectId, that.projectId) &&
                Objects.equals(authorId, that.authorId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(versionNumberRaw, that.versionNumberRaw) &&
                Objects.equals(changelog, that.changelog) &&
                Objects.equals(changelogUrl, that.changelogUrl) &&
                Objects.equals(datePublished, that.datePublished) &&
                Objects.equals(versionType, that.versionType) &&
                Objects.equals(status, that.status) &&
                Objects.equals(requestedStatus, that.requestedStatus) &&
                Objects.equals(files, that.files) &&
                Objects.equals(dependencies, that.dependencies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameVersions, loaders, id, projectId, authorId, featured, name, versionNumberRaw,
                changelog, changelogUrl, datePublished, downloads, versionType, status, requestedStatus, files, dependencies);
    }

    @Override
    public String toString() {
        return "ModrinthVersion{" +
                "gameVersions=" + gameVersions +
                ", loaders=" + loaders +
                ", id='" + id + '\'' +
                ", projectId='" + projectId + '\'' +
                ", authorId='" + authorId + '\'' +
                ", featured=" + featured +
                ", name='" + name + '\'' +
                ", versionNumberRaw='" + versionNumberRaw + '\'' +
                ", changelog='" + changelog + '\'' +
                ", changelogUrl='" + changelogUrl + '\'' +
                ", datePublished=" + datePublished +
                ", downloads=" + downloads +
                ", versionType='" + versionType + '\'' +
                ", status='" + status + '\'' +
                ", requestedStatus='" + requestedStatus + '\'' +
                ", files=" + files +
                ", dependencies=" + dependencies +
                '}';
    }
}
