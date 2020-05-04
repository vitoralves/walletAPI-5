package com.wallet.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.UserDTO;
import com.wallet.entity.User;
import com.wallet.service.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

	private static final Long ID = 1L;
	private static final String EMAIL = "teste@teste.com";
	private static final String NOME = "Rennan Costa";
	private static final String PASSWORD = "123456789";
	private static final String URL = "/user";
	
	
	@MockBean
	UserService service;
	
	@Autowired
	MockMvc mvc;
	
	@Test
	public void testSave() throws Exception {
		
		BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());
		mvc.perform(MockMvcRequestBuilders.post(URL)
				.content(getJsonPayLoad(ID, EMAIL, NOME, PASSWORD))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
		.andExpect(jsonPath("$.data.id").value(ID))
		.andExpect(jsonPath("$.data.email").value(EMAIL))
		.andExpect(jsonPath("$.data.name").value(NOME))
		.andExpect(jsonPath("$.data.password").doesNotExist());
		
	}
	
	@Test
	public void testSaveInvalidUser() throws JsonProcessingException, Exception{
		
		BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());
		mvc.perform(MockMvcRequestBuilders.post(URL)
				.content(getJsonPayLoad(ID, "gg", NOME, PASSWORD))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.erros.[0]").value("Email invalido"));
		
	}
	
	
	public User getMockUser() {
		User user = new User();
		user.setId(ID);
		user.setEmail(EMAIL);
		user.setName(NOME);
		user.setPassword(PASSWORD);
		
		return user;
		
	}
	
	public String getJsonPayLoad(Long id, String email, String nome, String password) throws JsonProcessingException {
		UserDTO dto = new UserDTO();
		dto.setId(id);
		dto.setEmail(email);
		dto.setName(nome);
		dto.setPassword(password);
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
	}
	
	
}
