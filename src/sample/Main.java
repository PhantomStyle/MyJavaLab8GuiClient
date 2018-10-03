package sample;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    @FXML
    private Button button;

    @FXML
    private ListView<String> showingField;

    @FXML
    private PasswordField passField;

    @FXML
    private TextField enteringField;

    @FXML
    private Button buttonLogin;

    @FXML
    private TextField loginField;

    @FXML
    private Text text1;

    @FXML
    private Text text2;

    private String clientCommand = "";


    private static Stage stage;

    private String login;
    private String pass;

    private static Map<String, String> db = new HashMap<>();

    static {
        db.put("root", "root");
        db.put("kek", "kek");
    }

    private String name = "";

    @FXML
    void onLoginButton(ActionEvent event) {
        String login = loginField.getText();
        String pass = passField.getText();
        this.login = login;
        this.pass = pass;
        try {
            if (db.get(login).equals(pass)) {
//                stage.close();
//                new Thread(() -> Main.launch(Main.class)).start();
                loginField.setVisible(false);
                passField.setVisible(false);
                buttonLogin.setVisible(false);
                text1.setVisible(false);
                text2.setVisible(false);
                button.setVisible(true);
                showingField.setVisible(true);
                enteringField.setVisible(true);
                name = login;
            }
        } catch (Exception e) {
            Stage st = new Stage();
            st.initModality(Modality.APPLICATION_MODAL);
            st.initOwner(stage);
            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(new Text("Wrong credentials"));
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            st.setScene(dialogScene);
            st.show();
            loginField.clear();
            passField.clear();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    @FXML
    public void initialize() throws IOException {
        button.setVisible(false);
        showingField.setVisible(false);
        enteringField.setVisible(false);
        Socket socket = new Socket("localhost", 3345);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        BufferedWriter oos = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        DataInputStream ois = new DataInputStream(socket.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        runAll(socket, br, oos, ois, reader);

    }

    private synchronized void runSender(Socket socket, BufferedReader br, BufferedWriter oos, DataInputStream ois) {
        new Thread() {

            @Override
            public void run() {
                System.out.println(1);
                while (!socket.isOutputShutdown()) {
// ждём консоли клиента на предмет появления в ней данных
                    try {
                        Thread.sleep(1000);
                        if (!clientCommand.equals("")) {
//                            System.out.println(2);
                            // данные появились - работаем
                            System.out.println("Client start writing in channel...");
//                            Thread.sleep(1000);
//                            String clientCommand = "";
//                            if(button.isPressed()) {
//                                clientCommand = enteringField.getText();
//                            }
//                            String clientCommand = br.readLine();

                            // пишем данные с консоли в канал сокета для сервера
                            //TODO: name
                            clientCommand = name + ": " + clientCommand;
                            oos.write(clientCommand + "\n");
                            oos.flush();
                            //                    System.out.println(clientCommand);
//                            Thread.sleep(1000);
                            // ждём чтобы сервер успел прочесть сообщение из сокета и ответить

                            // проверяем условие выхода из соединения
                            if (clientCommand.equalsIgnoreCase("quit")) {

                                // если условие выхода достигнуто разъединяемся
                                System.out.println("Client kill connections");
//                                Thread.sleep(2000);

                                // смотрим что нам ответил сервер на последок перед закрытием ресурсов
                                if (ois.read() > -1) {
                                    System.out.println("reading...");
                                    String in = ois.readUTF();
                                    System.out.println(in);
                                }


                                // после предварительных приготовлений выходим из цикла записи чтения
                                break;
                            }
                            clientCommand = "";
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

    private synchronized void runPrinter(BufferedReader reader, DataInputStream ois, Socket socket) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                InputStream inputStream = null;
//                try {
//                    inputStream = socket.getInputStream();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                BufferedReader reader1 = null;
//                try {
//                    reader1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                while (true) {
                    try {
                        Thread.sleep(1500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

//                        String in = reader.readLine();
//                        if (reader.ready()) {
//                            System.out.println(in);

                        // если успел забираем ответ из канала сервера в сокете и сохраняем её в ois переменную,  печатаем на свою клиентскую консоль
                        System.out.println("reading...");
                        String in = reader.readLine();
                        System.out.println("After reading");
                        //                        String in = ois.readUTF();
                            showingField.getItems().add(in);
//                            System.out.println(in);

//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private synchronized void runAll(Socket socket, BufferedReader br, BufferedWriter oos, DataInputStream ois, BufferedReader reader) {
        runSender(socket, br, oos, ois);
        runPrinter(reader, ois, socket);
    }


    @FXML
    private void handleButton1Action(ActionEvent event) {

        clientCommand = enteringField.getText();
        enteringField.clear();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
