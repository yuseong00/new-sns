package com.example.newsns.controller;

import com.example.newsns.controller.request.PostCreateRequest;
import com.example.newsns.controller.response.PostResponse;
import com.example.newsns.controller.response.Response;
import com.example.newsns.model.PostDto;
import com.example.newsns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;



    @PostMapping
    //일단은 반환타입을 넘겨줘야 하니까 Response를 써야 한다. //Authentication 는 인증된 자의 회원 정보이다.
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {

        postService.create(request.getTitle(), request.getTitle(), authentication.getName());

        return Response.success();  //완료되고 안되고 차이 반환
    }

    @PostMapping("/{postId}")
    //일단은 반환타입을 넘겨줘야 하니까 Response를 써야 한다. //Authentication 는 인증된 자의 회원 정보이다.
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostCreateRequest request, Authentication authentication) {

        PostDto postDto = postService.modify(request.getTitle(), request.getTitle(), authentication.getName(), postId);

        return Response.success(PostResponse.fromPostDto(postDto));
    }

    @DeleteMapping("/{postId}")
    //일단은 반환타입을 넘겨줘야 하니까 Response를 써야 한다. //Authentication 는 인증된 자의 회원 정보이다.
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {

        postService.delete(authentication.getName(), postId);

        return Response.success();
    }

    @GetMapping
    //리스트 형태의 response는 반드시 페이징이 필요하다. 페이징 안하면 성능저하생긴다. 꼭 Pageable 해라~!
    //그리고 반환타입은 페이지형태로 보내야 한다.
    public Response<Page<PostResponse>> list(Pageable pageable , Authentication authentication){

     return  Response.success(postService.list(pageable).map(PostResponse::fromPostDto));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable , Authentication authentication){

        return  Response.success(postService.my(authentication.getName(), pageable).map(PostResponse::fromPostDto));
    }


}
