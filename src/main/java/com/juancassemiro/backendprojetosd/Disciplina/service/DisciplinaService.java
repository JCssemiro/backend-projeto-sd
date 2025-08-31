package com.juancassemiro.backendprojetosd.Disciplina.service;


import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaCadastroDto;
import com.juancassemiro.backendprojetosd.Disciplina.dto.DisciplinaDto;
import com.juancassemiro.backendprojetosd.Disciplina.model.Disciplina;
import com.juancassemiro.backendprojetosd.Disciplina.model.Matricula;
import com.juancassemiro.backendprojetosd.Disciplina.model.MatriculaId;
import com.juancassemiro.backendprojetosd.Disciplina.repository.DisciplinaRepository;
import com.juancassemiro.backendprojetosd.Disciplina.repository.MatriculaRepository;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioDadosDto;
import com.juancassemiro.backendprojetosd.Usuario.dto.UsuarioDto;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import com.juancassemiro.backendprojetosd.Usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {


    private final DisciplinaRepository disciplinaRepository;
    private final UsuarioRepository usuarioRepository; // Para lidar com Professor e Alunos
    private final MatriculaRepository matriculaRepository;

    @Autowired
    public DisciplinaService(DisciplinaRepository disciplinaRepository,
                             UsuarioRepository usuarioRepository,
                             MatriculaRepository matriculaRepository) {
        this.disciplinaRepository = disciplinaRepository;
        this.usuarioRepository = usuarioRepository;
        this.matriculaRepository = matriculaRepository;
    }


    /**
     * Cria uma nova disciplina.
     * @param disciplinaDTO DTO com os dados da disciplina a ser criada.
     * @return O DTO da disciplina criada.
     * @throws NoSuchElementException Se o professor ou algum aluno não for encontrado.
     */
    @Transactional
    public DisciplinaDto criarDisciplina(DisciplinaCadastroDto disciplinaDTO) {
        // Cria a entidade Disciplina, assumindo que o construtor do DTO lida com campos básicos
        Disciplina disciplina = new Disciplina(disciplinaDTO);

        Usuario professor = usuarioRepository.findById(disciplinaDTO.professorId())
                .orElseThrow(() -> new NoSuchElementException("Professor com ID " + disciplinaDTO.professorId() + " não encontrado."));
        if(professor.getTipoUsuario().getId() != 2) { //Se não for professor
            throw new RuntimeException("Usuário informado não é do tipo professor");
        }
        disciplina.setProfessor(professor);

        // Salva a nova disciplina no banco de dados
        Disciplina disciplinaSalva = disciplinaRepository.save(disciplina);


        // --- Lógica para Alunos (ManyToMany) ---
        if (disciplinaDTO.alunosIds() != null && !disciplinaDTO.alunosIds().isEmpty()) {
            Set<Usuario> alunos = disciplinaDTO.alunosIds().stream()
                    .map(alunoId -> usuarioRepository.findById(alunoId)
                            .orElseThrow(() -> new NoSuchElementException("Aluno com ID " + alunoId + " não encontrado.")))
                    .collect(Collectors.toSet());
            Set<Matricula> novasMatriculas = alunos.stream().map(aluno-> new Matricula(new MatriculaId(aluno,disciplinaSalva),1)).collect(Collectors.toSet());
            matriculaRepository.saveAll(novasMatriculas);

        }
        // Retorna o DTO da disciplina criada
        return new DisciplinaDto(disciplinaSalva);
    }

    /**
     * Busca uma disciplina pelo ID.
     * @param id ID da disciplina.
     * @return O DTO da disciplina encontrada.
     * @throws NoSuchElementException Se a disciplina não for encontrada.
     */
    public DisciplinaDto buscarDisciplinaPorId(Long id) {
        // Busca a disciplina. Lança NoSuchElementException se não encontrada.
        try {
            return new DisciplinaDto(disciplinaRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Disciplina com ID " + id + " não encontrada.")));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lista todas as disciplinas com paginação.
     * @param pagina O número da página (base 1).
     * @param porPagina O número de itens por página.
     * @return Uma lista paginada de DTOs de disciplinas.
     */
    public List<DisciplinaDto> listarTodasDisciplinas(int pagina, int porPagina) {
        // Cria um objeto Pageable para a paginação. PageRequest é base 0.
        Pageable pageable = PageRequest.of(pagina - 1, porPagina);

        // Encontra todas as disciplinas paginadas e as mapeia para DTOs
        return disciplinaRepository.findAll(pageable)
                .stream()
                .map(DisciplinaDto::new) // Mapeia cada entidade para um DTO usando o construtor
                .toList();
    }

    /**
     * Atualiza uma disciplina existente.
     * @param id ID da disciplina a ser atualizada.
     * @param disciplinaAtualizacaoDto DTO com os dados atualizados da disciplina.
     * @return O DTO da disciplina atualizada.
     * @throws NoSuchElementException Se a disciplina, professor ou algum aluno não for encontrado.
     */
    @Transactional
    public DisciplinaDto atualizarDisciplina(Long id, DisciplinaCadastroDto disciplinaAtualizacaoDto) {
        // Busca a disciplina existente ou lança uma exceção
        Disciplina disciplinaExistente = disciplinaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Disciplina com ID " + id + " não encontrada para atualização."));

        // Atualiza o campo nome
        disciplinaExistente.setNome(disciplinaAtualizacaoDto.nome());

        // --- Lógica para Professor (ManyToOne) ---
        // Verifica se o ID do professor foi fornecido no DTO e se é diferente do atual
        if (disciplinaAtualizacaoDto.professorId() != null &&
                (disciplinaExistente.getProfessor() == null ||
                        !disciplinaAtualizacaoDto.professorId().equals(disciplinaExistente.getProfessor().getId()))) {
            Usuario novoProfessor = usuarioRepository.findById(disciplinaAtualizacaoDto.professorId())
                    .orElseThrow(() -> new NoSuchElementException("Novo Professor com ID " + disciplinaAtualizacaoDto.professorId() + " não encontrado."));
            disciplinaExistente.setProfessor(novoProfessor);
        } else if (disciplinaAtualizacaoDto.professorId() == null && disciplinaExistente.getProfessor() != null) {
            // Se o DTO enviar null para o professor, e já existir um professor, remova a associação
            disciplinaExistente.setProfessor(null);
        }


        // --- Lógica para Alunos (ManyToMany) ---
        // Limpa a lista de alunos existente e adiciona os novos do DTO
        if (disciplinaAtualizacaoDto.alunosIds() != null) {
            Set<Usuario> novosAlunos = disciplinaAtualizacaoDto.alunosIds().stream()
                    .map(alunoId -> usuarioRepository.findById(alunoId)
                            .orElseThrow(() -> new NoSuchElementException("Aluno com ID " + alunoId + " não encontrado.")))
                    .collect(Collectors.toSet());
            Set<Matricula> novasMatriculas = novosAlunos.stream().map(aluno-> new Matricula(new MatriculaId(aluno,disciplinaExistente),1)).collect(Collectors.toSet());
            matriculaRepository.saveAll(novasMatriculas);
        } else {
            // Se o DTO não fornecer alunosIds, pode significar que todos devem ser removidos
            matriculaRepository.deleteAllByIdDisciplina(id);
        }


        // Salva as alterações no banco de dados
        Disciplina disciplinaAtualizada = disciplinaRepository.save(disciplinaExistente);
        // Retorna o DTO da disciplina atualizada
        return new DisciplinaDto(disciplinaAtualizada);
    }

    /**
     * Deleta uma disciplina pelo ID.
     * @param id ID da disciplina a ser deletada.
     * @throws NoSuchElementException Se a disciplina não for encontrada.
     */
    @Transactional
    public void deletarDisciplina(Long id) {
        // Verifica se a disciplina existe antes de tentar deletar
        if (!disciplinaRepository.existsById(id)) {
            throw new NoSuchElementException("Disciplina com ID " + id + " não encontrada para exclusão.");
        }
        disciplinaRepository.deleteById(id);
    }

    public List<UsuarioDadosDto> buscarAlunosPorDisciplinaId(Long id, int pagina, int porPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, porPagina);
        Disciplina disciplinaExistente = disciplinaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Disciplina com ID " + id + " não encontrada."));

        return disciplinaExistente.getMatriculas().stream().map(d-> new UsuarioDadosDto(d.getId().getAluno())).toList();
    }
}
