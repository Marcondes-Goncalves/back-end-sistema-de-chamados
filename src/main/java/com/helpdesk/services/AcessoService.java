package com.helpdesk.services;

import org.springframework.stereotype.Service;

import com.helpdesk.exception.ExceptionMsg;
import com.helpdesk.models.Acesso;
import com.helpdesk.repository.AcessoRepository;

@Service 
public class AcessoService {
    
    private AcessoRepository acessoRepository;

    public AcessoService(AcessoRepository acessoRepository) {
        this.acessoRepository = acessoRepository;
    }

    public String criarAcesso(Acesso acesso) {

        String acessoUPPER = acesso.getAcesso().toUpperCase();

        Acesso existeAcesso = acessoRepository.buscarAcessoPorDescricao(acessoUPPER);

        if (existeAcesso != null) {
            throw new ExceptionMsg("Acesso já existe: " + acessoUPPER);
        }

        acesso.setAcesso(acessoUPPER);
        acessoRepository.save(acesso);

        return "Acesso criado: " + acessoUPPER;
    }
}
