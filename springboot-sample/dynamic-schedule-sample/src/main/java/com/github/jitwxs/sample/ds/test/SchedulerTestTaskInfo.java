package com.github.jitwxs.sample.ds.test;

import com.github.jitwxs.sample.ds.config.IDSTaskInfo;
import lombok.Builder;
import lombok.Data;

/**
 * @author jitwxs
 * @date 2021年03月27日 21:54
 */
@Data
@Builder
public class SchedulerTestTaskInfo implements IDSTaskInfo {
    private long id;

    private String cron;

    private boolean isValid;

    private String reference;

    @Override
    public boolean isChange(IDSTaskInfo oldTaskInfo) {
        if(oldTaskInfo instanceof SchedulerTestTaskInfo) {
            final SchedulerTestTaskInfo obj = (SchedulerTestTaskInfo) oldTaskInfo;
            return !this.cron.equals(obj.cron) || this.isValid != obj.isValid || !this.reference.equals(obj.getReference());
        } else {
            throw new IllegalArgumentException("Not Support IDSTaskInfo type");
        }
    }
}
