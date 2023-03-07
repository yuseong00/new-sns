package com.example.newsns.controller;

import com.fascampus.sns.controller.request.UserJoinRequest;
import com.fascampus.sns.controller.request.UserLoginRequest;
import com.fascampus.sns.exception.ErrorCode;
import com.fascampus.sns.exception.SnsApplicationException;
import com.fascampus.sns.model.User;
import com.fascampus.sns.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;  //ObjectMapper 를 사용하여 request 로 부터 받은 json 내역을 java 객체로 변환하는 역할이다.

    @MockBean
    private UserService userService;

    @Test
    public void  회원가입 ()throws Exception{
      String userName="userName";
      String password="password";

        when(userService.join(userName,password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo : add request body
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password)))
                ).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void  회원가입시_이미_회원가입된_username으로_회원가입을_하는경우_에러반환 ()throws Exception{
        String userName="userName";
        String password="password";

        when(userService.join(userName,password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo : add request body
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName,password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인 ()throws Exception{
        String userName="userName";
        String password="password";

        when(userService.login( userName, password)).thenReturn("test_token");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo : add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password)))
                ).andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void 로그인시_회원가입이_안된_username을_입력할경우_에러반환 ()throws Exception{
        String userName="userName";
        String password="password";

        when(userService.login(userName,password )).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo : add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password)))
                ).andDo(print())
                .andExpect(status().isNotFound());

    }
    @Test
    public void 로그인시_틀린_password를_입력할경우_에러반환 ()throws Exception{
        String userName="userName";
        String password="password";

        when(userService.login(userName,password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        // todo : add request body
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());

    }


}
