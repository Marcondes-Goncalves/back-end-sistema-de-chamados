package com.helpdesk;


import com.helpdesk.controllers.ChamadoController;
import com.helpdesk.enuns.PrioridadeChamado;
import com.helpdesk.enuns.StatusChamado;
import com.helpdesk.models.Acesso;
import com.helpdesk.models.Chamado;
import com.helpdesk.models.Usuario;
import com.helpdesk.services.ChamadoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(ChamadoController.class)
@WithMockUser(roles = "CLIENTE")
public class ChamadosControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChamadoService chamadoService;

    @Test
    public void deveCriarChamado() throws Exception {

        Acesso acessoUsuario = new Acesso();
        acessoUsuario.setId(1L);
        acessoUsuario.setAcesso("ROLE_CLIENTE");

        Usuario cliente = new Usuario();
        cliente.setId(1L);
        cliente.setEmail("cliente@example.com");
        cliente.setSenha("Marcondes");
        cliente.setAcessos(List.of(acessoUsuario));

        Chamado chamado = new Chamado();
        chamado.setId(1L);
        chamado.setTitulo("Problema com login");
        chamado.setDescricao("Não consigo acessar minha conta");
        chamado.setStatus(StatusChamado.ABERTO);
        chamado.setPrioridade(PrioridadeChamado.ALTA);
        chamado.setCliente(cliente);
        chamado.setDataAbertura(LocalDateTime.now());

        when(chamadoService.criarChamado(chamado)).thenReturn(chamado);

        mockMvc.perform(post("/chamados/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "titulo": "Problema com login",
                            "descricao": "Não consigo acessar minha conta",
                            "status": "ABERTO",
                            "prioridade": "ALTA",
                            "cliente": {
                                "id": 1,
                                "email": "cliente@example.com",
                                "acessos": [
                                    {
                                        "id": 1,
                                        "acesso": "ROLE_CLIENTE"
                                    }
                                ]
                            },
                            "dataAbertura": "2024-06-05T12:34:56"
                        }
                        """))
                .andExpectAll(
                        result -> {
                            System.out.println(result.getResponse().getContentAsString());
                        }
                );

    }
}
