package com.github.jitwxs.sample.mock.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author jitwxs
 * @date 2021-08-29 22:00
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    /**
     * 未成交
     */
    NOT_DEAL(1),
    /**
     * 部分成交
     */
    PART_DEAL(2),
    /**
     * 完成
     */
    COMPLETE(3),
    /**
     * 已取消
     */
    CANCELED(4)
    ;

    private int status;
}
