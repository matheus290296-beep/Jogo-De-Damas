import java.io.IOException;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Replays {
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
    // Exemplo: 00,22,11 (peça do 00 foi pra 22 e comeu 11)
    // Caso uma peça vire dama, haverá um .D no final dessa parte da string. Exemplo: [62,71.D]

    // Exemplo de formatação final:  23:29:52 - 30/10/2026 - JO - (1111 1111 0000 0000 0000 0000 2222 2222) - [37,46][62,51][etc até a última jogada] - IA


    // A classe Main irá chamar o metodo iniciarGravacao a cada nova partida
    // A classe Tabuleiros irá chamar o metodo gravarTabuleiro sempre que um tabuleiro novo for gerado no começo de uma partida
    // As classes Jogo e IA irão chamar o metodo gravarJogada sempre que uma jogada é feita, retornando um int[][]
    // Quando uma peça for comida, as classes Jogo e IA vão chamar o metodo gravarPecaComida, retornando um int[]
    // quemComecou e quemGanhou serão chamados pelo Main, quando apropriado

    // Essa static String será atualizada apenas pelos metodos dentro dessa classe, que receberão informações das classes de fora
    static String gravacaoPartida = "";

    public static void gravarReplay() throws IOException {
        if (Main.debugging) {
            System.out.printf("Gravação da partida após gravarReplay (último método): %s", gravacaoPartida);
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

        if (Main.debugging) {
            System.out.printf("Gravação da partida após iniciarGravacao: %s", gravacaoPartida);
        }
    }

    public static void gravarTabuleiro(char[][] tabuleiro) {
        // Lembrando o formato:
        // A informação será formatada dessa forma: 0123 2340 3401 4012 0123 234 3401 4012
        // São 8 linhas, portanto 8 grupos de 4 dígitos. São 4 coordenadas possiveis por linha, então 4 dígitos por grupo
        // 0 significa vazio. 1 significa quadrado. 2 significa circulo. 3 significa quadrado dama. 4 significa circulo dama

        String gravacaoTabuleiro = "";

        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (linha == 0 || linha == 2 || linha == 4 || linha == 6) {
                    if (coluna == 1 || coluna == 3 || coluna == 5 || coluna == 7) {
                        if (tabuleiro[linha][coluna] == '□') {
                            gravacaoTabuleiro += "1";
                        }
                        else if (tabuleiro[linha][coluna] == '○') {
                            gravacaoTabuleiro += "2";
                        }
                        else if (tabuleiro[linha][coluna] == ' ') {
                            gravacaoTabuleiro += "0";
                        }
                        else if (tabuleiro[linha][coluna] == '⊠') {
                            gravacaoTabuleiro += "3";
                        }
                        else if (tabuleiro[linha][coluna] == '◓') {
                            gravacaoTabuleiro += "4";
                        }
                        else {
                            System.out.println("Erro 10");
                            System.exit(10);
                        }
                    }
                }
                else {
                    if (coluna == 0 || coluna == 2 || coluna == 4 || coluna == 6) {
                        if (tabuleiro[linha][coluna] == '□') {
                            gravacaoTabuleiro += "1";
                        }
                        else if (tabuleiro[linha][coluna] == '○') {
                            gravacaoTabuleiro += "2";
                        }
                        else if (tabuleiro[linha][coluna] == ' ') {
                            gravacaoTabuleiro += "0";
                        }
                        else if (tabuleiro[linha][coluna] == '⊠') {
                            gravacaoTabuleiro += "3";
                        }
                        else if (tabuleiro[linha][coluna] == '◓') {
                            gravacaoTabuleiro += "4";
                        }
                        else {
                            System.out.println("Erro 10");
                            System.exit(10);
                        }
                    }
                }
            }
            gravacaoTabuleiro += " ";
        }
        gravacaoPartida += gravacaoTabuleiro + " - ";
        if (Main.debugging) {
            System.out.printf("Gravação da partida após gravarTabuleiro: %s", gravacaoPartida);
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

        if (Main.debugging) {
            System.out.printf("Gravação da partida após gravarJogada: %s", gravacaoPartida);
        }
    }

    public static void fecharJogadaAnterior() {
        char ultimoCaractere = gravacaoPartida.charAt(gravacaoPartida.length() - 1);
        if (Character.isDigit(ultimoCaractere)) {
            gravacaoPartida += "]";
        }
        if (Main.debugging) {
            System.out.printf("Gravação da partida após fecharJogadaAnterior: %s", gravacaoPartida);
        }
    }

    public static void gravarPecaComida(int[] pecaComida) {
        // Por design, uma jogada pode comer apenas uma peça. Isso garante que, toda vez que uma peça é comida, esse metodo é chamado uma vez
        String gravacao = "." + pecaComida[0] + pecaComida[1] + "]";
        gravacaoPartida += gravacao;
        if (Main.debugging) {
            System.out.printf("Gravação da partida após gravarPecaComida: %s", gravacaoPartida);
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
        if (Main.debugging) {
            System.out.printf("Gravação da partida após gravarQuemComecou: %s", gravacaoPartida);
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
            System.out.println("Erro 9");
            System.exit(9);
        }

        String gravacao = " - " + codigoVencedor;
        gravacaoPartida += gravacao;
        if (Main.debugging) {
            System.out.printf("Gravação da partida após gravarQuemGanhou: %s", gravacaoPartida);
        }

        gravarReplay();
    }

    public static void reproduzirReplay() {

    }

    public static int escolherReplay() {
        return 0;
    }
}
