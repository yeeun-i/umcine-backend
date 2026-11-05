package com.umc.movie.service;

import com.umc.movie.dto.EmailCheckResponse;
import com.umc.movie.dto.MemberResponse;
import com.umc.movie.dto.NicknameCheckResponse;
import com.umc.movie.dto.SignUpRequest;
import com.umc.movie.entity.Member;
import com.umc.movie.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class MemberService {

 private final MemberRepository members;
 private final PasswordEncoder passwordEncoder;

 public MemberService(
         MemberRepository members,
         PasswordEncoder passwordEncoder
 ) {
  this.members = members;
  this.passwordEncoder = passwordEncoder;
 }

 public NicknameCheckResponse checkNickname(String nickname) {
  return new NicknameCheckResponse(
          !members.existsByNickname(nickname)
  );
 }

 public EmailCheckResponse checkEmail(String email) {
  return new EmailCheckResponse(
          !members.existsByEmail(email)
  );
 }

 @Transactional
 public MemberResponse signUp(SignUpRequest request) {
  if (members.existsByEmail(request.email())) {
   throw new ResponseStatusException(
           HttpStatus.CONFLICT,
           "이미 사용 중인 이메일입니다."
   );
  }

  if (members.existsByNickname(request.nickname())) {
   throw new ResponseStatusException(
           HttpStatus.CONFLICT,
           "이미 사용 중인 닉네임입니다."
   );
  }

  Member member = new Member(
          request.email(),
          passwordEncoder.encode(request.password()),
          request.nickname()
  );

  Member savedMember = members.save(member);

  return new MemberResponse(
          savedMember.getMemberId(),
          savedMember.getEmail(),
          savedMember.getNickname()
  );
 }
}
