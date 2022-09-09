package nextstep.jwp.service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.model.User;

public class LoginService {

    public static User login(Map<String, String> requestBody) {
        validRequestBody(requestBody);

        User user = findUser(requestBody.get("account"));
        if (user.checkPassword(requestBody.get("password"))) {
            return user;
        }
        throw new IllegalArgumentException("password�� ��ġ���� �ʽ��ϴ�.");
    }

    private static User findUser(String account) {
        Optional<User> byAccount = InMemoryUserRepository.findByAccount(account);
        if (byAccount.isEmpty()) {
            throw new NoSuchElementException("�������� �ʴ� account�Դϴ�.");
        }
        return byAccount.get();
    }

    private static void validRequestBody(Map<String, String> requestBody) {
        if (requestBody.containsKey("account") || requestBody.get("account").isBlank()) {
            throw new NoSuchElementException("account�� �Է����ּ���");
        }
        if (requestBody.containsKey("password") || requestBody.get("password").isBlank()) {
            throw new NoSuchElementException("password�� �Է����ּ���");
        }
    }
}
