package com.lets.kkiri.controller;

import com.lets.kkiri.common.util.JwtTokenUtil;
import com.lets.kkiri.service.MypageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/mypage")
public class MypageController {
	private final MypageService mypageService;

	@GetMapping
	public ResponseEntity getMyPage(@RequestHeader(JwtTokenUtil.HEADER_STRING) String accessToken,
		@RequestParam String period) {
		String kakaoId = JwtTokenUtil.getIdentifier(accessToken);
		return ResponseEntity.status(200).body(mypageService.getMyPage(kakaoId, period));
	}
}
