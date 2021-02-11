package com.example.mongodb.user;

import com.example.mongodb.ControllerTest;
import com.example.mongodb.domain.user.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ControllerTest {
    @Autowired
    private UserController userController;


    @Override
    protected Object controller() {
        return userController;
    }

    @Test
    public void getUserByUserId() throws Exception {
        getMockMvc().perform(get("/users")).andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()));
    }

}
