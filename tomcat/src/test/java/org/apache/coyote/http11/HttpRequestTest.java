package org.apache.coyote.http11;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class HttpRequestTest {

    @Test
    void 쿼리_스트링이_파싱_되는지_확인한다() {
        String requestHeader = "POST /login?account=gugu&password=password";
        HttpRequest httpRequest = HttpRequest.from(requestHeader);

        assertAll(
                () -> assertThat(httpRequest.getRequestUrl()).isEqualTo("/login"),
                () -> assertThat(httpRequest.getRequestParams()).containsEntry("account", "gugu"),
                () -> assertThat(httpRequest.getRequestParams()).containsEntry("password", "password")
        );
    }

}