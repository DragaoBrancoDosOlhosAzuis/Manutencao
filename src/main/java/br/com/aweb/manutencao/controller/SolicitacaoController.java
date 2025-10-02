package br.com.aweb.manutencao.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import br.com.aweb.manutencao.model.Solicitacao;
import br.com.aweb.manutencao.repository.SolicitacaoRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/solicitacao")
public class SolicitacaoController {

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @GetMapping
    public ModelAndView list() {
        return new ModelAndView("list", Map.of("solicitacoes",
                solicitacaoRepository.findAll(Sort.by(Sort.Direction.DESC, "dataHoraAbertura"))));
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("form", Map.of("solicitacao", new Solicitacao()));
    }

    @PostMapping("/create")
    public String create(@Valid Solicitacao solicitacao, BindingResult result) {
        if (result.hasErrors())
            return "form";
        solicitacaoRepository.save(solicitacao);
        return "redirect:/solicitacao";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        var solicitacao = solicitacaoRepository.findById(id);
        if (solicitacao.isPresent() && "ABERTA".equals(solicitacao.get().getStatus()))
            return new ModelAndView("form", Map.of("solicitacao", solicitacao.get()));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/edit/{id}")
    public String edit(@Valid Solicitacao solicitacao, BindingResult result) {
        if (result.hasErrors())
            return "form";
        solicitacaoRepository.save(solicitacao);
        return "redirect:/solicitacao";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id) {
        var solicitacao = solicitacaoRepository.findById(id);
        if (solicitacao.isPresent())
            return new ModelAndView("delete", Map.of("solicitacao", solicitacao.get()));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/delete/{id}")
    public String delete(Solicitacao solicitacao) {
        solicitacaoRepository.delete(solicitacao);
        return "redirect:/solicitacao";
    }

    // Página para finalizar a solicitação
    @GetMapping("/finish/{id}")
    public ModelAndView finishPage(@PathVariable Long id) {
        var optionalSolicitacao = solicitacaoRepository.findById(id);
        if (optionalSolicitacao.isPresent() && "ABERTA".equals(optionalSolicitacao.get().getStatus())) {
            return new ModelAndView("finish", Map.of("solicitacao", optionalSolicitacao.get()));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    // Finalizar a solicitação
    @PostMapping("/finish/{id}")
    public String finish(@PathVariable Long id, @Valid Solicitacao solicitacaoAtualizada, BindingResult result) {
        var optionalSolicitacao = solicitacaoRepository.findById(id);
        
        if (optionalSolicitacao.isPresent() && "ABERTA".equals(optionalSolicitacao.get().getStatus())) {
            if (result.hasErrors()) {
                return "finish";
            }
            
            var solicitacao = optionalSolicitacao.get();
            solicitacao.setStatus("FINALIZADA");
            solicitacao.setDataHoraEncerramento(LocalDateTime.now());
            solicitacao.setResponsavelManutencao(solicitacaoAtualizada.getResponsavelManutencao());
            solicitacao.setObservacoesManutencao(solicitacaoAtualizada.getObservacoesManutencao());
            
            solicitacaoRepository.save(solicitacao);
            return "redirect:/solicitacao";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    
    // Visualizar detalhes da solicitação
    @GetMapping("/view/{id}")
    public ModelAndView view(@PathVariable Long id) {
        var solicitacao = solicitacaoRepository.findById(id);
        if (solicitacao.isPresent()) {
            return new ModelAndView("view", Map.of("solicitacao", solicitacao.get()));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}