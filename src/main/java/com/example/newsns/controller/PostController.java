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


    @PostMapping("/{postId}/likes")
    //좋아요버튼을 누르면 반환값이 없고, 카운팅만 되는 구현
    //로직을 짜기전에 입력값이 뭐가 있는지 확인한다. 일단 게시글번호,인증된 사용자가 필요하다)
    public Response<Void> like(@PathVariable Integer postId, Authentication authentication){
        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    //좋아요버튼을 누르면 반환값이 없고, 카운팅만 되는 구현
    //로직을 짜기전에 입력값이 뭐가 있는지 확인한다. 일단 게시글번호,인증된 사용자가 필요하다)
    public Response<Integer> likeCount(@PathVariable Integer postId, Authentication authentication){

        //success 의 반환타입은 제네릭으로 선언된 T이다. 따라서 likecount의 반환타입인 int로 결과값이 반환된다.
        return Response.success(postService.likeCount(postId));
    }


}
