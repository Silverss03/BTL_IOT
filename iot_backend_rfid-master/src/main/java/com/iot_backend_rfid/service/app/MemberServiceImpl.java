package com.iot_backend_rfid.service.app;

import com.iot_backend_rfid.dto.request.LoginRequest;
import com.iot_backend_rfid.dto.request.RegisterRequest;
import com.iot_backend_rfid.dto.response.LoginResponse;
import com.iot_backend_rfid.dto.response.RegisterResponse;
import com.iot_backend_rfid.exception.AppException;
import com.iot_backend_rfid.model.*;
import com.iot_backend_rfid.repository.*;
import com.iot_backend_rfid.security.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;

    @Override
    public LoginResponse memberLogin(LoginRequest loginRequest) throws Exception {
        Member member=memberRepository.findByUsername(loginRequest.getUsername()).orElseThrow(
                ()-> new AppException("Sai tên đăng nhập", HttpStatus.NOT_FOUND)
        );
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        );
        try {
            // tu dong goi loadUserByUsername de lay ra userDetail (setAuthorities luon)
            Authentication auth = this.authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException ex) {
            throw new AppException("Sai mật khẩu", HttpStatus.BAD_REQUEST);
        } catch (AuthenticationException ex) {
            log.error("{}",ex.getCause());
            throw new AppException("Đăng nhập thất bại", HttpStatus.BAD_REQUEST);
        }
        return LoginResponse.builder()
                .token(this.jwtTokenUtils.generateToken(member))
                .role(member.getRole())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponse register(RegisterRequest registerRequest) {
        Role role=roleRepository.findById(registerRequest.getRoleId()).orElseThrow(
                ()->new AppException("Role not found", HttpStatus.NOT_FOUND)
        );
        Member member=Member.builder()
                .memberName(registerRequest.getName())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role)
                .build();
        Member memberSave=memberRepository.saveAndFlush(member);
        if(registerRequest.getRoleId()==1){
            Student student=Student.builder()
                    .studentCode(registerRequest.getMemberCode())
                    .member(memberSave)
                    .build();
            studentRepository.saveAndFlush(student);
        }else if(registerRequest.getRoleId()==2){
            Teacher teacher=Teacher.builder()
                    .teacherCode(registerRequest.getMemberCode())
                    .member(memberSave)
                    .build();
            teacherRepository.saveAndFlush(teacher);
        }else if(registerRequest.getRoleId()==3){
            Admin admin=Admin.builder()
                    .adminCode(registerRequest.getMemberCode())
                    .member(memberSave)
                    .build();
            adminRepository.saveAndFlush(admin);
        }
        return RegisterResponse.builder()
                .name(memberSave.getMemberName())
                .username(memberSave.getUsername())
                .password(member.getPassword())
                .memberCode(registerRequest.getMemberCode())
                .role(role)
                .build();
    }

}
