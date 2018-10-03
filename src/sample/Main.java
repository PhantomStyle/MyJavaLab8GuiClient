package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Main extends Application {

    @FXML
    private TextArea showingField;

    @FXML
    private TextField enteringField;

    @FXML
    private Button button;

    private String clientCommand = "";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    @FXML
    public void initialize() throws IOException {
        String name = "kek";
        Socket socket = new Socket("localhost", 3345);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        DataInputStream ois = new DataInputStream(socket.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        runAll(socket, br, oos, ois, reader);
    }

    private synchronized void runSender(Socket socket, BufferedReader br, DataOutputStream oos, DataInputStream ois) {
        new Thread() {

            @Override
            public void run() {
                while (!socket.isOutputShutdown()) {

// ждём консоли клиента на предмет появления в ней данных
                    try {
                        if (br.ready()) {

                            // данные появились - работаем
                            System.out.println("Client start writing in channel...");
                            Thread.sleep(1000);
                            String clientCommand = "";
//                            if(button.isPressed()) {
//                                clientCommand = enteringField.getText();
//                            }
//                            String clientCommand = br.readLine();

                            // пишем данные с консоли в канал сокета для сервера
                            //TODO: name
                            clientCommand = "kek" + ": " + clientCommand;
                            oos.writeUTF(clientCommand);
                            oos.flush();
                            //                    System.out.println(clientCommand);
                            Thread.sleep(1000);
                            // ждём чтобы сервер успел прочесть сообщение из сокета и ответить

                            // проверяем условие выхода из соединения
                            if (clientCommand.equalsIgnoreCase("quit")) {

                                // если условие выхода достигнуто разъединяемся
                                System.out.println("Client kill connections");
                                Thread.sleep(2000);

                                // смотрим что нам ответил сервер на последок перед закрытием ресурсов
                                if (ois.read() > -1) {
                                    System.out.println("reading...");
                                    String in = ois.readUTF();
                                    System.out.println(in);
                                }

                                // после предварительных приготовлений выходим из цикла записи чтения
                                break;
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private synchronized void runPrinter(BufferedReader reader, DataInputStream ois) {
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        if (ois.read() > -1) {

                            // если успел забираем ответ из канала сервера в сокете и сохраняем её в ois переменную,  печатаем на свою клиентскую консоль
                            //                        System.out.println("reading...");
                            String in = reader.readLine();
                            //                        String in = ois.readUTF();
                            showingField.setText(in);
//                            System.out.println(in);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private synchronized void runAll(Socket socket, BufferedReader br, DataOutputStream oos, DataInputStream ois, BufferedReader reader){
        runSender(socket, br, oos, ois);
        runPrinter(reader, ois);
    }


    @FXML
    private void handleButton1Action(ActionEvent event) {

        clientCommand = enteringField.getText();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
