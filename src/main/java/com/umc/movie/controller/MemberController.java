package com.umc.movie.controller;

import com.umc.movie.dto.ApiResponse;
import com.umc.movie.dto.EmailCheckResponse;
import com.umc.movie.dto.MemberResponse;
import com.umc.movie.dto.NicknameCheckResponse;
import com.umc.movie.dto.SignUpRequest;
import com.umc.movie.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

 private final MemberService service;

 public MemberController(MemberService service) {
  this.service = service;
 }

 @GetMapping("/nickname/{nickname}")
 public NicknameCheckResponse checkNickname(
         @PathVariable("nickname") String nickname
 ) {
  return service.checkNickname(nickname);
 }

 @GetMapping("/email/{email}")
 public EmailCheckResponse checkEmail(
         @PathVariable("email") String email
 ) {
  return service.checkEmail(email);
 }

 @PostMapping
 public ResponseEntity<ApiResponse<MemberResponse>> signUp(
         @Valid @RequestBody SignUpRequest request
 ) {
  MemberResponse result = service.signUp(request);

  return ResponseEntity.status(201)
          .body(ApiResponse.created(
                  "회원가입에 성공했습니다.",
                  result
          ));
 }
}