package com.juancassemiro.backendprojetosd.Prova.controller;


import com.juancassemiro.backendprojetosd.Prova.dto.ProvaAlunoDto;
import com.juancassemiro.backendprojetosd.Prova.dto.ProvaCadastroDto;
import com.juancassemiro.backendprojetosd.Prova.dto.ProvaDto;
import com.juancassemiro.backendprojetosd.Prova.dto.RespostaAlunoDto;
import com.juancassemiro.backendprojetosd.Prova.repository.ProvaRepository;
import com.juancassemiro.backendprojetosd.Prova.service.ProvaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/prova")
public class ProvaController {

    private final ProvaService provaService;

    public ProvaController(ProvaService provaService, ProvaRepository provaRepository) {
        this.provaService = provaService;
    }

    @GetMapping("/{idProva}")
    public ResponseEntity<ProvaDto> buscarProvaPorId(@PathVariable Long idProva) {
        ProvaDto prova = provaService.buscarProvaPorId(idProva);
        return ResponseEntity.ok(prova);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<ProvaDto> cadastrarProva(@RequestBody ProvaCadastroDto dto,
                                                   UriComponentsBuilder uriBuilder) {
        ProvaDto prova = provaService.criarProva(dto);
        URI uri = uriBuilder.path("prova/{idProva}").buildAndExpand(prova.id()).toUri();
        return ResponseEntity.created(uri).body(prova);
    }

    @PutMapping("/{idProva}")
    public ResponseEntity<ProvaDto> atualizarProva(@RequestBody ProvaCadastroDto dto,
                                                   @PathVariable Long idProva){
        ProvaDto prova = provaService.atualizarProva(idProva,dto);
        return ResponseEntity.ok(prova);
    }

    @PostMapping("/responder-prova/{idProva}/{idAluno}")
    public ResponseEntity<Void> alunoResponderProva(@PathVariable("idProva") Long idProva,
                                                    @PathVariable("idAluno")Long idAluno,
                                                    @RequestBody List<RespostaAlunoDto> dto){
        provaService.alunoResponderProva(idProva,idAluno,dto);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para listar todas as provas disponíveis para um aluno específico.
     *
     * Exemplo de uso: GET /provas/aluno/123
     *
     * @param alunoId O ID do aluno.
     * @return Uma lista de DTOs de Prova.
     */
    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<ProvaAlunoDto>> listarProvasPorAluno(@PathVariable Long alunoId) {
        List<ProvaAlunoDto> provasDoAluno = provaService.listarProvasPorAluno(alunoId);
        return ResponseEntity.ok(provasDoAluno);
    }

    @DeleteMapping("/{idProva}")
    public ResponseEntity<Void> deletarProva(@PathVariable Long idProva){
        provaService.deletarProva(idProva);
        return ResponseEntity.noContent().build();
    }
}
