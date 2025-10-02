package br.com.aweb.manutencao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import br.com.aweb.manutencao.model.Manutencao;
import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long>{
    List<Manutencao> findByStatus(Manutencao.Status status, Sort sort);
    List<Manutencao> findBySolicitanteContainingIgnoreCase(String solicitante, Sort sort);
}