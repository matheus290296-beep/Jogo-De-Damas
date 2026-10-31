import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Arquivos {
    static Path caminhoPlacar = Path.of("Placar.txt");
    static Path caminhoReplays = Path.of("Replays.txt");

    public static void garantirIntegridade() throws IOException {
        if (!Files.exists(caminhoPlacar)) {
            Files.createFile(caminhoPlacar);
            Files.writeString(caminhoPlacar, "0;0;0");
        }

        if (!Files.exists(caminhoReplays)) {
            Files.createFile(caminhoReplays);
        }

        String[] placarFormatado = Files.readString(caminhoPlacar).split(";");
        if (placarFormatado.length != 3) {
            placarCorrompido();
        }
        try {
            for (int i = 0; i < 3; i++) {
                Integer.parseInt(placarFormatado[0]);
            }
        }
        catch (Exception e) {
            placarCorrompido();
        }
    }

    public static void placarCorrompido() throws IOException {
        System.out.println("O arquivo Placar.txt está corrompido!");
        System.out.println("Deseja criar um novo Placar.txt, ou deseja sair do programa?");
        System.out.println("1 - Criar novo Placar");
        System.out.println("2 - Sair");
        int escolha = ValidadorInput.obterInt(1, 2);
        switch (escolha) {
            case 1:
                Files.writeString(caminhoPlacar, "0;0;0");
                break;

            case 2:
                System.exit(0);
        }
    }

    public static void mostrarPlacar() throws IOException {
        System.out.println("===== PLACAR =====");
        String[] placarFormatado = Files.readString(caminhoPlacar).split(";");
        System.out.printf("Vitórias: %s\n", placarFormatado[0]);
        System.out.printf("Derrotas: %s\n", placarFormatado[1]);
        System.out.printf("Empates: %s\n", placarFormatado[2]);
    }

    public static void atualizarPlacar(String vencedor) throws IOException {
        String[] placar = Files.readString(caminhoPlacar).split(";");

        switch (vencedor) {
            case "□" -> {
                int vitorias = Integer.parseInt(placar[0]) + 1;
                placar[0] = String.valueOf(vitorias);
            }
            case "○" -> {
                int derrotas = Integer.parseInt(placar[1]) + 1;
                placar[1] = String.valueOf(derrotas);
            }
            case "empate" -> {
                int empates = Integer.parseInt(placar[2]) + 1;
                placar[2] = String.valueOf(empates);
            }
            default -> {
                System.out.println("Erro 8");
                System.exit(8);
            }
        }
        String placarFormatado = placar[0] + ";" + placar[1] + ";" + placar[2];
        Files.writeString(caminhoPlacar, placarFormatado);
    }

    public static void gravarReplay(String replay) throws IOException {
        List<String> linhas = Files.readAllLines(caminhoReplays);
        linhas.add(replay);
        Files.write(caminhoReplays, linhas);
    }

    public static String[] obterReplays() throws IOException {
        List<String> linhas = Files.readAllLines(caminhoReplays);
        String[] replays = new String[linhas.size()];
        for (int linha = 0; linha < linhas.size(); linha++) {
            replays[linha] = linhas.get(linha);
        }
        return replays;
    }
}
