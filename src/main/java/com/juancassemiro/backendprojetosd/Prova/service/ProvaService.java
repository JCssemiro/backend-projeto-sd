package com.juancassemiro.backendprojetosd.Prova.service;


import com.juancassemiro.backendprojetosd.Disciplina.model.Disciplina;
import com.juancassemiro.backendprojetosd.Disciplina.repository.DisciplinaRepository;
import com.juancassemiro.backendprojetosd.Prova.dto.ProvaAlunoDto;
import com.juancassemiro.backendprojetosd.Prova.dto.ProvaCadastroDto;
import com.juancassemiro.backendprojetosd.Prova.dto.ProvaDto;
import com.juancassemiro.backendprojetosd.Prova.dto.RespostaAlunoDto;
import com.juancassemiro.backendprojetosd.Prova.repository.ProvaRepository;
import com.juancassemiro.backendprojetosd.Questao.model.Questao;
import com.juancassemiro.backendprojetosd.Questao.model.ResultadoAluno;
import com.juancassemiro.backendprojetosd.Questao.model.ResultadoAlunoId;
import com.juancassemiro.backendprojetosd.Questao.repository.QuestaoRepository;
import com.juancassemiro.backendprojetosd.Questao.repository.ResultadoAlunoRepository;
import com.juancassemiro.backendprojetosd.Usuario.model.Usuario;
import com.juancassemiro.backendprojetosd.Usuario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.juancassemiro.backendprojetosd.Prova.model.Prova;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProvaService {

    private final ProvaRepository provaRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final QuestaoRepository questaoRepository; // Para persistir as questões aninhadas
    private final UsuarioRepository usuarioRepository; // Para lidar com o aluno ao responder a prova
    private final ResultadoAlunoRepository resultadoAlunoRepository;

    @Autowired
    public ProvaService(ProvaRepository provaRepository,
                        DisciplinaRepository disciplinaRepository,
                        QuestaoRepository questaoRepository,
                        UsuarioRepository usuarioRepository, ResultadoAlunoRepository resultadoAlunoRepository) {
        this.provaRepository = provaRepository;
        this.disciplinaRepository = disciplinaRepository;
        this.questaoRepository = questaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.resultadoAlunoRepository = resultadoAlunoRepository;
    }

    /**
     * Cria uma nova prova.
     * As questões associadas também são criadas.
     * @param provaDTO DTO com os dados da prova a ser criada.
     * @return O DTO da prova criada.
     * @throws NoSuchElementException Se a disciplina não for encontrada.
     * @throws IllegalArgumentException Se as questões forem inválidas.
     */
    @Transactional
    public ProvaDto criarProva(ProvaCadastroDto provaDTO) {
        // Busca a disciplina associada
        Disciplina disciplina = disciplinaRepository.findById(provaDTO.disciplinaId())
                .orElseThrow(() -> new NoSuchElementException("Disciplina com ID " + provaDTO.disciplinaId() + " não encontrada."));

        // Cria a entidade Prova
        Prova prova = new Prova();
        prova.setDisciplina(disciplina);
        prova.setTitulo(provaDTO.titulo());
        prova.setDescricao(provaDTO.descricao());
        prova.setData(provaDTO.data());
        prova.setTotalQuestoes(provaDTO.questoes().size());

        // Inicia o set de questões vazio
        prova.setQuestoes(List.of());
        Prova provaSalva = provaRepository.save(prova);

        // Valida e associa as questões à prova
        if (!provaDTO.questoes().isEmpty()) {
            List<Questao> questoesDaProva = provaDTO.questoes().stream()
                    .map(qDto -> {
                        Questao questao = new Questao(qDto); // Construtor de Questao que aceita QuestaoCadastroDto
                        questao.setProva(provaSalva); // Associa a questão à prova
                        return questao;
                    })
                    .toList();
            questaoRepository.saveAll(questoesDaProva);
            prova.setTotalQuestoes(questoesDaProva.size());
        } else {
            prova.setTotalQuestoes(0);
        }

        // Salva a prova (e as questões em cascata, se configurado no OneToMany)
        // Se a cascata não estiver configurada, você precisaria salvar cada questão individualmente

        // Se o cascade não for ALL ou PERSIST na Prova para Questao, salvar as questões separadamente
        // provaSalva.getQuestoes().forEach(questaoRepository::save);

        return new ProvaDto(provaSalva);
    }

    /**
     * Busca uma prova pelo ID.
     * @param id ID da prova.
     * @return O DTO da prova encontrada.
     * @throws NoSuchElementException Se a prova não for encontrada.
     */
    public ProvaDto buscarProvaPorId(Long id) {
        return new ProvaDto(provaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Prova com ID " + id + " não encontrada.")));
    }

    /**
     * Lista todas as provas com paginação.
     * @param pagina O número da página (base 1).
     * @param porPagina O número de itens por página.
     * @return Uma lista paginada de DTOs de provas.
     */
    public List<ProvaDto> listarTodasProvas(int pagina, int porPagina) {
        Pageable pageable = PageRequest.of(pagina - 1, porPagina);
        return provaRepository.findAll(pageable)
                .stream()
                .map(ProvaDto::new)
                .toList();
    }

    /**
     * Atualiza uma prova existente.
     * Permite atualizar os dados básicos da prova e suas questões.
     * @param id ID da prova a ser atualizada.
     * @param provaAtualizacaoDto DTO com os dados atualizados da prova.
     * @return O DTO da prova atualizada.
     * @throws NoSuchElementException Se a prova ou disciplina não for encontrada.
     */
    @Transactional
    public ProvaDto atualizarProva(Long id, ProvaCadastroDto provaAtualizacaoDto) {
        Prova provaExistente = provaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Prova com ID " + id + " não encontrada para atualização."));

        // Atualiza a disciplina se o ID for diferente
        if (provaAtualizacaoDto.disciplinaId() != null &&
                (provaExistente.getDisciplina() == null ||
                        !provaAtualizacaoDto.disciplinaId().equals(provaExistente.getDisciplina().getId()))) {
            Disciplina novaDisciplina = disciplinaRepository.findById(provaAtualizacaoDto.disciplinaId())
                    .orElseThrow(() -> new NoSuchElementException("Disciplina com ID " + provaAtualizacaoDto.disciplinaId() + " não encontrada."));
            provaExistente.setDisciplina(novaDisciplina);
        }

        provaExistente.setTitulo(provaAtualizacaoDto.titulo());
        provaExistente.setDescricao(provaAtualizacaoDto.descricao());
        provaExistente.setData(provaAtualizacaoDto.data());
        provaExistente.setAtiva(provaExistente.getAtiva());

        // --- Lógica para Questões (OneToMany) ---
        // Opção: Deletar questões antigas e criar novas. CUIDADO com dados relacionados (respostas de alunos)!
        // Uma abordagem mais robusta seria identificar quais questões foram adicionadas/removidas/modificadas.
        if (provaAtualizacaoDto.questoes() != null) {
            // Remove as questões antigas (se houver CASCADE.ALL ou ORPHAN_REMOVAL configurado)
            if (provaExistente.getQuestoes() != null) {
                questaoRepository.deleteAllByProvaId(id);
            }

            // Adiciona as novas questões com a associação à prova existente
            List<Questao> novasQuestoes = provaAtualizacaoDto.questoes().stream()
                    .map(qDto -> {
                        Questao novaQuestao = new Questao(qDto);
                        novaQuestao.setProva(provaExistente); // Associa a nova questão à prova existente
                        return novaQuestao;
                    })
                    .toList();

            provaExistente.setTotalQuestoes(novasQuestoes.size());
        } else {
            // Se o DTO não fornecer questões, limpa o total de questões
            provaExistente.setTotalQuestoes(0);
            if (provaExistente.getQuestoes() != null) {
                provaExistente.getQuestoes().clear(); // Se não quiser apagar, remova esta linha
            }
        }


        Prova provaAtualizada = provaRepository.save(provaExistente);
        return new ProvaDto(provaAtualizada);
    }

    /**
     * Deleta uma prova pelo ID.
     * @param id ID da prova a ser deletada.
     * @throws NoSuchElementException Se a prova não for encontrada.
     */
    @Transactional
    public void deletarProva(Long id) {
        if (!provaRepository.existsById(id)) {
            throw new NoSuchElementException("Prova com ID " + id + " não encontrada para exclusão.");
        }
        provaRepository.deleteById(id);
    }


    /**
     * Permite que um aluno responda a uma prova, registrando suas respostas na entidade ResultadoAluno.
     *
     * @param provaId O ID da prova que o aluno está respondendo.
     * @param alunoId O ID do aluno que está respondendo.
     * @param respostasAluno Lista de DTOs contendo o ID da questão e a alternativa escolhida pelo aluno.
     * @throws NoSuchElementException Se a prova, aluno ou alguma questão não for encontrada.
     * @throws IllegalArgumentException Se uma questão não pertencer à prova especificada ou a resposta for inválida.
     * @throws IllegalStateException Se o aluno já tiver respondido a uma das questões.
     */
    @Transactional
    public void alunoResponderProva(Long provaId, Long alunoId, List<RespostaAlunoDto> respostasAluno) {
        // 1. Busca a Prova e o Aluno
        Prova prova = provaRepository.findById(provaId)
                .orElseThrow(() -> new NoSuchElementException("Prova com ID " + provaId + " não encontrada."));
        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new NoSuchElementException("Aluno com ID " + alunoId + " não encontrado."));

        // Carregar todas as questões da prova para validação eficiente
        Set<Long> questoesIdsDaProva = prova.getQuestoes().stream()
                .map(Questao::getId)
                .collect(Collectors.toSet());

        if (questoesIdsDaProva.isEmpty()) {
            throw new IllegalArgumentException("A prova com ID " + provaId + " não possui questões associadas.");
        }

        // 2. Processa cada resposta do aluno
        for (RespostaAlunoDto respostaDto : respostasAluno) {
            Long questaoId = respostaDto.questaoId();
            String alternativaEscolhida = respostaDto.alternativa();

            Questao questao = questaoRepository.findById(questaoId)
                    .orElseThrow(() -> new NoSuchElementException("Questão com ID " + questaoId + " não encontrada."));

            // Valida se a questão realmente pertence à prova
            if (!questoesIdsDaProva.contains(questaoId)) {
                throw new IllegalArgumentException("Questão com ID " + questaoId + " não pertence à Prova com ID " + provaId + ".");
            }

            // Cria a chave composta para verificar se já existe resposta
            ResultadoAlunoId resultadoAlunoId = new ResultadoAlunoId(aluno, questao);

            // Valida se o aluno já respondeu a esta questão
            boolean jaRespondeu = resultadoAlunoRepository.findById(resultadoAlunoId).isPresent();
            if (jaRespondeu) {
                throw new IllegalStateException("O aluno com ID " + alunoId + " já respondeu à questão com ID " + questaoId + " nesta prova.");
            }

            // Opcional: Validar se a alternativa escolhida é válida (A, B, C, D, E)
            if (alternativaEscolhida == null || alternativaEscolhida.trim().isEmpty() ||
                    !Set.of("A", "B", "C", "D", "E").contains(alternativaEscolhida.toUpperCase())) {
                throw new IllegalArgumentException("Alternativa '" + alternativaEscolhida + "' inválida para a questão " + questao.getId() + ". Escolha entre A, B, C, D, E.");
            }

            // 3. Cria e preenche a entidade ResultadoAluno
            // Usando o novo construtor auxiliar
            boolean acertou = questao.getAlternativaCorreta().equalsIgnoreCase(alternativaEscolhida);
            ResultadoAluno resultadoAluno = new ResultadoAluno(
                    new ResultadoAlunoId(aluno,questao),
                    alternativaEscolhida.toUpperCase(),
                    acertou
            );

            // 4. Salva o ResultadoAluno no banco de dados
            resultadoAlunoRepository.save(resultadoAluno);
        }
        // Você pode retornar algo aqui, como um DTO de resultado da prova, ou simplesmente void/success
    }

    /**
     * Lista todas as provas disponíveis para um aluno específico, incluindo o status de 'ativa' e 'jaRespondida'.
     *
     * @param alunoId O ID do aluno.
     * @return Uma lista de ProvaAlunoDto.
     * @throws NoSuchElementException Se o aluno não for encontrado.
     */
    @Transactional
    public List<ProvaAlunoDto> listarProvasPorAluno(Long alunoId) {
        // 1. Busca o aluno.
        Usuario aluno = usuarioRepository.findById(alunoId)
                .orElseThrow(() -> new NoSuchElementException("Aluno com ID " + alunoId + " não encontrado."));

        // 2. Obtém os IDs das disciplinas cursadas pelo aluno.
        List<Long> disciplinasIds = aluno.getMatriculas().stream()
                .map(disciplina -> disciplina.getId().getDisciplina().getId())
                .collect(Collectors.toList());

        if (disciplinasIds.isEmpty()) {
            return List.of();
        }

        // 3. Busca todas as provas que pertencem a essas disciplinas.
        List<Prova> provas = provaRepository.findByDisciplinaIdIn(disciplinasIds);

        // 4. Mapeia cada prova para o DTO e preenche os campos 'ativa' e 'jaRespondida'.
        return provas.stream()
                .map(prova -> {

                    // Lógica para determinar se a prova foi respondida
                    // A prova é considerada "respondida" se o aluno tiver pelo menos uma resposta registrada.
                    boolean jaRespondeu = !resultadoAlunoRepository.findByIdAlunoAndInListQuestao(aluno,prova.getQuestoes()).isEmpty();
                    return new ProvaAlunoDto(prova,jaRespondeu);
                })
                .collect(Collectors.toList());
    }
}
