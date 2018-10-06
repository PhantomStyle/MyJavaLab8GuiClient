package sample;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {


    @FXML
    private ListView<String> showingField;

    @FXML
    private Rectangle r2;

    @FXML
    private Rectangle r3;

    @FXML
    private Rectangle r4;

    @FXML
    private Rectangle r5;

    @FXML
    private Button buttonLogin;

    @FXML
    private Rectangle r6;

    @FXML
    private Rectangle r7;

    @FXML
    private Rectangle r8;

    @FXML
    private Rectangle r9;

    @FXML
    private Button button;

    @FXML
    private Rectangle r10;

    @FXML
    private PasswordField passField;

    @FXML
    private Rectangle r12;

    @FXML
    private Rectangle r11;

    @FXML
    private TextField enteringField;

    @FXML
    private Button redB;

    @FXML
    private Button greenB;

    @FXML
    private CheckBox check;

    @FXML
    private BarChart<?, ?> gist;

    @FXML
    private Text text1;

    @FXML
    private Text text2;

    @FXML
    private Button blackB;

    @FXML
    private TextField loginField;

    @FXML
    private Button blueB;

    @FXML
    private Rectangle r1;


    private String clientCommand = "";

    private Color color;


    private static Stage stage;

    private String login;
    private String pass;

    private static Map<Integer, Rectangle> mapOfRectangles = new HashMap<>();


    private static Map<Integer, String> mapOfMessages = new HashMap<>();
    private static int idOfMessage = 0;

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
//                greenB.setVisible(true);
//                blackB.setVisible(true);
//                blueB.setVisible(true);
//                redB.setVisible(true);
                check.setVisible(false);
                button.setVisible(true);
                showingField.setVisible(true);
                enteringField.setVisible(true);

                greenB.setVisible(true);
                blueB.setVisible(true);
                blackB.setVisible(true);
                redB.setVisible(true);

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
//        greenB.setVisible(false);
//        blackB.setVisible(false);
//        blueB.setVisible(false);
//        redB.setVisible(false);
        check.setVisible(false);
        gist.setVisible(false);
        r1.setVisible(false);
        r2.setVisible(false);
        r3.setVisible(false);
        r4.setVisible(false);
        r5.setVisible(false);
        r6.setVisible(false);
        r7.setVisible(false);
        r8.setVisible(false);
        r9.setVisible(false);
        r10.setVisible(false);
        r11.setVisible(false);
        r12.setVisible(false);

        mapOfRectangles.put(0, r1);
        mapOfRectangles.put(1, r2);
        mapOfRectangles.put(2, r3);
        mapOfRectangles.put(3, r4);
        mapOfRectangles.put(4, r5);
        mapOfRectangles.put(5, r6);
        mapOfRectangles.put(6, r7);
        mapOfRectangles.put(7, r8);
        mapOfRectangles.put(8, r9);
        mapOfRectangles.put(9, r10);
        mapOfRectangles.put(10, r11);
        mapOfRectangles.put(11, r12);

        greenB.setVisible(false);
        blueB.setVisible(false);
        blackB.setVisible(false);
        redB.setVisible(false);

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
                        mapOfMessages.put(idOfMessage, "        " + in);
                        showingField.getItems().add("        " + in);
                        mapOfRectangles.get(idOfMessage).setVisible(true);
                        if(!in.split(":")[0].contains(name)){
                            mapOfRectangles.get(idOfMessage).setFill(javafx.scene.paint.Color.BROWN);
                        }
                        idOfMessage++;
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

    @FXML
    private void onCheckAction() {
        check.setVisible(true);
    }

    @FXML
    private void onBlue() {
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if (!(entry.getValue().split(":")[0].contains(name))) {
                if (mapOfRectangles.get(entry.getKey()).getFill().equals(javafx.scene.paint.Color.BLUE)) {
                    Stage st = new Stage();
                    st.initModality(Modality.APPLICATION_MODAL);
                    st.initOwner(stage);
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.getChildren().add(new Text("Color is used"));
                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                    st.setScene(dialogScene);
                    st.show();
                }
            }
        }
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if (entry.getValue().split(":")[0].contains(name)) {
                mapOfRectangles.get(entry.getKey()).setFill(javafx.scene.paint.Color.BLUE);
                mapOfRectangles.get(entry.getKey()).setVisible(true);
            }
        }
    }

    @FXML
    private void onRed() {
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if (!(entry.getValue().split(":")[0].contains(name))) {
                if (mapOfRectangles.get(entry.getKey()).getFill().equals(javafx.scene.paint.Color.RED)) {
                    Stage st = new Stage();
                    st.initModality(Modality.APPLICATION_MODAL);
                    st.initOwner(stage);
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.getChildren().add(new Text("Color is used"));
                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                    st.setScene(dialogScene);
                    st.show();
                }
            }
        }
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if (entry.getValue().split(":")[0].contains(name)) {
                mapOfRectangles.get(entry.getKey()).setFill(javafx.scene.paint.Color.RED);
                mapOfRectangles.get(entry.getKey()).setVisible(true);
            }
        }
    }

    @FXML
    private void onBlack() {
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if (!(entry.getValue().split(":")[0].contains(name))) {
                if (mapOfRectangles.get(entry.getKey()).getFill().equals(javafx.scene.paint.Color.BLACK)) {
                    Stage st = new Stage();
                    st.initModality(Modality.APPLICATION_MODAL);
                    st.initOwner(stage);
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.getChildren().add(new Text("Color is used"));
                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                    st.setScene(dialogScene);
                    st.show();
                }
            }
        }
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if (entry.getValue().split(":")[0].contains(name)) {
                mapOfRectangles.get(entry.getKey()).setFill(javafx.scene.paint.Color.BLACK);
                mapOfRectangles.get(entry.getKey()).setVisible(true);
            }
        }
    }

    @FXML
    private void onGreen() {
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if (!(entry.getValue().split(":")[0].contains(name))) {
                if (mapOfRectangles.get(entry.getKey()).getFill().equals(javafx.scene.paint.Color.GREEN)) {
                    Stage st = new Stage();
                    st.initModality(Modality.APPLICATION_MODAL);
                    st.initOwner(stage);
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.getChildren().add(new Text("Color is used"));
                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                    st.setScene(dialogScene);
                    st.show();
                }
            }
        }
        for (Map.Entry<Integer, String> entry : mapOfMessages.entrySet()) {
            if (entry.getValue().split(":")[0].contains(name)) {
                mapOfRectangles.get(entry.getKey()).setFill(javafx.scene.paint.Color.GREEN);
                mapOfRectangles.get(entry.getKey()).setVisible(true);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
