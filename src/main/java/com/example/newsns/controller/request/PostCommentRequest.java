package com.example.newsns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//data 어노테이션에는 getter,settet,tostring,AllArgsConstructor 등 다양한 어노테이션을 함축하고 있다.
@AllArgsConstructor
//근데 AllArgsConstructor를 굳이 왜 쓰는 이유는??
//data 어노테션을 쓰게 되면 그 클래스의 필드값을 기준으로 생성자를 만들수 있다. 예를 들면 comment,title이 있으면
//comment,title 함께있는 , comment,title 이 각자 한개만 있는 생성자를 해서 총 3개의 생성자를 만든다.
//다만 외부필드주입시 생성자를 만들수 없다. 테스트를 할때에는 가짜객체를 만들어서 필드주입을 해야 하는 경우가 있는데
//그런경우를 대비하더라도 @AllArgsConstructor 를 통해 외부 필드주입에 대한 준비를 하는거다!!
@NoArgsConstructor
//이거도 AllArgsConstructor 와 상황이 같다. @Data 를 통해 필드값을 가지고 생성자를 자동으로 만들어주지만
//추가적으로 필드값이 null 값인 상황에 생성자를 만들어 주지 않는다. 그래서 기본생성자가 필요로 하다.
//만약 이러한 기본생성자에 대한 어노테이션이 없으면 콘솔창에는 'although at least one creator' 라는 에러창이 뜬다.
public class PostCommentRequest {

    private String comment;


}
