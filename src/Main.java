import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HexFormat;

//https://www.codejava.net/java-se/file-io/how-to-read-and-write-binary-files-in-java
public class Main {
    public static void main(String[] args) throws IOException {

        //Forma nova usando NIO api
        //Finalidade -> Leitura de arquivos pequenos...
        //leia();


        //forma legada usando IO api
        //Finalidade -> Leitura de qualquer tamanho de arquivo...
        //OBS: Roda muito devagar porque copia(escreve) exatamente um byte por vez.
        //leiaLegado();


        //rapidamente absurdo em relação a escrita byte por byte, porem, a leitura é byte por byte ainda.
        //leiaLegadoRapido();

        //escrita de bloco grande, de 4096 bytes por vez
        //leiaLegadoRapidoDemais();

        //verificando o tipo de arquivo usando a tabela de assinatura de arquivos
        //isPng();

        //usando o bufferedInputStream e bufferedOutputStream
        //obs: Ela é recomendada para binarios de alto nivel, já que a FileOutputStream e FileInputStream
        //é mais recomendado para analizar ou criar seu proprio formato de arquivo.
        //Essas duas classe basicamente ela é mais recomendada, pois evita muita chamadas a API nativa,
        //DIFERENÇA: A única diferença é que um fluxo em buffer usa uma matriz de
        //bytes internamente para armazenar em buffer a entrada e a saída para reduzir
        // o número de chamadas para a API nativa, aumentando assim o desempenho de IO
        leiaLegadoRapidoDemaisUltra();
    }

    public static void leia() throws IOException {
        final String inputFile = "C:\\Users\\user\\Downloads\\binary_files\\A.png";
        final String outputFile = "C:\\Users\\user\\Downloads\\binary_files\\B.png";

        //leitura dos dados
        final byte[] allBytes = Files.readAllBytes(Paths.get(inputFile));

        //escrita dos dados
        Files.write(Paths.get(outputFile), allBytes);

        System.out.println("Leitura e escrita feita com sucesso.");
    }

    public static void leiaLegado() {
        final String inputFile = "C:\\Users\\user\\Downloads\\binary_files\\A.zip";
        final String outputFile = "C:\\Users\\user\\Downloads\\binary_files\\B.zip";

        try(InputStream inputStream = new FileInputStream(inputFile);
            OutputStream outputStream = new FileOutputStream(outputFile)){
            int byteRead = -1;

            while((byteRead = inputStream.read()) != -1){
                System.out.println(byteRead);
                outputStream.write(byteRead);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void leiaLegadoRapido(){
        final String inputFile = "C:\\Users\\user\\Downloads\\binary_files\\A.zip";
        final String outputFile = "C:\\Users\\user\\Downloads\\binary_files\\C.zip";

        try(InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile)){
            //precisamos do tamanho do arquivo para poder criar um array com o tamanho.
            //44.1mb - 46.299.648 bytes
            long fileSize = new File(inputFile).length();
            //logo, nosso array tera o tamanho de 46kk de bytes.
            byte[] allBytes = new byte[(int)fileSize];
            System.out.println("Leitura finalizada.");
            int bytesRead = inputStream.read(allBytes);
            outputStream.write(allBytes, 0, bytesRead);
            System.out.println("Escrita finalizada.");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static final int BUFFER_SIZE = 4096; //equivale a 4 KB

    public static void leiaLegadoRapidoDemais(){
        final String inputFile = "C:\\Users\\user\\Downloads\\binary_files\\A.zip";
        final String outputFile = "C:\\Users\\user\\Downloads\\binary_files\\D.zip";
        try(InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile)){

            //aqui mando o inputStream ler e o outputStream escrever de 4kb em 4kb
            byte[] buffer = new byte[BUFFER_SIZE];

            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1){
                System.out.println(bytesRead);
                outputStream.write(buffer, 0, bytesRead);
            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void isPng(){
        final String inputFile = "C:\\Users\\user\\Downloads\\binary_files\\A.png";
        //https://en.wikipedia.org/wiki/List_of_file_signatures
        final String[] pngHexSignature = {"89", "50", "4E", "47", "0D", "0A", "1A", "0A"};

        try(InputStream inputStream = new FileInputStream(inputFile)){
            //os oito primeiros bytes são o cabeçalho do arquivo.
            //isso varia de acordo com o tamanho da assinatura
            //exemplo-> O .zip tem 4 informações na assinatura, logo, 4bytes sera o cabeçalho.
            int[] headerBytes = new int[8];
            boolean isPNG = true;
            for(int i = 0; i < 8; i++){
                headerBytes[i] = inputStream.read();
                System.out.println("byte-> "+headerBytes[i]);
                int pngSignature = HexFormat.fromHexDigits(pngHexSignature[i]);
                //System.out.println("hex: "+pngSignature);
                if(headerBytes[i] != pngSignature){
                    isPNG = false;
                    break;
                }
            }
            System.out.println("Is PNG file? "+isPNG);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static final int BUFFER_SIZE_2 = 16384; //16kb buffer size - isso é muito rapido.
    public static void leiaLegadoRapidoDemaisUltra(){
        final String inputFile = "C:\\Users\\user\\Downloads\\binary_files\\A.zip";
        final String outputFile = "C:\\Users\\user\\Downloads\\binary_files\\G.zip";
        //o buffered já possui uma matriz padrão de 8192 - 8kb, mas pode ser customizado...
        try(InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile), BUFFER_SIZE_2);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile), BUFFER_SIZE_2)
        ){
            byte[] buffer = new byte[BUFFER_SIZE_2];
            int byteRead = -1;
            while ((byteRead = inputStream.read(buffer)) != -1){
                System.out.println(byteRead);

                outputStream.write(buffer, 0, byteRead);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
