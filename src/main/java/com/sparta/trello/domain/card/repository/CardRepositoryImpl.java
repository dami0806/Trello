package com.sparta.trello.domain.card.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.comment.entity.CommentStatus;
import com.sparta.trello.domain.comment.entity.QComment;
import com.sparta.trello.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findCommentsByCardId(Long cardId, Pageable pageable) {
        QComment qComment = QComment.comment;
        QUser qUser = QUser.user;

        List<Comment> comments = queryFactory.selectFrom(qComment)
                .leftJoin(qComment.user, qUser).fetchJoin()  // 댓글과 작성자를 함께 조회
                .where(qComment.card.id.eq(cardId).and(qComment.status.eq(CommentStatus.ACTIVE)))
                .orderBy(qComment.createAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(qComment)
                .where(qComment.card.id.eq(cardId).and(qComment.status.eq(CommentStatus.ACTIVE)))
                .fetchCount();

        return new PageImpl<>(comments, pageable, total);
    }
}
