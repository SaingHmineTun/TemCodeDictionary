package it.saimao.burma_dictionary;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;


public class Main extends Application {

    private ListView<Data> lvResult;
    private FlowPane fSynonyms;
    private Label lbSynonym;

    public static void main(String[] args) {

        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
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

        Label lbHeader = new Label("Burma Dictionary");
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
        primaryStage.setTitle("Burma Dictionary");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.jpg")));
        primaryStage.setResizable(false);
        primaryStage.show();


        lvResult.getSelectionModel().selectFirst();


    }

    private Label lbWord, lbState;
    private WebView webDef;
    private TextField tfSearch;

    private Node createRightPane() {
        VBox main = new VBox();
        main.setPrefWidth(400);
        main.setPadding(new Insets(10));
        main.setSpacing(5);


        HBox hbWord = new HBox();
        hbWord.setAlignment(Pos.CENTER_LEFT);
        hbWord.setSpacing(20);
        lbWord = new Label("");
        lbWord.setFont(titleFont);
        ImageView ivPronounce = new ImageView();
        ivPronounce.setCursor(Cursor.HAND);
        ivPronounce.setImage(new Image(getClass().getResourceAsStream("/images/speaker.png")));
        ivPronounce.setPreserveRatio(true);
        ivPronounce.setSmooth(true);
        ivPronounce.setCache(true);
        ivPronounce.setFitWidth(30);
        ivPronounce.setFitHeight(30);
        ivPronounce.setOnMouseClicked(e -> {

            Voice voice = VoiceManager.getInstance().getVoice("kevin16");
            if (voice != null) {
                voice.allocate();
                voice.speak(lbWord.getText());
                voice.deallocate();
            } else {
                System.out.println("Voice not found.");
            }

        });

        hbWord.getChildren().addAll(lbWord, ivPronounce);

        lbState = new Label("");
        webDef = new WebView();
        webDef.setPrefWidth(400);
        webDef.setPrefHeight(350);

        lbSynonym = new Label("Synonyms");
        lbSynonym.setFont(subTitleFont);

        fSynonyms = new FlowPane();
        fSynonyms.setPadding(new Insets(0, 0, 0, 0));
        fSynonyms.setHgap(5);
        fSynonyms.setVgap(5);
        FlowPane.setMargin(fSynonyms,  new Insets(20, 0, 0, 0));

        main.getChildren().addAll(hbWord, lbState, webDef, lbSynonym, fSynonyms);
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
        tfSearch = new TextField();
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

        lvResult = new ListView<>();
        lvResult.setPrefHeight(450);
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
                            Label lbWord = new Label(item.stripWord());
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
            var data = dataList.stream().filter(da -> da.stripWord().toLowerCase().startsWith(newValue.toLowerCase())).toList();
            lvResult.getItems().clear();
            lvResult.getItems().addAll(data);
            if (lvResult.getItems().isEmpty())
                lvResult.setPlaceholder(createEmptyResultPlaceholder());
            else
                lvResult.getSelectionModel().selectFirst();
        });

        btClear.setCursor(Cursor.HAND);
        btClear.setOnAction(event -> {
            tfSearch.clear();
        });

        return main;

    }

    private void selectWord(Data newValue) {
        lbWord.setText(newValue.stripWord());
        lbState.setText(newValue.title());
        webDef.getEngine().loadContent(newValue.definition());
        if (newValue.synonyms() != null && !newValue.synonyms().isEmpty()) {
            lbSynonym.setVisible(true);
            var synonyms = newValue.synonyms().split(", ");
            fSynonyms.getChildren().clear();
            for (String syn : synonyms) {
                Label lbSynonym = new Label(syn.strip());
                lbSynonym.setCursor(Cursor.HAND);
                lbSynonym.setOnMouseClicked(e -> {
                    tfSearch.setText(syn.strip());
                    lvResult.getSelectionModel().selectFirst();
                });
                lbSynonym.setFont(contentFont);
                lbSynonym.setPadding(new Insets(0, 5, 0, 5));
                lbSynonym.setBorder(new Border(new BorderStroke(Paint.valueOf("#999"), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
                fSynonyms.getChildren().add(lbSynonym);
            }
        } else {
            lbSynonym.setVisible(false);
        }
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