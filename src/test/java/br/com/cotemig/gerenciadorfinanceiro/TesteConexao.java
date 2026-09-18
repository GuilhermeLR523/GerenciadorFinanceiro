package br.com.cotemig.gerenciadorfinanceiro;

import br.com.cotemig.gerenciadorfinanceiro.config.JPAUtil;
import br.com.cotemig.gerenciadorfinanceiro.model.Meta;
import br.com.cotemig.gerenciadorfinanceiro.service.MetaService;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TesteConexao {

    public static void main(String[] args) {

        MetaService metaService = new MetaService();

        try {

            // 1 - CRIAR META
            Meta meta = metaService.cadastrar(
                    "Comprar Notebook",
                    new BigDecimal("5000.00"),
                    LocalDate.now().plusMonths(6)
            );

            System.out.println("Meta criada!");
            System.out.println("ID: " + meta.getId());
            System.out.println("Objetivo: R$ " + meta.getValorObjetivo());
            System.out.println("Valor atual: R$ " + meta.getValorAtual());


            // 2 - ADICIONAR VALOR
            metaService.adicionarValor(
                    meta.getId(),
                    new BigDecimal("1000.00")
            );


            // 3 - BUSCAR META ATUALIZADA
            Meta metaAtualizada =
                    metaService.buscarPorId(meta.getId());

            System.out.println("\nDepois de adicionar R$ 1000:");

            System.out.println(
                    "Valor atual: R$ "
                            + metaAtualizada.getValorAtual()
            );


            // 4 - CALCULAR PROGRESSO
            BigDecimal progresso =
                    metaService.calcularProgresso(meta.getId());

            System.out.println(
                    "Progresso: "
                            + progresso
                            + "%"
            );


            // 5 - VERIFICAR CONCLUSÃO
            boolean concluida =
                    metaService.verificarConclusao(meta.getId());

            System.out.println(
                    "Meta concluída: "
                            + concluida
            );


        } catch (Exception e) {

            System.out.println(
                    "ERRO: " + e.getMessage()
            );

            e.printStackTrace();

        } finally {

            JPAUtil.fechar();
        }
    }
}