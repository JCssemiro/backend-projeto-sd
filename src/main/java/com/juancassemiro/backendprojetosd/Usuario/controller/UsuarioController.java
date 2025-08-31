package com.juancassemiro.backendprojetosd.Usuario.controller;


import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaDto;
import com.juancassemiro.backendprojetosd.Disciplina.model.Disciplina;
import com.juancassemiro.backendprojetosd.Disciplina.service.DisciplinaService;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioCadastroDto;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioDadosDto;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioDto;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import com.juancassemiro.backendprojetosd.Usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final DisciplinaService disciplinaService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService,
                             DisciplinaService disciplinaService) {
        this.usuarioService = usuarioService;
        this.disciplinaService = disciplinaService;
    }

    @GetMapping("/disciplina/{idDisciplina}")
    public ResponseEntity<List<UsuarioDadosDto>> listarTodosAlunosDisciplina(@PathVariable Long idDisciplina,
                                                                             @RequestParam(name = "pagina",defaultValue = "1")int pagina,
                                                                             @RequestParam(name = "porPagina",defaultValue = "20")int porPagina
                                                                             ){
        List<UsuarioDadosDto> alunos = disciplinaService.buscarAlunosPorDisciplinaId(idDisciplina,pagina,porPagina);
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDto> buscarUsuarioPorId(@PathVariable Long idUsuario){
        UsuarioDto usuario = usuarioService.buscarUsuarioPorId(idUsuario);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioDto> cadastrarUsuario(@RequestBody @Valid UsuarioCadastroDto usuarioDto,
                                                       UriComponentsBuilder uriBuilder){
        UsuarioDto usuarioCadastrado = usuarioService.criarUsuario(usuarioDto);
        URI uri = uriBuilder.path("/usuario/{idUsuario}").buildAndExpand(usuarioCadastrado.id()).toUri();
        return ResponseEntity.created(uri).body(usuarioCadastrado);
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long idUsuario){
        usuarioService.deletarUsuario(idUsuario);
        return ResponseEntity.noContent().build();
    }

}
