package kr.adapterz.springboot;

import org.springframework.stereotype.Service;

@Service
public class InitialService {
    public String getMessage() {
        return "Hello, Adapterz!";
    }
}