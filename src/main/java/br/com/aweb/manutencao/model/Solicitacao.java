package br.com.aweb.manutencao.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @NotBlank(message = "Nome do solicitante é obrigatório")
    @Column(length = 100, nullable = false)
    private String nomeSolicitante;
    
    @Size(min = 10, max = 500, message = "Descrição deve ter entre 10 e 500 caracteres")
    @NotBlank(message = "Descrição do problema é obrigatória")
    @Column(length = 500, nullable = false)
    private String descricaoProblema;
    
    @Column(nullable = false)
    private LocalDateTime dataHoraAbertura = LocalDateTime.now();
    
    @Column(length = 20, nullable = false)
    private String status = "ABERTA"; // ABERTA, EM_ANDAMENTO, FINALIZADA
    
    @Column(nullable = true)
    private LocalDateTime dataHoraEncerramento;
    
    @Size(max = 500, message = "Observações não podem ultrapassar 500 caracteres")
    @Column(length = 500, nullable = true)
    private String observacoesManutencao;
    
    @Size(max = 100, message = "Nome do responsável não pode ultrapassar 100 caracteres")
    @Column(length = 100, nullable = true)
    private String responsavelManutencao;
}