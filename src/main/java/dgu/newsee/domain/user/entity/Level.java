package dgu.newsee.domain.user.entity;

public enum Level {
    HIGH("상"),
    MEDIUM("중"),
    LOW("하");

    private final String value;

    Level(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

