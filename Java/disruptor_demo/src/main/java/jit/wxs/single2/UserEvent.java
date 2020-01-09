package jit.wxs.single2;

import lombok.Data;
import lombok.Getter;

import java.util.Arrays;

/**
 * 事件对象
 * @author jitwxs
 * @date 2020年01月09日 23:54
 */
@Data
class UserEvent {
    private String username;

    private Double score;

    private SexEnum sexEnum;

    @Override
    public String toString() {
        return String.format("name: %s, sex: %s, score: %.2f", username, sexEnum.getDesc(), score);
    }

    @Getter
    enum SexEnum {
        /**
         * 男性
         */
        MALE(0, "男性"),
        /**
         * 女性
         */
        FEMALE(1, "女性");

        private int code;

        private String desc;

        SexEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        static SexEnum getByCode(int code) {
            return Arrays.stream(SexEnum.values()).filter(e -> e.getCode() == code).findFirst().orElse(null);
        }
    }
}
