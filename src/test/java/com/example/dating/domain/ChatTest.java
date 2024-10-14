//package com.example.dating.domain;
//
//import com.example.dating.dto.chat.ChatMessageDto;
//import com.example.dating.dto.fcm.FcmSendDto;
//import com.example.dating.dto.member.MemberInfoDto;
//import com.example.dating.repository.*;
//import com.example.dating.service.ChatRoomService;
//import com.example.dating.service.MemberService;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static com.example.dating.dto.chat.ChatMessageDto.MessageType.TALK;
//
//@Slf4j
//class ChatTest {
//
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private FeedRepository feedRepository;
//
//    @Autowired
//    private FeedCommentRepository feedCommentRepository;
//
//    @Autowired
//    private FeedLikeRepository feedLikeRepository;
//    @Autowired
//    private ChatRoomService chatRoomService;
//
//    @Autowired
//    private FeedBookmarkRepository feedBookmarkRepository;
//
//    @Autowired
//    private ProfileImagesRepository profileImagesRepository;
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//    @Autowired
//    private MessageRepository messageRepository;
//    @Autowired
//    private ChatReadRepository chatReadRepository;
//
//    @BeforeEach
//    void before() {
//        // 회원가입
//        LocalDate birthDay = LocalDate.parse("2024-09-26");  // 문자열을 LocalDate로 변환
//
//        Member testManMember = new Member(1L, birthDay, "testMan", "12345678", "testMan", "description", "Man", "address",
//                25, 180, "imageMan", "personalInfo", "personality", "interest", "likePersonality");
////        memberRepository.save(member1);
//
//        Member testWomanMember = new Member(1L, birthDay, "testWoman", "12345678", "testWoman", "description", "Woman", "address",
//                25, 180, "imageWoman", "personalInfo", "personality", "interest", "likePersonality");//        memberRepository.save(member2);
//
//
//        // 프로필 생성
////        List<String> images = Arrays.asList("image1", "image2", "image3");  // 여러 이미지들을 리스트로 변환
////
////        MemberInfoDto memberInfoDto1 = new MemberInfoDto("testMan", "description", birthDay, "address",
////                "gender", 52, 222, images, "image", "personalInfo", "personality", "interest", "likePersonality");
////
////        MemberInfoDto memberInfoDto2 = new MemberInfoDto("testWoman", "description", birthDay, "address",
////                "gender", 52, 222, images, "image", "personalInfo", "personality", "interest", "likePersonality");
////
////        memberService.saveProfileImages("testMan", memberInfoDto1);
////        memberService.save("testMan", memberInfoDto1);
////
////        memberService.saveProfileImages("testWoman", memberInfoDto2);
////        memberService.save("testWoman", memberInfoDto2);
//
//
//        // 채팅방 생성
////        Member memChat1 = memberRepository.findIdByEmail("testMan");
////        Member memChat2 = memberRepository.findIdByEmail("testWoman");
////
//        ChatRoom chatRoom = new ChatRoom(1L, testManMember, testWomanMember, "DM");
//
//    }
//
//    @Test
//    void 채팅읽음테이블조회() {
//        LocalDateTime chatTime = LocalDate.parse("2024-09-26-17:58:13").atStartOfDay();  // 문자열을 LocalDate로 변환
//
//        ChatMessageDto chatMessageDto = new ChatMessageDto(1L, "testMan", TALK, chatTime, "제발성공해줘-1");
//        ChatMessage chatMessage = new ChatMessage();
//        chatMessage.mapToEntity(chatMessageDto);
//        messageRepository.save(chatMessage);
//
//// 해당 채팅을 상대방이 읽지 않은 것으로 처리
//        System.out.println("1");
//
//// ChatRoom 조회
//        ChatRoom chatRoom = chatRoomRepository.findById(chatMessage.getChatRoomId()).orElse(null);
//        if (chatRoom != null) {
//            System.out.println("2");
//            System.out.println(chatRoom.getId());
//            System.out.println(chatRoom.getMember().getNickName());
//            System.out.println(chatRoom.getOtherMember().getNickName());
//            System.out.println(chatRoom.getType());
//
//            if (chatRoom.getMember().getNickName().equals(chatMessage.getNickName())) {
//                System.out.println("equals");
//                ChatRead chatRead =
//                        chatReadRepository.findByUserIdAndChatRoomId(chatRoom.getId(), chatRoom.getMember().getId());
//                chatRead.setIsRead(false);
//                chatReadRepository.save(chatRead);
//
//                System.out.println(chatRoom);
//            } else {
//                System.out.println("no");
//                ChatRead chatRead =
//                        chatReadRepository.findByUserIdAndChatRoomId(chatRoom.getId(), chatRoom.getOtherMember().getId());
//                chatRead.setIsRead(false);
//                chatReadRepository.save(chatRead);
//                System.out.println(chatRoom);
//            }
//
//
//        } else {
//            System.out.println(chatMessage.getChatRoomId());
//        }
//
//
//    }
//
//    @Test
//    @DisplayName("채팅방 나가기 테스트")
//    void quitChatRoomTest() {
//        LocalDate birthDay = LocalDate.parse("2024-09-26");  // 문자열을 LocalDate로 변환
//
//        Member testManMember = new Member(1L, birthDay, "testMan", "12345678", "testMan", "description", "Man", "address",
//                25, 180, "imageMan", "personalInfo", "personality", "interest", "likePersonality");
//
//        Member testWomanMember = new Member(1L, birthDay, "testWoman", "12345678", "testWoman", "description", "Woman", "address",
//                25, 180, "imageWoman", "personalInfo", "personality", "interest", "likePersonality");//        memberRepository.save(member2);
//
//        ChatRoom chatRoom = new ChatRoom(1L, testManMember, testWomanMember, "DM");
//
//
//
//
//
//
//        int isMember = chatRoomRepository.checkMemberByNickName(chatMessageDto.getChatRoomId(), chatMessageDto.getNickName());
//
//
//        Member member = new Member();
//        member.getId();
//
//        log.info(String.valueOf(isMember));
//
//        Member member = (isMember == 0) ? chatRoomRepository.findOtherMember(chatMessageDto.getChatRoomId()) :
//                chatRoomRepository.findMember(chatMessageDto.getChatRoomId());
//        log.info("2");
//
//        FcmSendDto fcmSendDto = new FcmSendDto(member.getNickName(), member.getNickName(),
//                chatMessageDto.getMessage(), chatMessageDto.getChatRoomId());
//        log.info("3");
//
//        fcmService.sendPush(fcmSendDto);
//        log.info("4");
//
//        messageRepository.deleteByChatRoomId(chatMessageDto.getChatRoomId());
//        chatReadRepository.deleteByChatRoomId(chatMessageDto.getChatRoomId());
//        chatRoomRepository.deleteById(chatMessageDto.getChatRoomId());
//    }
//
//    @Test
//    void 피드_댓글() {
//        // given
//        Member member2 = memberRepository.findById(2L).get();
//        Feed feed = feedRepository.findById(1L).get();
//
//        // when
//        FeedComment feedComment = new FeedComment(feed, member2, "member1 피드의 댓글입니다.");
//        feedCommentRepository.save(feedComment);
//        feed.addComment();
//
//        // then
//        FeedComment findFeedComment = feedCommentRepository.findById(1L).get();
//
//        Assertions.assertThat(findFeedComment.getContent()).isEqualTo(feedComment.getContent());
//        Assertions.assertThat(feed.getFeedComment()).isEqualTo(1);
//    }
//
//    @Test
//    void 피드_좋아요() {
//        // given
//        Member member2 = memberRepository.findById(2L).get();
//        Feed feed = feedRepository.findById(1L).get();
//
//        // when
//        FeedLike feedLike = new FeedLike(feed, member2);
//        feedLikeRepository.save(feedLike);
//        feed.addLike();
//
//        // then
//        FeedLike findFeedLike = feedLikeRepository.findById(1L).get();
//
//        Assertions.assertThat(feed.getFeedLike()).isEqualTo(1);
//        Assertions.assertThat(findFeedLike.getMember().getId()).isEqualTo(2L);
//    }
//
//    @Test
//    void 피드_북마크() {
//        // given
//        Member member2 = memberRepository.findById(2L).get();
//        Feed feed = feedRepository.findById(1L).get();
//
//        // when
//        FeedBookmark feedBookmark = new FeedBookmark(feed, member2);
//        feedBookmarkRepository.save(feedBookmark);
//        feed.addBookmark();
//
//        // then
//        FeedBookmark findFeedBookmark = feedBookmarkRepository.findById(1L).get();
//
//        Assertions.assertThat(feed.getFeedBookmark()).isEqualTo(1);
//        Assertions.assertThat(findFeedBookmark.getMember().getId()).isEqualTo(2L);
//    }
//
//    @AfterEach
//    void tearDown() {
//        // 테스트 후 데이터 정리
//        chatReadRepository.deleteAll();
//        profileImagesRepository.deleteAll();
//        messageRepository.deleteAll();
//        chatRoomRepository.deleteAll();
//        memberRepository.deleteAll();
//
//    }
//
//}