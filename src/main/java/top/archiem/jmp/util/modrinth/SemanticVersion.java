package top.archiem.jmp.util.modrinth;


import java.util.Objects;

class SemanticVersion implements Comparable<SemanticVersion> {
    private final int major;
    private final int minor;
    private final int patch;
    private final VersionType type;
    private final int buildNumber;

    public SemanticVersion(int major, int minor, int patch, VersionType type, int buildNumber) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.type = type;
        this.buildNumber = buildNumber;
    }

    public SemanticVersion(int major, int minor, int patch) {
        this(major, minor, patch, VersionType.RELEASE, 0);
    }

    public static SemanticVersion fromString(String version) {
        String[] versionParts = version.split("-");
        String versionPart = versionParts[0];
        String[] parts = versionPart.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid version format: " + version);
        }

        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        int patch = Integer.parseInt(parts[2]);

        if (versionParts.length == 1) {
            return new SemanticVersion(major, minor, patch);
        }

        VersionType type = VersionType.RELEASE;
        if (versionParts.length > 1) {
            switch (versionParts[1]) {
                case "dev":
                    type = VersionType.DEVELOPMENT;
                    break;
                default:
                    // Assume release if not explicitly 'dev' or other known type
                    break;
            }
        }

        int buildNumber = 0;
        if (versionParts.length == 3) { // e.g., 1.0.0-dev-123
            try {
                buildNumber = Integer.parseInt(versionParts[2]);
            } catch (NumberFormatException e) {
                // If the third part is not a number (e.g., "SNAPSHOT"), default buildNumber to 0
            }
        }

        return new SemanticVersion(major, minor, patch, type, buildNumber);
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    public VersionType getType() {
        return type;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    @Override
    public int compareTo(SemanticVersion other) {
        if (major != other.major) {
            return major - other.major;
        }
        if (minor != other.minor) {
            return minor - other.minor;
        }
        if (patch != other.patch) {
            return patch - other.patch;
        }

        if (type != other.type) {
            // RELEASE is considered "greater" (newer/more stable) than DEVELOPMENT
            return switch (type) {
                case RELEASE -> 1;
                case DEVELOPMENT -> -1;
            };
        }

        return buildNumber - other.buildNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemanticVersion that = (SemanticVersion) o;
        return major == that.major && minor == that.minor && patch == that.patch &&
                buildNumber == that.buildNumber && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch, type, buildNumber);
    }

    @Override
    public String toString() {
        if (type == VersionType.RELEASE) {
            return major + "." + minor + "." + patch;
        }

        // Format as major.minor.patch-suffix-buildNumber for non-release types
        return major + "." + minor + "." + patch + "-" + type.getSuffix() + "-" + buildNumber;
    }
}
