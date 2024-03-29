package com.lets.kkiri.controller;

import com.lets.kkiri.common.util.JwtTokenUtil;
import com.lets.kkiri.dto.noti.NotiMoimIdReq;
import com.lets.kkiri.dto.noti.PressNotiReq;
import com.lets.kkiri.dto.noti.RouteGuideNotiReq;
import com.lets.kkiri.service.NotiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/noti")
@RequiredArgsConstructor
public class NotiController {
    private final NotiService notiService;
    @PostMapping("/presses")
    public ResponseEntity<?> sendPressNoti(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String token,
            @RequestBody PressNotiReq pressNotiReq
    ) {
        String senderKakaoId = JwtTokenUtil.getIdentifier(token);
        notiService.sendPressNoti(senderKakaoId, pressNotiReq.getReceiverKakaoId(), pressNotiReq.getChatRoomId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/helps")
    public ResponseEntity<?> sendHelpNoti(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String token,
            @RequestBody NotiMoimIdReq notiMoimIdReq
    ) {
        String senderKakaoId = JwtTokenUtil.getIdentifier(token);
        notiService.sendHelpNoti(senderKakaoId, notiMoimIdReq.getMoimId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/helps/guides")
    public ResponseEntity<?> sendRouteGuide(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String token,
            @RequestBody RouteGuideNotiReq routeGuideReq)
    {
        String senderKakaoId = JwtTokenUtil.getIdentifier(token);
        notiService.sendRoute(senderKakaoId, routeGuideReq);
        return ResponseEntity.ok().body(routeGuideReq.getPath());
    }

    @GetMapping("/imminent")
    public ResponseEntity<?> sendImminentNoti(
            @RequestParam Long moimId
    ) {
        notiService.sendImminentNoti(moimId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/payments")
    public ResponseEntity<?> sendPaymentNoti(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String token,
            @RequestBody NotiMoimIdReq notiMoimIdReq
    ) {
        String senderKakaoId = JwtTokenUtil.getIdentifier(token);
        notiService.sendPaymentNoti(senderKakaoId, notiMoimIdReq.getMoimId());
        return ResponseEntity.ok().build();
    }
}
