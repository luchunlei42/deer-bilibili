package com.chunlei.bili.auth.service;

import com.chunlei.bili.auth.dto.LoginFormDTO;
import com.chunlei.bili.common.api.R;

import javax.servlet.http.HttpSession;

public interface UserService {
    R sendCode(String phone);

    R login(LoginFormDTO loginForm);
}
