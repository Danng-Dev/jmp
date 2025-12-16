package top.archiem.jmp.util.modrinth;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

class Files {
    private final Hashes hashes;
    private final String url;
    private final String filename;
    private final boolean primary;
    private final int size;
    @SerializedName("file_type")
    private final String fileType;

    public Files(Hashes hashes, String url, String filename, boolean primary, int size, String fileType) {
        this.hashes = hashes != null ? hashes : new Hashes();
        this.url = url;
        this.filename = filename;
        this.primary = primary;
        this.size = size;
        this.fileType = fileType;
    }

    public Hashes getHashes() {
        return hashes;
    }

    public String getUrl() {
        return url;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isPrimary() {
        return primary;
    }

    public int getSize() {
        return size;
    }

    public String getFileType() {
        return fileType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Files files = (Files) o;
        return primary == files.primary && size == files.size &&
                Objects.equals(hashes, files.hashes) &&
                Objects.equals(url, files.url) &&
                Objects.equals(filename, files.filename) &&
                Objects.equals(fileType, files.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashes, url, filename, primary, size, fileType);
    }

    @Override
    public String toString() {
        return "Files{" +
                "hashes=" + hashes +
                ", url='" + url + '\'' +
                ", filename='" + filename + '\'' +
                ", primary=" + primary +
                ", size=" + size +
                ", fileType='" + fileType + '\'' +
                '}';
    }
}
