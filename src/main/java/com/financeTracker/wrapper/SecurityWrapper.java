package com.financeTracker.wrapper;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SecurityWrapper {

    private String login;

    private String password;
}
