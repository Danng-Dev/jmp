package top.archiem.jmp.util.modrinth;

enum VersionType {
    RELEASE(""),
    DEVELOPMENT("dev");

    private final String suffix;

    VersionType(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
