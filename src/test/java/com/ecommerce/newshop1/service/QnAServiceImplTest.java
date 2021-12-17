//package com.ecommerce.newshop1.service;
//
//import com.ecommerce.newshop1.entity.QnAEntity;
//import com.ecommerce.newshop1.repository.QnARepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class QnAServiceImplTest {
//
//    @Autowired
//    QnARepository qnARepository;
//
//    @Autowired
//    QnAServiceImpl qnAService;
//
//    @Test
//    void deleteAllQnA() {
//
//        //given
//        QnAEntity qnAEntity = QnAEntity.builder()
//                .content("test")
//                .depth(1)
//                .build();
//        qnAEntity = qnARepository.save(qnAEntity);
//
//        List<Long> idList = new ArrayList<>();
//        idList.add(qnAEntity.getId());
//
//        //when
//        qnAService.deleteQnaAndReply(idList);
//
//        //then
//        Optional<QnAEntity> qna = qnARepository.findById(idList.get(0));
//        assertEquals(Optional.empty(), qna);
//    }
//
//
//    @Test
//    void testDeleteQnaAndReply() {
//
//        //given
//        QnAEntity qna = QnAEntity.builder()
//                .content("test")
//                .depth(1)
//                .build();
//        qna = qnARepository.save(qna);
//
//        QnAEntity reply = QnAEntity.builder()
//                .parent(qna.getId())
//                .content("testAnswer")
//                .build();
//        qnARepository.save(reply);
//
//        List<Long> idList = new ArrayList<>();
//        idList.add(qna.getId());
//
//        //when
//        qnAService.deleteQnaAndReply(idList);
//
//        //then
//        Optional<QnAEntity> result = qnARepository.findById(idList.get(0));
//        assertEquals(Optional.empty(), result);
//    }
//
//
//}
