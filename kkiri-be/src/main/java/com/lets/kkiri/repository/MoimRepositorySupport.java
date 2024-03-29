package com.lets.kkiri.repository;

import com.lets.kkiri.dto.moim.MoimCardDto;
import com.lets.kkiri.entity.Moim;
import com.lets.kkiri.entity.QMemberGroup;
import com.lets.kkiri.entity.QMoim;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MoimRepositorySupport {
    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;
    QMoim qMoim = QMoim.moim;
    QMemberGroup qMemberGroup = QMemberGroup.memberGroup;

    public List<MoimCardDto> findMoimsByMemberIdAndDate(Long memberId, LocalDate date) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qMemberGroup.member.id.eq(memberId));
        builder.and(qMemberGroup.moim.id.eq(qMoim.id));

        if (date != null) builder.and(qMoim.meetingAt.dayOfMonth().eq(date.getDayOfMonth()));
        return jpaQueryFactory.select(
                        Projections.constructor(
                                MoimCardDto.class,
                                qMoim.id,
                                qMoim.name,
                                qMoim.placeName,
                                qMoim.meetingAt,
                                qMoim.lateFee
                        )
                ).from(qMoim, qMemberGroup)
                .where(builder)
                .orderBy(qMoim.meetingAt.desc())
                .fetch();
    }

    public List<Moim> findMoimsByMeetingAt(String meetingAt) {
        StringExpression stringExpression = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})", qMoim.meetingAt, "%Y-%m-%d %H:%i"
        );
        return jpaQueryFactory.selectFrom(qMoim)
                .where(
                        stringExpression.eq(meetingAt)
                )
                .fetch();
    }
}
