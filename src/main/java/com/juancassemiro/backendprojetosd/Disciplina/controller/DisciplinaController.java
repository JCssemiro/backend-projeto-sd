package com.juancassemiro.backendprojetosd.Disciplina.controller;


import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaCadastroDto;
import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaDadosDto;
import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaDto;
import com.juancassemiro.backendprojetosd.Disciplina.service.DisciplinaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

    private final DisciplinaService disciplinaService;

    public DisciplinaController(DisciplinaService disciplinaService) {
        this.disciplinaService = disciplinaService;
    }

    @GetMapping
    public ResponseEntity<List<DisciplinaDto>> listarTodasDisciplinas(@RequestParam(name="pagina",defaultValue = "1")int pagina,
                                                                      @RequestParam(name="porPagina",defaultValue = "20") int porPagina){
        List<DisciplinaDto> disciplinas = disciplinaService.listarTodasDisciplinas(pagina, porPagina);
        return ResponseEntity.ok(disciplinas);
    }

    @GetMapping("/{idDisciplina}")
    public ResponseEntity<DisciplinaDto> buscarDisciplinaPorId(@PathVariable Long idDisciplina){
        DisciplinaDto disciplina = disciplinaService.buscarDisciplinaPorId(idDisciplina);
        return ResponseEntity.ok(disciplina);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<DisciplinaDto> cadastrarDisciplina(@RequestBody DisciplinaCadastroDto disciplinaDto,
                                                             UriComponentsBuilder uriBuilder){
        DisciplinaDto disciplina = disciplinaService.criarDisciplina(disciplinaDto);
        URI uri = uriBuilder.path("/disciplina/{idDisciplina}").buildAndExpand(disciplina.id()).toUri();
        return ResponseEntity.created(uri).body(disciplina);
    }

    @PutMapping("/{idDisciplina}")
    public ResponseEntity<DisciplinaDto> atualizarDisciplina(@PathVariable Long idDisciplina,
                                                             @RequestBody DisciplinaCadastroDto dto){
        DisciplinaDto disciplina = disciplinaService.atualizarDisciplina(idDisciplina,dto);
        return ResponseEntity.ok(disciplina);
    }

    @DeleteMapping("/{idDisciplina}")
    public ResponseEntity<Void> deletarDisciplina(@PathVariable Long idDisciplina){
        disciplinaService.deletarDisciplina(idDisciplina);
        return ResponseEntity.noContent().build();
    }


}
