package br.com.aweb.manutencao.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Size(min = 3, max = 100)
    @NotBlank(message = "O nome do solicitante é obrigatório")
    @Column(length = 100, nullable = false)
    private String solicitante;
    
    @Size(min = 10, max = 500)
    @NotBlank(message = "A descrição do problema é obrigatória")
    @Column(length = 500, nullable = false)
    private String descricaoProblema;
    
    @Column(nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();
    
    @Column(nullable = true)
    private LocalDateTime dataEncerramento;
    
    @Size(max = 100)
    @Column(length = 100, nullable = true)
    private String responsavelManutencao;
    
    @Size(max = 500)
    @Column(length = 500, nullable = true)
    private String observacoes;
    
    @NotBlank(message = "O local/setor é obrigatório")
    @Column(length = 100, nullable = false)
    private String localSetor;
    
    @NotNull(message = "A prioridade é obrigatória")
    @Column(nullable = false)
    private Prioridade prioridade;
    
    @Column(nullable = false)
    private Status status = Status.ABERTA;

    public enum Prioridade {
        BAIXA, MEDIA, ALTA, URGENTE
    }
    
    public enum Status {
        ABERTA, EM_ANDAMENTO, CONCLUIDA, CANCELADA
    }
}