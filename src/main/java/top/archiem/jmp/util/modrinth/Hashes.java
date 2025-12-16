package top.archiem.jmp.util.modrinth;

import java.util.Objects;

class Hashes {
    private final String sha512;
    private final String sha1;

    public Hashes(String sha512, String sha1) {
        this.sha512 = sha512;
        this.sha1 = sha1;
    }

    public Hashes() { // Default constructor for Gson
        this("", "");
    }

    public String getSha512() {
        return sha512;
    }

    public String getSha1() {
        return sha1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hashes hashes = (Hashes) o;
        return Objects.equals(sha512, hashes.sha512) &&
                Objects.equals(sha1, hashes.sha1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sha512, sha1);
    }

    @Override
    public String toString() {
        return "Hashes{" +
                "sha512='" + sha512 + '\'' +
                ", sha1='" + sha1 + '\'' +
                '}';
    }
}
