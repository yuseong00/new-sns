package com.example.newsns.controller;

import com.example.newsns.controller.request.PostCommentRequest;
import com.example.newsns.controller.request.PostCreateRequest;
import com.example.newsns.controller.response.CommentResponse;
import com.example.newsns.controller.response.PostResponse;
import com.example.newsns.controller.response.Response;
import com.example.newsns.model.PostDto;
import com.example.newsns.repository.LikeEntityRepository;
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
    private final LikeEntityRepository likeEntityRepository;


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
    //리스트 형태의 response 는 반드시 페이징이 필요하다. 페이징 안하면 성능저하생긴다. 꼭 Pageable 해라~!
    //그리고 반환타입은 페이지형태로 보내야 한다.
    public Response<Page<PostResponse>> list(Pageable pageable , Authentication authentication){

     return  Response.success(postService.list(pageable).map(PostResponse::fromPostDto));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Pageable pageable , Authentication authentication){

        return  Response.success(postService.my(authentication.getName(), pageable).map(PostResponse::fromPostDto));
    }


    @PostMapping("/{postId}/likes") //@PathVariable Integer postId 여기에서 postId는 /{postId}/likes" 의 postId를 뜻한다.
    //좋아요버튼을 누르면 반환값이 없고, 카운팅만 되는 구현
    //로직을 짜기전에 입력값이 뭐가 있는지 확인한다. 일단 게시글번호,인증된 사용자가 필요하다)
    public Response<Void> like(@PathVariable Integer postId, Authentication authentication){
        postService.like(postId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    //좋아요버튼을 누르면 반환값이 없고, 카운팅만 되는 구현
    //로직을 짜기전에 입력값이 뭐가 있는지 확인한다. 일단 게시글번호,인증된 사용자가 필요하다)
    public Response<Long> likeCount(@PathVariable Integer postId, Authentication authentication){

        //success 의 반환타입은 제네릭으로 선언된 T이다. 따라서 likecount의 반환타입인 int로 결과값이 반환된다.
        return Response.success(postService.likeCount(postId));
    }

    @PostMapping("/{postId}/comments")
    //@PathVariable Integer postId 여기에서 postId는 /{postId}/comments" 의 postId를 뜻한다.
    // POST 메서드로 요청을 보낼 때 HTTP 본문에 담겨있는 JSON 데이터가
    // PostCommentRequest 객체로 자동으로 변환되어 컨트롤러 메서드의 파라미터 request 로 전달
    //PostCommentRequest 필드값에 따라 request 의 정보가 달라진다.
    public Response<Void> comment(@PathVariable Integer postId, @RequestBody PostCommentRequest request, Authentication authentication){
        postService.comment(postId, request.getComment(),authentication.getName());
        return Response.success();
    }


    @GetMapping("/{postId}/comments") //페이징처리를 위해 Pageable 필요하다.
    public Response<Page<CommentResponse>> getComment(@PathVariable Integer postId, Pageable pageable , Authentication authentication){

        //getComments는 post엔티티의 postId와 관계되어있는 comment엔티티를 dto로 변환하여  페이징 처리한결과이고
        //이를 다시 프론트엔드단으로 보내기 위해 dto를 response로 바꿔서 보내준다.
        return Response.success(postService.getComments(postId,pageable).map(CommentResponse::fromCommentDto));
    }


}
