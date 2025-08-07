package it.saimao.temcodedictionary;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private final Font titleFont = Font.loadFont(getClass().getResourceAsStream("/fonts/title.ttf"), 26);
    private final Font subTitleFont = Font.loadFont(getClass().getResourceAsStream("/fonts/title.ttf"), 20);
    private final Font footerFont = Font.loadFont(getClass().getResourceAsStream("/fonts/title.ttf"), 12);
    private final Font contentFont = Font.loadFont(getClass().getResourceAsStream("/fonts/content.ttf"), 16);

    List<Data> dataList;

    @Override
    public void start(Stage primaryStage) throws Exception {

        LocalDatabase.ensureDatabaseExists();
        dataList = LocalDatabase.getAllData();

//        File file = new File("data.ser");
//        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
//        out.writeObject(dataList);
//        out.close();

        BorderPane main = new BorderPane();

        main.setLeft(createLeftPane());
        main.setRight(createRightPane());

        Label lbHeader = new Label("Tem Code Dictionary");
        lbHeader.setFont(titleFont);
        lbHeader.setAlignment(Pos.CENTER);
        lbHeader.setPrefHeight(60);
        lbHeader.setPrefWidth(800);
        BorderPane.setMargin(lbHeader, new Insets(0, 0, 10, 0));
        lbHeader.setPadding(new Insets(5));
        lbHeader.setBackground(Background.fill(Paint.valueOf("#F0F0F0")));
        main.setTop(lbHeader);

        main.setPadding(new Insets(5));
        main.setBackground(Background.fill(Paint.valueOf("#FFFFFF")));

        Label lbFooter = new Label("Developed by Sai Mao (TMK)");
        lbFooter.setFont(footerFont);
        lbFooter.setTextFill(Paint.valueOf("#555599"));
        BorderPane.setMargin(lbFooter, new Insets(5, 0, 0, 0));
        lbFooter.setAlignment(Pos.CENTER);
        lbFooter.setPrefHeight(30);
        lbFooter.setPrefWidth(800);
        lbFooter.setPadding(new Insets(5));
        lbFooter.setBackground(Background.fill(Paint.valueOf("#F0F0F0")));
        main.setBottom(lbFooter);

        Scene sc = new Scene(main);
        primaryStage.setScene(sc);
        primaryStage.setTitle("Tem Code Dictionary");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.jpg")));
        primaryStage.setResizable(false);
        primaryStage.show();


    }

    private Label lbWord, lbState, lbDef;

    private Node createRightPane() {
        VBox main = new VBox();
        main.setPrefWidth(400);
        main.setPadding(new Insets(10));
        main.setSpacing(5);

        lbWord = new Label("");
        lbWord.setFont(titleFont);
        lbState = new Label("");
        lbState.setFont(subTitleFont);
        lbDef = new Label("");
        lbDef.setFont(subTitleFont);
        lbDef.setWrapText(true);

        main.getChildren().addAll(lbWord, lbState, lbDef);
        main.setBorder(new Border(new BorderStroke(Paint.valueOf("#999"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));

        return main;
    }

    private Node createLeftPane() {
        VBox main = new VBox();
        main.setPrefWidth(400);
        main.setSpacing(5);
        main.setPadding(new Insets(0, 5, 0, 0));

        HBox hbAction = new HBox();
        hbAction.setSpacing(5);
        hbAction.setAlignment(Pos.CENTER_LEFT);
        // Search Label
        Label lbSearch = new Label("Search : ");
        lbSearch.setFont(contentFont);
        // Search TextField
        TextField tfSearch = new TextField();
        tfSearch.setPrefWidth(280);
        tfSearch.setPrefHeight(40);
        tfSearch.setPromptText("Search");
        tfSearch.setFont(contentFont);
        // For Clear Search Button
        Button btClear = new Button();
        ImageView ivClear = new ImageView(new Image(getClass().getResourceAsStream("/images/bin.png")));
        ivClear.setFitWidth(20);
        ivClear.setFitHeight(20);
        btClear.setGraphic(ivClear);
        btClear.setBackground(Background.fill(Paint.valueOf("#FFFFFF00")));
        btClear.setPrefSize(30, 30);
        hbAction.getChildren().addAll(lbSearch, tfSearch, btClear);
        btClear.setFont(contentFont);

        ListView<Data> lvResult = new ListView<>();
        lvResult.setItems(FXCollections.observableArrayList(dataList));
        lvResult.setCellFactory(new Callback<ListView<Data>, ListCell<Data>>() {
            @Override
            public ListCell<Data> call(ListView<Data> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Data item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Label lbWord = new Label(item.word());
                            lbWord.setFont(contentFont);
                            setGraphic(lbWord);
                            setText(null);
                        }
                    }
                };
            }
        });
        lvResult.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null)
                selectWord(newValue);
        }));

        main.getChildren().addAll(hbAction, lvResult);

        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            var data = dataList.stream().filter(da -> da.word().toLowerCase().startsWith(newValue.toLowerCase())).toList();
            lvResult.getItems().clear();
            lvResult.getItems().addAll(data);
            if (lvResult.getItems().isEmpty())
                lvResult.setPlaceholder(createEmptyResultPlaceholder());
            else
                lvResult.getSelectionModel().selectFirst();
        });

        btClear.setOnAction(event -> {
            tfSearch.clear();
            lvResult.getItems().clear();
        });

        return main;

    }

    private void selectWord(Data newValue) {
        lbWord.setText(newValue.word());
        lbState.setText(newValue.state());
        lbDef.setText(Rabbit.zg2uni(newValue.def()).replaceAll("\\|", ""));
    }

    private Node createEmptyResultPlaceholder() {
        Label lbEmpty = new Label("No results found");
        ImageView ivEmpty = new ImageView();
        ivEmpty.setFitWidth(120);
        ivEmpty.setFitHeight(120);
        ivEmpty.setImage(new Image(getClass().getResourceAsStream("/images/empty.png")));
        ivEmpty.setPreserveRatio(true);
        ivEmpty.setSmooth(true);
        ivEmpty.setCache(true);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.getChildren().addAll(ivEmpty, lbEmpty);
        return vBox;
    }
}