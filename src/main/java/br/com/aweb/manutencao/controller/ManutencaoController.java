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

import br.com.aweb.manutencao.model.Manutencao;
import br.com.aweb.manutencao.repository.ManutencaoRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/manutencao")
public class ManutencaoController {

    @Autowired
    ManutencaoRepository manutencaoRepository;

    @GetMapping
    public ModelAndView list() {
        var manutencoes = manutencaoRepository.findAll(Sort.by(Sort.Direction.DESC, "dataSolicitacao"));
        return new ModelAndView("list", Map.of("manutencoes", manutencoes));
    }

    @GetMapping("/create")
    public ModelAndView create() {
        return new ModelAndView("form", Map.of("manutencao", new Manutencao()));
    }

    @PostMapping("/create")
    public String create(@Valid Manutencao manutencao, BindingResult result) {
        if (result.hasErrors()) {
            return "form";
        }
        // Garantir que a data de solicitação seja definida
        if (manutencao.getDataSolicitacao() == null) {
            manutencao.setDataSolicitacao(LocalDateTime.now());
        }
        manutencaoRepository.save(manutencao);
        return "redirect:/manutencao";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id) {
        var manutencao = manutencaoRepository.findById(id);
        if (manutencao.isPresent() && manutencao.get().getStatus() != Manutencao.Status.CONCLUIDA) {
            return new ModelAndView("form", Map.of("manutencao", manutencao.get()));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/edit/{id}")
    public String edit(@Valid Manutencao manutencao, BindingResult result) {
        if (result.hasErrors()) {
            return "form";
        }
        manutencaoRepository.save(manutencao);
        return "redirect:/manutencao";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable Long id) {
        var manutencao = manutencaoRepository.findById(id);
        if (manutencao.isPresent()) {
            return new ModelAndView("delete", Map.of("manutencao", manutencao.get()));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/delete/{id}")
    public String delete(Manutencao manutencao) {
        manutencaoRepository.delete(manutencao);
        return "redirect:/manutencao";
    }

    @PostMapping("/iniciar/{id}")
    public String iniciar(@PathVariable Long id) {
        var optionalManutencao = manutencaoRepository.findById(id);
        if (optionalManutencao.isPresent() && optionalManutencao.get().getStatus() == Manutencao.Status.ABERTA) {
            var manutencao = optionalManutencao.get();
            manutencao.setStatus(Manutencao.Status.EM_ANDAMENTO);
            manutencaoRepository.save(manutencao);
            return "redirect:/manutencao";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/concluir/{id}")
    public String concluir(@PathVariable Long id) {
        var optionalManutencao = manutencaoRepository.findById(id);
        if (optionalManutencao.isPresent() && optionalManutencao.get().getStatus() != Manutencao.Status.CONCLUIDA) {
            var manutencao = optionalManutencao.get();
            manutencao.setStatus(Manutencao.Status.CONCLUIDA);
            manutencao.setDataEncerramento(LocalDateTime.now());
            manutencaoRepository.save(manutencao);
            return "redirect:/manutencao";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {
        var optionalManutencao = manutencaoRepository.findById(id);
        if (optionalManutencao.isPresent() && optionalManutencao.get().getStatus() != Manutencao.Status.CONCLUIDA) {
            var manutencao = optionalManutencao.get();
            manutencao.setStatus(Manutencao.Status.CANCELADA);
            manutencaoRepository.save(manutencao);
            return "redirect:/manutencao";
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}