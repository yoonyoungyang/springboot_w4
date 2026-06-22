package kr.adapterz.springboot.controller;

import kr.adapterz.springboot.dto.SignUpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // HTTP 요청을 받는 클래스 -> 컨트롤러 등록
@RequestMapping("/users") //공통 경로
public class UserController {

    @PostMapping("/signup")
    public String signup(@RequestBody SignUpRequest request) {
        return request.getEmail();
    }
}
