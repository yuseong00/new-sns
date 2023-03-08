package com.example.newsns.controller;

import com.example.newsns.controller.request.PostCreateRequest;
import com.example.newsns.controller.response.Response;
import com.example.newsns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;



    @PostMapping
    //일단은 반환타입을 넘겨줘야 하니까 Response를 써야 한다. //Authentication 는 인증된 자의 회원 정보이다.
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {

        postService.create(request.getTitle(), request.getTitle(), authentication.getName());

        return Response.success(null);
    }
}
