package dgu.newsee.domain.transformednews.entity;

public enum TransformLevel {
    EASY("하"),
    MEDIUM("중"),
    HARD("상");

    private final String kor;

    TransformLevel(String kor) {
        this.kor = kor;
    }

    public static TransformLevel fromKorean(String kor) {
        for (TransformLevel level : TransformLevel.values()) {
            if (level.kor.equals(kor)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown level: " + kor);
    }

    public String getKorean() {
        return kor;
    }
}
