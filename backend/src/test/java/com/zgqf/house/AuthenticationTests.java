package com.zgqf.house;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zgqf.house.entity.Buyer;
import com.zgqf.house.entity.Seller;
import com.zgqf.house.entity.User;
import com.zgqf.house.mapper.BuyerMapper;
import com.zgqf.house.mapper.SellerMapper;
import com.zgqf.house.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private BuyerMapper buyerMapper;

    @MockBean
    private SellerMapper sellerMapper;

    @BeforeEach
    void setUp() {
        // Setup default behavior for userMapper.insert to simulate auto-increment ID
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setU_id(123); // Simulate DB assigning ID
            return 1;
        }).when(userMapper).insert(any(User.class));
    }

    @Test
    void testRegisterBuyer() throws Exception {
        Map<String, Object> registerRequest = new HashMap<>();
        registerRequest.put("username", "testbuyer");
        registerRequest.put("password", "password123");
        registerRequest.put("type", "buyer");

        Map<String, Object> info = new HashMap<>();
        info.put("name", "John Buyer");
        info.put("phone", "1234567890");
        info.put("email", "john@example.com");
        info.put("mobileAssets", 100000.0);
        registerRequest.put("info", info);

        when(userMapper.findByUsername("testbuyer")).thenReturn(null);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registration successful"))
                .andExpect(jsonPath("$.userId").value(123));

        // Verify User insertion
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).insert(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("testbuyer", savedUser.getU_username());
        assertEquals("buyer", savedUser.getU_type());

        // Verify Buyer insertion
        ArgumentCaptor<Buyer> buyerCaptor = ArgumentCaptor.forClass(Buyer.class);
        verify(buyerMapper).insert(buyerCaptor.capture());
        Buyer savedBuyer = buyerCaptor.getValue();
        assertEquals(123, savedBuyer.getB_id());
        assertEquals("John Buyer", savedBuyer.getB_name());
        assertEquals(100000.0, savedBuyer.getB_mobile_assets());
    }

    @Test
    void testRegisterSeller() throws Exception {
        Map<String, Object> registerRequest = new HashMap<>();
        registerRequest.put("username", "testseller");
        registerRequest.put("password", "password123");
        registerRequest.put("type", "seller");

        Map<String, Object> info = new HashMap<>();
        info.put("name", "Best Properties Co.");
        info.put("phone", "0987654321");
        info.put("email", "contact@bestprops.com");
        info.put("describe", "Best houses in town");
        info.put("website", "www.bestprops.com");
        registerRequest.put("info", info);

        when(userMapper.findByUsername("testseller")).thenReturn(null);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Registration successful"));

        // Verify Seller insertion
        ArgumentCaptor<Seller> sellerCaptor = ArgumentCaptor.forClass(Seller.class);
        verify(sellerMapper).insert(sellerCaptor.capture());
        Seller savedSeller = sellerCaptor.getValue();
        assertEquals(123, savedSeller.getS_id());
        assertEquals("Best Properties Co.", savedSeller.getS_name());
    }

    @Test
    void testRegisterAdmin() throws Exception {
        Map<String, Object> registerRequest = new HashMap<>();
        registerRequest.put("username", "admin");
        registerRequest.put("password", "admin123");
        registerRequest.put("type", "admin");
        registerRequest.put("info", new HashMap<>());

        when(userMapper.findByUsername("admin")).thenReturn(null);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        verify(userMapper).insert(any(User.class));
        verify(buyerMapper, never()).insert(any(Buyer.class));
        verify(sellerMapper, never()).insert(any(Seller.class));
    }

    @Test
    void testLoginBuyer() throws Exception {
        User mockUser = new User();
        mockUser.setU_id(1);
        mockUser.setU_username("testbuyer");
        mockUser.setU_password("password123");
        mockUser.setU_type("buyer");

        Buyer mockBuyer = new Buyer();
        mockBuyer.setB_id(1);
        mockBuyer.setB_name("John Buyer");

        when(userMapper.findByUsername("testbuyer")).thenReturn(mockUser);
        when(buyerMapper.getBuyerById(1)).thenReturn(mockBuyer);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testbuyer");
        loginRequest.put("password", "password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("buyer"))
                .andExpect(jsonPath("$.user.b_name").value("John Buyer"))
                .andExpect(request().sessionAttribute("User", mockUser))
                .andExpect(request().sessionAttribute("Buyer", mockBuyer));
    }

    @Test
    void testLoginSeller() throws Exception {
        User mockUser = new User();
        mockUser.setU_id(2);
        mockUser.setU_username("testseller");
        mockUser.setU_password("password123");
        mockUser.setU_type("seller");

        Seller mockSeller = new Seller();
        mockSeller.setS_id(2);
        mockSeller.setS_name("Seller Co.");

        when(userMapper.findByUsername("testseller")).thenReturn(mockUser);
        when(sellerMapper.getSellerById(2)).thenReturn(mockSeller);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testseller");
        loginRequest.put("password", "password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("seller"))
                .andExpect(jsonPath("$.user.s_name").value("Seller Co."))
                .andExpect(request().sessionAttribute("Seller", mockSeller));
    }

    @Test
    void testLoginFailed() throws Exception {
        when(userMapper.findByUsername("wronguser")).thenReturn(null);

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "wronguser");
        loginRequest.put("password", "password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is(401));
    }
}
