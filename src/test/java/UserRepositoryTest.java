import com.cairin.controller.UserController;
import com.cairin.model.User;
import com.cairin.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetUserById() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");

        List<User> userList = Arrays.asList(user);
        Page<User> page = new PageImpl<>(userList);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);

        // Act
        ResponseEntity<Page<User>> response = userController.getUsers(0, 1, "id");
        Page<User> resultPage = response.getBody();
        List<User> resultList = resultPage.getContent();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(resultPage);
        assertEquals(1, resultList.size());
        User result = resultList.get(0);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
    }

    // Add more unit tests for other repository methods...
}
