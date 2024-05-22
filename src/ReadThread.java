import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReadThread implements Runnable{
    int k; // размер в байтах строки в формате UTF-8
    String s; // строка с данными
    int d; // контрольное число - количество символов в строке с данными
    int p; // номер части
    int c = 0; //количество считанных нами символов
    public String FileName;

    public ReadThread(String fileName) {
        this.FileName = fileName;
    }

    @Override
    public void run() {
        //считываю файл построчно
        List<String> lines = new ArrayList<>();
        try (DataInputStream input = new DataInputStream(new FileInputStream(FileName))) {
            String line;
            while ((line = input.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        k = Integer.parseInt(lines.get(0));
        s = lines.get(1);
        d = Integer.parseInt(lines.get(2));
        p = Integer.parseInt(lines.get(3));

        try (DataInputStream input = new DataInputStream(new FileInputStream(FileName))) {
            String line;
            int current = 0;
            while ((line = input.readLine()) != null) {
                if (current == 1) {
                    c = line.length();
                }
                current++;
            }
            if (d != c) {
                throw new RuntimeException("Количество символов в файле не соответствует");
            }

            LogFile file = new LogFile();
            writeLog(file);

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла");
        }
    }

    public void writeLog(LogFile file) {
        synchronized (ReadThread.class) {
            file.writeFile("Прочитали файл " + FileName + " кол-во байт данных: " + k +
                    " кол-во считанных символов: " +  c +
                    " контрольное число: " + d + " номер части: " + p);
        }
    }
}
