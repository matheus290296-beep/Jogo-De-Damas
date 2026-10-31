import java.io.IOException;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Replays {
    static boolean debugging = false;

    // Essa classe é responsável por gravar e reproduzir replays
    // A interação com o arquivo Replays.txt continuará sendo responsabilidade da classe Arquivos.txt. Não vamos interagir com arquivos nessa classe

    // O formato será esse: dd/MM/yyyy - HH:mm:ss - QC - () - [][][][] - QG
    // QC é o código para quem começou a partida
    // QG é o código para quem ganhou a partida, no final da string
    // Os códigos são nesse formado: J1 (Jogador 1) J2 (Jogador 2) JO (Jogador) IA (IA), EP (Empate). Será distinguido se foi partida com 2 jogadores ou com IA

    // O estado inicial do tabuleiro estará dentro do ()
    // A informação será formatada dessa forma: 0123 2340 3401 4012 0123 234 3401 4012
    // São 8 linhas, portanto 8 grupos de 4 dígitos. São 4 coordenadas possiveis por linha, então 4 dígitos por grupo
    // 0 significa vazio. 1 significa quadrado. 2 significa circulo. 3 significa quadrado dama. 4 significa circulo dama

    // As informações das jogadas individuais estarão dentro dos []
    // A imformação será formatada dessa forma:
    // Cada [] conterá as coordenadas, em dígitos duplos, da peça que está jogando. E em seguida, dígitos duplos da coordenada que jogou.
    // Por fim, mais 2 dígitos duplos caso comeu uma peça inimiga
    // Exemplo: 00,22.11 (peça do 00 foi pra 22 e comeu 11)
    // Caso uma peça vire dama, haverá um .D no final dessa parte da string. Exemplo: [62,71.D]

    // Exemplo de formatação final:  23:29:52 - 30/10/2026 - JO - (1111 1111 0000 0000 0000 0000 2222 2222) - [37,46][64,55][46,67.55][[etc até a última jogada] - IA


    // A classe Main irá chamar o metodo iniciarGravacao a cada nova partida
    // A classe Tabuleiros irá chamar o metodo gravarTabuleiro sempre que um tabuleiro novo for gerado no começo de uma partida
    // As classes Jogo e IA irão chamar o metodo gravarJogada sempre que uma jogada é feita, retornando um int[][]
    // Quando uma peça for comida, as classes Jogo e IA vão chamar o metodo gravarPecaComida, retornando um int[]
    // quemComecou e quemGanhou serão chamados pelo Main, quando apropriado

    // Essa static String será atualizada apenas pelos metodos dentro dessa classe, que receberão informações das classes de fora
    static String gravacaoPartida = "";

    public static void gravarReplay() throws IOException {
        if (Main.debugging || debugging) {
            System.out.printf("Gravação da partida após gravarReplay (último método): %s\n", gravacaoPartida);
        }
        Arquivos.gravarReplay(gravacaoPartida);
    }

    public static void iniciarGravacao() {
        // Quando esse metodo é chamado, a gravação é inicializada
        // A data e horário precisam ser gravadas aqui
        gravacaoPartida = "";

        LocalDate data = LocalDate.now();
        LocalTime hora = LocalTime.now();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");

        String dataFormatada = data.format(formatoData);
        String horaFormatada = hora.format(formatoHora);

        gravacaoPartida = horaFormatada + " - " + dataFormatada + " - ";

        if (Main.debugging || debugging) {
            System.out.printf("Gravação da partida após iniciarGravacao: %s\n", gravacaoPartida);
        }
    }

    public static void gravarTabuleiro(char[][] tabuleiro) {
        // Lembrando o formato:
        // A informação será formatada dessa forma: 0123 2340 3401 4012 0123 234 3401 4012
        // São 8 linhas, portanto 8 grupos de 4 dígitos. São 4 coordenadas possiveis por linha, então 4 dígitos por grupo
        // 0 significa vazio. 1 significa quadrado. 2 significa circulo. 3 significa quadrado dama. 4 significa circulo dama

        StringBuilder gravacaoTabuleiro = new StringBuilder();

        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (linha == 0 || linha == 2 || linha == 4 || linha == 6) {
                    if (coluna == 1 || coluna == 3 || coluna == 5 || coluna == 7) {
                        if (tabuleiro[linha][coluna] == '□') {
                            gravacaoTabuleiro.append("1");
                        }
                        else if (tabuleiro[linha][coluna] == '○') {
                            gravacaoTabuleiro.append("2");
                        }
                        else if (tabuleiro[linha][coluna] == ' ') {
                            gravacaoTabuleiro.append("0");
                        }
                        else if (tabuleiro[linha][coluna] == '⊠') {
                            gravacaoTabuleiro.append("3");
                        }
                        else if (tabuleiro[linha][coluna] == '◓') {
                            gravacaoTabuleiro.append("4");
                        }
                        else {
                            System.out.println("Erro 11");
                            System.exit(11);
                        }
                    }
                }
                else {
                    if (coluna == 0 || coluna == 2 || coluna == 4 || coluna == 6) {
                        if (tabuleiro[linha][coluna] == '□') {
                            gravacaoTabuleiro.append("1");
                        }
                        else if (tabuleiro[linha][coluna] == '○') {
                            gravacaoTabuleiro.append("2");
                        }
                        else if (tabuleiro[linha][coluna] == ' ') {
                            gravacaoTabuleiro.append("0");
                        }
                        else if (tabuleiro[linha][coluna] == '⊠') {
                            gravacaoTabuleiro.append("3");
                        }
                        else if (tabuleiro[linha][coluna] == '◓') {
                            gravacaoTabuleiro.append("4");
                        }
                        else {
                            System.out.println("Erro 15");
                            System.exit(15);
                        }
                    }
                }
            }
            gravacaoTabuleiro.append(" ");
        }
        gravacaoPartida += gravacaoTabuleiro + "- ";
        if (Main.debugging || debugging) {
            System.out.printf("Gravação da partida após gravarTabuleiro: %s\n", gravacaoPartida);
        }
    }

    public static void gravarJogada(int[][] coordenadasDaJogada) {
        // Serão recebidos dois arrays, nesse formato:
        // {{coordenadaVelha[0], coordenadaVelha[1]}, {coordenadaNova[0][cordenadaNova[1]}};
        // Coordenada velha é a coordenada da peça que jogou. Coordenada nova é para onde no tabuleiro essa peça foi
        // Precisa trabalhar considerando que as vezes, peças serao comidas, e as vezes não
        // Fechamos esse metodo sem fechar os []. Verificamos se o ultimo caractere do gravacaoPartida é um ] ou não, para fecha-lo se necessário
        // Só pode fazer a verificação descrita acima se a última coisa gravada foi realmente uma jogada (ou seja, o último caractere da gravação atual é um número)

        fecharJogadaAnterior();
        String jogada = "[" + coordenadasDaJogada[0][0] + coordenadasDaJogada[0][1] + "," + coordenadasDaJogada[1][0] + coordenadasDaJogada[1][1];
        gravacaoPartida += jogada;

        if (Main.debugging || debugging) {
            System.out.printf("Gravação da partida após gravarJogada: %s\n", gravacaoPartida);
        }
    }

    public static void fecharJogadaAnterior() {
        char ultimoCaractere = gravacaoPartida.charAt(gravacaoPartida.length() - 1);
        if (Character.isDigit(ultimoCaractere)) {
            gravacaoPartida += "]";
        }
        if (Main.debugging || debugging) {
            System.out.printf("Gravação da partida após fecharJogadaAnterior: %s\n", gravacaoPartida);
        }
    }

    public static void gravarPecaComida(int[] pecaComida) {
        // Por design, uma jogada pode comer apenas uma peça. Isso garante que, toda vez que uma peça é comida, esse metodo é chamado uma vez
        String gravacao = "." + pecaComida[0] + pecaComida[1] + "]";
        gravacaoPartida += gravacao;
        if (Main.debugging || debugging) {
            System.out.printf("Gravação da partida após gravarPecaComida: %s\n", gravacaoPartida);
        }
    }

    // Modo pode ser "singleplayer" ou "multiplayer"
    public static void gravarQuemComecou(int quemComecou, String modo) {
        String codigo = "";
        if (quemComecou == 1 && modo.equals("singleplayer")) {
            codigo = "JO";
        }
        else if (quemComecou == 1 && modo.equals("multiplayer")) {
            codigo = "J1";
        }
        else if (quemComecou == 2 && modo.equals("singleplayer")) {
            codigo = "CO";
        }
        else if (quemComecou == 2 && modo.equals("multiplayer")) {
            codigo = "J2";
        }
        else {
            System.out.println("Erro 9");
            System.exit(9);
        }
        gravacaoPartida += codigo + " - ";
        if (Main.debugging || debugging) {
            System.out.printf("Gravação da partida após gravarQuemComecou: %s\n", gravacaoPartida);
        }
    }

    // O símbolo é □ ou ○ ou a String "empate"
    public static void gravarQuemGanhou(String simboloDeQuemGanhou, String modo) throws IOException {
        // No final desse metodo, devemos chamar o gravarReplay, que então irá chamar o Arquivos para gravar o replay no nosso .txt
        // Assim como o gravarJogada, precisa fechar o [] de uma jogada que não comeu uma peça, caso necessário
        fecharJogadaAnterior();
        String codigoVencedor = "";
        if (simboloDeQuemGanhou.equals("□") && modo.equals("singleplayer")) {
            codigoVencedor = "JO";
        }
        else if (simboloDeQuemGanhou.equals("□") && modo.equals("multiplayer")) {
            codigoVencedor = "J1";
        }
        else if (simboloDeQuemGanhou.equals("○") && modo.equals("singleplayer")) {
            codigoVencedor = "CO";
        }
        else if (simboloDeQuemGanhou.equals("○") && modo.equals("multiplayer")) {
            codigoVencedor = "J2";
        }
        else if (simboloDeQuemGanhou.equals("empate")) {
            codigoVencedor = "EM";
        }
        else {
            System.out.println("Erro 10");
            System.exit(10);
        }

        String gravacao = " - " + codigoVencedor;
        gravacaoPartida += gravacao;
        if (Main.debugging || debugging) {
            System.out.printf("Gravação da partida após gravarQuemGanhou: %s\n", gravacaoPartida);
        }

        gravarReplay();
    }

    public static void reproduzirReplay() throws IOException {
        // Lembrando o exemplo de formatação:
        // 01:16:22 - 31/10/2026 - 0000 0000 0000 0003 0104 0100 0000 0003 - J1 - [52,61.36][47,25.43][25,52.61][52,70][76,54][70,43.43][54,21] - J1

        // TO-DO: Falta a implementação de virar dama

        String replay = escolherReplay();
        if (replay == null || replay.isBlank()) {
            return;
        }
        if (!verificarIntegridade(replay)) {
            return;
        }
        String data = obterData(replay);
        String horario = obterHorario(replay);
        char[][] tabuleiro = obterTabuleiro(replay);
        String jogadorInicial = obterJogadorInicial(replay);
        String vencedor = obterVencedor(replay);

        System.out.printf("Essa partida foi jogada no dia %s às %s!\n", data, horario);
        System.out.println("Pressione enter para iniciar o replay . . .");
        ValidadorInput.scanner.nextLine();

        Tabuleiros.mostrarTabuleiro(tabuleiro);
        System.out.printf("A partida começou com %s!\n", jogadorInicial);
        System.out.println("Pressione enter para realizar a primeira jogada do replay . . .");
        ValidadorInput.scanner.nextLine();


        int[][][] jogadas = obterJogadas(replay);
        for (int i = 0; i < jogadas.length; i++) {
            // Primeiro array é a coordenada da peça. Segundo array, coordenada pra onde essa peça foi
            // Se existir um terceiro array, é a coordenada da peça que ela comeu
            int[][] jogada = jogadas[i];
            char pecaJogada = tabuleiro[jogada[0][0]][jogada[0][1]];
            tabuleiro[jogada[0][0]][jogada[0][1]] = ' ';
            tabuleiro[jogada[1][0]][jogada[1][1]] = pecaJogada;
            if (jogada.length == 3) {
                tabuleiro[jogada[2][0]][jogada[2][1]] = ' ';
                System.out.printf("A peça das coordenadas %s comeu a peça das coordenadas %s e foi para %s!!\n",
                        Main.obterCoordenadaEmTexto(jogada[0]), Main.obterCoordenadaEmTexto(jogada[2]), Main.obterCoordenadaEmTexto(jogada[1]));
            }
            else {
                System.out.printf("A peça das coordenadas %s foi para as coordenadas %s!\n",
                        Main.obterCoordenadaEmTexto(jogada[0]), Main.obterCoordenadaEmTexto(jogada[1]));
            }
            Tabuleiros.mostrarTabuleiro(tabuleiro);
            System.out.println("Pressione enter para seguir com o replay . . .");
            ValidadorInput.scanner.nextLine();
        }
        System.out.printf("A partida acabou! Vencedor: %s\n", vencedor);
    }

    public static String escolherReplay() throws IOException {
        String[] replays = Arquivos.obterReplays();
        System.out.println("======== REPLAYS ========");
        for (int i = 0; i < replays.length; i++) {
            System.out.printf("%d - %s\n", i+1, replays[i]);
        }
        System.out.print("(0 para voltar ao menu inicial) ");
        int escolha = ValidadorInput.obterInt(0, replays.length);
        if (escolha == 0) {
            return null;
        }
        else {
            return (replays[escolha - 1]);
        }
    }

    public static boolean verificarIntegridade(String replay) {
        // TO-DO: Verificar integridade dos replays antes de roda-lo
        // Não só deve verificar a estrutura do replay, mas também se suas jogadas fazem sentido
        return true;
    }

    public static String obterData(String replay) {
        String data = replay.substring(0, 9);
        if (Main.debugging || debugging) {
            System.out.printf("(Replays) Data encontrada: %s\n", data);
        }
        return data;
    }

    public static String obterHorario(String replay) {
        String horario = replay.substring(11, 22);
        if (Main.debugging || debugging) {
            System.out.printf("(Replays) Horário encontrado: %s\n", horario);
        }
        return horario;
    }

    public static char[][] obterTabuleiro(String replay) {
        // 0 é vazio. 1 é quadrado. 2 é circulo. 3 é quadrado dama. 4 é circulo dama
        String pecas = replay.substring(24, 64).replace(" ", "");
        char[][] tabuleiro = Tabuleiros.gerarTabuleiroVazio();

        int contador = 0;
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (linha == 0 || linha == 2 || linha == 4 || linha == 6) {
                    if (coluna == 1 || coluna == 3 || coluna == 5 || coluna == 7) {
                        tabuleiro[linha][coluna] = esteChar(pecas.charAt(contador));
                        contador++;
                    }
                }
                else {
                    if (coluna == 0 || coluna == 2 || coluna == 4 || coluna == 6) {
                        tabuleiro[linha][coluna] = esteChar(pecas.charAt(contador));
                        contador++;
                    }
                }
            }
        }
        return tabuleiro;
    }

    public static char esteChar(char peca) {
        if (Main.debugging || debugging) {
            System.out.printf("(Replays) Método esteChar recebeu este caractere: %s\n", peca);
        }
        if (Character.getNumericValue(peca) == 0) {
            return ' ';
        }
        else if (Character.getNumericValue(peca) == 1) {
            return '□';
        }
        else if (Character.getNumericValue(peca) == 2) {
            return '○';
        }
        else if (Character.getNumericValue(peca) == 3) {
            return '⊠';
        }
        else if (Character.getNumericValue(peca) == 4) {
            return '◓';
        }
        else {
            System.out.println("Erro 12");
            System.exit(12);
            return ' ';
        }
    }

    public static String obterJogadorInicial(String replay) {
        String jogadorInicial = replay.substring(66, 68);
        if (jogadorInicial.equals("JO")) {
            return "Você";
        }
        else if (jogadorInicial.equals("CO")) {
            return "a IA";
        }
        else if (jogadorInicial.equals("J1")) {
            return "o Jogador Um";
        }
        else if (jogadorInicial.equals("J2")) {
            return "o Jogador Dois";
        }
        else {
            System.out.printf("(Replays) Erro 13. Valor analisado: %s\n", jogadorInicial);
            System.exit(13);
            return " ";
        }
    }

    public static String obterVencedor(String replay) {
        String vencedor = replay.substring(replay.length() - 2);
        if (Main.debugging || debugging) {
            System.out.printf("(Replays) O método obterVencedor recebeu %s e está lendo %s\n", replay, vencedor);
        }
        if (vencedor.equals("JO")) {
            return "Você";
        }
        else if (vencedor.equals("CO")) {
            return "A IA";
        }
        else if (vencedor.equals("J1")) {
            return "O Jogador Um";
        }
        else if (vencedor.equals("J2")) {
            return "O Jogador Dois";
        }
        else if (vencedor.equals("EM")) {
            return "Ninguém";
        }
        else {
            System.out.println("Erro 16");
            System.exit(16);
            return " ";
        }
    }

    public static int[][][] obterJogadas(String replay) {
        // Lembrete do formato: [37,46][64,55][46,67.55][[etc até a última jogada]
        // Sendo o "." as coordenadas de uma peça que foi comida
        String jogadas = replay.substring(71, replay.length() - 6);
        String[] cadaJogada = jogadas.split("]");
        int[][][] todasAsJogadas = new int[cadaJogada.length][][];
        for (int jogada = 0; jogada < cadaJogada.length; jogada++) {
            if (Main.debugging || debugging) {
                System.out.printf("(Replays) Analisando essa jogada: %s]\n", cadaJogada[jogada]);
            }
            if (cadaJogada[jogada].length() == 6) {
                todasAsJogadas[jogada] = new int[2][2];
                // Como estamos lidando com um Char em formato unicode, faremos - '0'
                // Será menos código do que usar um Character.getNumericValue em todos
                todasAsJogadas[jogada][0][0] = cadaJogada[jogada].charAt(1) - '0';
                todasAsJogadas[jogada][0][1] = cadaJogada[jogada].charAt(2) - '0';
                todasAsJogadas[jogada][1][0] = cadaJogada[jogada].charAt(4) - '0';
                todasAsJogadas[jogada][1][1] = cadaJogada[jogada].charAt(5) - '0';
                if (Main.debugging || debugging) {
                    System.out.printf("(Replays) Encontrei essa jogada: %s\n", Arrays.deepToString(todasAsJogadas[jogada]));
                }

            }
            else if (cadaJogada[jogada].length() == 9) {
                todasAsJogadas[jogada] = new int[3][2];
                todasAsJogadas[jogada][0][0] = cadaJogada[jogada].charAt(1) - '0';
                todasAsJogadas[jogada][0][1] = cadaJogada[jogada].charAt(2) - '0';
                todasAsJogadas[jogada][1][0] = cadaJogada[jogada].charAt(4) - '0';
                todasAsJogadas[jogada][1][1] = cadaJogada[jogada].charAt(5) - '0';
                todasAsJogadas[jogada][2][0] = cadaJogada[jogada].charAt(7) - '0';
                todasAsJogadas[jogada][2][1] = cadaJogada[jogada].charAt(8) - '0';
                if (Main.debugging || debugging) {
                    System.out.printf("(Replays) Encontrei essa jogada: %s\n", Arrays.deepToString(todasAsJogadas[jogada]));
                }
            }
        }
        return todasAsJogadas;
    }
}
