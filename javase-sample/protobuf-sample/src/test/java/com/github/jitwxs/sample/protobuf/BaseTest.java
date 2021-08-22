package com.github.jitwxs.sample.protobuf;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jitwxs
 * @date 2021-08-22 18:54
 */
public class BaseTest {
    protected MessageProto.User randomUser() {
        final Map<Integer, MessageProto.GradeInfo> gradeInfoMap = new HashMap<>();

        for (EnumMessageProto.SubjectEnum subjectEnum : EnumMessageProto.SubjectEnum.values()) {
            if (subjectEnum == EnumMessageProto.SubjectEnum.DEFAULT_SUBJECT || subjectEnum == EnumMessageProto.SubjectEnum.UNRECOGNIZED) {
                continue;
            }

            gradeInfoMap.put(subjectEnum.getNumber(), MessageProto.GradeInfo.newBuilder()
                    .setScore(RandomUtils.nextDouble(0, 100))
                    .setRank(RandomUtils.nextInt(1, 50))
                    .build());
        }

        final List<MessageProto.ParentUser> parentUserList = Arrays.asList(
                MessageProto.ParentUser.newBuilder().setRelation("father").setTel(RandomStringUtils.randomNumeric(13)).build(),
                MessageProto.ParentUser.newBuilder().setRelation("mother").setTel(RandomStringUtils.randomNumeric(13)).build()
        );

        return MessageProto.User.newBuilder()
                .setName(RandomStringUtils.randomAlphabetic(5))
                .setAge(RandomUtils.nextInt(1, 80))
                .setSex(EnumMessageProto.SexEnum.forNumber(RandomUtils.nextInt(1, 2)))
                .putAllGrade(gradeInfoMap)
                .addAllParent(parentUserList)
                .build();
    }
}
